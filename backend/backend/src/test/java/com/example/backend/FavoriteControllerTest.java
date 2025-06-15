package com.example.backend;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.model.Favorite;
import com.example.backend.model.FavoriteItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.FavoriteItemRepository;
import com.example.backend.repository.FavoriteRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.JwtUtils;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL",
        "spring.datasource.driver-class-name=org.h2.Driver", // 使用模拟数据库h2
        "spring.jpa.hibernate.ddl-auto=create-drop", // 使用Hibernate自动创建表
        "spring.sql.init.mode=never", // 禁用SQL脚本初始化
        "spring.jpa.defer-datasource-initialization=false" // 禁止JPA使用数据源初始化
    })
class FavoriteControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(FavoriteControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FavoriteRepository favoriteRepository;

  @Autowired
  private FavoriteItemRepository favoriteItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String token;

  private Long testFavoriteId;

  private Long testFavoriteItemId;

  private Long testProductId;

  private MockMvcUtils mockMvcUtils;

  @BeforeEach
  @Transactional
  void setup() {
    // 创建测试用用户
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPassword(passwordEncoder.encode("password"));
    userRepository.save(user);

    // 生成JWT令牌
    this.token = jwtUtils.generateToken(user.getUsername());
    assertEquals(user.getUsername(), jwtUtils.getUsernameFromToken(this.token));

    mockMvcUtils = new MockMvcUtils(mockMvc, token);

    // 创建测试收藏夹
    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favoriteRepository.save(favorite);
    this.testFavoriteId = favorite.getId();

    // 创建测试商品
    Product product1 = new Product();
    product1.setName("testProduct1");
    product1.setPrice(BigDecimal.valueOf(10));
    product1.setDescription("testDescription1");
    product1.setCategory("testCategory1");
    product1.setStock(10);
    product1.setImage("testImage1");
    productRepository.save(product1);
    this.testProductId = product1.getId();

    Product product2 = new Product();
    product2.setName("testProduct2");
    product2.setPrice(BigDecimal.valueOf(20));
    product2.setDescription("testDescription2");
    product2.setCategory("testCategory2");
    product2.setStock(5);
    product2.setImage("testImage2");
    productRepository.save(product2);

    // 创建测试收藏夹项
    FavoriteItem testFavoriteItem = new FavoriteItem();
    testFavoriteItem.setFavorite(favorite);
    testFavoriteItem.setProduct(product1);
    favoriteItemRepository.save(testFavoriteItem);
    testFavoriteItemId = testFavoriteItem.getId();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    favoriteItemRepository.deleteAll();
    favoriteRepository.deleteAll();
    productRepository.deleteAll();
    userRepository.deleteAll();
    testFavoriteId = null;
  }

  // 测试获取收藏夹
  @Test
  void testGetFavorites() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/favorites/my-favorites")
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试获取收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试未登录时获取收藏夹
  @Test
  void testGetFavoritesWithoutToken() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/favorites/my-favorites")
        .andExpect(status().isForbidden()).andReturn();

    logger.info("测试未登录时获取收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试增加收藏夹（无名称）
  @Test
  void testCreateFavoriteWithoutName() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/favorites/my-favorites")
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试增加收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult favoritesResult = mockMvcUtils.performGetRequest("/api/favorites/my-favorites")
        .andExpect(status().isOk()).andReturn();
    assertFalse(favoritesResult.getResponse().getContentAsString().isEmpty());

    logger.info("获取收藏夹成功: ");
    logger.info("返回内容: {}", favoritesResult.getResponse().getContentAsString());
  }

  // 测试增加收藏夹（有名称）
  @Test
  void testCreateFavoriteWithName() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/favorites/my-favorites", """
        { "name": "测试收藏夹" }
        """).andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试增加收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult favoritesResult = mockMvcUtils.performGetRequest("/api/favorites/my-favorites")
        .andExpect(status().isOk()).andReturn();
    assertFalse(favoritesResult.getResponse().getContentAsString().isEmpty());

    logger.info("获取收藏夹成功: ");
    logger.info("返回内容: {}", favoritesResult.getResponse().getContentAsString());
  }

  // 测试添加收藏夹项
  @Test
  void testAddToFavorite() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/favorites/" + testFavoriteId + "/items", """
            {
              "productId": """ + testProductId + "," + """
              "quantity": 3
            }""").andExpect(status().isOk()).andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试添加收藏夹项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试添加收藏夹项至不存在的收藏夹
  @Test
  void testAddToFavoriteWithNonExistFavorite() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/favorites/" + testFavoriteId + 1 + "/items", """
            {
              "productId": 2,
              "quantity": 3
            }""").andExpect(status().isNotFound())
            .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();

    logger.info("测试添加收藏夹项至不存在的收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试添加含有不存在的产品收藏夹项
  @Test
  void testAddToFavoriteWithNonExistProduct() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/favorites/" + testFavoriteId + "/items", """
            {
              "productId": 3,
              "quantity": 3
            }""").andExpect(status().isNotFound())
            .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();

    logger.info("测试添加含有不存在的产品收藏夹项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除单个收藏夹项
  @Test
  void testDeleteFavoriteItem() throws Exception {
    MvcResult result = mockMvcUtils
        .performDeleteRequest("/api/favorites/" + testFavoriteId + "/items/" + +testFavoriteItemId)
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"favoriteItems\":[]"))).andReturn();
    logger.info("测试删除单个收藏夹项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除不存在的收藏夹项
  @Test
  void testDeleteNonExistFavoriteItem() throws Exception {
    MvcResult result =
        mockMvcUtils.performDeleteRequest("/api/favorites/" + testFavoriteId + "/items/2")
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();
    logger.info("测试删除不存在的收藏夹项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试清空收藏夹
  @Test
  void testClearFavorite() throws Exception {
    MvcResult result =
        mockMvcUtils.performDeleteRequest("/api/favorites/" + testFavoriteId + "/items")
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"favoriteItems\":[]"))).andReturn();
    logger.info("测试清空收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除收藏夹
  @Test
  void testDeleteFavorite() throws Exception {
    mockMvcUtils.performDeleteRequest("/api/favorites/" + testFavoriteId)
        .andExpect(status().isNoContent());

    MvcResult result = mockMvcUtils.performGetRequest("/api/favorites/my-favorites").andReturn();
    logger.info("测试删除收藏夹: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

}
