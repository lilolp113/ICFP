package com.icfp.frame.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.icfp.frame.params.ConfigParamList;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * jasperreport模板转换类
 * @author liufei
 * 
 */
public class JasperConverter{

	/**
	 * jasperreport模板导出Pdf
	 * @param fileName 模板名称
	 * @param list 数据信息列表
	 * @param parameters 参数
	 * date 2013-04-15
	 */
	@SuppressWarnings("unchecked")
	public static void ExportPdf(String fileName,List list,Map<String, String> parameters)
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		//项目根路径
		String path=request.getSession().getServletContext().getRealPath("/");
		//获取模板路径
		String filePath=path+ConfigParamList.JASPER+"\\"+fileName;
		File reportFile = new File(filePath);
		JasperPrint jasperPrint = null;
		try{
		//装载模板
		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(reportFile);
		//填充数据
		jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
				new JRBeanCollectionDataSource(list));
		}catch (Exception e) {
			e.printStackTrace();
		}
		try{
			if(null != jasperPrint){
				String fName=fileName.substring(0,fileName.indexOf("."));
				String pdf=path+ConfigParamList.PDFPATH+"\\"+fName+".pdf";
				//导出文件
				JasperExportManager.exportReportToPdfFile(jasperPrint, pdf);
			}
		}catch (JRException e) {
			e.printStackTrace();
		}
	}
	
}
