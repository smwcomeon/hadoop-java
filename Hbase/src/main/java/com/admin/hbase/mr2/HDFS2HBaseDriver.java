package com.admin.hbase.mr2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.admin.hbase.mr.Fruit2FruitMRJob;

public class HDFS2HBaseDriver extends Configured implements Tool{

	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
		HDFS2HBaseDriver tool = new HDFS2HBaseDriver();

		try {
			ToolRunner.run(conf, tool, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int run(String[] args) throws Exception {
		//实例化对象，用当前类示例化
		Configuration conf = this.getConf();

		//组装job
		Job job =Job.getInstance(conf);

		//设置jar
		job.setJarByClass(Fruit2FruitMRJob.class);

		//指定文件输入路径
		Path inPath = new Path("hdfs://hadoop-senior01.itguigu.com:8020/input_fruit/fruit.tsv");
		//添加输入路径
		FileInputFormat.addInputPath(job, inPath);

		//组装访问HDFS的Mapper
		job.setMapperClass(ReadFruitFromHDFSMapper.class);
		job.setMapOutputKeyClass(ImmutableBytesWritable.class);
		job.setMapOutputValueClass(Put.class);

		TableMapReduceUtil.initTableReducerJob(
				"fruit_from_hdfs", 
				WriteBaseFruitReducer.class,
				job);

		job.setNumReduceTasks(1);//设置reducer的个数
		boolean isSucceed = job.waitForCompletion(true);

		return isSucceed ? 0 : 1;
	}
}
