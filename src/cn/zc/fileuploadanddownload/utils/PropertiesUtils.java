package cn.zc.fileuploadanddownload.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @type 专门用来存储Properties配置文件键值对参数的class对象 
 * @description: 
 * @author 张大帅
 */
public class PropertiesUtils {
	
	private static PropertiesUtils propertiesUtils = new PropertiesUtils();
	private PropertiesUtils() {};
	public static PropertiesUtils newInstance() {
		return propertiesUtils;
	}
	//专门用来存储配置参数的map集合
	private Map<String, String> map = new HashMap<>();
	public void setKey(String key,String value) {
		this.map.put(key, value);
	}
	public String getKey(String key) {
		return this.map.get(key);
	}
}
