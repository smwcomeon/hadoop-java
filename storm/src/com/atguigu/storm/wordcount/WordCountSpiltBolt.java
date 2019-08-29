package com.atguigu.storm.wordcount;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class WordCountSpiltBolt extends BaseRichBolt{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	
	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	
	//接受数据
	@Override
	public void execute(Tuple input) {
		//1.获取数据
		String line = input.getString(0);
		//2.截取
		String[] splits = line.split(" ");
		
		//3.写出去
		for (String word : splits) {
			collector.emit(new Values(word,1));
		}
	}


	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 声明字段 对应new Values(word,1)
		declarer.declare(new Fields("word","num"));
		
	}

}
