package com.atguigu.zkCase;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZkServer {
	private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181,";
	private int sessionTimeout = 2000;
	private ZooKeeper zkClient=null;
	private String parentNode="/servers";
	
	// 创建连接
	public void getConnect() throws Exception {

		 zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
	}
	
	//注册节点
	public void regist(String hostname) throws Exception {
		String create = zkClient.create(parentNode+"/server", hostname.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(hostname+"-is online-"+create);
	}
	
	//具体业务逻辑
	public void business() throws Exception{
		System.out.println("zk--start----");
		Thread.sleep(Long.MAX_VALUE);
	}
	public static void main(String[] args) throws Exception {
		//1.获取连接
		ZkServer server = new ZkServer();
		server.getConnect();
		
		//2.注册（创建节点）
		server.regist(args[0]);
		
		//具体业务逻辑
		server.business();
	}
}
