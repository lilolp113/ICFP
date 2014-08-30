package com.icfp.frame.mail.pojo;

/**
 * 邮件发送辅助类
 * @author liufei
 *
 */
public class Mail {

	//标题
	private String title;
	//内容
	private String content;
	//接收人
	private String receiver;
	//暗送人
	private String cc;
	//附件地址
	private String filePath;
	
	/**
	 * 获取邮件标题
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * 设置邮件标题
	 * @param title 标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 获取邮件内容
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 设置邮件内容
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 获取接收人
	 * @return 接收人
	 */
	public String getReceiver() {
		return receiver;
	}
	
	/**
	 * 设置接收人
	 * @param receiver 接收人
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * 获取附件地址
	 * @return 附件地址
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * 设置附件地址
	 * @param filePath 附件地址
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * 获取暗送人
	 * @return 暗送人
	 */
	public String getCc() {
		return cc;
	}
	
	/**
	 * 设置暗送人
	 * @param cc 暗送人
 	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	
}
