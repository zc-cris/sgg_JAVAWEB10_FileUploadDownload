package cn.zc.fileuploadanddownload.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.zc.fileuploadanddownload.bean.UploadFileBean;
import cn.zc.fileuploadanddownload.utils.JdbcUtil;

/**
 * 上传和下载文件dao层实现类
 * @type 
 * @description: 
 * @author 张大帅
 */
public class UploadAndDonwloadFileDao implements IUploadAndDownFileDao {

	@Override
	public void save(UploadFileBean fileBean) throws SQLException {
		String sql = "insert into tb_files (fileName,filePath,fileDesc) values (?,?,?)";
		JdbcUtil.getQueryRunner().update(sql, fileBean.getFileName(),fileBean.getFilePath(),fileBean.getDesc());
	}

	@Override
	public List<Map<String, Object>> getAllFile() throws SQLException{
//		String sql = "SELECT fileName from tb_files";
//		return JdbcUtil.getQueryRunner().query(sql, new ColumnListHandler<>());
		String sql = "SELECT fileName,filePath from tb_files;";
		return JdbcUtil.getQueryRunner().query(sql, new MapListHandler());
	}
}
