 package com.j1.servlet;
 
 import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.j1.util.FFMpegUtil;
import com.j1.util.FileUtils;
import com.j1.util.FormatUtils;
import com.j1.util.RandomUtil;
import com.j1.util.StringUtil;
 
 public class UploadFileServlet extends HttpServlet
 {
   private static final long serialVersionUID = 1L;
   private static Logger logger = Logger.getLogger(UploadFileServlet.class);
   private ServletFileUpload upload;
   /**
    * 文件上传目录
    */
   public static final String UPLOAD_BASE_PATH = "/opt/upload";
   /**
    * 文件web路径
    * 线上访问地址
    */
   //public static final String READ_PATH = "http://soa.app.j1.com/upload/read";
   //测试访问地址 
   public static final String READ_PATH = "http://192.168.2.191:8087/upload/read";
   /**
    * 视频上传统一转码格式
    */
   public static final String URI_VIDEO_FORMAT = ".mp4";
   /**
    * 视频截图统一格式
    */
   public static final String URI_VIDEO_PICTURE_FORMAT = ".jpg";
   /**
    * 视频截图统一尺寸
    */
   public static final String URI_VIDEO_PICTURE_SIZE = "400x300";
   /**
    * 音频上传同一转码格式
    */
   public static final String URI_AUDIO_FORMAT = ".mp3";
   /**
    * 插件FFMPEG
    */
   public static final String FFMPEG_COMMOND = "ffmpeg";
 
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     doPost(request, response);
   }
 
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
     try
     {
       List items = this.upload.parseRequest(request);
       Iterator iter = items.iterator();
       StringBuffer result = new StringBuffer();
       if(iter == null){
    	   throw new Exception("the request doesn't contain a multipart/form-data or multipart/mixed stream");
       }
       while (iter.hasNext()) {
         FileItem item = (FileItem)iter.next();
         if (!item.isFormField()) {
	           String fileName = item.getName();
	 
	           if (!StringUtil.isEmpty(fileName))
	           {
		             Calendar calendar = Calendar.getInstance();
		             
		             File savePath = new File(UPLOAD_BASE_PATH + File.separator + 
		               String.valueOf(calendar
		               .get(1)) + 
		               File.separator + 
		               String.valueOf(calendar
		               .get(2) + 
		               1) + File.separator);
		 
		             if (!savePath.exists())
		               savePath.mkdirs();
		             String extName = FileUtils.extName(fileName);
		 
		             String newFileName = FormatUtils.sdf.format(new Date()) + RandomUtil.random.nextInt(1000);
		 
		             if (!StringUtil.isEmpty(extName)) {
		               newFileName = newFileName + "." + extName;
		             }
		 
		             File saveFile = new File(savePath + File.separator + newFileName);
		             item.write(saveFile);
		             String path = saveFile.getAbsolutePath();
		             
	            	 if (FFMpegUtil.checkContentTypeForAudio(path))//音频格式转码
	            	 {
	            		 StringBuffer out_path = new StringBuffer();
		            	 out_path.append(path.substring(0, path.lastIndexOf(".")));
		            	 
		            	 FFMpegUtil ffmpeg = new FFMpegUtil("ffmpeg", path);
		            	 Integer time = Integer.valueOf(ffmpeg.getRuntime());
		            	 out_path.append("_" + time).append(URI_AUDIO_FORMAT);
		            	 
		            	 
		            	 FFMpegUtil.processFLV(path, out_path.toString());
	            		 
	            		 result.append(READ_PATH + out_path.toString().replace(UPLOAD_BASE_PATH, ""));
	            		 result.append(",");
	            	 }else if(FFMpegUtil.checkContentTypeForVideo(path)){//视频格式转码, 还要截取一帧图片返回(目前，ios与安装客户端录制视频时上传都是MP4)
	            		
	            		 StringBuffer out_path = new StringBuffer();
		            	 out_path.append(path.substring(0, path.lastIndexOf(".")));
		            	 
		            	 FFMpegUtil ffmpeg = new FFMpegUtil("ffmpeg", path);
		            	 Integer time = Integer.valueOf(ffmpeg.getRuntime());
		            	 out_path.append("_").append(time).append(URI_VIDEO_FORMAT);
		            	 
		            	 FFMpegUtil.processFLV(path, out_path.toString());
	            		 
	            		 result.append(READ_PATH + out_path.toString().replace(UPLOAD_BASE_PATH, ""));
	            		 
	            		 result.append("|");//加“|”隔开视频web地址与视频截图web地址
	            		 //截取视频的一帧图片（图片保存的位置与视频在同一目录）
	            		 StringBuffer img_out_path = new StringBuffer();
	            		 img_out_path.append(path.substring(0, path.lastIndexOf(".")));
		            	 img_out_path.append("_").append(URI_VIDEO_PICTURE_SIZE).append(URI_VIDEO_PICTURE_FORMAT);
		            	 FFMpegUtil.makeScreenCut(path, img_out_path.toString(), URI_VIDEO_PICTURE_SIZE);
	            		 
	            		 result.append(READ_PATH + img_out_path.toString().replace(UPLOAD_BASE_PATH, ""));
	            		 result.append(",");
	            	 } else {//其他文件格式原样保存
	            		 
	            		 result.append(READ_PATH + saveFile
	            				 .getAbsolutePath().replace(UPLOAD_BASE_PATH, ""));
	            		 
	            		 result.append(",");
	            	 }
	           	}
	           
         	}
       }
       
       String resultStr = result.length() > 0 ? result.substring(0, result.length() - 1) : result.toString();
       response.getWriter().write(resultStr);
 
     } catch (Exception e) {
       e.printStackTrace();
       logger.error(e.getMessage());
     }
   }
 
public void destroy()
   {
     super.destroy();
   }
 
   public void init() throws ServletException
   {
     DiskFileItemFactory factory = new DiskFileItemFactory();
     this.upload = new ServletFileUpload(factory);
     super.init();
   }
   
}
