package cn.zc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * @type 文件上传基础（如果使用eclipse自带的浏览器测试，文件域item.getName()显示的是文件的总路径，如果使用其他浏览器，
 * 仅仅只是显示文件的名字）
 * @description: 
 * @author 张大帅
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. (create a factory for disk-based file items)
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//2. set factory constraints
		//设置文件读取到内存中的最大字节数
		factory.setSizeThreshold(1024*500);
		//设置临时的文件夹（如果文件的内容在读取中大于上面设置的最大字节数，就写入临时的文件夹）
		File repository = new File("D:"+File.separator+"repositoryFile");
		factory.setRepository(repository);
		
		//3. create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//4. set overall request size constraint 
		//设置上传的文件单个最大大小
		upload.setFileSizeMax(1024*1024*3);
		//设置上传的总文件的最大大小
		upload.setSizeMax(1024*1024*50);
		
		//5. parse the request
		try {
			//解析request对象
			List<FileItem> items = upload.parseRequest(request);
			//6. 遍历items
			for(FileItem item : items) {
				// 如果是普通的文本域
				if(item.isFormField()) {
					//form表单中的普通标签的name属性
					String fieldName = item.getFieldName();
					//form表单中的普通标签的value值
					String value = item.getString();
					System.out.println(fieldName);
					System.out.println(value);
					
				}else {			//如果是文件域则保存在d:\\files下
					
					//form表单中该文件域标签的name属性(uploadFile)
					String fieldName = item.getFieldName();
					//上传文件的文件名字(2.简单MVC流程.pptx)
					String name = item.getName();
					//上传文件的类型
					String contentType = item.getContentType();
					//上传文件的大小（字节）
					long size = item.getSize();
					//上传文件是否在内存中
					boolean inMemory = item.isInMemory();
					System.out.println(fieldName);
					System.out.println(name);
					System.out.println(contentType);
					System.out.println(size);
					System.out.println(inMemory);
					
					//将文件从item读出来然后写到硬盘上
					InputStream in = item.getInputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					//需要从输出到的路径
					name = "d:\\files\\"+name;
					OutputStream out = new FileOutputStream(name);
					while((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					in.close();
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
