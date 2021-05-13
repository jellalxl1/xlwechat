package com.wechat.demo;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechat.pojo.menu.Button;
import com.wechat.pojo.menu.CommonButtonClick;
import com.wechat.pojo.menu.CommonButtonView;
import com.wechat.pojo.menu.ComplexButton;
import com.wechat.pojo.menu.Menu;
import com.wechat.pojo.weixin.WeixinOauth2Token;
import com.wechat.pojo.weixin.WeixinUserInfo;
import com.wechat.utils.WeixinUtils;

/**
* 类名: MenuManager </br>
* 包名： com.souvc.weixin.main
* 描述:菜单管理器类 </br>
* 开发人员： liuhf </br>
 *
* 创建时间：  2015-12-1 </br>
* 发布版本：V1.0  </br>
 */
@Controller
@RequestMapping(value = "/wechat/test" ,produces = { "application/json; charset=utf-8", "text/html; charset=utf-8" })
public class DemoManager {
  
  private final static Log logger = LogFactory.getLog(DemoManager.class);
  @Autowired
  private WeixinUtils wxs;

  @RequestMapping(value = "/index")
  public String toIndex() {
     return "index";
   }
  @GetMapping(value = "/creatmenu")
  public @ResponseBody String creatMenu() {
    // 调用接口创建菜单
    String result = wxs.createMenu(getMenu());

    // 判断菜单创建结果
    if (StringUtils.equals("0", result))
      logger.info("菜单创建成功！");
    else
      logger.info("菜单创建失败，错误码：" + result);
    return result;
  }
  
  @GetMapping(value = "/getUserInfo")
  public @ResponseBody String getUserInfo() throws Exception {
      
    WeixinUserInfo ws = wxs.getUserInfo("oqOMPxNa7Kvm_9sZNGA0ORLyWWsA");
    return ws.getNickname();
  }
  
  @GetMapping(value = "/wangyeshouqyuan")
  public String wangyeshouqyuan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");

    // 用户同意授权后，能获取到code
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    // 用户同意授权
    if (!"authdeny".equals(code)) {
        // 获取网页授权access_token
        WeixinOauth2Token weixinOauth2Token = wxs.getOauth2AccessToken(code);
        // 网页授权接口访问凭证
        String accessToken = weixinOauth2Token.getAccessToken();
        // 用户标识
        String openId = weixinOauth2Token.getOpenId();
        // 获取用户信息
        WeixinUserInfo snsUserInfo = wxs.getSNSUserInfo(accessToken, openId);

        // 设置要传递的参数
        request.setAttribute("snsUserInfo", snsUserInfo);
        request.setAttribute("state", state);
    }
    return "index";
  }
  
  @GetMapping(value = "/testwy")
  public String testwy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    
     url = url.replace("APPID","wx494a7bdca2d1deb1")
                 .replace("REDIRECT_URI",URLEncoder.encode("http://ysdtest.jlpay.com/wx-agent2/wechat/test/wangyeshouqyuan.html", "UTF-8"))
                 .replace("SCOPE", "snsapi_userinfo")
                 .replace("STATE", "1");
      logger.debug(url);
      return "redirect:"+url;
  }

    /**
     * 组装菜单数据
     * 
     * @return
     */
    private Menu getMenu() {
        CommonButtonClick btn11 = new CommonButtonClick();
        btn11.setName("天气预报");
        btn11.setType("click");
        btn11.setKey("11");

        CommonButtonClick btn12 = new CommonButtonClick();
        btn12.setName("公交查询");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButtonClick btn13 = new CommonButtonClick();
        btn13.setName("周边搜索");
        btn13.setType("click");
        btn13.setKey("13");

        CommonButtonClick btn14 = new CommonButtonClick();
        btn14.setName("历史上的今天");
        btn14.setType("click");
        btn14.setKey("14");

        CommonButtonView btn21 = new CommonButtonView();
        btn21.setName("百度");
        btn21.setType("view");
        btn21.setUrl("http://ysdtest.jlpay.com/wx-agent2/wechat/test/index.html");

        CommonButtonClick btn22 = new CommonButtonClick();
        btn22.setName("经典游戏");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButtonClick btn23 = new CommonButtonClick();
        btn23.setName("美女电台");
        btn23.setType("click");
        btn23.setKey("23");

        CommonButtonClick btn24 = new CommonButtonClick();
        btn24.setName("人脸识别");
        btn24.setType("click");
        btn24.setKey("24");

        CommonButtonClick btn25 = new CommonButtonClick();
        btn25.setName("聊天唠嗑");
        btn25.setType("click");
        btn25.setKey("25");

        CommonButtonClick btn31 = new CommonButtonClick();
        btn31.setName("Q友圈");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButtonClick btn32 = new CommonButtonClick();
        btn32.setName("电影排行榜");
        btn32.setType("click");
        btn32.setKey("32");

        CommonButtonClick btn33 = new CommonButtonClick();
        btn33.setName("幽默笑话");
        btn33.setType("click");
        btn33.setKey("33");

        
        /**
         * 微信：  mainBtn1,mainBtn2,mainBtn3底部的三个一级菜单。
         */
        
        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("生活助手");
        //一级下有4个子菜单
        mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13, btn14 });

        
        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("休闲驿站");
        mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24, btn25 });

        
        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("更多体验");
        mainBtn3.setSub_button(new Button[] { btn31, btn32, btn33 });

        
        /**
         * 封装整个菜单
         */
        Menu menu = new Menu();
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

        return menu;
    }
}