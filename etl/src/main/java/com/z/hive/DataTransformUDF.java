package com.z.hive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.core.NewCookie;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 日期格式转换
 * @author admin
 *31/Aug/2015:00:04:53 +0800
 */
public class DataTransformUDF extends UDF {
	private final SimpleDateFormat inputFormat=new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss",Locale.ENGLISH);
	private final SimpleDateFormat outputFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Text evaluate (Text str){
		Text output = new Text();
		if (str==null) {
			return null;
		};
		if (StringUtils.isBlank(str.toString())) {
			return null;
		};
		Date parseDate;
		try {
			parseDate=inputFormat.parse(str.toString().trim());
			String outputDate=outputFormat.format(parseDate);
			
			//写出去 ,封装output
			output.set(outputDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
		 
	}
	
	public static void main(String[] args) {
		DataTransformUDF transformUDF = new DataTransformUDF();
		Text evaluaText = transformUDF.evaluate(new Text("31/Aug/2015:00:04:53 +0800"));
		System.out.println(evaluaText);
	}
}
