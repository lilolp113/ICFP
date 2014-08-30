package com.icfp.frame.mail;

import java.util.Properties;

import com.management.entity.AA05;

public class Config {

	/** 
	* 获取SMTP默认配置 
	* @return 
	*/ 
	public static Properties getSMTP(AA05 aa05) { 
		Properties p = new Properties(); 
		p.setProperty("mail.smtp.host", aa05.getAAE002()); // 按需要更改 
		p.setProperty("mail.smtp.protocol",aa05.getAAE003()); 
		p.setProperty("mail.smtp.port", aa05.getAAE007()); 
		p.setProperty("mail.smtp.auth", "true");
		if("465".equals(aa05.getAAE007()))
		{
			// SSL安全连接参数 
			p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
			p.setProperty("mail.smtp.socketFactory.fallback", "false"); 
			p.setProperty("mail.smtp.socketFactory.port", aa05.getAAE007()); 
		}
		return p; 
	} 
	/** 
	* 获取POP3收信配置 
	* @return 
	*/ 
	public static Properties getPOP3(AA05 aa05) { 
		Properties p = new Properties(); 
		p.setProperty("mail.pop3.host",aa05.getAAE002()); // 按需要更改 
		p.setProperty("mail.pop3.port", aa05.getAAE007()); 
		return p;
	} 
	/** 
	* 获取IMAP收信配置 
	* @return 
	*/ 
	public static Properties getIMAP(AA05 aa05) { 
		Properties p = new Properties(); 
		p.setProperty("mail.imap.host", aa05.getAAE002()); // 按需要更改 
		p.setProperty("mail.imap.port", aa05.getAAE007()); 
		return p;
	} 
	
}
