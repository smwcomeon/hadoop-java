package com.atguigu.hdfs;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.net.DNSToSwitchMapping;

public class AutoRack implements DNSToSwitchMapping{
	
	@Override
	public List<String> resolve(List<String> ips) {
		// ips : hadoop102 或者1172.18.1.102
		ArrayList<String> list = new ArrayList<>();
		int number=0;
		//1.获取机架数
		if(ips !=null && ips.size()>1){
			for (String ip : ips) {
				if(ip.startsWith("hadoop")){
					String numbers = ip.substring(6);
					number=Integer.parseInt(numbers);
				}else if(ip.startsWith("172")){
					int index = ip.lastIndexOf(".");
					String numbers = ip.substring(index+1);
					number=Integer.parseInt(numbers);
				}
				//2.定义几家感知《把102、103定义为机架1；把104、105定义为机架2》
				if(number < 104){
					list.add("/rack1/"+number);
				}else {
					list.add("/rack2/"+number);
				}
			}
		}
		//返回结果
		return list;
	}
	
	@Override
	public void reloadCachedMappings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadCachedMappings(List<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
