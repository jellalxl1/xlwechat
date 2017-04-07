package com.wechat.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/wechat/view")
public class WechateViewController {
  
  @RequestMapping(value = "/index1")
  public String index() {
    System.out.println(" is  ok ");
    return "index";
  }
}
