package com.icfp.frame.util;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
/***
 * excel导出类
 * @author liufei
 */
public class ExportExcel {
 /***************************************************************************
  * @param fileName EXCEL文件名称
  * @param listTitle EXCEL文件第一行列标题集合
  * @param listContent EXCEL文件正文数据集合
  * @return
  */
 public final static String exportExcel(String fileName,String[] Titles,int[] Width, List<Object> listContent) {
  String result="系统提示：Excel文件导出成功！";  
  // 以下开始输出到EXCEL
  try {    
   //定义输出流，以便打开保存对话框______________________begin
   HttpServletResponse response=ServletActionContext.getResponse();
   OutputStream os = response.getOutputStream();// 取得输出流      
   response.reset();// 清空输出流      
   response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("GB2312"),"ISO8859-1"));
// 设定输出文件头      
   response.setContentType("application/msexcel");// 定义输出类型    
   //定义输出流，以便打开保存对话框_______________________end

   /** **********创建工作簿************ */
   WritableWorkbook workbook = Workbook.createWorkbook(os);

   /** **********创建工作表************ */

   WritableSheet sheet = workbook.createSheet("Sheet1", 0);

   /** **********设置纵横打印（默认为纵打）、打印纸***************** */
   jxl.SheetSettings sheetset = sheet.getSettings();
   sheetset.setProtected(false);


   /** ************设置单元格字体************** */
   WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
   WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);

   /** ************以下设置三种单元格样式，灵活备用************ */
   // 用于标题居中
   WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
   wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
   wcf_center.setWrap(false); // 文字是否换行
   wcf_center.setBackground(Colour.GRAY_25);   //背景色
   
   // 用于正文居左
   WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
   wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
   wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
   wcf_left.setWrap(true); // 文字是否换行   
 

   /** ***************以下是EXCEL开头大标题，暂时省略********************* */
//   sheet.mergeCells(0, 0, colWidth, 0);
//   sheet.addCell(new Label(0, 0, title, wcf_center));
   /** ***************以下是EXCEL第一行列标题********************* */
   for (int i = 0; i < Titles.length; i++) {
	sheet.addCell(new Label(1, 0,"序号",wcf_center));
    sheet.addCell(new Label(i+2, 0,Titles[i],wcf_center));
    sheet.setColumnView(1,8);
    sheet.setColumnView(i+2, Width[i]);
   }
   /** ***************以下是EXCEL正文数据********************* */
//list<Object>形式数据正文添加
//   Field[] fields=null;
//   int i=1;
//   for(Object obj:listContent){
//	   fields=obj.getClass().getDeclaredFields();
//	   int j=0;
//	   for(Field v:fields){
//		   v.setAccessible(true);
//		   Object va=v.get(obj);
//		   if(va==null){
//			   va="";
//		   }
//		   sheet.addCell(new Label(j, i,va.toString(),wcf_left));
//		   j++;
//	   }
//	   i++;
//   }
 //list形式数据正文添加
   for(int i=0;i<listContent.size();i++)
   {
	   Object[] obj=(Object[])listContent.get(i);
	   for (int j = 0; j < obj.length; j++) {
		   sheet.addCell(new Label(1,i+1,i+1+"", wcf_left)); 
		   sheet.addCell(new Label(j+2, i + 1, (null==obj[j])?"":obj[j].toString(), wcf_left)); 
	}
   }
   
   /** **********将以上缓存中的内容写到EXCEL文件中******** */
   workbook.write();
   /** *********关闭文件************* */
   workbook.close(); 
   
   //关闭文件流
   os.close();
  } catch (Exception e) {
   result="系统提示：Excel文件导出失败，原因："+ e.toString();
   System.out.println(result); 
   e.printStackTrace();
  }
  return result;
 }
 
 /**
  * 导出excel表格文件,可添加主标题
  * @param title 表格主标题
  * @param fileName EXCEL文件名称
  * @param listTitle EXCEL文件第一行列标题集合
  * @param listContent EXCEL文件正文数据集合
  * @return
  */
 public final static String exportExcelWithTit(String fileName,String title,String[] Titles,int[] Width, List<Object> listContent) {
  String result="系统提示：Excel文件导出成功！";  
  // 以下开始输出到EXCEL
  try {    
   //定义输出流，以便打开保存对话框______________________begin
   HttpServletResponse response=ServletActionContext.getResponse();
   OutputStream os = response.getOutputStream();// 取得输出流      
   response.reset();// 清空输出流      
   response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("GB2312"),"ISO8859-1"));
// 设定输出文件头      
   response.setContentType("application/msexcel");// 定义输出类型    
   //定义输出流，以便打开保存对话框_______________________end

   /** **********创建工作簿************ */
   WritableWorkbook workbook = Workbook.createWorkbook(os);

   /** **********创建工作表************ */

   WritableSheet sheet = workbook.createSheet("Sheet1", 0);

   /** **********设置纵横打印（默认为纵打）、打印纸***************** */
   jxl.SheetSettings sheetset = sheet.getSettings();
   sheetset.setProtected(false);


   /** ************设置单元格字体************** */
   WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 12);
   WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 14,WritableFont.BOLD);

   /** ************以下设置三种单元格样式，灵活备用************ */
   // 用于首标题居中
   WritableFont TitFont = new WritableFont(WritableFont.ARIAL, 20,WritableFont.BOLD);
   WritableCellFormat wcf_tit = new WritableCellFormat(TitFont);
   wcf_tit.setBorder(Border.ALL, BorderLineStyle.NONE); // 线条
   wcf_tit.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
   wcf_tit.setAlignment(Alignment.CENTRE); // 文字水平对齐
   wcf_tit.setWrap(false); // 文字是否换行

   // 用于标题居中
   WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
   wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
   wcf_center.setWrap(false); // 文字是否换行
   wcf_center.setBackground(Colour.GRAY_25);   //背景色
   
   // 用于正文居左
   WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
   wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
   wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
   wcf_left.setWrap(true); // 文字是否换行   
 

   /** ***************以下是EXCEL开头大标题，暂时省略********************* */
   sheet.mergeCells(0, 0, Titles.length, 0);
   sheet.addCell(new Label(0, 0, title, wcf_tit));
   /** ***************以下是EXCEL第一行列标题********************* */
   for (int i = 0; i < Titles.length; i++) {
	sheet.addCell(new Label(0, 1,"序号",wcf_center));
    sheet.addCell(new Label(i+1,1,Titles[i],wcf_center));
    sheet.setColumnView(0,8);
    sheet.setColumnView(i+1, Width[i]);
   }
   /** ***************以下是EXCEL正文数据********************* */
//list<Object>形式数据正文添加
//   Field[] fields=null;
//   int i=1;
//   for(Object obj:listContent){
//	   fields=obj.getClass().getDeclaredFields();
//	   int j=0;
//	   for(Field v:fields){
//		   v.setAccessible(true);
//		   Object va=v.get(obj);
//		   if(va==null){
//			   va="";
//		   }
//		   sheet.addCell(new Label(j, i,va.toString(),wcf_left));
//		   j++;
//	   }
//	   i++;
//   }
 //list形式数据正文添加
   for(int i=0;i<listContent.size();i++)
   {
	   Object[] obj=(Object[])listContent.get(i);
	   for (int j = 0; j < obj.length; j++) {
		   sheet.addCell(new Label(0,i+2,i+1+"", wcf_left)); 
		   sheet.addCell(new Label(j+1, i + 2, (null==obj[j])?"":obj[j].toString(), wcf_left)); 
	}
   }
   
   /** **********将以上缓存中的内容写到EXCEL文件中******** */
   workbook.write();
   /** *********关闭文件************* */
   workbook.close(); 
   
   //关闭文件流
   os.close();
  } catch (Exception e) {
   result="系统提示：Excel文件导出失败，原因："+ e.toString();
   System.out.println(result); 
   e.printStackTrace();
  }
  return result;
 }
}