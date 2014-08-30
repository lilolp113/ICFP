<%@ page language="java" contentType="image/jpeg" import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*,java.io.*" pageEncoding="UTF-8"%>
<%!
	Random random = new Random();
	Color getRandColor(int from,int to){
		Random random = new Random();
		if(to>255) from=255;
		if(to>255) to=255;
		int rang = Math.abs(to - from);
		int r=from+random.nextInt(rang);
		int g=from+random.nextInt(rang);
		int b=from+random.nextInt(rang);
		return new Color(r,g,b);
	}
	String getOneChar(int type){
    	String result = null;
    	switch(type){
	    	case 0:
	    		result = String.valueOf(random.nextInt(10));
	    		break;
	    	case 1:
	    		result = String.valueOf((char)(random.nextInt(26)+65));
	    		break;
	    	case 2:
	    		if(random.nextBoolean()){
	    			result = String.valueOf(random.nextInt(10));
	    		}else{
	    			result = String.valueOf((char)(random.nextInt(26)+65));
	    		}
	    		break;
	    	default:
	    		result=null;
	    		break;
    	}
    	if(result==null)throw new NullPointerException("获取验证码出错");
    	return result;
    }
	String getValidateCode(int size,int type){
		StringBuffer validate_code = new StringBuffer();
		for(int i = 0; i < size; i++){
			validate_code.append(getOneChar(type));
		}
		return validate_code.toString();
	}
%>
<%
try{  
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires", 0);
    int reqCount = 4;
    int reqWidth = 150;
    int reqHeight = 50;
    int reqType = 0;
    int line = 200;
    Font font = new Font("Courier New",Font.BOLD,reqWidth/reqCount);
    BufferedImage image = new BufferedImage(reqWidth, reqHeight, BufferedImage.TYPE_INT_RGB);
    OutputStream os = response.getOutputStream(); 
    Graphics g = image.getGraphics();
    g.setColor(getRandColor(200,250));
    g.fillRect(0,0,reqWidth,reqHeight);
    g.setColor(getRandColor(160,200));
    for(int i=0;i<line;i++){
    	int x = random.nextInt(reqWidth);
    	int y = random.nextInt(reqHeight);
    	int xl = random.nextInt(12);
    	int yl = random.nextInt(12);
    	g.drawLine(x,y,x+xl,y+yl);
    }
    g.setFont(font);
    String validate_code = getValidateCode(reqCount,reqType);
    request.getSession().setAttribute("RandStr",validate_code);
    for(int i=0;i<reqCount;i++){
    	g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
    	int x = (int)(reqWidth/reqCount)*i;
    	int y = (int)((reqHeight+font.getSize())/2)-5;
    	g.drawString(String.valueOf(validate_code.charAt(i)),x,y);
    }
    g.dispose();
    ImageIO.write(image,"JPEG",os);
    os.flush();   
    os.close();   
    os=null;   
    response.flushBuffer();   
    out.clear();
    out = pageContext.pushBody();   
}catch(IllegalStateException e){   
	System.out.println(e.getMessage());   
	e.printStackTrace();
}
%>



