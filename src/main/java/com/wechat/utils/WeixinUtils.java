package com.wechat.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pojo.menu.Menu;
import com.wechat.pojo.weixin.Token;
import com.wechat.pojo.weixin.WeixinOauth2Token;
import com.wechat.pojo.weixin.WeixinUserInfo;
import com.wechat.utils.HttpsGetData;

@Component
public class WeixinUtils {
  
  private final ObjectMapper  objectMapper  = new ObjectMapper();
  
  
  @Autowired
  private HttpsGetData httpsGetData;
  

  private final static Log logger = LogFactory.getLog(WeixinUtils.class);

  // 菜单创建（POST） 限100（次/天）
  @Value("${creat.menu.url}")
  private String createMenuUrl;
  
  @Value("${get.user.info.url}")
  private String getUserInfoUrl;
  
  @Value("${weixin.appid}")
  private String appID;
  
  @Value("${weixin.appsecret}")
  private String appsecret;
  
  @Value("${get.token.url}")
  private String getTokenUrl;
  
  @Value("${get.access.token.url}")
  private String getAccessTokenUrl;
  
  @Value("${get.sns.user.info.url}")
  private String getSnsUserInfoUrl;
  /**
   * 创建菜单
   * 
   * @param menu 菜单实例
   * @param accessToken 有效的access_token
   * @return 0表示成功，其他值表示失败
   */
  public String createMenu(Menu menu) {
    
    String result = "0";
    try {
      String jsonMenu = objectMapper.writeValueAsString(menu);
      
      String url = createMenuUrl + "?access_token="+getAccToken();
      // 调用接口创建菜单
      String requestMenuStr = httpsGetData.httpRequest(url, "POST", jsonMenu);
      
      logger.debug(requestMenuStr);
      Map map = objectMapper.readValue(requestMenuStr,Map.class);
      
      if (!map.isEmpty()) {
        if (map.containsKey("errcode") 
            && !StringUtils.equals("0", map.get("errcode").toString())) {
          result = map.get("errcode").toString();
          logger.debug("创建菜单失败 errcode:{"+map.get("errcode").toString()
              +"} errmsg:{"+map.get("errmsg").toString()+"}");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return result;
  }
  
  /**
   * 获取用户信息
   * 
   * @param accessToken 接口访问凭证
   * @param openId 用户标识
   * @return WeixinUserInfo
   * @throws Exception 
   */
  public WeixinUserInfo getUserInfo(String openId) throws Exception {
      WeixinUserInfo weixinUserInfo = null;
      // 拼接请求地址
      String requestUrl = getUserInfoUrl + "?access_token=" + getAccToken() + "&openid=" + openId;
      // 获取用户信息
      String requestUserInfoStr = httpsGetData.httpRequest(requestUrl, "GET", null);
      logger.debug(requestUserInfoStr);
      if (StringUtils.isNoneBlank(requestUserInfoStr)) {
          try {
            weixinUserInfo = objectMapper.readValue(requestUserInfoStr,WeixinUserInfo.class);
          } catch (Exception e) {
              Map map = objectMapper.readValue(requestUserInfoStr,Map.class);
              logger.error("错误信息：",e);
              if (map.containsKey("subscribe") 
                  && StringUtils.equals("0", map.get("subscribe").toString())) {
                logger.debug("用户{"+weixinUserInfo.getOpenid()+"}已取消关注" );
              } else {
                  
                  String errorCode = map.get("errcode").toString();
                  String errorMsg = map.get("errmsg").toString();
                  logger.debug("获取用户信息失败 errcode:{"+errorCode+"} errmsg:{"+errorMsg+"}");
              }
          }
      }
      return weixinUserInfo;
  }
  /**
   * 获取token
   * @throws Exception
   */
  public void getToken() throws Exception {
    Map<String, Object> http = new HashMap<String, Object>();
    http.put("grant_type",Constans.WX_GET_TOKEN_GRANT_TYPE);
    http.put("appid", appID);
    http.put("secret", appsecret);
    logger.debug(http);
    String mapStr= httpsGetData.sendGet(getTokenUrl, http);
    Map map = objectMapper.readValue(mapStr, Map.class);
    
    if (map.containsKey("access_token") && StringUtils.isNoneBlank(map.get("access_token").toString()))
    Token.getInstance().setAccessToken(map.get("access_token").toString());
    
    if (map.containsKey("expires_in") && StringUtils.isNoneBlank(map.get("expires_in").toString()))
    Token.getInstance().setExpiresIn((int) map.get("expires_in"));
    
    logger.info(map);
  }
  /**
   * 对外提供的获取token
   * 
   * @return
   * @throws Exception
   */
  public String getAccToken() throws Exception{
    Token token = Token.getInstance();
    if (StringUtils.isBlank(token.getAccessToken())){
      getToken();
    } 
    return token.getAccessToken();
  }
  
  /**
   * 获取网页授权凭证
   * 
   * @param appId 公众账号的唯一标识
   * @param appSecret 公众账号的密钥
   * @param code
   * @return WeixinAouth2Token
   * @throws IOException 
   * @throws JsonMappingException 
   * @throws JsonParseException 
   */
  public WeixinOauth2Token getOauth2AccessToken(String code) throws JsonParseException, JsonMappingException, IOException {
    WeixinOauth2Token wat = null;
    // 拼接请求地址
    String requestUrl = getAccessTokenUrl + "?appid="+appID
        +"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
    // 获取网页授权凭证
    String requestUserInfoStr = httpsGetData.httpRequest(requestUrl, "GET", null);
    
    logger.debug(requestUserInfoStr);
    if (StringUtils.isNoneBlank(requestUserInfoStr)) {
      try {
        wat = objectMapper.readValue(requestUserInfoStr,WeixinOauth2Token.class);
      } catch (Exception e) {
        logger.error("错误信息：",e);
        Map map = objectMapper.readValue(requestUserInfoStr,Map.class);
        
        String errorCode = map.get("errcode").toString();
        String errorMsg = map.get("errmsg").toString();
        logger.debug("获取用户信息失败 errcode:{"+errorCode+"} errmsg:{"+errorMsg+"}");
        }
      }
    return wat;
  }
  
  /**
   * 通过网页授权获取用户信息
   * 
   * @param accessToken 网页授权接口调用凭证
   * @param openId 用户标识
   * @return SNSUserInfo
   * @throws IOException 
   * @throws JsonMappingException 
   * @throws JsonParseException 
   */
  public  WeixinUserInfo getSNSUserInfo(String accessToken, String openId) throws JsonParseException, JsonMappingException, IOException {
    WeixinUserInfo weixinUserInfo = null;
      // 拼接请求地址
    String requestUrl = getSnsUserInfoUrl+"?access_token="+accessToken+"&openid="+openId;
    String requestUserInfoStr = httpsGetData.httpRequest(requestUrl, "GET", null);
    logger.debug(requestUserInfoStr);
    if (StringUtils.isNoneBlank(requestUserInfoStr)) {
        try {
          weixinUserInfo = objectMapper.readValue(requestUserInfoStr,WeixinUserInfo.class);
        } catch (Exception e) {
          logger.error("错误信息：",e);
          Map map = objectMapper.readValue(requestUserInfoStr,Map.class);
          String errorCode = map.get("errcode").toString();
          String errorMsg = map.get("errmsg").toString();
          logger.debug("获取用户信息失败 errcode:{"+errorCode+"} errmsg:{"+errorMsg+"}");
        }
    }
    return weixinUserInfo;
  }
  
  public static String requestParamToUrl(ServletRequest request) {
    StringBuffer paramUrlSb = new StringBuffer("?");
    Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
      String key = params.nextElement();
      String value = request.getParameter(key);
      paramUrlSb.append(key).append("=").append(value).append("&");
    }
    // paramUrlSb.append("v=").append(new Date().getTime());
    return paramUrlSb.substring(0, paramUrlSb.length() - 1);
  }
  
}
