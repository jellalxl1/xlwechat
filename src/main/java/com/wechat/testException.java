package com.wechat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testException {

  public static void main(String[] args)  throws Exception{
    /*通过正则获取url中的参数*/
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2699700d8723242a&redirect_uri=http://ysdceshi.jlpay.com/wx-agent/wechat/view/registerMerchant.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
    String host = "";
    String pattern = "redirect_uri=(.+\\.html)";
    Pattern p =  Pattern.compile(pattern);
    Matcher matcher = p.matcher(url);  
    if(matcher.find()){
     host = matcher.group();
     System.out.println(     matcher.group(1));
     System.out.println(host);
    }
  }

  public static void first () throws Exception {
    System.out.println("3333");
    throw new RuntimeException();
  }
}
