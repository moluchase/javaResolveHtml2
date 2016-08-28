package com.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * start入口程序
 * 
 * @author Administrator
 *
 */
public class Start {

	// 用于接收控制台的输入信息
	// private static Scanner scanner;

	private static Scanner scanner;

	public static void main(String[] args) {
		// 接收输入的字符串
		String str = "";
		scanner = new Scanner(System.in);
		System.out.println("****************************************");
		System.out.println("欢迎登入下载程序，输入q退出程序");
		System.out.println("请在阅读完readme.txt下操作此程序");
		System.out.println("制作人  :moluchase");
		System.out.println("****************************************");
		// 存放整形的课程号
		List<Integer> classNos = null;
		while (!str.equals("q")) {
			// 清空课程号
			try {
				classNos.clear();
			} catch (Exception e) {
				//
			}

			System.out.println("请在下面输入指定格式串:(例如   123,324,34!D:\\\\imooc/Android/)");
			// 读取输入的一行
			str = scanner.nextLine();
			if (str.equals("q"))
				return;
			str = str.replace("，", ",");
			String[] strings = str.split("!");
			if (strings.length > 1) {
				String path = strings[1].trim();
				if (path == null || path.length() < 4 || path.charAt(1) != ':'
						|| path.charAt(path.length() - 1) != '/') {
					System.out.println("输入地址有误");
					continue;
				}
				String[] courses = strings[0].split(",");
				classNos = new ArrayList<Integer>();
				for (String string : courses) {

					try {
						int i = Integer.parseInt(string);
						classNos.add(i);
					} catch (Exception e) {
						System.out.println("输入课程" + string + "有误，将取消该课程下载");
					}

				}
				// 将输入的课程号，地址传入Main函数
				Main.MainDemo(classNos, path);
			} else {
				System.out.println("输入有误！");
			}
		}
		while (!str.equals("q")) {
			str = scanner.nextLine();
			System.out.println(str);
		}
		scanner.close();
	}

}
