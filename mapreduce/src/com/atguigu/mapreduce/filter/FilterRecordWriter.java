package com.atguigu.mapreduce.filter;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class FilterRecordWriter extends RecordWriter<Text, NullWritable> {
	private FSDataOutputStream atguigu = null;
	private FSDataOutputStream otherOut = null;

	//
	public FilterRecordWriter(TaskAttemptContext job) {
		Configuration configuration = new Configuration();

			try {
				// 获取文件系统
				FileSystem fs = FileSystem.get(configuration);

				// 创建两个文件的输出流
				atguigu = fs.create(new Path("D:/hadoopdata/OutputFormatFilter/atguigu.log"));
				otherOut = fs.create(new Path("D:/hadoopdata/OutputFormatFilter/otherout.log"));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void write(Text key, NullWritable value) throws IOException, InterruptedException {
		//区分输入的key是否包含atguigu
		if (key.toString().contains("atguigu")) {
			atguigu.write(key.toString().getBytes());
		}else {
			otherOut.write(key.toString().getBytes());
		}
	}

	@Override
	public void close(TaskAttemptContext context) throws IOException, InterruptedException {
		if (atguigu != null) {
			atguigu.close();
			otherOut.close();
		}
	}

}
