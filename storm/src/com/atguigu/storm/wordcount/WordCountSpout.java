package com.atguigu.storm.wordcount;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

//发送一条消息
public class WordCountSpout extends BaseRichSpout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	
	@Override
	public void nextTuple() {
		//发送数据
		collector.emit(new Values("I am ximenqing love jinlian"));
		
		//延时0.5
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("love")); //love字段对应上边的Values
		//相当于love = I am ximenqing love jinlian     key values形式
	}

}
