package com.atguigu.mapreduce.index;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/*
 （1）第一次预期输出结果
atguigu--a.txt	3
atguigu--b.txt	2
atguigu--c.txt	2
pingping--a.txt	 1
pingping--b.txt	3
pingping--c.txt	 1
ss--a.txt	2
ss--b.txt	1
ss--c.txt	1
（2）第二次预期输出结果
atguigu	c.txt-->2	b.txt-->2	a.txt-->3	
pingping	c.txt-->1	b.txt-->3	a.txt-->1	
ss	c.txt-->1	b.txt-->1	a.txt-->2	

 */
public class OneIndexMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	Text k = new Text();
	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		//1.获取一行
		String line = value.toString();
		
		//2.截取
		String[] fields = line.split(" ");
		
		//3.获取文件名称
		FileSplit inputSplit = (FileSplit) context.getInputSplit();
		String name = inputSplit.getPath().getName();
		
		//4.拼接
		for (int i = 0; i < fields.length; i++) {
			k.set(fields[i]+"--"+name);
			
			//5.输出
			context.write(k, new IntWritable(1));
		}
	}
	
	
	
	
	
}
