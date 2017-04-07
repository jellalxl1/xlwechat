package com.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pojo.weixin.Token;
import com.wechat.utils.HttpsGetData;

@RestController
public class BaseController {
  
  @Value("${weixin.appid}")
  protected String appID;
 
  @Value("${weixin.appsecret}")
  protected String appsecret;
  
  protected final ObjectMapper  objectMapper  = new ObjectMapper();
 
  protected final Token token = Token.getInstance();
  
  @Autowired
  protected HttpsGetData httpsGetData;
}
