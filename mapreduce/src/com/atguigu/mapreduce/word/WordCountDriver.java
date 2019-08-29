package com.atguigu.mapreduce.word;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountDriver {
	public static void main(String[] args) throws Exception {
		// 1.获取job对象信息
		Configuration configuration = new Configuration();
//		configuration.set("mapred.jar", "hadooptest.jar");
		Job job = Job.getInstance(configuration);

		// 2.设置加载jar位置
		job.setJarByClass(WordCountDriver.class);

		// 3.设置mapper和reducer的class类
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReduce.class);

		// 4.设置mapper的数据类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		// 5.设置最终数据输出的类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// 6.设置输入数据和输出数据的路径
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		// 8.设置分区
		job.setPartitionerClass(WordCountPartition.class);
		job.setNumReduceTasks(2);

		// //9.关联Combiner
		// job.setCombinerClass(WordCountCombiner.class);

		// 处理小文件 如果不设置InputFormat,它默认用的是TextInputFormat.class
		job.setInputFormatClass(CombineTextInputFormat.class);
		CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
		CombineTextInputFormat.setMinInputSplitSize(job, 2097152);

		// 7.提交
		boolean result = job.waitForCompletion(true);

		System.exit(result ? 0 : 1);

	}
}
