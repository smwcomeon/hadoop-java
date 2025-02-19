package com.atguigu.mapreduce.table;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TableDriver {
	public static void main(String[] args) throws Exception {
		// 1.获取job对象信息
		Configuration configuration = new Configuration();
		Job job = Job.getInstance(configuration);

		// 2.设置加载jar位置
		job.setJarByClass(TableDriver.class);

		// 3.设置mapper和reducer的class类
		job.setMapperClass(TableMapper.class);
		job.setReducerClass(TableReducer.class);

		// 4.设置mapper的数据类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(TableBean.class);

		// 5.设置最终数据输出的类型
		job.setOutputKeyClass(TableBean.class);
		job.setOutputValueClass(NullWritable.class);

		// 6.设置输入数据和输出数据的路径
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

//		// 8.设置分区
//		job.setPartitionerClass(WordCountPartition.class);
//		job.setNumReduceTasks(2);

		// 处理小文件 如果不设置InputFormat,它默认用的是TextInputFormat.class
//		job.setInputFormatClass(CombineTextInputFormat.class);
//		CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
//		CombineTextInputFormat.setMinInputSplitSize(job, 2097152);

		// 7.提交
		boolean result = job.waitForCompletion(true);

		System.exit(result ? 0 : 1);

	}
}
