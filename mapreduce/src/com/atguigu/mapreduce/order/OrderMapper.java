package com.atguigu.mapreduce.order;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class OrderMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
	OrderBean bean= new OrderBean();
	@Override
	protected void map(LongWritable key, Text value,
			Context context)
			throws IOException, InterruptedException {
		//1.读取一行  Order_0000001	Pdt_01	222.8
		String line = value.toString();
		
		//2.截取数据
		String[] fileds = line.split("\t");
//		System.out.println(fileds.length);
		
		//3.封装本对象
		bean.setOrderId(fileds[0]);	//Order_0000001
		bean.setPrice(Double.parseDouble(fileds[2]));//222.8
		
		//4.写出去
		context.write(bean, NullWritable.get());
		
	}
}
