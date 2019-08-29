package com.admin.hbase.mr;

import java.io.IOException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

public class ReadFruitMapper extends TableMapper<ImmutableBytesWritable, Put>{
	
	@Override
	protected void map(ImmutableBytesWritable key, Result value,Context context)
			throws IOException, InterruptedException {
		//获取数据。相当于line.get
		Put put = new Put(key.get());
		
		//遍历该rowkey下面的所有单元格
		for(Cell cell: value.rawCells()){
			//如果当前单元格访问到的数据是info列族，则进行下一步操作
			if ("info".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
				//name color 是info列族下的列名
				if("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))){
					//如果对数据进行清洗和转换则需要取出数据，然后重新封装
					put.add(cell);
				}else if ("color".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
					put.add(cell);
				}
			}
		}
		context.write(key, put);
	}
}
