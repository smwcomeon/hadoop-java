package com.atguigu.mapreduce.index;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/**
 atguigu--a.txt	3
 */
public class TwoIndexMapper extends Mapper<LongWritable, Text, Text, Text>{
	Text k = new Text();
	Text v = new Text();
	
	@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
		//获取一行、截取
		String line = value.toString();
		String[] fields = line.split("--");
		
		k.set(fields[0]);
		v.set(fields[1]);
		
		//写出去
		context.write(k, v);
	}
}
