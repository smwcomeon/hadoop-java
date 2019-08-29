package com.atguigu.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;
/*
 * 文件上传
 */
public class IOToHDFS {
	public static void main(String[] args) throws Exception {
		Configuration conf=new Configuration();

		// 1 获取文件系统
		FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.获取输出流
		FSDataOutputStream fos = fs.create(new Path("/user/atguigu/output/dongsi.txt"));

		//3。获取文件的输入流
		FileInputStream fis=new FileInputStream(new File("f:/dongsi.txt"));
		try {
			//4.文件的对接
			IOUtils.copyBytes(fis, fos, conf);
		} catch (Exception e) {
		}finally {
			IOUtils.closeStream(fis);
			IOUtils.closeStream(fos);
		}


	}
	@Test
	public  void IOPutFileHDFS() throws Exception{
		Configuration conf=new Configuration();

		// 1 获取文件系统
		FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.获取输出流
		FSDataOutputStream fos = fs.create(new Path("/user/atguigu/output/dongsi1.txt"));

		//3。获取文件的输入流
		FileInputStream fis=new FileInputStream(new File("e:/dongsi1.txt"));
		try {
			//4.文件的对接
			IOUtils.copyBytes(fis, fos, conf);
		} catch (Exception e) {
		}finally {
			IOUtils.closeStream(fis);
			IOUtils.closeStream(fos);
		}
	}
	//文件下载 以IO流的形式
	@Test
	public  void IOGetFileHDFS() throws Exception{
		Configuration conf=new Configuration();

		// 1 获取文件系统
		FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.获取输入流
		FSDataInputStream fis = fs.open(new Path("/user/atguigu/output/dongsi.txt"));

		//3.获取文件的输入流
		FileOutputStream fos=new FileOutputStream(new File("e:/dongsi1.txt"));

		try {
			//4.文件的对接
			IOUtils.copyBytes(fis, fos, conf);
		} catch (Exception e) {
		}finally {
			IOUtils.closeStream(fis);
			IOUtils.closeStream(fos);
		}
	}

	//下载大小大于128M的文件
	//第一块
	@SuppressWarnings("resource")
	@Test
	public void getFileFordHDFSSeek1() throws IOException, InterruptedException, URISyntaxException{
		Configuration conf=new Configuration();
		//1.获取文件系统
		FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.获取输入流
		FSDataInputStream fis = fs.open(new Path("/user/atguigu/hadoop-2.7.2.tar.gz"));

		//3.创建输出流
		FileOutputStream fos = new FileOutputStream(new File("f:/hadoop-2.7.2.tar.gz1")); 			

		//4.流对接（只读取1024）
		byte[] b=new byte[1024];
		for (int i = 0; i < 128*1024; i++) {
			fis.read(b);
			fos.write(b);
		}

		//5.关闭流
		try {
			IOUtils.closeStream(fis);
			IOUtils.closeStream(fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//第二块
	@Test
	public void getFileFordHDFSSeek2() throws IOException, InterruptedException, URISyntaxException{
		Configuration conf=new Configuration();
		//1.获取文件系统
		FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:8020"), conf, "atguigu");

		//2.获取输入流
		FSDataInputStream fis = fs.open(new Path("/user/atguigu/hadoop-2.7.2.tar.gz"));

		//3.创建输出流
		FileOutputStream fos = new FileOutputStream(new File("f:/hadoop-2.7.2.tar.gz2")); 			
		//4.流对接（只读取1024）
		//定位到128M
		byte[] b=new byte[1024];
		
		fis.seek(1024*1024*128);
		//5.关闭流
		try {
			IOUtils.copyBytes(fis, fos, conf);
		} catch (Exception e) {
		}finally {
			IOUtils.closeStream(fis);
			IOUtils.closeStream(fos);

		}
	}
	//然后第二块拼接到第一块上
	//type hadoop-2.7.2.tar.gz2 >> hadoop-2.7.2.tar.gz1
	
}
