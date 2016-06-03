package com.j1.util;


public class Test {
	private static final String ffmpeg_path = "D:/java/ffmpeg/bin/ffmpeg.exe";

	public static void main(String[] args) {
		//window中，如果java代码调用，ffmpeg_path必须制定操作文件的可执行文件地址，DOS界面可以安装使用命令“ffmpeg”；
		//linux中需要安装ffmpeg后，使用“ffmpeg”命令
		FFMpegUtil ffmpeg = new FFMpegUtil(ffmpeg_path,"D:\\opt\\upload\\2016\\5\\31161342428387.mp4");
		FFMpegUtil.makeScreenCut("D:\\opt\\upload\\2016\\5\\31161342428387.mp4", "D:\\opt\\upload\\2016\\5\\31161342428387.jpg", "300x200");
		//System.out.println(ffmpeg.getRuntime());
	}
}
