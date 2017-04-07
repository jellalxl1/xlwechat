package com.wechat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
	
	public static void main(String args[]) {
		String str = "http://www.jb51.net";
		String pattern = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		System.out.println(m.matches());
	}

}