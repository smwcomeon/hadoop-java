package com.atguigu.mapreduce.flowsort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
/**
 * 	Order_0000001	222.8
	Order_0000001	25.8
	Order_0000002	722.4
	Order_0000002	522.8
	Order_0000002	122.4
	Order_0000003	222.8
	Order_0000003	33.8
 * @author Administrator
 *
 */
public class FlowBean implements WritableComparable<FlowBean> {
	private Long upFlow;
	private Long downFlow;
	private Long sumFlow;
	
	//无参构造 ，实现Writable接口必须添加无参构造函数
	public FlowBean() {
		super();
	}

	//构造函数
	public FlowBean(Long upFlow, Long downFlow) {
		super();
		this.upFlow = upFlow;
		this.downFlow = downFlow;
		this.sumFlow=upFlow + downFlow;
	}
	
	public void set(Long upFlow, Long downFlow){
		this.upFlow = upFlow;
		this.downFlow = downFlow;
		this.sumFlow=upFlow + downFlow;
	}
	
	public Long getUpFlow() {
		return upFlow;
	}

	public void setUpFlow(Long upFlow) {
		this.upFlow = upFlow;
	}

	public Long getDownFlow() {
		return downFlow;
	}

	public void setDownFlow(Long downFlow) {
		this.downFlow = downFlow;
	}

	//序列化
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(upFlow);
		out.writeLong(downFlow);
		out.writeLong(sumFlow);
	}
	
	public Long getSumFlow() {
		return sumFlow;
	}

	public void setSumFlow(Long sumFlow) {
		this.sumFlow = sumFlow;
	}

	//反序列化 反序列化时没有参数限制，输出的时候必须有输入的顺序一致；
	@Override
	public void readFields(DataInput in) throws IOException {
		this.upFlow=in.readLong();
		this.downFlow=in.readLong();
		this.sumFlow=in.readLong();
		
	}
	
	@Override
	public String toString() {
		return upFlow + "\t" + downFlow + "\t" + sumFlow ;
	}

	//排序 倒序
	@Override
	public int compareTo(FlowBean o) {
		// TODO Auto-generated method stub
		return this.sumFlow >o.getSumFlow() ? -1: 1;
	}
}
