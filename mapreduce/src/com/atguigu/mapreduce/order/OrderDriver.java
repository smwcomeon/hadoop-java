package com.atguigu.mapreduce.order;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class OrderDriver {
	public static void main(String[] args) throws Exception {
		// 1.获取job对象
		Configuration configuration = new Configuration();
		Job job = Job.getInstance(configuration);

		// 2.获取jar位置
		job.setJarByClass(OrderDriver.class);

		// 3.设置mapper和reducer位置
		job.setMapperClass(OrderMapper.class);
		job.setReducerClass(OrderReducer.class);

		// 4.设置mapper的key和value的类型
		job.setMapOutputKeyClass(OrderBean.class);
		job.setMapOutputValueClass(NullWritable.class);

		// 5.设置最终的key values的类型
		job.setOutputKeyClass(OrderBean.class);
		job.setOutputValueClass(NullWritable.class);

		// 6.设置输入输出路径
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

//		//8.设置分区
		job.setPartitionerClass(OrderPartitioner.class);

		// 9.设置reduce个数
		job.setNumReduceTasks(3);
		
		//10.关联OrderGroupCompare
		job.setGroupingComparatorClass(OrderGroupCompartor.class);
		
		// 7提交
		boolean result = job.waitForCompletion(true);
		System.exit(result ? 0 : 1);
	}
}
