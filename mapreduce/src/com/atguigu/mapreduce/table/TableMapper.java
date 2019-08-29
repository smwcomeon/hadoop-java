package com.atguigu.mapreduce.table;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import com.fasterxml.jackson.databind.util.BeanUtil;

public class TableMapper extends Mapper<LongWritable, Text, Text, TableBean> {
	
	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		TableBean bean = new TableBean();
		Text k = new Text();
		
		//获取map阶段截取的文件名称
		FileSplit inputSplit = (FileSplit) context.getInputSplit();
		String name = inputSplit.getPath().getName();
		
		//1.获取行内容
		String line = value.toString();
		
		if (name.startsWith("order")) {
			//order.txt 的截取片段 1001	01	1
			String[] fileds = line.split("\t");
			
			//2.设置key
			k.set(fileds[1]);
			
			//封装bean对象
			bean.setOrder_id(fileds[0]);
			bean.setP_id(fileds[1]);
			bean.setAmount(Integer.parseInt(fileds[2]));
			bean.setPname("");
			bean.setFlag("0");
			
		}else {//商品信息的截取文件  01	小米
			//切割
			String[] fileds = line.split("\t");
			
			//设置key
			k.set(fileds[0]);
			
			//封装bean对象
			bean.setOrder_id("");
			bean.setP_id(fileds[0]);
			bean.setAmount(0);
			bean.setPname(fileds[1]);
			bean.setFlag("1");
		}
		//3.封装数据写出去
		context.write(k, bean);
	}
}
