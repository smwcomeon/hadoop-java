package com.atguigu.hdfs;

import java.util.Arrays;

public class SplitTest {
	public static void main(String[] args) {
		String str="a1aa";
		String[] split = str.split("a");
		System.out.println(Arrays.toString(split));
	}
}
