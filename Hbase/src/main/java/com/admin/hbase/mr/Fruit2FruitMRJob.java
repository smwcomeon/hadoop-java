package com.admin.hbase.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
/**
 * 把数据从fruit表导入到fruit_mr表
 * @author admin
 *
 */
public class Fruit2FruitMRJob extends Configured implements Tool {
	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
		Fruit2FruitMRJob tool = new Fruit2FruitMRJob();
		
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
		
		Scan scan =new Scan();
		//设置扫描对象的一写属性 可以不设置
		scan.setCacheBlocks(false);
		scan.setCaching(500);
		
		TableMapReduceUtil.initTableMapperJob(
				"fruit",//Mapper操作的表名
				scan,//扫描对象 
				ReadFruitMapper.class,//制定Mapper类 
				ImmutableBytesWritable.class,//制定Mapper的输出key
				Put.class,//指定Mapper的输出类型
				job);//指定job
		TableMapReduceUtil.initTableReducerJob(
				"fruit_mr", 
				WriteFruitReducer.class,
				job);
		
		job.setNumReduceTasks(1);//设置reducer的个数
		boolean isSucceed = job.waitForCompletion(true);
		
		return isSucceed ? 0 : 1;
	}

	

}
