package com.icfp.frame.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * @author liufei
 *
 */
public class DateUtil {

	 //用来全局控制 上一周，本周，下一周的周数变化   
    private static int weeks = 0;   
    private static int MaxDate;//一月最大天数   
    private static int MaxYear;//一年最大天数   
    
    /**
     * 获取当前年份
     * @return
     */
    public static String getYear()
    {
    	Date now = new Date();      
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy");
        String  year  = dateFormat.format(now);
        return year;
    }
              
    /**  
        * 得到二个日期间的间隔天数  
        */  
    public static String getTwoDay(String sj1, String sj2) {   
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");   
        long day = 0;   
        try {   
         java.util.Date date = myFormatter.parse(sj1);   
         java.util.Date mydate = myFormatter.parse(sj2);   
         day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);   
        } catch (Exception e) {   
         return "";   
        }   
        return day + "";   
    }   
  
  
    /**  
        * 根据一个日期，返回是星期几的字符串  
        *   
        * @param sdate  
        * @return  
        */  
    public static String getWeek(String sdate) {   
        // 再转换为时间   
        Date date = strToDate(sdate,null);   
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
        // int hour=c.get(Calendar.DAY_OF_WEEK);   
        // hour中存的就是星期几了，其范围 1~7   
        // 1=星期日 7=星期六，其他类推   
        return new SimpleDateFormat("EEEE").format(c.getTime());   
    }   
  
    /**  
     * @author liufei
        * 将string时间格式字符串转换为时间 yyyy-MM-dd   
        *   
        * @param strDate  
        * @return  
        */  
    public static Date strToDate(String strDate,String fmt) {
    	if(fmt==null || "".equals(fmt))
    	{
    		fmt="yyyy-MM-dd";
    	}
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);   
        ParsePosition pos = new ParsePosition(0);   
        Date strtodate = formatter.parse(strDate, pos);   
        return strtodate;   
    }
    
    /**
     * 将日期类型转换为对应字符串
     * @param pDate
     * @return
     */
    public static String dateToStr(Date pDate,String fmt)
    {
    	if(fmt==null || "".equals(fmt))
    	{
    		fmt="yyyy-MM-dd";
    	}
    	SimpleDateFormat smf=new SimpleDateFormat(fmt);
    	String strDate=smf.format(pDate);
    	return strDate;
    }
  
    /**  
        * 两个时间之间的天数  
        *   
        * @param date1  
        * @param date2  
        * @return  
        */  
    public static long getDays(String date1, String date2) {   
        if (date1 == null || date1.equals(""))   
         return 0;   
        if (date2 == null || date2.equals(""))   
         return 0;   
        // 转换为标准时间   
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");   
        java.util.Date date = null;   
        java.util.Date mydate = null;   
        try {   
         date = myFormatter.parse(date1);   
         mydate = myFormatter.parse(date2);   
        } catch (Exception e) {   
        }   
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);   
        return day;   
    }   
  
           
    /**
     *  计算当月最后一天,返回字符串  
     */
    public static String getDefaultDay(){     
       String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.DATE,1);//设为当前月的1号   
       lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号   
       lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天   
          
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 上月第一天   
     * @return
     */
    public static String getPreviousMonthFirst(){     
       String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.DATE,1);//设为当前月的1号   
       lastDate.add(Calendar.MONTH,-1);//减一个月，变为下月的1号   
       //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天   
          
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 获取当月第一天   
     * @return
     */
    public static String getFirstDayOfMonth(){     
       String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
       lastDate.set(Calendar.DATE,1);//设为当前月的1号   
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 获得本周星期日的日期     
     * @return
     */
    public static Date getSundayOFWeek(String fmt) {
        weeks = 0;
        if(fmt==null || "".equals(fmt))
        {
        	fmt="yyyy-MM-dd";
        }
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus+6);   
        Date monday = currentDate.getTime();   
           
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);
        
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);   
        ParsePosition pos = new ParsePosition(0);   
        Date date = formatter.parse(preMonday, pos);
        return date;   
    }
    
    /**
     * 获取本周任意星期时间
     * param day 加减天数 0-6
     * @return 所需星期日期字符串
     */
    public static String getEvyWeekday(int day) {   
        weeks = 0;   
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus+day);   
        Date monday = currentDate.getTime();   
           
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }
    
    /**
     * 获取本周任意星期时间
     * @author liufei
     * @param day 所取周期与周一间差数 0-6
     * @param fmt 日期格式
     * @return 所需日期
     */
    public static Date getEvyWeekday(int day,String fmt)
    {
    	weeks = 0; 
    	if(fmt==null || "".equals(fmt))
    	{
    		fmt="yyyy-MM-dd";
    	}
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus+day);   
        Date monday = currentDate.getTime();   
           
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);
        
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);   
        ParsePosition pos = new ParsePosition(0);   
        Date date = formatter.parse(preMonday, pos);
        
        return date;
    }
              
    /**
     * 获取当天时间
     */
    public static String getNowTime(String dateformat){   
        Date   now   =   new   Date();      
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat(dateformat);//可以方便地修改日期格式      
        String  hehe  = dateFormat.format(now);      
        return hehe;   
    }   
       
    /**
     * 获得当前日期与本周日相差的天数   
     * @return
     */
    private static int getMondayPlus() {   
        Calendar cd = Calendar.getInstance();   
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......   
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;         //因为按中国礼拜一作为第一天所以这里减1   
        if (dayOfWeek == 1) {   
            return 0;   
        } else {   
            return 1 - dayOfWeek;
        }   
    }   
       
    /**
     * 获得本周一的日期   
     * @return
     */
    public static Date getMondayOFWeek(String fmt){   
         weeks = 0;
         if(fmt==null || "".equals(fmt))
         {
        	 fmt="yyyy-MM-dd";
         }
         int mondayPlus = getMondayPlus();   
         GregorianCalendar currentDate = new GregorianCalendar();   
         currentDate.add(GregorianCalendar.DATE, mondayPlus);   
         Date monday = currentDate.getTime();   
            
         DateFormat df = DateFormat.getDateInstance();   
         String preMonday = df.format(monday);
         
         SimpleDateFormat formatter = new SimpleDateFormat(fmt);   
         ParsePosition pos = new ParsePosition(0);   
         Date date = formatter.parse(preMonday, pos);
         return date;   
    }   
       
    /**
     * 获得相应周的周六的日期   
     * @return
     */
    public static String getSaturday() {   
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }   
       
    /**
     * 获得上周星期日的日期   
     * @return
     */
    public static Date getPreviousWeekSunday(String fmt) {   
        if(fmt==null || "".equals(fmt))
        {
        	fmt="yyyy-MM-dd";
        }
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);       //获取本周日的时间
        Date date=calendar.getTime();
        GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(date);
    	gc.add(GregorianCalendar.WEEK_OF_YEAR, -1);
    	return gc.getTime();
    }   
  
    /**
     * 获得上周星期一的日期   
     * @return
     */
    public static Date getPreviousWeekday(String fmt) {   
        weeks--;
        if(fmt==null || "".equals(fmt.trim()))
        {
        	fmt="yyyy-MM-dd";
        }
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);  
        
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);   
        ParsePosition pos = new ParsePosition(0);   
        Date date = formatter.parse(preMonday, pos);
        return date;   
    }   
    
    /**
     * 获取某一日期相应几周日期
     * @param date 
     * @param no
     * @return
     */
    public static Date getEnyWeekDate(Date date,int no)
    {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(date);
    	gc.add(GregorianCalendar.WEEK_OF_YEAR, no);
    	return gc.getTime();
    }
       
    /**
     * 获得下周星期一的日期   
     * @return
     */
    public static String getNextMonday() {   
        weeks++;   
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }   
       
    /**
     * 获得下周星期日的日期
     */
    public static String getNextSunday() {   
           
        int mondayPlus = getMondayPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7+6);   
        Date monday = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preMonday = df.format(monday);   
        return preMonday;   
    }  
    
    /**
     * 根据传入时间获取相应周周一日期
     * @param dstr
     * @return
     */
    public static Date getEvyDayOfMon(String dstr)
    {
    	Date d=null;
    	SimpleDateFormat fomat=new SimpleDateFormat("yyyy-MM-dd");
    	Date date=null;
		try {
			date = fomat.parse(dstr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	Calendar calendar=Calendar.getInstance();
    	calendar.setTime(date);
    	int day=calendar.get(Calendar.DAY_OF_WEEK);
    	if(day==1)
    	{
    		calendar.add(Calendar.DATE,1);
    		d=calendar.getTime();
    	}else if(day==2){
    		d=calendar.getTime();
    	}else if(day==3){
    		calendar.add(Calendar.DATE,-1);
    		d=calendar.getTime();
    	}else if(day==4){
    		calendar.add(Calendar.DATE,-2);
    		d=calendar.getTime();
    	}else if(day==5){
    		calendar.add(Calendar.DATE,-3);
    		d=calendar.getTime();
    	}else if(day==6){
    		calendar.add(Calendar.DATE,-4);
    		d=calendar.getTime();
    	}else{
    		calendar.add(Calendar.DATE,-5);
    		d=calendar.getTime();
    	}
    	return d;
    }
    
    /**
     * 根据传入日期获取相应周周么时间
     * @param dstr
     * @return
     */
    public static Date getEvyDayOfSun(String dstr)
    {
    	Date d=null;
    	SimpleDateFormat fomat=new SimpleDateFormat("yyyy-MM-dd");
    	Date date=null;
		try {
			date = fomat.parse(dstr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	Calendar calendar=Calendar.getInstance();
    	calendar.setTime(date);
    	int day=calendar.get(Calendar.DAY_OF_WEEK);
    	if(day==1)
    	{
    		calendar.add(Calendar.DATE,7);
    		d=calendar.getTime();
    	}else if(day==2){
    		calendar.add(Calendar.DATE,6);
    		d=calendar.getTime();
    	}else if(day==3){
    		calendar.add(Calendar.DATE,5);
    		d=calendar.getTime();
    	}else if(day==4){
    		calendar.add(Calendar.DATE,4);
    		d=calendar.getTime();
    	}else if(day==5){
    		calendar.add(Calendar.DATE,3);
    		d=calendar.getTime();
    	}else if(day==6){
    		calendar.add(Calendar.DATE,2);
    		d=calendar.getTime();
    	}else{
    		calendar.add(Calendar.DATE,1);
    		d=calendar.getTime();
    	}
    	return d;
    }
       
              
    private static int getMonthPlus(){   
        Calendar cd = Calendar.getInstance();   
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);   
        cd.set(Calendar.DATE, 1);//把日期设置为当月第一天    
        cd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天    
        MaxDate=cd.get(Calendar.DATE);    
        if(monthOfNumber == 1){   
            return -MaxDate;   
        }else{   
            return 1-monthOfNumber;   
        }   
    }   
       
    /**
     * 获得上月最后一天的日期   
     * @return
     */
    public static String getPreviousMonthEnd(){   
        String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
      lastDate.add(Calendar.MONTH,-1);//减一个月   
      lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天    
      lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天    
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 获得下个月第一天的日期   
     * @return
     */
    public static String getNextMonthFirst(){   
        String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
      lastDate.add(Calendar.MONTH,1);//减一个月   
      lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天    
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 获得下个月最后一天的日期  
     * @return
     */
    public static String getNextMonthEnd(){   
        String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
       Calendar lastDate = Calendar.getInstance();   
      lastDate.add(Calendar.MONTH,1);//加一个月   
      lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天    
      lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天    
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 获得明年最后一天的日期   
     * @return
     */
    public static String getNextYearEnd(){   
        String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
      Calendar lastDate = Calendar.getInstance();   
      lastDate.add(Calendar.YEAR,1);//加一个年   
      lastDate.set(Calendar.DAY_OF_YEAR, 1);   
     lastDate.roll(Calendar.DAY_OF_YEAR, -1);   
       str=sdf.format(lastDate.getTime());   
       return str;     
    }   
       
    /**
     * 获得明年第一天的日期
     * @return
     */
    public static String getNextYearFirst(){   
        String str = "";   
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");       
  
      Calendar lastDate = Calendar.getInstance();   
      lastDate.add(Calendar.YEAR,1);//加一个年   
      lastDate.set(Calendar.DAY_OF_YEAR, 1);   
           str=sdf.format(lastDate.getTime());   
      return str;     
           
    }   
       
  /**
   * 获取本年有多少天
   * @return
   */
    private static int getYearOfDay(){   
        Calendar cd = Calendar.getInstance();   
        cd.set(Calendar.DAY_OF_YEAR,1);//把日期设为当年第一天   
        cd.roll(Calendar.DAY_OF_YEAR,-1);//把日期回滚一天。   
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);    
        return MaxYear;   
    }   
       
    private static int getYearPlus(){   
        Calendar cd = Calendar.getInstance();   
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天   
        cd.set(Calendar.DAY_OF_YEAR,1);//把日期设为当年第一天   
        cd.roll(Calendar.DAY_OF_YEAR,-1);//把日期回滚一天。   
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);   
        if(yearOfNumber == 1){   
            return -MaxYear;   
        }else{   
            return 1-yearOfNumber;   
        }   
    }   
    /**
     * 获得本年第一天的日期   
     * @return
     */
    public static String getCurrentYearFirst(){   
        int yearPlus = getYearPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE,yearPlus);   
        Date yearDay = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preYearDay = df.format(yearDay);   
        return preYearDay;   
    }   
              
    /**
     * 获得本年最后一天的日期 *
     * @return
     */
    public static String getCurrentYearEnd(){   
        Date date = new Date();   
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格式      
        String  years  = dateFormat.format(date);      
        return years+"-12-31";   
    }   
       
       
    /**
     * 获得上年第一天的日期 *
     * @return
     */
    public static String getPreviousYearFirst(){   
        Date date = new Date();   
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格式      
        String  years  = dateFormat.format(date); int years_value = Integer.parseInt(years);     
        years_value--;   
        return years_value+"-1-1";   
    }   
       
    /**
     * 获得上年最后一天的日期 
     * @return
     */
    public static String getPreviousYearEnd(){   
        weeks--;   
        int yearPlus = getYearPlus();   
        GregorianCalendar currentDate = new GregorianCalendar();   
        currentDate.add(GregorianCalendar.DATE,yearPlus+MaxYear*weeks+(MaxYear-1));   
        Date yearDay = currentDate.getTime();   
        DateFormat df = DateFormat.getDateInstance();   
        String preYearDay = df.format(yearDay);   
        getThisSeasonTime(11);   
        return preYearDay;   
    }   
       
    /**
     * 获得本季度   
     * @param month
     * @return
     */
    public static String getThisSeasonTime(int month){   
        int array[][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};   
        int season = 1;   
        if(month>=1&&month<=3){   
            season = 1;   
        }   
        if(month>=4&&month<=6){   
            season = 2;   
        }   
        if(month>=7&&month<=9){   
            season = 3;   
        }   
        if(month>=10&&month<=12){   
            season = 4;   
        }   
        int start_month = array[season-1][0];   
        int end_month = array[season-1][2];   
           
        Date date = new Date();   
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//可以方便地修改日期格式      
        String  years  = dateFormat.format(date);      
        int years_value = Integer.parseInt(years);   
           
        int start_days =1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);   
        int end_days = getLastDayOfMonth(years_value,end_month);   
        String seasonDate = years_value+"-"+start_month+"-"+start_days+";"+years_value+"-"+end_month+"-"+end_days;   
        return seasonDate;   
           
    }   
       
    /**  
     * 获取某年某月的最后一天  
     * @param year 年  
     * @param month 月  
     * @return 最后一天  
     */  
   private static int getLastDayOfMonth(int year, int month) {   
         if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8  
                   || month == 10 || month == 12) {   
               return 31;   
         }   
         if (month == 4 || month == 6 || month == 9 || month == 11) {   
               return 30;   
         }   
         if (month == 2) {   
               if (isLeapYear(year)) {   
                   return 29;   
               } else {   
                   return 28;   
               }   
         }   
         return 0;   
   }   
   /**  
    * 是否闰年  
    * @param year 年  
    * @return   
    */  
  public static boolean isLeapYear(int year) {   
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);     
  }   
	
}
