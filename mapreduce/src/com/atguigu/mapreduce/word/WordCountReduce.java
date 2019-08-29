package com.atguigu.mapreduce.word;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {
		// 1.统计所有单词的个数
		int count=0;
		for (IntWritable value : values) {
			count +=value.get();
		}

		//2.输出所有单词的个数
		context.write(key, new IntWritable(count));
	}


}
