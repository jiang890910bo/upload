/*    */ package com.j1.file.attr;
/*    */ 
/*    */ import com.j1.util.ClassUtils;
/*    */ import com.j1.util.FileUtils;

/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;

/*    */ import org.apache.log4j.Logger;
/*    */ import org.csource.common.NameValuePair;
/*    */ 
/*    */ public class FileAttrReader
/*    */ {
/* 15 */   private static Logger logger = Logger.getLogger(FileAttrReader.class);
/*    */ 
/* 20 */   private static List<IFileAttrReader> fileAttrReaders = new ArrayList();
/*    */ 
/*    */   public static NameValuePair[] readFileAttr(String filepath)
/*    */   {
/* 33 */     String extName = FileUtils.extName(filepath);
/* 34 */     IFileAttrReader reader = getFileReader(extName);
/* 35 */     if (reader == null)
/* 36 */       return new NameValuePair[0];
/* 37 */     return reader.readFileAttr(filepath);
/*    */   }
/*    */ 
/*    */   private static IFileAttrReader getFileReader(String extName) {
/* 41 */     for (IFileAttrReader fileAttrReader : fileAttrReaders) {
/* 42 */       if (fileAttrReader.canHandle(extName))
/* 43 */         return fileAttrReader;
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 21 */     Set<Class<?>> classes = ClassUtils.getClassAtPackageByParent("com.j1.file.attr", IFileAttrReader.class);
/*    */     try
/*    */     {
/* 24 */       for (Class clazz : classes)
/* 25 */         fileAttrReaders.add((IFileAttrReader)clazz.newInstance());
/*    */     }
/*    */     catch (Exception e) {
/* 28 */       logger.error(e.getMessage());
/*    */     }
/*    */   }
/*    */ }
