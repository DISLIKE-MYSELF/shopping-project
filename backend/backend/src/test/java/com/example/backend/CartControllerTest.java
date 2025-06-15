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
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.CartRepository;
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
class CartControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(CartControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String token;

  private Long testCartId;

  private Long testCartItemId;

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

    // 创建测试购物车
    Cart cart = new Cart();
    cart.setUser(user);
    cartRepository.save(cart);
    this.testCartId = cart.getId();

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

    // 创建测试购物车项
    CartItem testCartItem = new CartItem();
    testCartItem.setCart(cart);
    testCartItem.setProduct(product1);
    testCartItem.setQuantity(2);
    cartItemRepository.save(testCartItem);
    testCartItemId = testCartItem.getId();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    cartItemRepository.deleteAll();
    cartRepository.deleteAll();
    productRepository.deleteAll();
    userRepository.deleteAll();
    testCartId = null;
  }

  // 测试获取购物车
  @Test
  void testGetCarts() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/carts/my-carts")
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试获取购物车: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试未登录时获取购物车
  @Test
  void testGetCartsWithoutToken() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/carts/my-carts")
        .andExpect(status().isForbidden()).andReturn();

    logger.info("测试未登录时获取购物车: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试增加购物车
  @Test
  void testCreateCart() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/carts/my-carts", "")
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试增加购物车: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult cartsResult = mockMvcUtils.performGetRequest("/api/carts/my-carts")
        .andExpect(status().isOk()).andReturn();
    assertFalse(cartsResult.getResponse().getContentAsString().isEmpty());

    logger.info("获取购物车成功: ");
    logger.info("返回内容: {}", cartsResult.getResponse().getContentAsString());
  }

  // 测试添加购物车项
  @Test
  void testAddToCart() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/carts/" + testCartId + "/items", """
        {
          "productId": """ + testProductId + "," + """
          "quantity": 3
        }""").andExpect(status().isOk()).andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试添加购物车项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试添加购物车项至不存在的购物车
  @Test
  void testAddToCartWithNonExistCart() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/carts/" + testCartId + 1 + "/items", """
            {
              "productId": 2,
              "quantity": 3
            }""").andExpect(status().isNotFound())
            .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();

    logger.info("测试添加购物车项至不存在的购物车: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试添加含有不存在的产品购物车项
  @Test
  void testAddToCartWithNonExistProduct() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/carts/" + testCartId + "/items", """
        {
          "productId": 3,
          "quantity": 3
        }""").andExpect(status().isNotFound())
        .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();

    logger.info("测试添加含有不存在的产品购物车项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试添加超出产品库存的购物车项
  @Test
  void testAddToCartWithExceededStock() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/carts/" + testCartId + "/items", """
        {
          "productId": 1,
          "quantity": 12
        }""").andExpect(status().isBadRequest()).andExpect(content().string(containsString("库存不足")))
        .andReturn();

    logger.info("测试添加超出产品库存的购物车项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新购物车项
  @Test
  void testUpdateCartItem() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/carts/" + testCartId + "/items/" + testCartItemId, """
            {
              "quantity": 4
            }""").andExpect(status().isOk())
            .andExpect(content().string(containsString("\"quantity\":4"))).andReturn();

    logger.info("测试更新购物车项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新购物车项超过商品库存
  @Test
  void testUpdateCartItemWithExceededStock() throws Exception {
    MvcResult result = mockMvcUtils
        .performPostRequest("/api/carts/" + testCartId + "/items/" + +testCartItemId, """
            {
              "quantity": 12
            }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("库存不足"))).andReturn();

    logger.info("测试更新购物车项超过商品库存: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除单个购物车项
  @Test
  void testDeleteCartItem() throws Exception {
    MvcResult result =
        mockMvcUtils.performDeleteRequest("/api/carts/" + testCartId + "/items/" + +testCartItemId)
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"cartItems\":[]"))).andReturn();
    logger.info("测试删除单个购物车项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除不存在的购物车项
  @Test
  void testDeleteNonExistCartItem() throws Exception {
    MvcResult result = mockMvcUtils.performDeleteRequest("/api/carts/" + testCartId + "/items/2")
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();
    logger.info("测试删除不存在的购物车项: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试清空购物车
  @Test
  void testClearCart() throws Exception {
    MvcResult result = mockMvcUtils.performDeleteRequest("/api/carts/" + testCartId + "/items")
        .andExpect(status().isOk()).andExpect(content().string(containsString("\"cartItems\":[]")))
        .andReturn();
    logger.info("测试清空购物车: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除购物车
  @Test
  void testDeleteCart() throws Exception {
    mockMvcUtils.performDeleteRequest("/api/carts/" + testCartId).andExpect(status().isNoContent());

    MvcResult result = mockMvcUtils.performGetRequest("/api/carts/my-carts").andReturn();
    logger.info("测试删除购物车: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

}
