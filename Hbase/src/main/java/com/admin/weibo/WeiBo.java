package com.admin.weibo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.el.Literal;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;


public class WeiBo {
	//操作HBase数据库 create一个HBaseConfiguration对象
	private Configuration conf = HBaseConfiguration.create(); 

	//微博内容表的表名
	private static final byte[] TABLE_CONTENT = Bytes.toBytes("weibo:content");
	//用户关系表的表名
	private static final byte[] TABLE_RELATIONS = Bytes.toBytes("weibo:relations");
	//微博收件箱表的表名
	private static final byte[] TABLE_RECEIVE_CONTENT_EMAIL = Bytes.toBytes("weibo:receive_content_email");

	public void initTable(){
		initNamespace();
		createTableContent();
		createTableRelations();
		createTableReceiveContentEmail();
	}

	/**
	 * 初始化命名空间
	 * @param 
	 */
	private void initNamespace() {
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(conf);
			//命名空间类是关系型数据库中的schema，可以当作文件夹
			NamespaceDescriptor weibo = NamespaceDescriptor.create("weibo")
					.addConfiguration("creatot","smw")
					.addConfiguration("create_time", String.valueOf(System.currentTimeMillis()))
					.build();
			admin.createNamespace(weibo);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != admin){
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 创建微博内容表
	 * Table Name:weibo:content
	 * RowKey:用户ID_时间戳
	 * ColumnFamily:info
	 * ColumnLabel:标题	内容		图片URL
	 * Version:1个版本
	 */
	public void createTableContent(){
		HBaseAdmin admin = null ;
		try {
			admin = new HBaseAdmin(conf);
			//创建表描述
			HTableDescriptor content= new HTableDescriptor(TableName.valueOf(TABLE_CONTENT));
			//创建列族描述
			HColumnDescriptor info = new HColumnDescriptor(Bytes.toBytes("info"));
			//设置块缓存
			info.setBlockCacheEnabled(true);
			//设置快缓存大小
			info.setBlocksize(2097512);
			//设置压缩方法
//			info.setCompressionType(Algorithm.SNAPPY);
			//设置版本确界
			info.setMaxVersions(1);
			info.setMinVersions(1);

			content.addFamily(info);
			admin.createTable(content);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != admin){
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 用户关系表
	 * Table Name:weibo:relations
	 * RowKey:用户ID
	 * ColumnFamily:attends,fans
	 * ColumnLabel:关注用户ID，粉丝用户ID
	 * ColumnValue:用户ID
	 * Version：1个版本
	 */
	public void createTableRelations(){
		HBaseAdmin admin = null ;
		try {
			admin = new HBaseAdmin(conf);
			//创建表描述
			HTableDescriptor relations= new HTableDescriptor(TableName.valueOf(TABLE_RELATIONS));
			//关注的人的列族
			HColumnDescriptor attends = new HColumnDescriptor(Bytes.toBytes("attends"));
			//设置块缓存
			attends.setBlockCacheEnabled(true);
			//设置快缓存大小
			attends.setBlocksize(2097512);
			//设置压缩方法
//			info.setCompressionType(Algorithm.SNAPPY);
			//设置版本确界
			attends.setMaxVersions(1);
			attends.setMinVersions(1);
			
			//创建粉丝表
			HColumnDescriptor fans = new HColumnDescriptor(Bytes.toBytes("fans"));
			fans.setBlockCacheEnabled(true);
			fans.setBlocksize(2097152);
			fans.setMaxVersions(1);
			fans.setMinVersions(1);
			
			relations.addFamily(attends);
			relations.addFamily(fans);
			admin.createTable(relations);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != admin){
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 创建微博收件箱表
	 * Table Name: weibo:receive_content_email
	 * RowKey:用户ID
	 * ColumnFamily:info
	 * ColumnLabel:用户ID-发布微博的人的用户ID
	 * ColumnValue:关注的人的微博的RowKey
	 * Version:1000
	 */
	public void  createTableReceiveContentEmail(){
		HBaseAdmin admin =  null;
		try {
			admin = new HBaseAdmin(conf);
			
			HTableDescriptor receive_content_email = new HTableDescriptor(TableName.valueOf(TABLE_RECEIVE_CONTENT_EMAIL));
			HColumnDescriptor info = new HColumnDescriptor(Bytes.toBytes("info")); 
			
			info.setBlockCacheEnabled(true);
			info.setBlocksize(2097152);
			info.setMaxVersions(1000);
			info.setBlocksize(1000);
			receive_content_email.addFamily(info);
			admin.createTable(receive_content_email);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (null != admin) {
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 发布微博
	 * a、微博内容表中数据+1
	 * b、向微博收件箱表中加入微博的Rowkey
	 * @param uid
	 * @param content
	 */
	public void publishContent(String uid,String content){
		HConnection connection = null;
		try {
			//获取连接
			connection = HConnectionManager.createConnection(conf);
			//a、微博内容表中添加一条数据，首先获取微博内容表的相关信息描述
			HTableInterface contentTBL = connection.getTable(TableName.valueOf(TABLE_CONTENT));
			//组装Rowkey
			long timestamp = System.currentTimeMillis();
			String rowKey = uid + "_"+ timestamp;
		
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes("info"), Bytes.toBytes("content"), timestamp, Bytes.toBytes(content));
			contentTBL.put(put);
			
			//b\向微博收件箱表中加入发布的rowkey
			//b.1 查询用户关系表，得到当前用户有那些粉丝
			HTableInterface relationsTBL = connection.getTable(TableName.valueOf(TABLE_RELATIONS));
			//b.2、取出目标数据
			Get get = new Get(Bytes.toBytes(uid));
			get.addFamily(Bytes.toBytes("fans"));
			
			Result result = relationsTBL.get(get);
			List<byte[]> fans =  new ArrayList<>();
			
			//遍历取出发布微博的用户的所有粉丝数据
			for (Cell cell: result.rawCells()) {
				fans.add(CellUtil.cloneQualifier(cell));

			}
			if (fans.size() <= 0) {
				return;
			}
			
			//开始操作收件表
			HTableInterface recTBL = connection.getTable(TableName.valueOf(TABLE_RELATIONS));
			List<Put> puts = new ArrayList<>();
			for (byte[] fan : fans) {
				Put fanPut = new Put(fan);
				fanPut.add(Bytes.toBytes("info"), Bytes.toBytes(uid),timestamp, Bytes.toBytes(rowKey));
				puts.add(fanPut);
			}
			recTBL.put(puts);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (null != connection) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 关注用户逻辑
	 * a、在微博用户关系表中，对当前主动操作的用户添加新的关注的好友
	 * b、在微博用户关系表中，对被关注的用户添加粉丝（当前操作的用户）
	 * c、当前操作用户的微博收件箱添加所关注的用户发布的微博rowkey
	 */
	public void addAttends (String uid,String... attends){
		//参数过滤 具体公司有各自的参数认证
		if(attends == null || attends.length <=0 || uid ==null || uid.length() <=0 ){
			return;
		}
		HConnection connection=null;
		try {
			connection =HConnectionManager.createConnection(conf);
			//用户关系表操作对象（连接到用户关系表）
			HTableInterface releationsTBL = connection.getTable(TableName.valueOf(TABLE_RELATIONS));
			List<Put> puts = new ArrayList<>();
			//a、在微波用户关系表中，添加新关注的好友
			Put attendPut = new Put(Bytes.toBytes(uid));
			for(String attend:attends){
				//为当前用户添加关注的人
				attendPut.add(Bytes.toBytes("attends"),Bytes.toBytes(attend),Bytes.toBytes(attend));
				//b、为被关注的人，添加粉丝
				Put fansPUt = new Put(Bytes.toBytes(attend));
				fansPUt.add(Bytes.toBytes("fans"), Bytes.toBytes(uid), Bytes.toBytes(uid));
				//将所有关注的人一个个的添加到puts（list）集合中
				puts.add(fansPUt);
			}
			puts.add(attendPut);
			releationsTBL.put(puts);
			
			//c1 微博收件箱添加关注的用户发布的微博内容（cotent)的rowkey
			HTableInterface contentTBL = connection.getTable(TableName.valueOf(TABLE_CONTENT));
			//扫描关注的人发布的所有微博
			Scan scan = new Scan();
			List<byte[]> rowkeys	= new ArrayList<>();
			
			for(String attend : attends){
				//过滤扫描rowkey，即配置位置被关注的人的uid_
				RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(attend + "_"));
				//为扫描对象指定过滤规则
				scan.setFilter(filter);
				//通过扫描对象得到过滤guize
				ResultScanner result = contentTBL.getScanner(scan);
				//迭代遍历扫描出来的结果
				Iterator<Result> iterator = result.iterator();
				while(iterator.hasNext()){
					//取出每一个符合扫描结果的那一行数据
					Result r	 = iterator.next();
					for (Cell cell : r.rawCells()) {
						//将得到的Rowkey放置于集和容器中
						rowkeys.add(CellUtil.cloneRow(cell));
					}
				}
			}
			
			//c2 取出的weibo rowkey放置当前操作用户的收件箱中
			if (rowkeys.size() <=0) {
				return;
			}
			HTableInterface recTBL = connection.getTable(TableName.valueOf(TABLE_RECEIVE_CONTENT_EMAIL));
			//用于存放多个关注的用户的发布的多条微博rowkey信息
			List<Put> recPuts = new ArrayList<>();
			for (byte[] rk : rowkeys) {
				Put put = new Put(Bytes.toBytes(uid));
				//uid_timestamp
				String rowkey = Bytes.toString(rk);
				//uid
				String attendUID	= rowkey.substring(0,rowkey.indexOf("_"));
				Long timestamp = Long.parseLong(rowkey.substring(rowkey.indexOf("_")+1));
				//将微波rowkey添加到指定单元格中
				put.add(Bytes.toBytes("info"), Bytes.toBytes(attendUID), timestamp, rk);
				recPuts.add(put);
			}
			recTBL.put(recPuts);
		
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != connection){
				try {
					connection.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 取消关注（remove)
	 * a、在微博用户关系表中，对当前主动操作的用户删除对应取关的好友
	 * b、在微博用户关系表中，对被取消关注的人删除粉丝（当前操作人）
	 * c、从收件箱中，删除取关的人的微博的rowkey
	 * 
	 */
	public void removeAttends(String uid, String... attends){
		//过滤数据
		if(uid == null || uid.length() <= 0 || attends == null || attends.length <= 0) return;
		HConnection connection = null;
		
		try {
			connection = HConnectionManager.createConnection(conf);
			//a、在微博用户关系表中，删除已关注的好友
			HTableInterface relationsTBL = connection.getTable(TableName.valueOf(TABLE_RELATIONS));
			
			//待删除的用户关系表中的所有数据
			List<Delete> deletes = new ArrayList<Delete>();
			//当前取关操作者的uid对应的Delete对象
			Delete attendDelete = new Delete(Bytes.toBytes(uid));
			//遍历取关，同时每次取关都要将被取关的人的粉丝-1
			for(String attend : attends){
				attendDelete.deleteColumn(Bytes.toBytes("attends"), Bytes.toBytes(attend));
				//b
				Delete fansDelete = new Delete(Bytes.toBytes(attend));
				fansDelete.deleteColumn(Bytes.toBytes("fans"), Bytes.toBytes(uid));
				deletes.add(fansDelete);
			}
			
			deletes.add(attendDelete);
			relationsTBL.delete(deletes);
			
			//c、删除取关的人的微博rowkey 从 收件箱表中
			HTableInterface recTBL = connection.getTable(TableName.valueOf(TABLE_RECEIVE_CONTENT_EMAIL));
			
			Delete recDelete = new Delete(Bytes.toBytes(uid));
			for(String attend : attends){
				recDelete.deleteColumn(Bytes.toBytes("info"), Bytes.toBytes(attend));
			}
			recTBL.delete(recDelete);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取微博实际内容
	 * a、从微博收件箱中获取所有关注的人的发布的微博的rowkey
	 * b、根据得到的rowkey去微博内容表中得到数据
	 * c、将得到的数据封装到Message对象中
	 */
	public List<Message> getAttendsContent(String uid){
		HConnection connection = null;
		try {
			connection = HConnectionManager.createConnection(conf);
			HTableInterface recTBL = connection.getTable(TableName.valueOf(TABLE_RECEIVE_CONTENT_EMAIL));
			//a、从收件箱中取得微博rowKey
			Get get = new Get(Bytes.toBytes(uid));
			//设置最大版本号
			get.setMaxVersions(5);
			List<byte[]> rowkeys = new ArrayList<byte[]>();
			Result result = recTBL.get(get);
			for(Cell cell : result.rawCells()){
				rowkeys.add(CellUtil.cloneValue(cell));
			}
			//b、根据取出的所有rowkey去微博内容表中检索数据
			HTableInterface contentTBL = connection.getTable(TableName.valueOf(TABLE_CONTENT));
			List<Get> gets = new ArrayList<Get>();
			//根据rowkey取出对应微博的具体内容
			for(byte[] rk : rowkeys){
				Get g = new Get(rk);
				gets.add(g);
			}
			//得到所有的微博内容的result对象
			Result[] results = contentTBL.get(gets);
			
			List<Message> messages = new ArrayList<Message>();
			for(Result res : results){
				for(Cell cell : res.rawCells()){
					Message message = new Message();
					
					String rowKey = Bytes.toString(CellUtil.cloneRow(cell));
					String userid = rowKey.substring(0, rowKey.indexOf("_"));
					String timestamp = rowKey.substring(rowKey.indexOf("_") + 1);
					String content = Bytes.toString(CellUtil.cloneValue(cell));
					
					message.setContent(content);
					message.setTimestamp(timestamp);
					message.setUid(userid);
					
					messages.add(message);
				}
			}
			
			return messages;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	/**
	 * 发布微博内容
	 * 添加关注
	 * 取消关注
	 * 展示内容
	 */
	
	public void testPublishContent(WeiBo wb){
		wb.publishContent("0001", "今天买了一包空气，送了点薯片，非常开心！！");
		wb.publishContent("0001", "今天天气不错。");
	}
	
	public void testAddAttend(WeiBo wb){
		wb.publishContent("0008", "准备下课！");
		wb.publishContent("0009", "准备关机！");
		wb.addAttends("0001", "0008", "0009");
	}
	
	public void testRemoveAttend(WeiBo wb){
		wb.removeAttends("0001", "0008");
	}
	
	public void testShowMessage(WeiBo wb){
		List<Message> messages = wb.getAttendsContent("0001");
		for(Message message : messages){
			System.out.println(message);
		}
	}
	
	public static void main(String[] args) {
		WeiBo weibo = new WeiBo();
		//weibo.initTable();
		
		weibo.testPublishContent(weibo);
		weibo.testAddAttend(weibo);
		weibo.testShowMessage(weibo);
		weibo.testRemoveAttend(weibo);
		weibo.testShowMessage(weibo);
	}
}
