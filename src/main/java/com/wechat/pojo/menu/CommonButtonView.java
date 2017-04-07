package com.wechat.pojo.menu;
/**
 * 用于创建view类型的菜单
 * @author xielang
 *
 */
public class CommonButtonView extends Button {

  private String type;
  private String url;

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
}
