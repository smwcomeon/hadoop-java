package com.atguigu.mapreduce.index;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
atguigu--a.txt	3
atguigu--b.txt	2
atguigu--c.txt	2

每一个reducer只能接受key相同的mapper ，也可以修改key让两个不相同的key让系统认为是一个key
 */
public class TwoIndexReducer extends Reducer<Text, Text, Text, Text>{
	Text v=new Text();
	@Override
	protected void reduce(Text key, Iterable<Text> value, Context context)
			throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		
		for (Text text : value) {
			sb.append(text.toString()+"\t");
		}
		
		//设置输出value  没有对key进行处理 可以直接写出去
		v.set(sb.toString());
		
		//写出去
		context.write(key,v);
		
	}
	
}	
