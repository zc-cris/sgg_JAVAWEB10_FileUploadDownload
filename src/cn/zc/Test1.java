package cn.zc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test1 {
	
	public static void main(String[] args) {
		List<String> list= new ArrayList<>();
		list.add("fa");
		list.add("eee");
		System.out.println(list.toString());	//[fa, eee]
		System.out.println(list);				//[fa, eee]
		String str = "12.345";
		str = str.substring(str.lastIndexOf("."));
		System.out.println(str);
	}
	
}
