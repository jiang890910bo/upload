/*    */ package com.j1.util;
/*    */ 
/*    */ import com.mortennobel.imagescaling.AdvancedResizeOp;
/*    */ import com.mortennobel.imagescaling.ResampleOp;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class ImageUtils
/*    */ {
/* 15 */   private static Logger logger = Logger.getLogger(ImageUtils.class);
/*    */ 
/*    */   public static File scale(File file, int width, int height) {
/*    */     try {
/* 19 */       BufferedImage src = ImageIO.read(file);
/*    */ 
/* 21 */       String fileExtName = FileUtils.extName(file.getAbsolutePath());
/*    */ 
/* 23 */       ResampleOp resampleOp = new ResampleOp(width, height);
/* 24 */       resampleOp
/* 25 */         .setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
/*    */ 
/* 26 */       BufferedImage rescaled = resampleOp.filter(src, null);
/*    */ 
/* 29 */       File result = new File(FileUtils.TEMP_ROOT + File.separator + 
/* 29 */         System.currentTimeMillis() + "." + fileExtName);
/*    */ 
/* 31 */       ImageIO.write(rescaled, fileExtName, result);
/*    */ 
/* 33 */       return result;
/*    */     } catch (Exception e) {
/* 35 */       logger.error(e.getMessage());
/*    */     }
/* 37 */     return null;
/*    */   }
/*    */ }
