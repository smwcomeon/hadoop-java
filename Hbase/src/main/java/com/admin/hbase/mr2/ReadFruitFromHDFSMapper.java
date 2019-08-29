package com.admin.hbase.mr2;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ReadFruitFromHDFSMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{
	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		//数据导入过程中可能会伴随着清洗
		//数学模型
		//初始化列族 和列名称
		String[] CFs={"info","other"};
		String[] qualifier = {"name","color"};
		/*,
		 *00001	apple	red 
		 */
		String[] values = line.split("\t");
		String rowKey = values[0];
		String name = values[1];
		String red = values[2];
		
		ImmutableBytesWritable immutableBytesWritable = new ImmutableBytesWritable(Bytes.toBytes(rowKey));
		
		//传入rowkey然后进行封装 组装cell
		Put put = new Put(Bytes.toBytes(rowKey));
		
		put.add(Bytes.toBytes(CFs[0]), Bytes.toBytes(qualifier[0]), Bytes.toBytes(name));
		put.add(Bytes.toBytes(CFs[0]), Bytes.toBytes(qualifier[1]), Bytes.toBytes(red));
		
		context.write(immutableBytesWritable, put);
	}
}
