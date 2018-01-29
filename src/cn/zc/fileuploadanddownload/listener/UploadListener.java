package cn.zc.fileuploadanddownload.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.zc.fileuploadanddownload.utils.PropertiesUtils;

/**
 * ServletContext监听器，将上传参数设置给指定的类
 * @type 
 * @description: 
 * @author 张大帅
 */
public class UploadListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
    	//1. 读取配置文件
    	InputStream in = getClass().getClassLoader().getResourceAsStream("/uploadParameters.properties");
    	Properties pro = new Properties();
    	try {
    		//2.将配置文件加载进properties对象
			pro.load(in);
			//3.循环得到properties对象里面的每一个键值对
			for (Entry<Object, Object> entry : pro.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				//4.将键值对放进PropertiesUtils对象的map集合中
				PropertiesUtils.newInstance().setKey(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
