package com.wechat.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ThreadIdConvert extends ClassicConverter {

  @Override
  public String convert(ILoggingEvent event) {
    return String.valueOf(Thread.currentThread().getId());
  }

}
