package com.example.backend;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.JwtUtils;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = {"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL",
        "spring.datasource.driver-class-name=org.h2.Driver", // 使用模拟数据库h2
        "spring.jpa.hibernate.ddl-auto=create-drop", // 使用Hibernate自动创建表
        "spring.sql.init.mode=never", // 禁用SQL脚本初始化
        "spring.jpa.defer-datasource-initialization=false" // 禁止JPA使用数据源初始化
    })
class UserControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private String token;

  private MockMvcUtils mockMvcUtils;


  @BeforeEach
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
  }

  @AfterEach
  void tearDown() {
    // 清空数据
    userRepository.deleteAll();
  }

  // 测试注册
  @Test
  void testRegister() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "testuser1",
          "email": "test1@example.com",
          "password": "password"
        }""").andExpect(status().isOk()).andReturn();

    User user = userRepository.findByUsername("testuser1").orElse(null);
    assertNotNull(user);
    assertEquals("test1@example.com", user.getEmail());

    logger.info("测试注册");
    logger.info("返回的url: {}", result.getResponse().getRedirectedUrl());
    logger.info("用户ID: {}", user.getId());
    logger.info("用户名: {}", user.getUsername());
    logger.info("用户密码: {}", user.getPassword());
    logger.info("用户邮箱: {}", user.getEmail());
    logger.info("用户创建时间: {}", user.getCreatedAt());
    logger.info("用户上次登录时间: {}", user.getLastLogin());
    logger.info("用户地址: {}", user.getAddress());
  }

  // 测试注册相同用户名
  @Test
  void testRegisterWithDuplicateUsername() throws Exception {

    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "testuser",
          "email": "test1@example.com",
          "password": "password"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("用户名已存在"))).andReturn();

    logger.info("测试注册相同用户名");
    logger.info("返回内容{}", result.getResponse().getContentAsString());
  }

  // 测试注册相同邮箱
  @Test
  void testRegisterWithDuplicateEmail() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "testuser1",
          "email": "test@example.com",
          "password": "password"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("邮箱已存在"))).andReturn();

    logger.info("测试注册相同邮箱");
    logger.info("返回内容{}", result.getResponse().getContentAsString());
  }

  // 测试注册错误邮箱格式
  @Test
  void testRegisterWithWrongEmail() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "testuser",
          "email": "testexample.com",
          "password": "password"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("must be a well-formed email address")))
        .andReturn();

    logger.info("测试注册错误邮箱格式");
    logger.info("返回内容{}", result.getResponse().getContentAsString());
  }

  // 测试注册空白用户名
  @Test
  void testRegisterWithBlankUsername() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "",
          "email": "test@example.com",
          "password": "password"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("must not be blank"))).andReturn();

    logger.info("测试注册空白用户名");
    logger.info("返回内容{}", result.getResponse().getContentAsString());
  }

  // 测试注册短于六位的密码
  @Test
  void testRegisterWithShortPassword() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "testuser1",
          "email": "test1@example.com",
          "password": "1234"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("size must be between 6 and 12"))).andReturn();

    logger.info("测试注册短于六位的密码");
    logger.info("返回内容{}", result.getResponse().getContentAsString());
  }

  // 测试注册长于十二位的密码
  @Test
  void testRegisterWithLongPassword() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/register", """
        {
          "username": "testuser1",
          "email": "test1@example.com",
          "password": "passwordpassword"
        }""").andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("size must be between 6 and 12"))).andReturn();

    logger.info("测试注册长于十二位的密码");
    logger.info("返回内容{}", result.getResponse().getContentAsString());
  }

  // 测试登录并获取当前用户
  @Test
  void testLoginAndGetCurrentUser() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/login", """
        {
          "username": "testuser",
          "password": "password"
        }""").andExpect(status().isOk()).andExpect(jsonPath("$.data.token").exists()).andReturn();

    // 解析token
    String response = result.getResponse().getContentAsString();
    String curToken = JsonPath.parse(response).read("$.data.token");
    assertNotNull(curToken);
    assertTrue(curToken.length() > 10);

    // 获取当前登录用户
    MvcResult curResult =
        mockMvc.perform(get("/api/users/current").header("Authorization", "Bearer " + curToken))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.username").value("testuser"))
            .andReturn();

    logger.info("测试登录并获取当前用户");
    logger.info("返回结果: {}", response);
    logger.info("token: {}", curToken);
    logger.info("当前用户: {}", curResult.getResponse().getContentAsString());
  }

  // 测试登录密码错误
  @Test
  void testLoginWithWrongPassword() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/login", """
        {
          "username": "testuser",
          "password": "password1"
        }""").andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("密码错误"))).andReturn();

    logger.info("测试登录密码错误");
    logger.info("返回结果: {}", result.getResponse().getContentAsString());
  }

  // 测试登录不存在用户
  @Test
  void testLoginWithNonExistUsername() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/login", """
        {
          "username": "testuser1",
          "password": "password1"
        }""").andExpect(status().isNotFound())
        .andExpect(content().string(containsString("EntityNotFoundException"))).andReturn();

    logger.info("测试登录不存在用户");
    logger.info("返回结果: {}", result.getResponse().getContentAsString());
  }

  // 测试未登录访问当前用户接口
  @Test
  void testUnauthenticatedAccess() throws Exception {
    MvcResult result = mockMvcUtils.performGetRequestWithoutToken("/api/users/current")
        .andExpect(status().isForbidden()).andReturn();

    logger.info("测试未登录访问当前用户接口");
    logger.info("返回结果: {}", result.getResponse().getContentAsString());
  }

  // 测试删除当前用户
  @Test
  void testDeleteCurrentUser() throws Exception {
    // 首先登录
    MvcResult result = mockMvcUtils.performPostRequestWithoutToken("/api/users/login", """
        {
          "username": "testuser",
          "password": "password"
        }""").andExpect(status().isOk()).andExpect(jsonPath("$.data.token").exists()).andReturn();

    // 解析token
    String response = result.getResponse().getContentAsString();
    String curToken = JsonPath.parse(response).read("$.data.token");
    assertNotNull(curToken);
    assertTrue(curToken.length() > 10);

    // 获取当前登录用户
    MvcResult curResult =
        mockMvc.perform(get("/api/users/current").header("Authorization", "Bearer " + curToken))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.username").value("testuser"))
            .andReturn();

    logger.info("测试登录并获取当前用户");
    logger.info("返回结果: {}", response);
    logger.info("token: {}", curToken);
    logger.info("当前用户: {}", curResult.getResponse().getContentAsString());

    mockMvcUtils.performDeleteRequest("/api/users/current").andExpect(status().isNoContent());

    assertEquals(userRepository.findByUsername("testuser").orElse(null), null);
  }

  // 测试更新当前用户信息
  @Test
  void testUpdateCurrentUser() throws Exception {
    MvcResult result = mockMvcUtils.performPostRequest("/api/users/current", """
        {
          "address": "复旦大学"
        }
        """).andExpect(status().isOk()).andExpect(jsonPath("$.data.address", is("复旦大学")))
        .andReturn();

    logger.info("测试更新当前用户信息");
    logger.info("返回结果: {}", result.getResponse().getContentAsString());
  }

}
