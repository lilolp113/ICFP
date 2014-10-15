package com.icfp.frame.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;


public class SessionFilter implements HttpSessionListener,HttpSessionAttributeListener, ServletContextListener {
	
	public static String SYS_DB_USERNAME = "icfp";

	public static String SYS_DB_PASSWORD = "";

	public static String BIZ_DB_USERNAME = "icfp";

	public static String BIZ_DB_PASSWORD = "";

	public static String BIZ_DB_IP = "";
	
	public static String BIZ_DB_SID = "";
	
	public static String BIZ_DB_DRIVERCLASS = null;
	
	private static Logger logger = Logger.getLogger(SessionFilter.class);

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		logger.info("读取数据库参数");
		Properties prop = new Properties();
		InputStream in = servletContext.getResourceAsStream("/WEB-INF/jdbc.properties");
		try {
			prop.load(in);
			SYS_DB_USERNAME = prop.getProperty("username").trim();
			SYS_DB_PASSWORD = prop.getProperty("password").trim();
			BIZ_DB_USERNAME = prop.getProperty("username").trim();
			BIZ_DB_PASSWORD = prop.getProperty("username").trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	public void attributeAdded(HttpSessionBindingEvent arg0) {
		
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		
	}
	
}
