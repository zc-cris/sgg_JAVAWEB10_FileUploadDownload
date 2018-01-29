package cn.zc.fileuploadanddownload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩组件，专门用来压缩多个文件或者文件夹
 * @type 
 * @description: 
 * @author 张大帅
 */
public class ZipUtils {
    
    private ZipUtils(){
    }
    
    public static void doCompress(String srcFile, String zipFile) throws IOException {
        doCompress(new File(srcFile), new File(zipFile));
    }
    
    /**
     * 文件压缩
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    public static void doCompress(File srcFile, File zipFile) throws IOException {
        ZipOutputStream out = null;
        try {
        	//得到压缩后的zip文件的输出流
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            
            doCompress(srcFile, out);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();//记得关闭资源
        }
    }
    
    //1. 根据文件路径创建File对象
    public static void doCompress(String filelName, ZipOutputStream out) throws IOException{
        doCompress(new File(filelName), out);
    }
    //1.1 调用递归方法（实现文件夹的压缩）
    public static void doCompress(File file, ZipOutputStream out) throws IOException{
        doCompress(file, out, "");
    }
    public static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
    	//如果inFile是文件夹	D:\\
        if ( inFile.isDirectory() ) {
            File[] files = inFile.listFiles();
            if (files!=null && files.length>0) {
                for (File file : files) {
                    String name = inFile.getName();				
                    if (!"".equals(dir)) {		
                        name = dir + "/" + name;
                    }
                    ZipUtils.doCompress(file, out, name);	
                }
            }
        } else {
             ZipUtils.doZip(inFile, out, dir);
        }
    }
    
    //2. inFile就是每个非文件夹的文件，dir就是当前文件的路径（dir为""，说明最开始传入的File对象就是文件）
    public static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }
        //创建一个ZipEntry 类，用于表示 ZIP 文件条目。  利用这个类压缩和解压zip文件
        ZipEntry entry = new ZipEntry(entryName);
        //压缩输出流放入zip文件条目
        out.putNextEntry(entry);
        
        int len = 0 ;
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(inFile);
        while ((len = fis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            out.flush();
        }
        //关闭压缩输出流中的zip文件条目
        out.closeEntry();
        fis.close();
    }
    
    public static void main(String[] args) throws IOException {
//        doCompress("D:/java/", "D:/java.zip");
    }
    
}