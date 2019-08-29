  package com.atguigu.mapreduce.friends1;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/*
A:B,C,D,F,E,O
B:A,C,E,K
C:F,A,D,I
D:A,E,F,L
E:B,C,D,M,L
F:A,B,C,D,E,O,M
G:A,C,D,E,F
H:A,C,D,E,O
I:A,O
J:B,O
K:A,C,D
L:D,E,F
M:E,F,G
O:A,H,I,J
谁的好友有谁
 */
//第一次reducer A是谁的好友
public class FriendsMapper extends Mapper<LongWritable, Text, Text, Text>{
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//获取一行 A:B,C,D,F,E,O
		String line = value.toString();
		
		//截取
		String[] fields = line.split(":");
		
		//3.获取person和好友   
		String person = fields[0];
		String[] friends = fields[1].split(",");
		
		//4.写出去
		for (String friend : friends) {
			//输出<好友，人>
			context.write(new Text(friend), new Text(person));
		}
		
	}
}
