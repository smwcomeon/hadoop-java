package com.atguigu.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.s3.Block;
import org.junit.Test;
/**
 *1
 * @author Administrator
 *
 */
public class HDFSClient2 {
	public static void main(String[] args) throws Exception {

		Configuration conf=new Configuration();
		//conf.set("fs.defaultFS", "hdfs://hadoop102:8020");

		// 1 获取文件系统
		//FileSystem fs = FileSystem.get(conf);
		FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.从本地拷贝资料
		fs.copyFromLocalFile(new Path("f:/hadoop-2.7.2.tar.gz"), new Path("/user/atguigu/"));

		//3.关闭资源
		fs.close();
	}
	
	//获取文件系统名称
	@Test
	public	void getFileSystem() throws Exception, InterruptedException, URISyntaxException{
		Configuration conf = new Configuration();
		// 1 获取文件系统
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");
		//2获取文件系统名称ͳ
		System.out.print(fs);

		//3.关闭资源
		fs.close();
	}
	
	//获取文件系统名称
	@Test
	public	void getFileSystem2() throws Exception, InterruptedException, URISyntaxException{
		Configuration conf = new Configuration();
		// 1 获取文件系统
		FileSystem fs = FileSystem.get(conf);
		//2获取文件系统名称ͳ
		System.out.print(fs);

		//3.关闭资源
		fs.close();
	}
	
	//上传文件（删除本地源文件）
	@Test
	public	void putFileHDFS() throws Exception, InterruptedException, URISyntaxException{
		Configuration conf = new Configuration();
		// 1 获取文件系统
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//上传文件  true为删除本地源文件
		fs.copyFromLocalFile(true,new Path("e://nihao.txt"), new Path("/user/atguigu/"));

		System.out.print(fs);
		//3.关闭资源
		fs.close();
	}
	
	//文件下载
	@Test
	public	void getFileHDFS() throws Exception, InterruptedException, URISyntaxException{
		Configuration conf = new Configuration();
		// 1 获取文件系统
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//上传文件  删除本地源文件
		//fs.copyToLocalFile(new Path("/user/atguigu/xiyou1.txt"), new Path("e:/xiyou1.txt"));
		fs.copyToLocalFile(false, new Path("/user/atguigu/nihao.txt"), new Path("e:/nihao.txt"), true);
		System.out.print(fs);
		//3.关闭资源
		fs.close();
	}

	//创建目录
	@Test
	public	void mkdirFileHDFS() throws Exception, InterruptedException, URISyntaxException{
		Configuration conf = new Configuration();
		// 1 获取文件系统
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.创建文件夹(单级目录)
		fs.mkdirs(new Path("/user/atguigu/output"));
		System.out.print(fs);
		//3.关闭资源
		fs.close();
	}
	
	//删除文件夹
	@Test
	public void deleteAtHDFS() throws Exception{
		// 1 创建配置信息对象
		Configuration configuration = new Configuration();

		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"),configuration, "atguigu");	

		//2 删除文件夹 ，如果是非空文件夹，参数2必须给值true
		fs.delete(new Path("/user/atguigu/nihao.txt"), true);
		fs.close();
	}
	
	//重命名
	@Test
	public void renameAtHDFS() throws Exception{
		// 1 创建配置信息对象
		Configuration configuration = new Configuration();

		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"),configuration, "atguigu");	

		//2 删除文件夹 ，如果是非空文件夹，参数2必须给值true
		fs.rename(new Path("/user/atguigu/xiyou.txt"), new Path("/user/atguigu/xiyou0.txt"));
		//3.关闭资源
		fs.close();
	}
	
	//读取文件信息
	@Test
	public void readFileHDFS() throws Exception{
		// 1 创建配置信息对象
		Configuration configuration = new Configuration();

		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"),configuration, "atguigu");	

		//2 执行查看文件详情
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

		while (listFiles.hasNext()) {
			LocatedFileStatus status = listFiles.next();
			//获取文件路径
			System.out.println(status.getPath());

			//获取文件名称
			System.out.println(status.getPath().getName());

			//块大小
			System.out.println(status.getBlockSize());

			//文件权限
			System.out.println(status.getPermission());

			BlockLocation[] blockLocations = status.getBlockLocations();

			for (BlockLocation bl : blockLocations) {

				System.out.println("block-offset:" + bl.getOffset());
				//获取文件的数据节点	
				String[] hosts = bl.getHosts();

				for (String host : hosts) {
					System.out.println(host);
				}
			}
		}
		//3.关闭资源
		fs.close();
	}
	
	//获取文件夹详情
	@Test
	public void readFloderHDFS() throws Exception{
		// 1 创建配置信息对象
		Configuration configuration = new Configuration();

		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"),configuration, "atguigu");	

		//2 执行查看文件详情
		// 2 获取查询路径下的文件状态信息
		FileStatus[] listStatus = fs.listStatus(new Path("/user/atguigu/"));

		// 3 遍历所有文件状态
		for (FileStatus status : listStatus) {
			if (status.isFile()) {
				System.out.println("f--" + status.getPath().getName());
			} else {
				System.out.println("d--" + status.getPath().getName());
			}
		}
		//3.关闭资源
		fs.close();
	}
}
