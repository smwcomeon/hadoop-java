package com.atguigu.mapreduce.order;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
/**
Order_0000001	Pdt_01	222.8
Order_0000002	Pdt_05	722.4
Order_0000001	Pdt_05	25.8
Order_0000003	Pdt_01	222.8
Order_0000003	Pdt_01	33.8
Order_0000002	Pdt_03	522.8
Order_0000002	Pdt_04	122.4
 现在需要求出每一个订单中最贵的商品。
 */
public class OrderBean implements WritableComparable<OrderBean>{
	private String orderId;   //商品id
	private Double price;   //商品价格
	
	
	public OrderBean() {
		super();
	}

	public OrderBean(String orderId, Double price) {
		super();
		this.orderId = orderId;
		this.price = price;
	}
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	//序列化
	@Override
	public void write(DataOutput out) throws IOException {
	 out.writeUTF(orderId);
	 out.writeDouble(price);
		
	}
	//反序列化
	@Override
	public void readFields(DataInput in) throws IOException {
		this.orderId = in.readUTF();
		this.price = in.readDouble();
	}

	@Override
	public int compareTo(OrderBean o) {
		//完成两次排序 第一次按oderId正序排序  
		int compareResult = this.orderId.compareTo(o.getOrderId());
		
		//第二次按价格倒序排序
		if(compareResult == 0){
			compareResult=this.price > o.getPrice() ? -1 : 1;
		}
		return compareResult;
	}

	@Override
	public String toString() {
		return  orderId + "\t" + price ;
	}
	
}
