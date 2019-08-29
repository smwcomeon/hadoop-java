package com.admin.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PUT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;



public class HBaseDemo {
	//创建Haddop以及HBase管理配置对象
	public static Configuration conf;
	
	//使用HBaseConfiguration的单例模式
	static{
		conf=HBaseConfiguration.create();
	}
	
	/**
	 * 判断表是否存在
	 * @param tableName
	 * @return
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
	public static boolean isTableExist(String tableName) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		//在HBase中管理、访问需要先创建HBaseAdmin对象
		HBaseAdmin admin=new HBaseAdmin(conf);
		return admin.tableExists(tableName);
	}
	
	/**
	 * 创建表
	 * @param tableName
	 * @param columFamily
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 * @throws MasterNotRunningException 
	 */
	public static void createTable(String tableName,String ...columFamily) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		HBaseAdmin admin=new HBaseAdmin(conf);
		if(isTableExist(tableName)){
			System.out.println("表"+tableName+"已存在");
		}else {
			//创建表属性对象，表名需要转字节
			HTableDescriptor descriptor=new HTableDescriptor(TableName.valueOf(tableName));
			
			//创建多个列族
			for (String cf : columFamily) {
				descriptor.addFamily(new HColumnDescriptor(cf));
			}
			
			//根据表的配置，创建表
			admin.createTable(descriptor);
			System.out.println("表"+tableName+"创建成功");
		}
	}
	
	/**
	 * 删除表
	 * @param tableName
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 */
	public static void dropTable(String tableName) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (isTableExist(tableName)) {
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println("表"+tableName+"删除成功");
		}else{
			System.out.println("表"+tableName+"不存在");
		}
	}
	
	/**
	 * 向表中插入数据
	 * @param tableName
	 * @param rowKey
	 * @param columFamily
	 * @param colum
	 * @param value
	 * @throws IOException
	 */
	 public static void addRowData(String tableName,String rowKey,String columFamily,String colum,String value) throws IOException{
		 //创建HTable对象
		 HTable hTable = new HTable(conf, tableName); 
		
		 //向表中插入数据
		 Put put = new Put(Bytes.toBytes(rowKey));
		 //向put对象中组装数据
		 put.add(Bytes.toBytes(columFamily), Bytes.toBytes(colum), Bytes.toBytes(value));
		 
		 hTable.put(put);
		 hTable.close();
		 System.out.println("插入数据成功");
	 }
	
	 /**
	  * 删除多行数据
	  * @param tableName
	  * @param rows
	 * @throws IOException 
	  */
	 public static void deleteMultiRow(String tableName,String ... rows) throws IOException{
		 HTable hTable = new HTable(conf, tableName);
		//封装行
		 List<Delete> deleteList = new ArrayList<Delete>();
		
		 //遍历行
		 for (String row : rows) {
			Delete delete=new Delete(Bytes.toBytes(row));
			deleteList.add(delete);
		}
		 hTable.delete(deleteList);
		 hTable.close();
		 System.out.println("删除成功");
	 }
	 
	 /**
	  * 得到所有的数据
	  * @param tableName
	  * @throws IOException
	  */
	 public static void getAllRows(String tableName) throws IOException{
		 HTable hTable = new HTable(conf, tableName);
		 //得到用于扫描的region对象
		 Scan scan = new Scan();
		 //使用HTable得到resulcanner实现类的对象
		 ResultScanner resultScanner=hTable.getScanner(scan);
		 for (Result result : resultScanner) {
			Cell[] cells = result.rawCells();
			for (Cell cell : cells) {
				//得到rowkey
				System.out.println("CellUtil.cloneRow="+Bytes.toString(CellUtil.cloneRow(cell)));
				//得到列族
				System.out.println("CellUtil.cloneFamily="+Bytes.toString(CellUtil.cloneFamily(cell)));
				System.out.println("CellUtil.cloneQualifier="+Bytes.toString(CellUtil.cloneQualifier(cell)));
				System.out.println("CellUtil.cloneValue="+Bytes.toString(CellUtil.cloneValue(cell)));
				System.out.println("===================-=====");
			}
		
		}
	 }
	 
	
	public static void main(String[] args) {
		try {
			//System.out.println(isTableExist("student"));
			createTable("fruit_mr", "info");
			//dropTable("fruit_mr");
//			addRowData("person", "1011", "job", "name", "Nick");
//			addRowData("person", "1011", "job", "name1", "Nick1");
//			addRowData("person", "1011", "job", "name2", "Nick2");
//			addRowData("person", "1011", "hetay", "name3", "Nick3");
//			addRowData("person", "1011", "hetay", "name4", "Nick4");
//			addRowData("person", "1012", "job", "name", "Nick");
//			addRowData("person", "1012", "job", "name1", "Nick1");
//			addRowData("person", "1012", "job", "name2", "Nick2");
//			addRowData("person", "1012", "hetay", "name3", "Nick3");
//			addRowData("person", "1013", "hetay", "name4", "Nick4");
//			addRowData("person", "1013", "name", "wangze", "xiaoqiao");
//			addRowData("person", "1013", "name", "wangze", "daqiao");
			
			//deleteMultiRow("person", "1011");
		//	getAllRows("person");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
