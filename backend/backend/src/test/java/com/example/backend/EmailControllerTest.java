package com.example.backend;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.example.backend.model.Email;
import com.example.backend.model.User;
import com.example.backend.repository.EmailRepository;
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
class EmailControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(EmailControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailRepository emailRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String token;

  private Long testEmailId;

  private Long testUserId;

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
    testUserId = user.getId();

    // 生成JWT令牌
    this.token = jwtUtils.generateToken(user.getUsername());
    assertEquals(user.getUsername(), jwtUtils.getUsernameFromToken(this.token));

    mockMvcUtils = new MockMvcUtils(mockMvc, token);

    Email testEmail = new Email();
    testEmail.setUser(user);;
    testEmail.setSubject("订单已发货");
    testEmail.setContent("您的订单已发货，请注意查收");
    emailRepository.save(testEmail);
    testEmailId = testEmail.getId();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    emailRepository.deleteAll();
    userRepository.deleteAll();
    testEmailId = null;
  }

  // 测试获取登录用户所有邮件
  @Test
  void testGetEmails() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/emails/my-emails")
        .andExpect(status().isOk()).andExpect(jsonPath("$[0].subject", is("订单已发货"))).andReturn();

    logger.info("测试获取登录用户所有邮件: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取指定邮件
  @Test
  void testGetEmailById() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/emails/" + testEmailId)
        .andExpect(status().isOk()).andExpect(jsonPath("$.subject", is("订单已发货"))).andReturn();

    logger.info("测试获取指定邮件: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试获取不存在的邮件
  @Test
  void testGetNonExistEmail() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequest("/api/emails/" + testEmailId + 1)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试获取不存在的邮件: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试未登录时获取邮件
  @Test
  void testGetEmailsWithoutToken() throws Exception {

    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/emails/my-emails")
        .andExpect(status().isForbidden()).andReturn();

    logger.info("测试未登录时获取邮件: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试发送邮件
  @Test
  void testSendEmail() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/emails", """
        {
          "userId": """ + testUserId + """
          ,
          "subject": "快递已签收",
          "content": "您的快递已被签收"
        }""").andExpect(status().isOk()).andExpect(jsonPath("$.subject", is("快递已签收"))).andReturn();

    logger.info("测试发送邮件: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试发送邮件（用户不存在）
  @Test
  void testSendEmailWithNonExistOrder() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/emails", """
        {
          "userId": """ + testUserId + 1 + """
          ,
          "subject": "快递已签收",
          "content": "您的快递已被签收"
        }""").andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("EntityNotFoundException"))).andReturn();

    logger.info("测试发送邮件（用户不存在）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试发送邮件（无主题）
  @Test
  void testSendEmailWithoutSubject() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/emails", """
        {
          "userId": """ + testUserId + """
          ,
          "content": "您的快递已被签收"
        }""").andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("ValidationException")))
        .andExpect(jsonPath("$.details[*]", hasItem("subject: must not be blank"))).andReturn();

    logger.info("测试发送邮件（无主题）: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());
  }

  // 测试删除邮件
  @Test
  void testDeleteEmail() throws Exception {
    mockMvcUtils.performDeleteRequest("/api/emails/" + testEmailId)
        .andExpect(status().isNoContent());

    MvcResult result = mockMvcUtils.performGetRequest("/api/emails/my-emails").andReturn();
    logger.info("测试删除邮件: ");
    logger.info("返回内容: {}", result.getResponse().getContentAsString());

    MvcResult emailsResult = mockMvcUtils.performGetRequest("/api/emails/my-emails")
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0))).andReturn();

    logger.info("获取邮件成功: ");
    logger.info("返回内容: {}", emailsResult.getResponse().getContentAsString());
  }

}
