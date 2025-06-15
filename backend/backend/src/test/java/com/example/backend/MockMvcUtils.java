package com.example.backend;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockMvcUtils {
  private MockMvc mockMvc;
  private String token;

  public MockMvcUtils(MockMvc mockMvc, String token) {
    this.mockMvc = mockMvc;
    this.token = token;
  }

  public MockMvcUtils(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
    this.token = "";
  }

  /**
   * 发送 POST 请求
   *
   * @param url 请求的 URL
   * @param content 请求体内容
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performPostRequest(String url, String content) throws Exception {
    return mockMvc
        .perform(MockMvcRequestBuilders.post(url).header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON).content(content));
  }

  /**
   * 发送不带 token 的 POST 请求
   *
   * @param url 请求的 URL
   * @param content 请求体内容
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performPostRequestWithoutToken(String url, String content) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(content));
  }

  /**
   * 发送无内容的 POST 请求
   *
   * @param url 请求的 URL
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performPostRequest(String url) throws Exception {
    return mockMvc
        .perform(MockMvcRequestBuilders.post(url).header("Authorization", "Bearer " + token));
  }

  /**
   * 发送不带 token 的无内容的 POST 请求
   *
   * @param url 请求的 URL
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performPostRequestWithoutToken(String url) throws Exception {
    return mockMvc.perform(MockMvcRequestBuilders.post(url));
  }

  /**
   * 发送 GET 请求
   *
   * @param url 请求 URL
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performGetRequest(String url) throws Exception {
    return mockMvc
        .perform(MockMvcRequestBuilders.get(url).header("Authorization", "Bearer " + token));
  }

  /**
   * 发送不带 token 的 GET 请求
   *
   * @param url 请求 URL
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performGetRequestWithoutToken(String url) throws Exception {
    return mockMvc.perform(MockMvcRequestBuilders.get(url));
  }

  /**
   * 发送 DELETE 请求
   *
   * @param url 请求 URL
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performDeleteRequest(String url) throws Exception {
    return mockMvc
        .perform(MockMvcRequestBuilders.delete(url).header("Authorization", "Bearer " + token));
  }

  /**
   * 发送不带 token 的 DELETE 请求
   *
   * @param url 请求 URL
   * @return ResultActions
   * @throws Exception 如果请求失败
   */
  public ResultActions performDeleteRequestWithoutToken(String url) throws Exception {
    return mockMvc.perform(MockMvcRequestBuilders.delete(url));
  }
}
