package com.atguigu.mapreduce.weblog;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WebLogMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//1.获取一行
		String line = value.toString();
		
		//2,.解析日志
		boolean result = parseLog(line,context);
		
		//3.判断是否合法
		if(!result) {
			return;
		}
		
		//4.合法的日志写出去
		context.write(value, NullWritable.get());
	}

	private boolean parseLog(String line, Context context) {
		//1.截取
		String[] fileds = line.split(" ");
		
		//2.判断记录长度是否大于11
		if(fileds.length > 11) {
			context.getCounter("map", "true").increment(1);	// 3 记录合法次数
			
			return true;
		}else {
			context.getCounter("map", "false").increment(1);// 4 记录不合法的次数
			return false;
		}
		
	}
	
}
