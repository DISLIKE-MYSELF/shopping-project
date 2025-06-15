package com.example.backend;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL",
        "spring.datasource.driver-class-name=org.h2.Driver", // 使用模拟数据库h2
        "spring.jpa.hibernate.ddl-auto=create-drop", // 使用Hibernate自动创建表
        "spring.sql.init.mode=never", // 禁用SQL脚本初始化
        "spring.jpa.defer-datasource-initialization=false" // 禁止JPA使用数据源初始化
    })
class ProductControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(ProductControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductRepository productRepository;

  private Long testProductId;

  private MockMvcUtils mockMvcUtils;

  @BeforeEach
  @Transactional
  void setup() {
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

    mockMvcUtils = new MockMvcUtils(mockMvc);
  }

  @AfterEach
  @Transactional
  void tearDown() {
    productRepository.deleteAll();
  }

  // 测试获取所有商品信息
  @Test
  void testGetProducts() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/products")
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试获取所有商品信息: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取指定商品信息
  @Test
  void testGetProduct() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/products/" + testProductId)
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试获取指定商品信息: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取不存在的商品信息
  @Test
  void testGetNonExistProduct() throws Exception {

    MvcResult result =
        mockMvcUtils.performGetRequestWithoutToken("/api/products/" + testProductId + 1)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试获取不存在的商品信息: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试增加商品
  @Test
  void testCreateProduct() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/products", """
        {
          "name": "丰川祥子",
          "image": "image",
          "price": 9999.99,
          "stock": 1,
          "description": "哇这个好可爱啊",
          "rating": 4.5
        }""").andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试增加商品: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult productsResult = mockMvcUtils.performGetRequestWithoutToken("/api/products")
        .andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("获取商品信息成功: ");
    logger.info("返回内容: {}", productsResult.getResponse().getContentAsString());
  }

  // 测试增加商品（存在非法字段）
  @Test
  void testCreateProductWithInvalidParam() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/products", """
        {
          "name": "丰川祥子",
          "image": "image",
          "price": -1,
          "stock": 1,
          "description": "哇这个好可爱啊",
          "rating": -100
        }""").andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("ValidationException")))
        .andExpect(jsonPath("$.details[*]", hasItem("price: must be greater than 0")))
        .andExpect(jsonPath("$.details[*]", hasItem("rating: must be greater than or equal to 0")))
        .andReturn();

    logger.info("测试增加商品（存在非法字段）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新商品
  @Test
  void testUpdateProduct() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequestWithoutToken("/api/products/" + testProductId, """
            {
              "name": "丰川祥子",
              "image": "image",
              "price": 9999.99,
              "stock": 1,
              "description": "哇这个好可爱啊",
              "rating": 4.5
            }""").andExpect(status().isOk()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试更新商品: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新不存在的商品
  @Test
  void testUpdateNonExistProduct() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequestWithoutToken("/api/products/" + testProductId + 1, """
            {
              "name": "丰川祥子",
              "image": "image",
              "price": 9999.99,
              "stock": 1,
              "description": "哇这个好可爱啊",
              "rating": 4.5
            }""").andExpect(status().isNotFound()).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试更新不存在的商品: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新商品（存在非法字段）
  @Test
  void testUpdateProductWithInvalidParam() throws Exception {
    MvcResult result = mockMvcUtils
        .performPostRequestWithoutToken("/api/products/" + testProductId, """
            {
              "name": "丰川祥子",
              "image": "image",
              "price": -1,
              "stock": 1,
              "description": "哇这个好可爱啊",
              "rating": 4.5
            }""").andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("ValidationException")))
        .andExpect(jsonPath("$.details[*]", hasItem("price: must be greater than 0"))).andReturn();

    logger.info("测试更新商品（存在非法字段）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除商品
  @Test
  void testDeleteProduct() throws Exception {
    mockMvcUtils.performDeleteRequestWithoutToken("/api/products/" + testProductId)
        .andExpect(status().isNoContent()).andReturn();

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/products/" + testProductId)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试删除商品: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }
}
