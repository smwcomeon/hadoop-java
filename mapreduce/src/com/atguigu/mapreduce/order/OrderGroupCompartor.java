package com.atguigu.mapreduce.order;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class OrderGroupCompartor extends WritableComparator{
	//无参构造
	public OrderGroupCompartor() {
		super(OrderBean.class,true);
		//super(OrderBean.class);
	}
	//重写比较方法
	 @Override
	public int compare(WritableComparable a, WritableComparable b) {
		 OrderBean aBean=(OrderBean) a;
		 OrderBean bBean=(OrderBean) b;
		 //根据订单id比较，判断是不是同一组
		
		 /*
		    reducer处理的是bean对象（OrderBean bean）
		    bean不同不会放入一个reduce人中
		 	没重写方法之前是两组数据，不能进同一个reduce
		 	Order_0000001	222.8
			Order_0000001	25.8
			
			重写compare之后，欺骗reduce为同一个数据，只保留第一个
				实现同一组数据只有一组数据 Order_0000001	222.8
		 */
		 return aBean.getOrderId().compareTo(bBean.getOrderId());
	}
}	
