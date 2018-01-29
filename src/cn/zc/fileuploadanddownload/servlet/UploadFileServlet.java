package cn.zc.fileuploadanddownload.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.zc.fileuploadanddownload.bean.UploadFileBean;
import cn.zc.fileuploadanddownload.dao.IUploadAndDownFileDao;
import cn.zc.fileuploadanddownload.dao.UploadAndDonwloadFileDao;
import cn.zc.fileuploadanddownload.exception.UploadException;
import cn.zc.fileuploadanddownload.utils.DeleteFileUtils;
import cn.zc.fileuploadanddownload.utils.PropertiesUtils;

/**
 * 处理上传文件的servlet类
 * @type 
 * @description: 
 * @author 张大帅
 */
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String type = PropertiesUtils.newInstance().getKey("type"); // 文件类型
	private String uploadFilePath = PropertiesUtils.newInstance().getKey("upload.file.path"); // 文件的上传根路径
	private IUploadAndDownFileDao uploadFileDao = new UploadAndDonwloadFileDao();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = "";		//上传成功或者失败需要转发到的路径
		// 1.获取到ServletFileUpload对象
		request.setCharacterEncoding("utf-8");
		ServletFileUpload upload = getServletFileUpload();
		try {
			// 2.解析request对象得到FileItem对象的集合，并且创建一个UploadFileBean对象的集合
			List<FileItem> items = upload.parseRequest(request);
			List<UploadFileBean> fileBeans = new ArrayList<>();

			// 2.1校验每个文件的大小和总文件的大小是否合乎标准(在解析时，组件已经帮我们校验了，我们只需要得到异常
			// 的结果并且通知用户即可);

			// 3.检查文件的扩展名是否合乎规范并且把每一个FileItem对象设置到一个UploadFileBean对象中
			check(items, fileBeans, request);

			// 如果当前request域中没有message提示信息属性的值
			// 4.进行文件的上传操作
			upload(fileBeans);

			// 5.将文件信息保存到数据库
			save(fileBeans);
			path = "/case/success.jsp";
			
			//6.清空临时文件夹里的缓存
			DeleteFileUtils.delete(new File(PropertiesUtils.newInstance().getKey("temp.file.path")));

		} catch (FileUploadException | SQLException | UploadException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				request.setAttribute("message", e.getMessage());
				path = "/case/upload.jsp";
		} finally {
			request.getRequestDispatcher(path).forward(request, response);
		}
	}

	/**
	 * 校验文件名是否合法，如果合法就将FileItem对象设置到fileBean对象成员变量中去
	 * 
	 * @param items
	 * @param fileBeans
	 * @throws UploadException
	 *             自定义上传异常
	 * @throws UnsupportedEncodingException 
	 */
	private void check(List<FileItem> items, List<UploadFileBean> fileBeans, HttpServletRequest request)
			throws UploadException, UnsupportedEncodingException {
		String name = null;
		String subName = null;
		List<String> typeList = Arrays.asList(type.split(","));
		Map<String, String> formFields = new HashMap<>();
		for (FileItem item : items) {
			UploadFileBean fileBean = null;
			// 如果是普通的文本域
			if (item.isFormField()) {
				isFormField(formFields, item);
			} else { // 如果是文件域
				isFileField(fileBeans, typeList, item);
			}
		}
		fillFileBean(formFields, fileBeans);
	}
	
	/**
	 * 如果是普通的文本域进行的操作
	 * @param formFields
	 * @param item
	 * @throws UnsupportedEncodingException
	 */
	public void isFormField(Map<String, String> formFields, FileItem item) throws UnsupportedEncodingException {
		// System.out.println(item.getString());
		if (item.getString() == null || item.getString().equals("")) {
			throw new UploadException("文件描述不能为空！！！");
		}
		//防止getString获取到的中文在数据库中乱码，所以需要在这里指定编码格式
		formFields.put(item.getFieldName(), item.getString("UTF-8"));
	}
	
	/**
	 * 如果是文件域则进行的操作
	 * @param fileBeans
	 * @param typeList
	 * @param item
	 */
	public void isFileField(List<UploadFileBean> fileBeans, List<String> typeList, FileItem item) {
		String name;
		String subName;
		UploadFileBean fileBean;
		name = item.getName(); // 上传文件的文件名字(2.简单MVC流程.pptx)
		if (name == null || name == "") {
			throw new UploadException("不能上传空文件！！！");
		}
		subName = name.substring(name.lastIndexOf("."));
		// 如果上传的文件扩展名不符合要求的话，直接返回到上传页面并提醒用户
		if (!(typeList.contains(subName))) {
			throw new UploadException("您上传的名为" + name + "的文件的扩展名不正确，必须为" + type + "类型！！！");
		} else {
			// 注意：存储在数据库的文件名就是上传文件的名字，但是存储在硬盘上的文件路径需要使用uuid唯一区分，可以防止同名文件覆盖
			fileBean = new UploadFileBean(name,
					uploadFilePath + UUID.randomUUID().toString().replaceAll("-", "") + subName, null, item);
			fileBeans.add(fileBean);
		}
	}

	/**
	 * 将fileBeans集合中的每一个UploadFileBean对象的desc属性填上
	 * 
	 * @param formFields
	 * @param fileBeans
	 */
	private void fillFileBean(Map<String, String> formFields, List<UploadFileBean> fileBeans) {
		for (UploadFileBean uploadFileBean : fileBeans) {
			String fieldName = uploadFileBean.getFileItem().getFieldName();
			uploadFileBean.setDesc(formFields.get("desc" + fieldName.charAt(fieldName.length() - 1)));
		}
	}

	/**
	 * 文件的上传
	 * 
	 * @param fileBeans
	 * @throws IOException
	 */
	private void upload(List<UploadFileBean> fileBeans) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		for (UploadFileBean fileBean : fileBeans) {
			// 将文件从item读出来然后写到硬盘上
			in = fileBean.getFileItem().getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			out = new FileOutputStream(fileBean.getFilePath());
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.close();
			in.close();
		}
	}

	/**
	 * 将文件保存到数据库
	 * 
	 * @param fileBeans
	 * @throws SQLException
	 */
	private void save(List<UploadFileBean> fileBeans) throws SQLException {
		for (UploadFileBean uploadFileBean : fileBeans) {
			uploadFileDao.save(uploadFileBean);
		}
	}

	/**
	 * 获取到ServeltFileUpload对象
	 * 
	 * @return
	 */
	public ServletFileUpload getServletFileUpload() {
		PropertiesUtils newInstance = PropertiesUtils.newInstance();
		String perFileSize = newInstance.getKey("per.file.size");
		String totalFileSize = newInstance.getKey("total.file.size");
		String tempSize = newInstance.getKey("temp.size");
		String tempFilepath = newInstance.getKey("temp.file.path");

		// System.out.println(type+"----"+fileSize+"-----"+totalFileSize);

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置文件读取到内存中的最大字节数
		factory.setSizeThreshold(Integer.parseInt(tempSize));
		// 设置临时的文件夹（如果文件的内容在读取中大于上面设置的最大字节数，就写入临时的文件夹）
		File repository = new File(tempFilepath);
		factory.setRepository(repository);

		// 3. create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// 4. set overall request size constraint
		// 设置上传的文件单个最大大小
		upload.setFileSizeMax(Integer.parseInt(perFileSize));
		// 设置上传的总文件的最大大小
		upload.setSizeMax(Integer.parseInt(totalFileSize));
		return upload;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

}
