package com.atguigu.mapreduce.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlowPartition extends Partitioner<Text, FlowBean> {

	@Override
	public int getPartition(Text key, FlowBean arg1, int arg2) {
		String phoneNum = key.toString().substring(0, 3);
		int partition=4;
		if("135".equals(phoneNum)){
			partition=0;
		}else if ("136".equals(phoneNum)) {
			partition=1;
		}else if ("137".equals(phoneNum)) {
			partition=2;
		}else if ("138".equals(phoneNum)) {
			partition=3;
		}
		return partition;
	}

}
