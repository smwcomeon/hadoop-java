package com.atguigu.mapreduce.table;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class TableReducer extends Reducer< Text, TableBean, TableBean, NullWritable>{
	@Override
	protected void reduce(Text key, Iterable<TableBean> values,
		Context context) throws IOException, InterruptedException {
		//准备存储数据的缓存
			//商品信息bean
		TableBean pbean = new TableBean();
			//order的bean
		ArrayList<TableBean> orderBeans =  new ArrayList<TableBean>();
		
		//根据文件的不同分别进行处理，flag为标记 0：订单数据处理   1：为商品信息
		for (TableBean bean : values) {
			if("0".equals(bean.getFlag())) { //订单数据处理  	 1001		1
				//订单中同一订单号可能有多个，所以放在ArrayList集合中
				// 1001 1
				// 1001 1
				TableBean orderBean = new TableBean();
			
				try {
					BeanUtils.copyProperties(orderBean, bean);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				orderBeans.add(orderBean);
			}else {
				try {
					BeanUtils.copyProperties(pbean, bean);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		//数据进行处理，把两个文件截取的不同属性放在一起
		for (TableBean bean : orderBeans) {
			bean.setPname(pbean.getPname());// 更新产品名称字段
			//加入Pname属性之后，遍历写出
			context.write(bean, NullWritable.get());
		}
		
	}
}
