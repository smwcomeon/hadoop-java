package com.atguigu.mapreduce.flowsort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FlowSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
	FlowBean bean=new FlowBean();
	Text v=new Text();
	//数据处理类型 ：13480253104	180	180	360
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		//1.获取一行内容
		String line = value.toString();
		
		//2.截取字段 一行的数据内容
		String[] fileds = line.split("\t");
		
		//3.封装对象及获取电弧号码
		long upFlow=Long.parseLong(fileds[1]);
		long downFlow=Long.parseLong(fileds[2]);
		
		bean.set(upFlow, downFlow);
		v.set(fileds[0]);
		
		//4.写出去
		context.write(bean, v);

	}
}
