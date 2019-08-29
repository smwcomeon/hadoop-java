package com.atguigu.mapreduce.word;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class WordCountPartition extends Partitioner<Text, IntWritable>{

	@Override
	public int getPartition(Text key, IntWritable value, int numPartition) {
		// 需求按照单词首字母的ASCII的奇偶排序
		String line = key.toString();
		String fiword = line.substring(0, 1);
		
		//转换ASCII吗
		char[] charArray = fiword.toCharArray();
		int result = charArray[0];
		
		//按照奇偶分区
		if(result % 2 == 0){
			return 0;
		}else{
			return 1;
		}
	}

}
