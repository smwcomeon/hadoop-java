package com.atguigu.zkCase;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZkClient<E> {
	private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181,";
	private int sessionTimeout = 2000;
	private ZooKeeper zkClient = null;
	private String parentNode = "/servers";

	// 创建连接
	public void getConnect() throws Exception {

		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println(event.getType() + "--" + event.getPath());

				// 保证监听循环执行
				try {
					getServers();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 2.监听节点变化
	public void getServers() throws Exception {
		// 键盘parentNode路径的子节点
		List<String> children = zkClient.getChildren(parentNode, true);

		ArrayList<String> servers = new ArrayList<>();
		// 获取所有子节点数据
//		path:略 
//		watcher:监听器注册，这里用默认的监听器watcher的回调watchedEvent，也可以填null，不管回调 
//		stat:可以通过stat去接收stat的值，这里选择null，不需要
		for (String child : children) {
			byte[] data = zkClient.getData(parentNode + "/" + child, false, null);
			servers.add(new String(data));
		}

		// 输出节点数据
		System.out.println(servers);
	}

	// 具体业务逻辑
	public void business() throws Exception {
		System.out.println("ss 来送客");
		Thread.sleep(Long.MAX_VALUE);
	}

	public static void main(String[] args) throws Exception {
		// 1.连接server
		ZkClient client = new ZkClient();
		client.getConnect();

		// 2.监听节点变化
		client.getServers();

		// 3.实现自己的业务逻辑
		client.business();

	}
}
