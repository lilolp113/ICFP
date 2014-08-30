package com.icfp.frame.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.icfp.frame.params.ConfigParamList;

/*
* PDF转SWF工具
* @author liufei
*
*/
public class Converter {
	
	/**
	 * 文件转换swf
	 * @param fileName 转换后swf文件名，需和对应文件同名
	 * @return
	 * @throws IOException
	 */
	public static int convertPDF2SWF(String fileName) throws IOException {
		HttpServletRequest request=ServletActionContext.getRequest();
		//项目根路径
		String path=request.getSession().getServletContext().getRealPath("/");
		//目标路径不存在则建立目标路径
		String storePath=path+ConfigParamList.SWFPATH;
		File dest = new File(storePath);
		if (!dest.exists()) dest.mkdirs();
		
		//源文件不存在则返回
		String pdfPath=path+ConfigParamList.PDFPATH;
		File source = new File(pdfPath);
		if (!source.exists()) return 0;
		
		//获取pdf2swf执行程序
		String softPath=ConfigParamList.SWFTools;
		//获取中文字符集文件
		String xpdf=ConfigParamList.XPDF;
		//调用pdf2swf命令进行转换
		String command= softPath+" -t \""+pdfPath+"\\"+fileName+".pdf\" -o  \""+storePath+"\\"+fileName+".swf\" -s flashversion=9 -s languagedir="+xpdf;  
		System.out.println("cmd:"+command);
		Process process = Runtime.getRuntime().exec(command); // 调用外部程序   
		final InputStream is1 = process.getInputStream();   
		new Thread(new Runnable() {   
		    public void run() {   
		        BufferedReader br = new BufferedReader(new InputStreamReader(is1));    
		        try {
					while(br.readLine()!= null) ;
				} catch (IOException e) {
					e.printStackTrace();
				}   
		    }   
		}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区   
		InputStream is2 = process.getErrorStream();   
		BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));    
		StringBuilder buf = new StringBuilder(); // 保存输出结果流   
		String line = null;   
		while((line = br2.readLine()) != null) buf.append(line); // 循环等待ffmpeg进程结束   
		System.out.println("输出结果为：" + buf);
		
		while (br2.readLine() != null); 
		
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return process.exitValue();

		
	}
}