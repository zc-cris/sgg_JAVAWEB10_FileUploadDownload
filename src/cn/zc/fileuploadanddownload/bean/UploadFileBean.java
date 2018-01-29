package cn.zc.fileuploadanddownload.bean;

import org.apache.commons.fileupload.FileItem;

/**
 * 
 * @type 用户上传的每一个FileItem对象和描述信息desc的集合，所以写成一个javaBean对象，并且和数据库一一对应 
 * @description: 
 * @author 张大帅
 */
public class UploadFileBean {
	//文件编号，由数据库自动生成
	private Integer id;
	//文件名字
	private String fileName;
	//文件存放在硬盘上的路径
	private String filePath;
	//文件描述
	private String desc;
	private FileItem fileItem;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public FileItem getFileItem() {
		return fileItem;
	}
	public void setFileItem(FileItem fileItem) {
		this.fileItem = fileItem;
	}
	public UploadFileBean(String fileName, String filePath, String desc, FileItem fileItem) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.desc = desc;
		this.fileItem = fileItem;
	}
	public UploadFileBean() {
	}

}
