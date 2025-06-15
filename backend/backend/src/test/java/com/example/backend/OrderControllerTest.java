package com.example.backend;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.List;
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
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;
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
class OrderControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(OrderControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String token;

  private Long testOrderId;

  private List<Long> testCartItemIds;

  private User user;

  private MockMvcUtils mockMvcUtils;

  @BeforeEach
  @Transactional
  void setup() {
    // 创建测试用用户
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setAddress("新日暮里");
    user.setPassword(passwordEncoder.encode("password"));
    this.user = userRepository.save(user);

    // 生成JWT令牌
    this.token = jwtUtils.generateToken(user.getUsername());
    assertEquals(user.getUsername(), jwtUtils.getUsernameFromToken(this.token));

    mockMvcUtils = new MockMvcUtils(mockMvc, token);

    // 创建测试订单
    Order order = new Order();
    order.setUser(user);
    order.setAddress("幻想乡");
    orderRepository.save(order);
    this.testOrderId = order.getId();

    // 创建测试商品
    Product product1 = new Product();
    product1.setName("testProduct1");
    product1.setPrice(BigDecimal.valueOf(10));
    product1.setDescription("testDescription1");
    product1.setCategory("testCategory1");
    product1.setStock(10);
    product1.setImage("testImage1");
    productRepository.save(product1);

    Product product2 = new Product();
    product2.setName("testProduct2");
    product2.setPrice(BigDecimal.valueOf(20));
    product2.setDescription("testDescription2");
    product2.setCategory("testCategory2");
    product2.setStock(5);
    product2.setImage("testImage2");
    productRepository.save(product2);

    // 创建测试订单项
    OrderItem testOrderItem = new OrderItem();
    testOrderItem.setOrder(order);
    testOrderItem.setProduct(product1);
    testOrderItem.setQuantity(2);
    orderItemRepository.save(testOrderItem);

    // 创建测试购物车
    Cart cart = new Cart();
    cart.setUser(user);
    cartRepository.save(cart);

    // 创建测试购物车项
    CartItem testCartItem1 = new CartItem();
    testCartItem1.setCart(cart);
    testCartItem1.setProduct(product1);
    testCartItem1.setQuantity(2);
    cartItemRepository.save(testCartItem1);

    CartItem testCartItem2 = new CartItem();
    testCartItem2.setCart(cart);
    testCartItem2.setProduct(product2);
    testCartItem2.setQuantity(4);
    cartItemRepository.save(testCartItem2);
    testCartItemIds = List.of(testCartItem1.getId(), testCartItem2.getId());
  }

  @AfterEach
  @Transactional
  void tearDown() {
    orderItemRepository.deleteAll();
    orderRepository.deleteAll();
    cartItemRepository.deleteAll();
    cartRepository.deleteAll();
    productRepository.deleteAll();
    userRepository.deleteAll();
    testOrderId = null;
  }

  // 测试获取订单
  @Test
  void testGetOrders() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/orders/my-orders")
        .andExpect(status().isOk()).andExpect(content().string(containsString("幻想乡"))).andReturn();
    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试获取订单: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试未登录时获取订单
  @Test
  void testGetOrdersWithoutToken() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/orders/my-orders")
        .andExpect(status().isForbidden()).andReturn();

    logger.info("测试未登录时获取订单: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试创建订单（使用用户默认地址）
  @Test
  void testCreateOrderWithUserAddress() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/orders/my-orders", """
        {
          "cartItemIds": """ + testCartItemIds + """
        }""").andExpect(status().isOk())
        .andExpect(content().string(containsString(user.getAddress()))).andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试创建订单（使用用户默认地址）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult ordersResult = mockMvcUtils.performGetRequest("/api/orders/my-orders")
        .andExpect(status().isOk()).andReturn();
    assertFalse(ordersResult.getResponse().getContentAsString().isEmpty());

    logger.info("获取订单成功: ");
    logger.info("返回内容: {}", ordersResult.getResponse().getContentAsString());
  }

  // 测试创建订单（使用指定地址）
  @Test
  void testCreateOrderWithAddress() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/orders/my-orders", """
        {
          "address": "复旦大学",
          "cartItemIds": """ + testCartItemIds + """
        }""").andExpect(status().isOk()).andExpect(content().string(containsString("复旦大学")))
        .andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试创建订单（使用指定地址）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult ordersResult = mockMvcUtils.performGetRequest("/api/orders/my-orders")
        .andExpect(status().isOk()).andReturn();
    assertFalse(ordersResult.getResponse().getContentAsString().isEmpty());

    logger.info("获取订单成功: ");
    logger.info("返回内容: {}", ordersResult.getResponse().getContentAsString());
  }

  // 测试创建订单（无地址）
  @Test
  void testCreateOrderWithoutAddress() throws Exception {
    user.setAddress(null);
    userRepository.save(user);

    MvcResult result = mockMvcUtils.performPostRequest("/api/orders/my-orders", """
        {
          "cartItemIds": """ + testCartItemIds + """
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("请填写收货地址"))).andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试创建订单（无地址）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试创建订单（空购物车项）
  @Test
  void testCreateOrderWithEmptyCartItem() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/orders/my-orders", """
        {
          "cartItemIds": []
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("must not be empty"))).andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试创建订单（空购物车项）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更改订单状态
  @Test
  void testUpdateOrderStatus() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/orders/" + testOrderId + "/status", """
        {
          "status": "shipped"
        }""").andExpect(status().isOk()).andExpect(content().string(containsString("shipped")))
        .andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试更改订单状态: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更改订单状态为错误状态
  @Test
  void testUpdateOrderWrongStatus() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/orders/" + testOrderId + "/status", """
        {
          "status": "unknown"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("未知订单状态"))).andReturn();

    assertFalse(result.getResponse().getContentAsString().isEmpty());

    logger.info("测试更改订单状态为错误状态: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除订单
  @Test
  void testDeleteOrder() throws Exception {
    mockMvcUtils.performDeleteRequest("/api/orders/" + testOrderId)
        .andExpect(status().isNoContent());

    MvcResult result = mockMvcUtils.performGetRequest("/api/orders/my-orders").andReturn();

    logger.info("测试删除订单: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }
}
