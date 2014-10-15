package com.icfp.frame.filter;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.icfp.frame.params.SysParamsList;

public class UrlFilter implements Filter{
	
	private static Logger logger;
	private String[] freePages;
	private String toPage = null;
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
		int i = 0;
		String pages = null;
		StringTokenizer strTokenizer = null;

		if (logger.isDebugEnabled()) {
			logger.debug("init validate session filter ");
		}

		this.filterConfig = filterConfig;
		// 以下从配置文件获取配置信息

		this.toPage = filterConfig.getInitParameter("toPage");
		pages = filterConfig.getInitParameter("freePages");
		if (toPage == null || pages == null || toPage.trim().length() == 0
				|| pages.trim().length() == 0) {
			logger
					.error("web.xml中filterServlet没有配置初始化参数\"toPage\"或\"freePage\".");
			throw new ServletException(
					"web.xml中filterServlet没有配置初始化参数\"toPage\"或\"freePage\".");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("toPage：" + toPage);
			logger.debug("freePages:" + pages);
		}

		strTokenizer = new StringTokenizer(pages, ";");
		this.freePages = new String[strTokenizer.countTokens()];
		while (strTokenizer.hasMoreTokens()) {
			freePages[i++] = strTokenizer.nextToken();
		}

		if (!isFreePage(toPage)) {

			logger
					.error("web.xml中filter初始化参数\"toPage\"的值必须是\"freePage\"中的某个页面.");
			throw new ServletException(
					"web.xml中filter初始化参数\"toPage\"的值必须是\"freePage\"中的某个页面.");
		}
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		String requestURI = null;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		requestURI = httpRequest.getRequestURI();
		if (!isFreePage(requestURI)) {// 如果是保护页面
			if (!isValidSession(request)) {// 如果Session无效    
				String toPageURL = null;
				try {
					toPageURL = httpRequest.getContextPath() + toPage;
					httpResponse.encodeRedirectURL(toPageURL);
					httpResponse.sendRedirect(toPageURL);// 转发响应
				} catch (IOException ex) {
					logger.error("Session filter过滤时发生IO异常", ex);
				}
			}
		}
		if (!httpResponse.isCommitted()) {// 如果响应未提交,交给过滤器链

			try {
				filterChain.doFilter(request, response);
			} catch (ServletException sx) {
				filterConfig.getServletContext().log(sx.getMessage());
			} catch (IOException iox) {
				filterConfig.getServletContext().log(iox.getMessage());
			}
		}
	}
	
	
	/**
	 * 判断请求是否为有效Session
	 * 
	 * @param request
	 *            ServletRequest 请求对象
	 * @return boolean 返回true为有效Session
	 */
	private boolean isValidSession(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (httpRequest.getSession().getAttribute(SysParamsList.LOGIN_SESSION_NAME) != null) {
			return true;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Session无效,请求：" + httpRequest.getRequestURI());
		}
		return false;
	}
	
	/**
	 * 判断一个请求URI是否是不过滤的页面
	 * 
	 * @param requestURI
	 *            String 请求URI
	 * @return boolean 返回true为不过滤页面
	 */

	private boolean isFreePage(String requestURI) {
		boolean isFree = false;
		for (int i = 0; i < freePages.length; i++) {
			if (requestURI.endsWith(freePages[i])) {
				return true;
			}
		}
		return isFree;
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public String[] getFreePages() {
		return freePages;
	}

	public void setFreePages(String[] freePages) {
		this.freePages = freePages;
	}

	public String getToPage() {
		return toPage;
	}

	public void setToPage(String toPage) {
		this.toPage = toPage;
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
	
}
