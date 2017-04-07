package com.wechat.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RequestMapping(value = "/wechat/menu")
public class MenuController extends BaseController{
  @RequestMapping(value = "/index")
  public String toIndex() {
     return "index";
   }
}
