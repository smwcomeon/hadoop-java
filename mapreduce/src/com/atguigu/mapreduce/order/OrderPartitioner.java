package com.atguigu.mapreduce.order;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderPartitioner extends Partitioner<OrderBean, NullWritable> {

	@Override
	public int getPartition(OrderBean key, NullWritable values, int numReduceTasks) {
		//根据他OrderId进行hash排序  然后根据hash值进行分区
		return (key.getOrderId().hashCode() & Integer.MAX_VALUE) % numReduceTasks;
	}

}
