package com.icfp.frame.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.icfp.frame.params.ConfigParamList;

/**
 * 系统配置装载类
 * @author liufei
 *
 */
public class ConfigListener implements ServletContextListener{
	
	private static Logger logger = Logger.getLogger(ConfigListener.class);
	
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		logger.info("读取系统配置参数");
		Properties prop = new Properties();
		InputStream in = servletContext.getResourceAsStream("/WEB-INF/config.properties");
		try {
			prop.load(in);
			ConfigParamList.SWFTools = prop.getProperty("swftools.addr").trim();
			logger.info("获取swftools中pdf2swf.exe路径："+ConfigParamList.SWFTools);
			ConfigParamList.XPDF = prop.getProperty("swftools.xpdf").trim();
			logger.info("获取中文字符集文件路径："+ConfigParamList.XPDF);
			ConfigParamList.JASPER=prop.getProperty("report.jasper").trim();
			logger.info("获取报表模板文件路径："+ConfigParamList.JASPER);
			ConfigParamList.PDFPATH=prop.getProperty("pdf.path").trim();
			logger.info("获取pdf文件路径："+ConfigParamList.PDFPATH);
			ConfigParamList.SWFPATH=prop.getProperty("swf.path").trim();
			logger.info("获取swf文件路径："+ConfigParamList.SWFPATH);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
}
