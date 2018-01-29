package cn.zc.fileuploadanddownload.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.zc.fileuploadanddownload.bean.UploadFileBean;

/**
 * 上传和下载的接口
 * @type 
 * @description: 
 * @author 张大帅
 */
public interface IUploadAndDownFileDao {
	/**
	 * 将每一个UploadFileBean对象保存到数据库中
	 * @param fileBean
	 * @throws SQLException
	 */
	void save(UploadFileBean fileBean) throws SQLException;

	/**
	 * 从数据库拿到所有文件的信息
	 * @return 
	 */
	List<Map<String, Object>> getAllFile() throws SQLException;
}