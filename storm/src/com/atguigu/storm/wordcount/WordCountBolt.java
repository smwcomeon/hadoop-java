package com.atguigu.storm.wordcount;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class WordCountBolt extends BaseRichBolt{
	//单词为key，单词出现的次数为value
	private Map<String,Integer> map = new HashMap<>();
	
	
	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void execute(Tuple input) {
		//1.获取传递过来的数据
		String word = input.getString(0);
		Integer num = input.getInteger(1);
		
		//2业务处理
		if(map.containsKey(word)) {
			Integer count = map.get(word);
			map.put(word, count+num);
		}else {
			map.put(word,num);
		}
		
		//3写出去word
		System.err.println(Thread.currentThread().getId()+"   word："+word+"   num："+map.get(word));
	}

	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
