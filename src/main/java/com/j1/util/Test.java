package com.j1.util;


public class Test {
	private static final String ffmpeg_path = "D:/java/ffmpeg/bin/ffmpeg.exe";

	public static void main(String[] args) {
		try {
			//window中，如果java代码调用，ffmpeg_path必须制定操作文件的可执行文件地址，DOS界面可以安装使用命令“ffmpeg”；
			//linux中需要安装ffmpeg后，使用“ffmpeg”命令
			FFMpegUtil ffmpeg = new FFMpegUtil(ffmpeg_path,"D:\\opt\\upload\\2016\\5\\视频格式\\26165725772891.3gp");
			//FFMpegUtil.makeScreenCut("D:\\opt\\upload\\2016\\5\\视频格式\\26165725772891.3gp", "D:\\opt\\upload\\2016\\5\\31161342428387.jpg", "300x200");
			
			FFMpegUtil.processFLV("D:\\opt\\upload\\2016\\5\\视频格式\\26165725772891.3gp","D:\\opt\\upload\\2016\\6\\26165725772891_"+ffmpeg.getRuntime()+".mp4");
			//System.out.println(ffmpeg.getRuntime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
