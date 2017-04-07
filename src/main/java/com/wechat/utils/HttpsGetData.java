package com.wechat.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;


import java.security.cert.X509Certificate;

@Component
public class HttpsGetData {
  
  private final static Log logger = LogFactory.getLog(HttpsGetData.class);
  private static class TrustAnyTrustManager implements X509TrustManager {
        
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
   
    public X509Certificate[] getAcceptedIssuers() { 
      return new X509Certificate[] {};
    }
  }
  private static class TrustAnyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }
    
  public String sendGet(String url,Map<String,Object> keyValueParams) throws Exception {
    String result = "";
    BufferedReader in = null;
    try {
                
      String urlStr = url  +"?"+ getParamStr(keyValueParams);
      logger.debug("GET请求的URL为："+urlStr);
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
      new java.security.SecureRandom());
      URL realUrl = new URL(urlStr);
      // 打开和URL之间的连接
      HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
      //设置https相关属性
      connection.setSSLSocketFactory(sc.getSocketFactory());
      connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
      connection.setDoOutput(true);
      connection.setConnectTimeout(2 * 1000);
      connection.setReadTimeout(2 * 1000);
                
      // 设置通用的请求属性
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("user-agent",
      "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // 建立实际的连接
      connection.connect();
                
      // 定义 BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
      logger.debug("获取的结果为："+result);
    } catch (Exception e) {
        logger.error("发送GET请求出现异常！" + e);
    }
   // 使用finally块来关闭输入流
   finally {
     try {
       if (in != null) {
         in.close();
       }
     } catch (Exception e2) {
         logger.error("发送GET请求出现异常！" + e2);
     }
   }
   return result;
    
 }

  
  /**
   * 描述:  发起https请求并获取结果
   * @param requestUrl 请求地址
   * @param requestMethod 请求方式（GET、POST）
   * @param outputStr 提交的数据
   * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
   */
  public  String httpRequest(String requestUrl, String requestMethod, String outputStr) {
      
    String returnStr = null;
    StringBuffer buffer = new StringBuffer();
      try {
          // 创建SSLContext对象，并使用我们指定的信任管理器初始化
          TrustManager[] tm = { new TrustAnyTrustManager() };
          SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
          sslContext.init(null, tm, new java.security.SecureRandom());
          // 从上述SSLContext对象中得到SSLSocketFactory对象
          SSLSocketFactory ssf = sslContext.getSocketFactory();

          URL url = new URL(requestUrl);
          HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
          httpUrlConn.setSSLSocketFactory(ssf);

          httpUrlConn.setDoOutput(true);
          httpUrlConn.setDoInput(true);
          httpUrlConn.setUseCaches(false);
          
          // 设置请求方式（GET/POST）
          httpUrlConn.setRequestMethod(requestMethod);

          if ("GET".equalsIgnoreCase(requestMethod))
              httpUrlConn.connect();

          // 当有数据需要提交时
          if (null != outputStr) {
              OutputStream outputStream = httpUrlConn.getOutputStream();
              // 注意编码格式，防止中文乱码
              outputStream.write(outputStr.getBytes("UTF-8"));
              outputStream.close();
          }

          // 将返回的输入流转换成字符串
          InputStream inputStream = httpUrlConn.getInputStream();
          InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

          String str = null;
          while ((str = bufferedReader.readLine()) != null) {
              buffer.append(str);
          }
          bufferedReader.close();
          inputStreamReader.close();
          // 释放资源
          inputStream.close();
          inputStream = null;
          httpUrlConn.disconnect();
          returnStr = buffer.toString();
      } catch (ConnectException ce) {
        logger.error("Weixin server connection timed out.");
      } catch (Exception e) {
        logger.error("https request error:{}", e);
      }
      return returnStr;
  }

 private String getParamStr(Map<String,Object> keyValueParams) {
   String paramStr="";
   // 获取所有响应头字段
   Map<String, Object> params = keyValueParams;
   // 获取参数列表组成参数字符串
   for (String key : params.keySet()) {
     paramStr+=key+"="+params.get(key)+"&";
   }
   //去除最后一个"&"
   paramStr=paramStr.substring(0, paramStr.length()-1);
        
    return paramStr;
  }
}

