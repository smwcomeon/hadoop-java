package com.atguigu.mapreduce.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FlowDriver {
	public static void main(String[] args) throws Exception {
//		 System.setProperty("HADOOP_USER_NAME", "atguigu");
		// 1.获取配置信息
		Configuration configuration = new Configuration();
//		configuration.set("mapreduce.framework.name", "yarn");
//		configuration.set("fs.defaultFS", "hdfs://172.18.1.102:8020");
//        configuration.set("yarn.resourcemanager.address", "172.18.1.104:50090");
//        configuration.set("mapreduce.app-submission.cross-platform", "true");
//        configuration.set("mapreduce.job.jar", "C:/Users/Administrator/Desktop/wordcountdriver.jar");
		
		Job job = Job.getInstance(configuration);

		// 2.获取jar的存储路径
		job.setJarByClass(FlowDriver.class);

		// 3.关联map和reducer的class类
		job.setMapperClass(FlowMapper.class);
		job.setReducerClass(FlowReducer.class);

		// 4.设置map阶段输出的key和value类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		// 5.设置最后输出的数据key和value类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
//		//6.设置分区
//		job.setPartitionerClass(FlowPartition.class);
//		//设置reducernum
//		job.setNumReduceTasks(5);
		
		
		// 6.设置输入数据的路径和输出数据路径
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		// 7.提交
		boolean result = job.waitForCompletion(true);

		System.exit(result ? 0 : 1);
	}
}
