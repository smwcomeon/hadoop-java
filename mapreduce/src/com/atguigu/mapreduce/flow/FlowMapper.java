package com.atguigu.mapreduce.flow;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 对数据进行处理，一般为业务处理逻辑层
 * 必须声明数据类型 前两个位输入类型 后两个为输出类型
 *LongWritable：表示行号   Text：表示每一行的内容
 */
public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
	FlowBean bean = new FlowBean();
	Text k = new Text();

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// 1.获取一行数据
		String line = value.toString();

		// 2.获取字段
		String[] fileds = line.split("\t");

		// 3.封装bean对象以及获取电话号码
		String phoneNum = fileds[1];
		Long upFlow = Long.parseLong(fileds[fileds.length - 3]);
		Long downFlow = Long.parseLong(fileds[fileds.length - 2]);

		// 通过setget方法注入值，在外边new对象避免对象重复new 消耗内存
		bean.set(upFlow, downFlow);
		k.set(phoneNum);

		// 4.写出去
		context.write(k, bean);

	}
}
