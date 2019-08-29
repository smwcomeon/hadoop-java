package com.atguigu.mapreduce.friends2;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FriendsReducer2 extends Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		// A B,D
		// E B,D
		StringBuffer sb = new StringBuffer();

		for (Text friend : values) {
			sb.append(friend).append(" ");
		}

		context.write(key, new Text(sb.toString()));

	}
}
