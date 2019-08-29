package com.atguigu.mapreduce.friends2;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


/*
A	I,K,C,B,G,F,H,O,D,
B	A,F,J,E,
C	A,E,B,H,F,G,K,
D	G,C,K,A,L,F,E,H,
E	G,M,L,H,A,F,B,D,
F	L,M,D,C,G,A,
G	M,
H	O,
I	O,C,
J	O,
K	B,
L	D,E,
M	E,F,
O	A,H,I,J,F,
谁是谁的好友
 */
public class FriendsMapper2 extends Mapper<LongWritable, Text, Text, Text> {
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//获取一行  A	I,K,C,B,G,F,H,O,D,
		String line = value.toString();
		
		//截取
		String[] fields = line.split("\t");
		
		
		String friend=fields[0];
		String[] person=fields[1].split(","); //[I,K,C,B,G,F,H,O,D]
		
		//排序
		Arrays.sort(person);
		//A B,D  
		//E B,D
		for (int i = 0; i < person.length-1; i++) {
			for (int j = i+1; j < person.length; j++) {
			// 发出 <人-人，好友> ，这样，相同的“人-人”对的所有好友就会到同1个reduce中去
				context.write(new Text(person[i] + "-" + person[j]), new Text(friend));
			}
		}
		
	}
}
