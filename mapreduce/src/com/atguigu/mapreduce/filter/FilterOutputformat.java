package com.atguigu.mapreduce.filter;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//控制输出流
/**
1）需求
	过滤输入的log日志中是否包含atguigu
	（1）包含atguigu的网站输出到e:/atguigu.log
	（2）不包含atguigu的网站输出到e:/other.log
 */
public class FilterOutputformat extends FileOutputFormat<Text, NullWritable>{

	@Override
	public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		
		// 创建一个RecordWriter
		return new FilterRecordWriter(job);
	}

}
