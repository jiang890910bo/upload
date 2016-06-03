/*    */ package com.j1.file.attr;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.csource.common.NameValuePair;
/*    */ 
/*    */ public class ImageFileAttrReader
/*    */   implements IFileAttrReader
/*    */ {
/* 18 */   private static String[] handleable = { "jpg", "JPG", "jpeg", "JEPG", "gif", "GIF", "png", "PNG" };
/*    */ 
/* 21 */   private static Logger logger = Logger.getLogger(ImageFileAttrReader.class);
/*    */ 
/*    */   public NameValuePair[] readFileAttr(String filepath)
/*    */   {
/* 25 */     NameValuePair[] nameValuePairs = new NameValuePair[3];
/*    */ 
/* 27 */     File imgfile = new File(filepath);
/*    */     try {
/* 29 */       FileInputStream fis = new FileInputStream(imgfile);
/* 30 */       BufferedImage buff = ImageIO.read(imgfile);
/* 31 */       nameValuePairs[0] = new NameValuePair("width", 
/* 32 */         String.valueOf(buff
/* 32 */         .getWidth()));
/* 33 */       nameValuePairs[1] = new NameValuePair("height", 
/* 34 */         String.valueOf(buff
/* 34 */         .getHeight()));
/* 35 */       nameValuePairs[2] = new NameValuePair("length", 
/* 36 */         String.valueOf(imgfile
/* 36 */         .length()));
/* 37 */       fis.close();
/*    */     } catch (FileNotFoundException e) {
/* 39 */       logger.error("FileNotFoundException:" + imgfile.getPath() + " does not exists!");
/*    */     }
/*    */     catch (IOException e) {
/* 42 */       logger.error(e.getMessage());
/*    */     }
/* 44 */     return new NameValuePair[0];
/*    */   }
/*    */ 
/*    */   public boolean canHandle(String fileExtName)
/*    */   {
/* 49 */     if (fileExtName == null)
/* 50 */       return false;
/* 51 */     for (String extName : handleable) {
/* 52 */       if (extName.equals(fileExtName))
/* 53 */         return true;
/*    */     }
/* 55 */     return false;
/*    */   }
/*    */ }
