package com.icfp.frame.filter;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.icfp.frame.params.RiaParamList;

public class UrlFilter implements Filter{
	
	private static Logger logger;
	private FilterConfig filterConfig;
	
	/**
	 * 初始化filter（这里重写父类的方法）
	 * 
	 * @param filterConfig
	 *            FilterConfig filter配置对象
	 * @throws ServletException
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		logger = Logger.getRootLogger();
		String pages = null;
		StringTokenizer strTokenizer = null;

		if (logger.isDebugEnabled()) {
			logger.debug("init validate session filter ");
		}

		this.filterConfig = filterConfig;
		// 以下从配置文件获取配置信息

		pages = filterConfig.getInitParameter("freePages");
		if (pages == null || pages.trim().length() == 0) {
			logger
					.error("web.xml中filterServlet没有配置初始化参数\"freePage\".");
			throw new ServletException(
					"web.xml中filterServlet没有配置初始化参数\"freePage\".");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("freePages:" + pages);
		}

		strTokenizer = new StringTokenizer(pages, ";");
		while (strTokenizer.hasMoreTokens()) {
			RiaParamList.FREEPAGES.add(strTokenizer.nextToken());
		}
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
	
}
