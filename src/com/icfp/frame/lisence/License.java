package com.icfp.frame.lisence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.crypto.Cipher;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

public class License extends TimerTask implements ServletContextListener{
	
	public static boolean FLAG;
	
	private Timer t = null;
	
	private static Logger logger = Logger.getLogger(License.class);
	
	private String path = getClass().getResource("/").getPath() + "license/";
	
	@Override
	public void run() {
		byte[] bytes = null;
		try {
			bytes = secretDecrypt();
		} catch (Exception e1) {
			FLAG = false;
			logger.error("读取Lisence出错");
			logger.error(e1.getClass() + ": " + e1.getMessage());
			e1.printStackTrace();
		}
		String inBuf = new String(bytes);
		String begin = inBuf.substring(0, 8);
		String end = inBuf.substring(8);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = sdf.parse(begin);
			endDate = sdf.parse(end);
		} catch (ParseException e) {
			FLAG = false;
			logger.error("读取Lisence解析为时间格式出错");
			logger.error(e.getClass() + ": " + e.getMessage());
			e.printStackTrace();
		}
		Date now = new Date();
		if (now.after(beginDate) && now.before(endDate)) {
			FLAG = true;
		} else {
			FLAG = false;
		}
	}

	public void contextInitialized(ServletContextEvent sce) {
		long delay = 1000 * 1;
		t = new Timer();
		License lisence = new License();
		t.schedule(lisence, delay, 1000 * 60);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		t.cancel();
	}

	public byte[] secretDecrypt() throws Exception {
		byte[] result = null;
		Cipher cipher = Cipher.getInstance("AES");
		Key key = loadKey("byCFP.key");
		byte[] src = loadData("byCFP.dat");
		cipher.init(Cipher.DECRYPT_MODE, key);
		result = cipher.doFinal(src);
		return result;

	}

	// 取出数据
	private byte[] loadData(String dataString) throws FileNotFoundException,
			IOException {
		FileInputStream fisDat = new FileInputStream(path + dataString);
		byte[] src = new byte[fisDat.available()];
		int len = fisDat.read(src);
		int total = 0;
		while (len < src.length) {
			total += len;
			len = fisDat.read(src, len, src.length - len);
		}
		fisDat.close();
		return src;
	}

	// 取出密钥
	private Key loadKey(String keyString) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		FileInputStream fisKey = new FileInputStream(path + keyString);
		ObjectInputStream ois = new ObjectInputStream(fisKey);
		Key key = (Key) ois.readObject();
		ois.close();
		fisKey.close();
		return key;
	}
	
}
