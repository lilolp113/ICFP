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

import com.icfp.frame.entity.AA02;
import com.management.entity.SA03;
import com.management.entity.SA09;

public class AA02Listener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		String path = servletContext.getRealPath("/")+"/icfp/ui/js/";
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		Object biz = wac.getBean("coreBiz");
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
			
			Method m1 = c.getMethod("querySA03",parameterTypes);
			Class<?>[] sa09parameterTypes = {String.class};
			Method m2 = c.getMethod("querySA09",sa09parameterTypes);
			List<?> SA03lists =  (List<?>) m1.invoke(biz,arglist);
			int sa03size = SA03lists.size();
			if(SA03lists!=null && sa03size>0){
				File file = new File(path+"menulist.js");
				OutputStream out = new FileOutputStream(file,false);
				String code = "var __MenuList = [];";
				byte bcode[] = code.getBytes("utf-8");
				out.write(bcode);
				for(int i=0;i<sa03size;i++){
					StringBuffer role = new StringBuffer("");
					SA03 sa03 = (SA03)SA03lists.get(i);
					role.append("__MenuList.push({'roleid':'"+sa03.getSAB001()+"'");
					String hql = "select a from SA09 a,SA10 b where a.SAE001 = b.id.SAE001 and b.id.SAF001='"+ sa03.getSAB001() +"' ORDER BY a.SAE008";
					Object SA09arglist[] = {hql};
					List<?> SA09lists =  (List<?>) m2.invoke(biz,SA09arglist);
					int sa09size = SA09lists.size();
					if(SA09lists!=null && sa09size>0){
						role.append(",menus:[");
						for(int j=0;j<sa09size;j++){
							SA09 sa09 = (SA09)SA09lists.get(j);
							role.append("{");
							role.append("'SAE001':'"+sa09.getSAE001()+"',");
							role.append("'SAE004':'"+sa09.getSAE004()+"',");
							role.append("'SAE002':'"+sa09.getSAE002()+"',");
							role.append("'SAE007':'"+sa09.getSAE007()+"',");
							role.append("'SAE008':'"+sa09.getSAE008()+"',");
							role.append("'SAE009':'"+sa09.getSAE009()+"'");
							if(j==(sa09size-1)){
								role.append("}");
							}else{
								role.append("},");
							}
						}
						role.append("]");
					}
					role.append("});");
					if(role!=null && !"".equals(role.toString())){
						byte ecode[] = (role.toString()).getBytes("utf-8");
						out.write(ecode);
					}
				}
				out.close();
			}
			
			/*List<?> SA09lists =  (List<?>) m1.invoke(biz,arglist);
			if(SA09lists!=null && SA09lists.size()>0){
				File file = new File(path+"menulist.js");
				OutputStream out = new FileOutputStream(file,false);
				String code = "var MenuList = [];";
				byte bcode[] = code.getBytes("utf-8");
				out.write(bcode);
				StringBuffer row = new StringBuffer();
				for(int i=0;i<SA09lists.size();i++){
					SA09 sa09 = (SA09)SA09lists.get(i);
					row.append("MenuList.push({");
					row.append("'SAE001':'"+sa09.getSAE001()+"',");
					row.append("'SAE004':'"+sa09.getSAE004()+"',");
					row.append("'SAE002':'"+sa09.getSAE002()+"',");
					row.append("'SAE007':'"+sa09.getSAE007()+"',");
					row.append("'SAE009':'"+sa09.getSAE009()+"'");
					row.append("});");
				}
				if(row!=null && !"".equals(row.toString())){
					byte ecode[] = (row.toString()).getBytes("utf-8");
					out.write(ecode);
				}
				out.close();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
