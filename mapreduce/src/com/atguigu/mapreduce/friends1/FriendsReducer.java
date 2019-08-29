package com.atguigu.mapreduce.friends1;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FriendsReducer extends Reducer<Text, Text, Text, Text> {
	//输入数据<好友，人>
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		// 1 拼接
		for (Text friend : values) {
			sb.append(friend).append(",");
		}

		// 2 写出
		context.write(key, new Text(sb.toString()));

	}
}
