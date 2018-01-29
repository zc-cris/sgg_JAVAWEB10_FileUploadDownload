package cn.zc.fileuploadanddownload.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sun.corba.se.impl.ior.GenericTaggedComponent;

/**
 * 通过c3p0连接池来获取数据库连接
 * 通过dbUtils工具包来获取QueryRunner核心类
 * @author Administrator
 *
 */
public class JdbcUtil {

	private static DataSource dataSource;
	static {
		//默认会去src目录下查找c3p0-config.xml配置文件
		dataSource = new ComboPooledDataSource();
	}
	
	public static QueryRunner getQueryRunner() throws SQLException {
		return new QueryRunner(dataSource);
	}
	
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
