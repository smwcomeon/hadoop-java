package com.z.hive;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class RemoveQuotesUDF extends UDF{
	
	public Text evaluate(Text str){
		if(str == null){
			return null;
		};
		if (StringUtils.isBlank(str.toString())) {
			return null;
		}
		
		return new Text(str.toString().replaceAll("\"", ""));
	}
	
	public static void main(String[] args) {
		RemoveQuotesUDF removeQuotesUDF = new RemoveQuotesUDF();
		//System.out.println(removeQuotesUDF.evaluate(new Text("\"31/Aug/2015:00:04:53 +0800\"")));
		System.out.println(removeQuotesUDF.evaluate(new Text(args[0])));
	}
}
