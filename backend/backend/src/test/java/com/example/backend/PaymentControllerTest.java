package com.example.backend;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.Payment;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
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
class PaymentControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(PaymentControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String token;

  private Long testPaymentId;

  private Long testOrderId;

  private Long orderIdWithoutPayment;

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

    // 创建测试订单
    Order order = new Order();
    order.setUser(user);
    order.setAddress("幻想乡");
    orderRepository.save(order);
    this.testOrderId = order.getId();

    Order orderWithoutPayment = new Order();
    orderWithoutPayment.setUser(user);
    orderWithoutPayment.setAddress("新日暮里");
    orderRepository.save(orderWithoutPayment);
    this.orderIdWithoutPayment = orderWithoutPayment.getId();

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

    Payment testPayment = new Payment();
    testPayment.setOrder(order);
    testPayment.setAmount(BigDecimal.valueOf(1000));
    testPayment.setPaymentMethod("alipay");
    paymentRepository.save(testPayment);
    testPaymentId = testPayment.getId();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    paymentRepository.deleteAll();
    orderItemRepository.deleteAll();
    orderRepository.deleteAll();
    productRepository.deleteAll();
    userRepository.deleteAll();
    testPaymentId = null;
    testOrderId = null;
  }

  // 测试获取某用户所有支付记录
  @Test
  void testGetPayments() throws Exception {

    MvcResult result =
        mockMvcUtils.performGetRequest("/api/payments/my-payments").andExpect(status().isOk())
            .andExpect(jsonPath("$[0].paymentMethod", is("alipay"))).andReturn();

    logger.info("测试获取登录用户所有支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取指定支付记录
  @Test
  void testGetPaymentById() throws Exception {

    MvcResult result =
        mockMvcUtils.performGetRequest("/api/payments/" + testPaymentId).andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentMethod", is("alipay"))).andReturn();

    logger.info("测试获取指定支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取不存在的支付记录
  @Test
  void testGetNonExistPayment() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/payments/" + testPaymentId + 1)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试获取不存在的支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试未登录时获取支付记录
  @Test
  void testGetPaymentsWithoutToken() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/payments/my-payments")
        .andExpect(status().isForbidden()).andReturn();

    logger.info("测试未登录时获取支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取某订单的支付记录
  @Test
  void testGetPaymentByOrderId() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/payments/orders/" + testOrderId)
        .andExpect(status().isOk()).andExpect(jsonPath("$.paymentMethod", is("alipay")))
        .andReturn();

    logger.info("测试获取某订单的支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取某订单的支付记录（支付记录不存在）
  @Test
  void testGetNonExistPaymentByOrderId() throws Exception {

    MvcResult result =
        mockMvcUtils.performGetRequest("/api/payments/orders/" + orderIdWithoutPayment)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试获取某订单的支付记录（支付记录不存在）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取某订单的支付记录（订单不存在）
  @Test
  void testGetPaymentByNonExistOrderId() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/payments/" + testPaymentId + 1)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试获取某订单的支付记录（订单不存在）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试创建支付记录
  @Test
  void testCreatePayment() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/payments", """
        {
          "orderId": """ + orderIdWithoutPayment + """
          ,
          "amount": 99999999,
          "paymentMethod": "WechatPay"
        }""").andExpect(status().isOk()).andExpect(jsonPath("$.paymentMethod", is("WechatPay")))
        .andReturn();

    logger.info("测试创建支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试创建支付记录（订单不存在）
  @Test
  void testCreatePaymentWithNonExistOrder() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/payments", """
        {
          "orderId": """ + orderIdWithoutPayment + 1 + """
          ,
          "amount": 99999999,
          "paymentMethod": "WechatPay"
        }""").andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试创建支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新支付状态（已支付）
  @Test
  void testUpdatePaymentPaied() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/payments/" + testPaymentId + "/status", """
            {
              "status": "paied"
            }""").andExpect(status().isOk()).andExpect(jsonPath("$.status", is("paied")))
            .andExpect(jsonPath("$.paymentTime", not(is(nullValue())))).andReturn();

    logger.info("测试更新支付状态（已支付）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新支付状态（失败）
  @Test
  void testUpdatePaymentFailed() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/payments/" + testPaymentId + "/status", """
            {
              "status": "failed"
            }""").andExpect(status().isOk()).andExpect(jsonPath("$.status", is("failed")))
            .andExpect(jsonPath("$.paymentTime", is(nullValue()))).andReturn();

    logger.info("测试更新支付状态（失败）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试更新支付状态（未知状态）
  @Test
  void testUpdatePaymentUnknown() throws Exception {
    MvcResult result =
        mockMvcUtils.performPostRequest("/api/payments/" + testPaymentId + "/status", """
            {
              "status": "unknown"
            }""").andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("未知订单状态：unknown"))).andReturn();

    logger.info("测试更新支付状态（未知状态）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除支付记录
  @Test
  void testDeletePayment() throws Exception {
    mockMvcUtils.performDeleteRequest("/api/payments/" + testPaymentId)
        .andExpect(status().isNoContent());

    MvcResult result = mockMvcUtils.performGetRequest("/api/payments/my-payments").andReturn();
    logger.info("测试删除支付记录: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult paymentsResult = mockMvcUtils.performGetRequest("/api/payments/my-payments")
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0))).andReturn();

    logger.info("获取支付记录成功: ");
    logger.info("返回内容: {}", paymentsResult.getResponse().getContentAsString());
  }

}
