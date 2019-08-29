package com.admin.hbase.mr2;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;

public class WriteBaseFruitReducer extends TableReducer<ImmutableBytesWritable, Put,  NullWritable>{
	@Override
	protected void reduce(ImmutableBytesWritable immutableBytesWritable, Iterable<Put> puts,
			Context context)
			throws IOException, InterruptedException {
		for (Put put : puts) {
			//一个个数据写出去
			context.write(NullWritable.get(),put);
		}
	}
}
