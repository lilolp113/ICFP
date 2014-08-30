package com.icfp.frame.mail;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icfp.frame.mail.pojo.Mail;
import com.management.entity.AA05;
import com.management.entity.MB01;

/**
 * 邮件发送实现
 * 发送形式：普通邮件形式、网页邮件形式
 * @author liufei
 *
 */
public class AutoEmail {
	
	@SuppressWarnings("unchecked")
	private static Vector file=new Vector();//附件文件集合
	
	protected static Logger log = LoggerFactory.getLogger(AutoEmail.class);
	
	/**
	 * @author liufei
	 * 发送普通邮件
	 * @param mail
	 */
	@SuppressWarnings("unchecked")
	public static boolean sendMail(final AA05 aa05,final MB01 mb01) {
		log.info("获取邮件配置信息...");
		// 创建Properties 对象
		Properties props =null;
		if("smtp".equals(aa05.getAAE003()))
		{
			props=Config.getSMTP(aa05);
		}else if("pop3".equals(aa05.getAAE003())){
			props=Config.getPOP3(aa05);
		}else if("imap".equals(aa05.getAAE003())){
			props=Config.getIMAP(aa05);
		}else{
			log.info("邮件协议类型匹配错误,发送终止...");
			return false;
		}
		Session session=null;
		try{
			log.info("开始用户验证...");
			session = Session.getDefaultInstance(props, new Authenticator(){
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(aa05.getAAE004(),aa05.getAAE005());
				}
				
			});
		}catch(Exception ex){
			log.info("用户验证失败!");
			return false;
		}
		// 创建邮件会话
		try {
			// 定义邮件信息
			MimeMessage message = new MimeMessage(session);           //实例化一个MimeMessage集成自abstract Message 。参数为session
			message.setFrom(new InternetAddress(aa05.getAAE004()));           //设置发出方,使用setXXX设置单用户，使用addXXX添加InternetAddress[]
			if(mb01.getMBA004()!=null && !"".equals(mb01.getMBA004()))
			{
				if(mb01.getMBA004().indexOf(";")<0)
				{
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(mb01.getMBA004()));      //设置接收方
				}else{
					String[] res=mb01.getMBA004().split(";");
					for(String str:res)
					{
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(str));      //设置接收方
					}
				}
			}
			message.setHeader("Header:",aa05.getAAE006());
			message.setSubject(mb01.getMBA002());              //邮件标题
			//构造Multipart
			Multipart mp = new MimeMultipart();  
			//向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setText(mb01.getMBA003());
			//向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);
			if(mb01.getMBA006()!=null &&!"".equals(mb01.getMBA006()))
			{
				AutoEmail.attachfile(mb01.getMBA006());
			}
			if(file!=null && !file.isEmpty())
			{
				// 向Multipart添加附件
				Enumeration efile = file.elements();
				while (efile.hasMoreElements()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					String filename = efile.nextElement().toString();
					FileDataSource fds = new FileDataSource(filename);
					mbpFile.setDataHandler(new DataHandler(fds));
					mbpFile.setFileName(fds.getName());
					// 向MimeMessage添加（Multipart代表附件）
					mp.addBodyPart(mbpFile);
				}
			}
			message.setContent(mp);          //邮件内容
			message.setSentDate(new Date()); // 设置发送时间

			// 发送消息
			Transport.send(message);          
			log.info("邮件发送成功!");
			return true;
		} catch (MessagingException e) {
			log.info("邮件发送失败!");
			return false;
		}
	}
	
	/**
	 * 发送网页形式的邮件
	 * @author liufei 
	 * @param mail
	 */
	@SuppressWarnings("unchecked")
	public static void sendMailWihtHtml(final AA05 aa05, final Mail mail) {
		InternetAddress[] address = null;
		boolean sessionDebug = false;

		try {
			log.info("获取邮件配置信息...");
			// 创建Properties 对象
			Properties props =null;
			if("smtp".equals(aa05.getAAE003()))
			{
				props=Config.getSMTP(aa05);
			}else if("pop3".equals(aa05.getAAE003())){
				props=Config.getPOP3(aa05);
			}else if("imap".equals(aa05.getAAE003())){
				props=Config.getIMAP(aa05);
			}else{
				log.info("邮件协议类型匹配错误,发送终止...");
			}

			// 产生新的Session 服务
			javax.mail.Session mailSession = javax.mail.Session
					.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);

			// 设定发邮件的人
			msg.setFrom(new InternetAddress(aa05.getAAE004()));

			if(mail.getReceiver().indexOf(";")<0)
			{
				address = InternetAddress.parse(mail.getReceiver(), false);
				msg.setRecipients(Message.RecipientType.TO, address);
			}else{
				String[] res=mail.getReceiver().split(";");
				for(String str:res)
				{
					// 设定收信人的信箱
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(str));      //设置接收方
				}
				
			}
			// 设定信中的主题
			msg.setSubject(mail.getTitle());

			// 设定送信的时间
			msg.setSentDate(new Date());

			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();

			// 设定邮件内容的类型为 text/plain 或 text/html
			mbp.setContent(mail.getContent(), "text/html;charset=GB2312");
			mp.addBodyPart(mbp);
			if(mail.getFilePath()!=null &&!"".equals(mail.getFilePath()))
			{
				AutoEmail.attachfile(mail.getFilePath());
			}
			if(file!=null && !file.isEmpty())
			{
				// 向Multipart添加附件
				Enumeration efile = file.elements();
				while (efile.hasMoreElements()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					String filename = efile.nextElement().toString();
					FileDataSource fds = new FileDataSource(filename);
					mbpFile.setDataHandler(new DataHandler(fds));
					mbpFile.setFileName(fds.getName());
					// 向MimeMessage添加（Multipart代表附件）
					mp.addBodyPart(mbpFile);
				}
			}
			msg.setContent(mp);
			Transport transport = mailSession.getTransport(aa05.getAAE003());
			// 请填入你的邮箱用户名和密码
			transport.connect(aa05.getAAE004(),aa05.getAAE005());
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			log.info("邮件发送成功...");
			// Transport.send(msg);

		} catch (MessagingException mex) {
			log.info("邮件发送失败!");
			mex.printStackTrace();
		}
	}
	
	/**
	 * *<br>
	 * 方法说明：把主题转换为中文 *<br>
	 * 输入参数：String strText *<br>
	 * 返回类型：
	 * */
	public String transferChinese(String strText) {
		try {
			strText = MimeUtility.encodeText(new String(strText.getBytes(),
					"GB2312"), "GB2312", "B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strText;
	}
	
	/** 
	 **<br>方法说明：往附件组合中添加附件 
	 **<br>输入参数：
	 **<br>返回类型：
	 **/
	@SuppressWarnings("unchecked")
	public static void attachfile(String fname){
		file.addElement(fname);
	}
}
