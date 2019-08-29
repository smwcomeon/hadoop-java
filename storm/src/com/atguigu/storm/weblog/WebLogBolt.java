package com.atguigu.storm.weblog;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class WebLogBolt implements IRichBolt{
	private int line_num = 0;
	private OutputCollector collector = null;
	@Override
	public void cleanup() {
		// 清除资源
		
	}

	@Override
	public void execute(Tuple input) {
		//1获取传输过来的数据
		//两种获取方法，第一种是通过定义的log来获取，log在WebLogSpout类中的declareOutputFields方法中定义
//		String stringByField = input.getStringByField("log");
		//第二种方法是通过WebLogSpout中nextTuple方法定义的collector.emit(new Values(str));
		String line = input.getString(0);
		
		//2.切割数据  www.atguigu.com	BBYH61456FGHHJ7JL89RG5VV9UYU7	2017-08-07 10:40:49
		String[] split = line.split("\t");
		String session_id = split[1];
		//3.统计发送数据
		line_num ++;
		
		//4.打印
		System.out.println(Thread.currentThread().getId()+"session_id"+session_id+"line_num"+line_num);
	}

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector=collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 声明输出字段类型
		declarer.declare(new Fields(""));
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
