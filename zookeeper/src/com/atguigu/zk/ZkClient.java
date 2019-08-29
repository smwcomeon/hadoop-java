package com.atguigu.zk;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op.Create;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class ZkClient {
	// Zk客户端的连接地址
	private String connectString = "hadoop102:2181,hadoop102:2183,hadoop102:2184";
	// 回话超时时间 单位毫秒
	private int sessionTimeout = 2000;
	private ZooKeeper zkClient = null;
	
	//初始化zkCilent 
	@Before
	public void initZk() throws IOException {
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				System.out.println(event.getType() + "---" + event.getPath());
			}
		});

	}
	//创建子节点
	@Test
	public void createNode() throws Exception {
		//1.路径  2.创建节点内容 3.文件权限  4.数据类型 （持久性、非持久性、持久性序号、非持久性序号） 
		String create = zkClient.create("/atguigu", "wusong".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(create);
	}
	
	//获取子节点
	 @Test
	 public void getChild() throws Exception {
		 List<String> children = zkClient.getChildren("/", false);
		 //遍历节点
		 for (String child : children) {
			System.out.println(child);
		}
	 }
	 
	 //判断某一节点是否存在
	 @Test
	 public void isExist() throws Exception {
		 Stat exists = zkClient.exists("/atguigu", true);
		 
		 System.out.println(exists == null ? "no exist" : "exist" );
	 }
	
}
