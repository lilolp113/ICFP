package com.icfp.frame.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.icfp.core.entity.AA02;

public class AA02Listener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		String path = servletContext.getRealPath("/")+"/desktop/dui/js/";
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		Object biz = wac.getBean("formbiz");
		String className = biz.getClass().getName();
		try {
			Class<?> c = Class.forName(className);
			Class<?>[] parameterTypes = {};
			Object arglist[] = {};
			Method m = c.getMethod("queryAA02",parameterTypes);
			List<?> lists =  (List<?>) m.invoke(biz,arglist);
			if(lists!=null && lists.size()>0){
				File file = new File(path+"aa02code.js");
				OutputStream out = new FileOutputStream(file,false);
				String code = "var AA02code = {";
				byte bcode[] = code.getBytes("utf-8");
				out.write(bcode);
				StringBuffer row = new StringBuffer();
				String AAA001 = "";
				for(int i=0;i<lists.size();i++){
					AA02 aa02 = (AA02)lists.get(i);
					String XAAA001 = aa02.getId().getAAA001();
					if(i==0){
						AAA001 = aa02.getId().getAAA001();
						row.append("'"+AAA001+"':{");
					}
					if(AAA001.equals(XAAA001) ){
						row.append("'"+aa02.getId().getAAA002()+"':'"+aa02.getAAA004()+"',");
					}else{
						row.append("'a':'a'},");
						code = row.toString();
						byte rcode[] = code.getBytes("utf-8");
						out.write(rcode);
						row.delete(0,row.length());
						AAA001 = aa02.getId().getAAA001();
						row.append("'"+AAA001+"':{");
						row.append("'"+aa02.getId().getAAA002()+"':'"+aa02.getAAA004()+"',");
					}
				}
				if(row!=null && !"".equals(row.toString())){
					byte ecode[] = (row.toString()+"'a':'a'}};").getBytes("utf-8");
					out.write(ecode);
				}else{
					byte ecode[] ="};".getBytes("utf-8");
					out.write(ecode);
				}
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
