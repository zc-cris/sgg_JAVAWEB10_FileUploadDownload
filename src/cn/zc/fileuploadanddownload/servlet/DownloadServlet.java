package cn.zc.fileuploadanddownload.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zc.fileuploadanddownload.dao.IUploadAndDownFileDao;
import cn.zc.fileuploadanddownload.dao.UploadAndDonwloadFileDao;
import cn.zc.fileuploadanddownload.utils.ZipUtils;

/**
 * 实现文件的下载（连接数据库查询文件信息，多个文件压缩下载）
 * @type 
 * @description: 
 * @author 张大帅
 */
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IUploadAndDownFileDao downloadDao = new UploadAndDonwloadFileDao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			 List<Map<String, Object>> lists = downloadDao.getAllFile();
//			 String name = (String) lists.get(0).get("fileName");
//			 String path = (String) lists.get(0).get("filePath");
//			 System.out.println(name+"-------"+path);
			 
			request.setAttribute("lists", lists);
			request.getRequestDispatcher("/case/download.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		String zipName = "myfile.zip";
		String[] filePaths = request.getParameterValues("downloadFile");
		response.setHeader("Content-Disposition","attachment; filename="+zipName);
		response.setContentType("APPLICATION/OCTET-STREAM");  
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try {
            for(String str : filePaths){
                ZipUtils.doCompress(str, out);
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            out.close();
        }
		
//		request.setCharacterEncoding("utf-8");
//		String[] filePaths = request.getParameterValues("downloadFile");
//		if(filePaths != null && filePaths.length > 0) {
//			OutputStream out = response.getOutputStream();
//			InputStream in = null;
//			String fileName = null;
//			String filePath = null;
//			for(String str : filePaths) {
//				fileName = str.substring(str.indexOf("+")+1);
//				filePath = str.substring(0, str.indexOf("+"));
//				response.setContentType("application/x-msdownload");
//				response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode((fileName), "UTF-8"));
//				in = new FileInputStream(filePath);
//				byte[] buffer = new byte[1024];
//				int len = 0;
//				while((len = in.read(buffer)) != -1) {
//					out.write(buffer,0,len);
//				}
//			}
//		}else {
//			//用户如果不勾选下载的资源就提示用户（这种参数判断最好是放在前台来做）
//			request.setAttribute("message", "请勾选要选择下载的资源");
//			this.doGet(request, response);
//		}
		
	}

}
