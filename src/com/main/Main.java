package com.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.down.DownLoadVideo;
import com.down.DownloadCourse;

/**
 * 主程序，操作的是learn下的界面
 * 
 * @author Administrator
 *
 */
// Android方面的视频
// int[]classNos={606,650,513,536,353,162,656,468,648,500,268,341,136,684,308,358,431,653,302,462,223};
// int[] classNos={304,142};

// Java方面的
// 设计模式
// int[] classNos={112,261,214,146,415,257,165,145};
// 其他
// int[]classNos={352,354,271,213,272,482};
// 并发
// int[]classNos={587,630,631,632};
// 加密
// int[]classNos={286,287,288,289};
// int[]classNos={289};
// 案例
// int[] classNos={586,679,531,629,584,567,518,180,585,466,283,265,368,401};
// 框架
// int[] classNos={558,524,465,498,452,478,154,260};
// 工具
// int[] classNos={443,356,109};

// Python
// int[] classNos={177,475,458,563,416,457,550,317,581,712,532,595};

// 数据库
// MySQL
// int[] classNos={589,533,449,427,398,194,117,435,572};
// 其他
// int[] classNos={};
// Oracle
// int[] classNos={414,437,423,370,360,337};
// mongodb
// int[]
// classNos={614,575,552,534,521,325,298,297,295,255,595,594,582,578,562,528,490,501};

// Web
// 路径138,167,418,530,429,430,
// int[] classNos={385,250,56,101,21,147,100,204,50,137};
// 其他710,680,668,588,643,565,542,488,(453),445,
// int[] classNos={403,367,357,256,276,243,186,121,
// 362,192,93,95,119,118,15,706,598,659,644,639,580,566,545,514,488,471,453,454,425,428,412,
// 386,366,367,359,191,277,12,22,176,174,144,62,34,44,120,99,270,675,496,374,222,172,
// 637,590,564,556,434,367,348,221,75,197};

// php
// 路径
// int[]
// classNos={175,54,116,349,164,26,329,115,219,148,69,245,404,440,467,491,520,696,111};
// 其他177,475,458,563,416,457,550,317,581,712,532
// int[] classNos={
// 616,617,618,619,620,621,623,607,604,596,591,547,527,509,417,483,463,469,
// 438,433,419,380,350,330,278,236,244,26,205,219,170,184,150,155,115,113,94,68};

public class Main {

	// 课件集合，避免重复
	public static Set<String> set = new HashSet<>();
	// 用于存储下载的字节数，并计算下载速度
	public static List<Map<String, Long>> bps = new ArrayList<Map<String, Long>>();

	public static void MainDemo(List<Integer> classNos, String path) {
		// Jsoup的Document对象

		Document doc = null;

		String title;
		String savePath;
		String[] videoNos;
		String videoName;

		int classNo;

		for (int i : classNos) {
			classNo = i;
			try {
				set.clear();
				bps.clear();
			} catch (Exception e) {
				System.out.println(e);
			}

			try {

				// 获取Jsoup连接
				Connection con = Jsoup.connect("http://www.imooc.com/learn/" + classNo).timeout(60 * 1000);
				// 获取Document对象
				doc = con.get();

			} catch (Exception e) {
				System.out.println(e);
			}
			// 获取标签为h2的元素，并获取HTML代码,这个是整个课程的名称
			title = doc.getElementsByTag("h2").html();
			// 过滤文件夹非法字符，这个写法参考正则表达式
			title = title.replaceAll("[\\\\/:\\*\\?\"<>\\|]", "#");
			title = classNo + "_" + title;
			// 地址的写法，根目录用\\,子目录用/
			savePath = path + title + "/";
			// File file = new File(savePath);
			System.out.println(title);
			// 这个用法还没找到，我的理解是查找标签a中含有video的标签元素
			Elements videos = doc.select(".video a");
			// 选择高清
			int videoDef = 1;

			for (Element element : videos) {
				videoNos = element.attr("href").split("/");
				if (videoNos[0].equals("")) {
					// System.out.println(videoNos[2]);
					DealChildrenHtml.doGetFile(videoNos[2]);
					// 获取元素的文本
					videoName = element.text();
					videoName = videoName.substring(0, videoName.length() - 4).trim();
					videoName = videoName.replace(" ", "").replace("(", "").replace(")", "").replace(":", "")
							.replace("-", "_");
					// 这句话很重要的，在处理路径上如果课程名中包含\时，则会报FileNotFoundException，在下载标号为360的课程时遇到的
					videoName = videoName.replaceAll("[\\\\/:\\*\\?\"<>\\|]", "#");

					try {
						// 获取视频下载地址
						DownLoadVideo.getVideoURL(videoNos[2], videoDef, videoName, savePath);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
			DownloadCourse.getCourseURLAndName(set, savePath);
			System.out.println("下载完毕！");
			getBps(title, savePath);
		}

	}

	/**
	 * 将下载速度导入
	 * 
	 * @param title
	 */
	public static void getBps(String title, String savePath) {
		long times = 0;
		long bytes = 0;

		for (Map<String, Long> map : bps) {
			times += map.get("time");
			bytes += map.get("bytes");
		}
		String str = title + "下载完毕，" + "本次下载网速为："
				+ (float) ((int) (((float) bytes * 1.0) / ((float) (times * 1024 * 1.0) / 1000.0 * 1024) * 100) * 1.0)
						/ (float) (100 * 1.0)
				+ "Mb/s";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		String strData = sdf.format(date);
		System.out.println(str);

		File file = new File(savePath + "bps.txt");
		FileWriter fileWriter = null;
		try {

			fileWriter = new FileWriter(file, true);
			// \r\n时针对文件的换行操作
			fileWriter.write(str + "【" + strData + "】" + "【共" + bytes + "字节】" + "【512*1024】" + "\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
