package com.atguigu.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
/**
 *1
 * @author Administrator
 *
 */
public class HDFSClient1 {
	public static void main(String[] args) throws Exception {
		// 直接配置访问集群的路径和访问集群的用户名称
		Configuration conf=new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop102:8020");
		
		// 1 获取文件系统
		FileSystem fs = FileSystem.get(conf);
		
		
		// 2 把本地文件上传到文件系统中
		fs.copyFromLocalFile(new Path("e:/xiyou.txt"), new Path("/user/atguigu/xiyou.txt"));
		
		//3.关闭资源
		fs.close();
	}
}
