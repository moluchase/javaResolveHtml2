package com.down;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.main.Main;

/**
 * 下载程序，其中包含了获取课件地址的函数
 * 
 * @author Administrator
 *
 */
public class DownloadCourse {

	/**
	 * 通过课件的Set获取课件的下载地址和课件名
	 * 
	 * @param set
	 *            set为课件的地址
	 * @param savePath
	 *            路径
	 */
	public static void getCourseURLAndName(Set<String> set, String savePath) {
		for (String string : set) {
			// 在传给Set的string时，以！为界将网址和filename分开了
			String[] path = string.split("!");
			String url = path[0];
			String fileName = path[1];

			downloadByURLAndName2(url, fileName, savePath);

		}
	}

	/**
	 * 用字节流处理,竟然可以
	 * 
	 * @param url
	 *            地址
	 * @param fileName
	 *            文件名
	 * @param savePath
	 *            路径
	 */
	public static void downloadByURLAndName2(String url, String fileName, String savePath) {

		long startTime;
		long bytes;
		startTime = System.currentTimeMillis();
		bytes = 0;
		try {
			// 建立连接，获得输入流
			URL httpUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
			con.setReadTimeout(60 * 1000);
			InputStream in = con.getInputStream();
			// 设置保存路径，获取输出流
			File file;
			new File(savePath).mkdirs();
			file = new File(savePath + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			// 读取与写入
			byte[] b = new byte[512 * 1024];
			int len;
			while ((len = in.read(b)) != -1) {
				bytes += len;
				fos.write(b, 0, len);
			}
			fos.close();
			in.close();
			System.out.println(fileName + "下载完成");
			// 主要是记录字节，时间的操作
			long endTime = System.currentTimeMillis();
			Map<String, Long> map = new HashMap<String, Long>();
			map.put("time", endTime - startTime);
			map.put("bytes", bytes);
			Main.bps.add(map);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
