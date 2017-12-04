/**
 * 
 */
package uaes.bda.antianchor.demo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 张伦
 * @Description: TODO
 * @ClassName: MyUtil
 * @date May 5, 2017 4:09:15 PM
 */
public class MyUtil {
	private static Toast toast;
	/**
	 * 防止Toast点击多次不断提示
	 */
	/**
	 * 存储单位.
	 */
	private static final int STOREUNIT = 1024;

	/**
	 * 时间毫秒单位.
	 */
	private static final int TIMEMSUNIT = 1000;

	/**
	 * 时间单位.
	 */
	private static final int TIMEUNIT = 60;


	/**
	 * 转化文件单位.
	 * @param size 转化前大小(byte)
	 * @return 转化后大小
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / STOREUNIT;
		if (kiloByte < 1) {
			return size + " Byte";
		}

		double megaByte = kiloByte / STOREUNIT;
		if (megaByte < 1) {
			BigDecimal result = new BigDecimal(Double.toString(kiloByte));
			return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " KB";
		}

		double gigaByte = megaByte / STOREUNIT;
		if (gigaByte < 1) {
			BigDecimal result = new BigDecimal(Double.toString(megaByte));
			return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MB";
		}

		double teraBytes = gigaByte / STOREUNIT;
		if (teraBytes < 1) {
			BigDecimal result = new BigDecimal(Double.toString(gigaByte));
			return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " GB";
		}
		BigDecimal result = new BigDecimal(teraBytes);
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " TB";
	}

	/**
	 * 转化时间单位.
	 * @param time 转化前大小(MS)
	 * @return 转化后大小
	 */
	public static String getFormatTime(long time) {
		double second = (double) time / TIMEMSUNIT;
		if (second < 1) {
			return time + " MS";
		}

		double minute = second / TIMEUNIT;
		if (minute < 1) {
			BigDecimal result = new BigDecimal(Double.toString(second));
			return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " SEC";
		}

		double hour = minute / TIMEUNIT;
		if (hour < 1) {
			BigDecimal result = new BigDecimal(Double.toString(minute));
			return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MIN";
		}

		BigDecimal result = new BigDecimal(Double.toString(hour));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " H";
	}

	/**
	 * 转化字符串.
	 * @param source 转化前字符串
	 * @param encoding 编码格式
	 * @return 转化后字符串
	 */
	public static String convertString(String source, String encoding) {
		try {
			byte[] data = source.getBytes("ISO8859-1");
			return new String(data, encoding);
		} catch (UnsupportedEncodingException ex) {
			return source;
		}
	}
	public static void showToast(Context context, String content){
		if(toast == null){
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		}else{
			toast.setText(content);
		}
		toast.show();
	}
	/**
	 * 把一个 Object对象安全转换成整型
	 */
	public final static int convertToInt(Object value, int defaultValue) {
		if (value == null || "".equals(value.toString().trim())) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value.toString());
		} catch (Exception e) {
			try {
				return Double.valueOf(value.toString()).intValue();
			} catch (Exception e1) {
				return defaultValue;
			}
		}
	}
	private static long lastClickTime;
	public static boolean isFastClick(long ClickIntervalTime) {
		long ClickingTime = System.currentTimeMillis();
		if ( ClickingTime - lastClickTime < ClickIntervalTime) {
			return true;
		}
		lastClickTime = ClickingTime;
		return false;
	}
	public static void sendHandleMsg(int what, Object obj , Handler handler) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	//时间戳转字符串
	public static String getDateToString(long milSecond, String pattern) {
		Date date = new Date(milSecond);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	//字符串转时间戳
	public static long getStringToDate(String dateString, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = new Date();
		try{
			date = dateFormat.parse(dateString);
		} catch(ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}
}
