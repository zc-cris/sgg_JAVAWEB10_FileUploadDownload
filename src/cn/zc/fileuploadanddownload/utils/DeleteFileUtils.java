package cn.zc.fileuploadanddownload.utils;

import java.io.File;

/**
 * 删除指定文件夹下的所有文件
 * 先判断，后删除，逻辑缜密，节省资源
 * @param file
 */
public class DeleteFileUtils {

public static void delete(File file) {
	if (file.isFile()) {
		System.out.println("传入的是文件，请传入文件夹");
		return;
	}
	File[] listFiles = file.listFiles();
	if (listFiles.length == 0) {
		System.out.println("传入的是空文件夹，请检查参数");
		return;
	}

	for (File f : listFiles) {
		deleteFile(f);
	}

}
/**
 * 循环递归删除文件
 * @param file
 */
private static void deleteFile(File file) {
	if(file.isDirectory()) {
		File[] files = file.listFiles();
		for(File  f: files) {
			deleteFile(f);
		}
	}
	file.delete();
	}
}
