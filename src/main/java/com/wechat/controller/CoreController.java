package com.wechat.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.service.CoreService;
import com.wechat.utils.SignUtil;

@RestController()
@RequestMapping(value = "/wechat/core",produces = { "application/json; charset=utf-8", "application/xml; charset=utf-8" })
public class CoreController extends BaseController {
  
  @Autowired
  private CoreService coreService;
  
  private final static Log logger = LogFactory.getLog(CoreController.class);
  @GetMapping(value = "/receivewxmsg")
  public String getReceiveWechatMsg(HttpServletRequest request, HttpServletResponse response) {
   
    logger.debug("对接微信成功！");
    // 微信加密签名
    String signature = request.getParameter("signature");
    // 时间戳
    String timestamp = request.getParameter("timestamp");
    // 随机数
    String nonce = request.getParameter("nonce");
    // 随机字符串
    String echostr = request.getParameter("echostr");

    
    // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
    if (SignUtil.checkSignature(signature, timestamp, nonce)) {
      return echostr;
    } else {
      return echostr;
    }
    
  }
  
  @PostMapping(value = "/receivewxmsg" )
  public String postReceiveWechatMsg(HttpServletRequest request, HttpServletResponse response) {
    // 消息的接收、处理、响应
    // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
    String respXml = null;
    try {
      request.setCharacterEncoding("UTF-8");
      respXml = new String(coreService.processRequest(request).getBytes(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    // 调用核心业务类接收消息、处理消息
    logger.info("回应微信消息为："+respXml);
    return respXml;
    
  }
  
  @GetMapping(value = "/receivewxmsg1" )
  public String postReceiveWechatMsg1(HttpServletRequest request, HttpServletResponse response) {
    return "dfsgfg卧室中文";
    
  }
}
