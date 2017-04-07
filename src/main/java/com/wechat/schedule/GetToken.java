package com.wechat.schedule;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wechat.utils.WeixinUtils;

/**
 * 定时任务配置类
 *
 * @author xl
 * @myblog http://blog.csdn.net/catoop/
 * @create
 */
@Configuration
@EnableScheduling
@Component
public class GetToken {
  
  @Autowired
  private WeixinUtils weixinUtils;
  
  private final static Log logger = LogFactory.getLog(GetToken.class);
  
  @Scheduled(cron = "${get.token.timer}") // 每20秒执行一次
  public void getToken() throws Exception {
    weixinUtils.getToken();
  }
}
