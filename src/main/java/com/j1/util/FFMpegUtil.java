package com.j1.util;

import java.util.ArrayList;
import java.util.List;

import com.j1.util.cmd.CmdExecuter;

/**
 * 本类封装FFMpeg对视频的操作
 * 
 * @author Administrator
 *
 */
public class FFMpegUtil {
	public static final String FFMPEG = "ffmpeg";
	private Integer runtime;
	private String ffmpegUri; // ffmpeg地址
	private String originFileUri; // 视频源文件地址
	private FFMpegUtilStatus status = FFMpegUtilStatus.Empty;
	private boolean isSupported;
	private List<String> cmd = new ArrayList();
	
	/**
	 * 视频支持转换的格式有(非MP4的视频类型上传全部转成手机通用认识的MP4类型)
	 */
	private static List<String> videoTypeList;
	

	static{
		videoTypeList = new ArrayList<String>();
		videoTypeList.add("3gp"); 
		videoTypeList.add("flv"); 
		videoTypeList.add("avi"); 
		videoTypeList.add("mpg"); 
		videoTypeList.add("mkv"); 
		videoTypeList.add("wmv"); 
		videoTypeList.add("mov"); 
		videoTypeList.add("mp4"); 
	}

	 /** 
     * 构造函数 
     * @param ffmpegUri ffmpeg的全路径  
     *      如e:/ffmpeg/ffmpeg.exe 或 /root/ffmpeg/bin/ffmpeg 
     * @param originFileUri 所操作视频文件的全路径  
     *      如e:/upload/temp/test.wmv 
     */
	public FFMpegUtil(String ffmpegUri, String originFileUri) {
		this.ffmpegUri = ffmpegUri;
		this.originFileUri = originFileUri;
		this.runtime = Integer.valueOf(0);
	}

	
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	/** 
     * 获取视频时长 
     * @return 
     */ 
	public synchronized int getRuntime() {
		this.status = FFMpegUtilStatus.GettingRuntime;
		this.cmd.clear();
		this.cmd.add(this.ffmpegUri);
		this.cmd.add("-i");
		this.cmd.add(this.originFileUri);
		CmdExecuter.exec(this.cmd, this);
		return this.runtime.intValue();
	}

	 /** 
     * 检测文件是否是支持的格式 
     * 将检测视频文件本身，而不是扩展名 
     * @return 
     */ 
	public boolean isSupported() {
		this.isSupported = false;
		this.status = FFMpegUtilStatus.CheckingFile;
		this.cmd.clear();
		this.cmd.add(this.ffmpegUri);
		this.cmd.add("-i");
		this.cmd.add(this.originFileUri);
		this.cmd.add("Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s");
		CmdExecuter.exec(this.cmd, this);
		return this.isSupported;
	}

	/**
	 * 检查音频类型
	 * @param path
	 * @return
	 */
	public static boolean checkContentTypeForAudio(String path) {
		boolean back = false;

		String type = path.substring(path.lastIndexOf(".") + 1, path.length())
				.toLowerCase();
		if ((type.equals("wav")) || (type.equals("amr"))) {
			back = true;
		}
		return back;
	}
	
	/**
	 * 检查视频类型
	 * @param path
	 * @return
	 */
	public static boolean checkContentTypeForVideo(String path) {
		boolean back = false;

		String type = path.substring(path.lastIndexOf(".") + 1, path.length())
				.toLowerCase();
		for (String string : videoTypeList) {
			if(type.endsWith(string)){
				back = true;
				break;
			}
		}
		return back;
	}

	/**
	 * 转换成FLV
	 * @param path
	 * @param out_path
	 * @return
	 */
	public synchronized static boolean processFLV(String path, String out_path) {
		List<String> commend = new ArrayList<String>();
		commend.add(FFMPEG);
		commend.add("-y");
		commend.add("-i");
		commend.add(path);
		commend.add("-ab");
		commend.add("56");
		commend.add("-ar");
		commend.add("22050");

		commend.add(out_path);
		try {
			ProcessBuilder builder = new ProcessBuilder(commend);
			builder.command(commend);
			builder.start();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    /** 
     * 生成视频截图 
     * @param imageSavePath 截图文件保存全路径 
     * @param screenSize 截图大小 如640x480 
     */
	public synchronized static void makeScreenCut(String originFileUri,String imageSavePath, String screenSize) {
		List<String> commend = new ArrayList<String>();
		commend.add(FFMPEG);
		commend.add("-i");
		commend.add(originFileUri);
		commend.add("-y");
		commend.add("-f");
		commend.add("image2");
		commend.add("-ss");
		commend.add("2");
		commend.add("-t");
		commend.add("0.001");
		commend.add("-s");
		commend.add(screenSize);
		commend.add(imageSavePath);
		CmdExecuter.exec(commend, new FFMpegUtil(FFMPEG,originFileUri));
	}

	/*
	 * public void dealString(String str)
	 * 
	 * System.out.println(str);
	 * 
	 * switch
	 * (1.$SwitchMap$com$j1$util$FFMpegUtil$FFMpegUtilStatus[this.status.ordinal
	 * ()]) { case 1: break; case 2: break; case 3: }
	 */

	private static enum FFMpegUtilStatus {
		Empty, CheckingFile, GettingRuntime;
	}
}
