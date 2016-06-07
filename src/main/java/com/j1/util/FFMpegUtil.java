package com.j1.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.j1.util.cmd.CmdExecuter;

/**
 * 本类封装FFMpeg对视频的操作
 * 
 * @author Administrator
 *
 */
public class FFMpegUtil {
	static Logger logger = Logger.getLogger(FFMpegUtil.class);
	
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
	public synchronized static void processFLV(String path, String out_path) {
		List<String> commend = new ArrayList<String>();
		commend.add(FFMPEG);
		commend.add("-y");//覆盖已存在的文件
		commend.add("-i");//获得源文件物理信息
		commend.add(path);//源文件路径
		commend.add("-ab");//<比特率> 设定声音比特率
		commend.add("80");
		commend.add("-ar");//<采样率> 设定声音采样率
		commend.add("22050");

		commend.add(out_path);
		
		CmdExecuter.exec(commend, new FFMpegUtil(FFMPEG,path));
		
		/*try {
			//预处理进程
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);

			//build.start()方法该方法会返回一个Process对象,该对象即代表正在运行的ffmpeg.exe这个程。
			//该对象有个waitFor()方法，该方法会阻塞当前线程，直至ffmpeg.exe运行结束。
			Process p = builder.start();
			//查看进程读取的数据信息
			printProcessInfo(p);
			*//** 
	        * 等候进程运行结束->p.waitFor()
	        *//*
			p.waitFor();
			return true;
		}catch(Exception e){
			logger.error(e);
		}
		return false;*/
	}
	
	/**
	 * 打印进程读取的数据
	 * @param p
	 * @return
	 */
	public static void printProcessInfo(Process p) {
        InputStream in = null;
        InputStream err = null;
        try {
            System.out.println("coming......");
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false; // Set to true when p is finished
  
            if (!finished) {
            	InputStreamReader isr1 = null;
            	BufferedReader br1 = null;
            	InputStreamReader isr2 = null;
            	BufferedReader br2 = null;
            	
                try {
                	isr1 = new InputStreamReader(in);
                	br1 = new BufferedReader(isr1); 
                    try {
                        String lineB = null;    
                        while ((lineB = br1.readLine()) != null ){    
                            if(lineB != null)System.out.println(lineB);    
                        }    
                    } catch (IOException e) {    
                    	logger.error(e);
                    }
                    
                    isr2 = new InputStreamReader(err);
                    br2 = new BufferedReader(isr2);    
                    try {    
                        String lineC = null;    
                        while ( (lineC = br2.readLine()) != null){    
                            if(lineC != null)System.out.println(lineC);    
                        }    
                    } catch (IOException e) {    
                    	logger.error(e);   
                    }
  
                    finished = true;
  
                } catch (IllegalThreadStateException e) {
                	logger.error(e);
                }finally{
                	try {
						if(br1 !=null) br1.close();
					} catch (Exception e) {
						logger.error(e);
					}
                	
                	try {
						if(isr1 !=null) isr1.close();
					} catch (Exception e) {
						logger.error(e);
					}
                	
                	try {
						if(br2 !=null) br2.close();
					} catch (Exception e) {
						logger.error(e);
					}
                	
                	try {
						if(isr2 !=null) isr2.close();
					} catch (Exception e) {
						logger.error(e);
					}
                }
            }
        } catch (Exception e) {
        	logger.error("doWaitFor();: unexpected exception - "
                    + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
  
            } catch (IOException e) {
            	logger.error(e);
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                	logger.error(e);
                }
            }
        }
       
    }

    /** 
     * 生成视频截图 
     * @param imageSavePath 截图文件保存全路径 
     * @param screenSize 截图大小 如640x480 
     */
	public synchronized static void makeScreenCut(String originFileUri,String imageSavePath, String screenSize) {
		try {
			List<String> commend = new ArrayList<String>();
			commend.add(FFMPEG);
			commend.add("-i");//获得文件信息
			commend.add(originFileUri);
			commend.add("-y");//覆盖已有的文件
			commend.add("-f");//指定格式
			commend.add("image2");
			commend.add("-ss");//-ss后跟的时间单位为秒
			commend.add("2");
			commend.add("-vframes");//"-vframes 1"指定一帧
			commend.add("1");
			commend.add(screenSize);
			commend.add(imageSavePath);
			//CmdExecuter.exec(commend, new FFMpegUtil(FFMPEG,originFileUri));
			
			try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);

			//build.start()方法该方法会返回一个Process对象,该对象即代表正在运行的ffmpeg.exe这个程。
			//该对象有个waitFor()方法，该方法会阻塞当前线程，直至ffmpeg.exe运行结束。
			Process p = builder.start();
			//查看进程读取的数据信息
			printProcessInfo(p);
			/** 
	        * 等候进程运行结束->p.waitFor()
	        */
			p.waitFor();
		}catch(Exception e){
			logger.error(e);
		}
		} catch (Exception e) {
			logger.error(e);
		}
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
