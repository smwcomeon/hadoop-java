package com.atguigu.storm.weblog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class WebLogSpout implements IRichSpout  {

	private static final long serialVersionUID = 1L;
	private BufferedReader reader;
	private SpoutOutputCollector collector=null;
	private String str=null;
	
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		// 打开文件
		try {
			this.collector=collector;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("e:/website.log"),"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void nextTuple() {
		// 循环带调用的方法
		try {
			while((str=this.reader.readLine()) != null) {
				//向外写数据
				//spout 向bolt写数据通过collector传输
				collector.emit(new Values(str));
				Thread.sleep(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//声明输出字段类型
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 过滤spout为log的spout 只想bolt传为log的spout
		declarer.declare(new Fields("log"));
		
	}
	
	
	@Override
	public void ack(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		
		try {
			if(reader != null) {
				reader.close();
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
