/*     */ package com.j1.servlet;
/*     */ 
/*     */ import com.j1.util.ImageUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class ReadFileServlet extends HttpServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  26 */   private static Logger logger = Logger.getLogger(ReadFileServlet.class);
/*     */ 
/*     */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/*  34 */     doPost(request, response);
/*     */   }
/*     */ 
/*     */   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
/*     */   {
/*  39 */     String requestURI = request.getRequestURI();
/*  40 */     String contextPath = request.getContextPath();
/*     */ 
/*  42 */     requestURI = requestURI.replaceAll(contextPath + "/read/", "");
/*  43 */     String floder = requestURI.substring(0, requestURI.lastIndexOf("/"));
/*  44 */     String fileName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
/*     */ 
/*  46 */     String fileBasePath = "/opt/upload" + File.separator + floder;
/*     */ 
/*  49 */     String filePath = fileBasePath + File.separator + fileName;
/*     */ 
/*  51 */     File file = new File(filePath);
/*  52 */     if (!file.exists()) {
/*  53 */       file = null;
/*     */     }
/*  55 */     if (file == null)
/*     */     {
/*  57 */       Pattern pattern = Pattern.compile("\\.\\d+x\\d+\\.");
/*  58 */       Matcher matcher = pattern.matcher(fileName);
/*  59 */       Matcher matcherFileIds = pattern.matcher(fileName);
/*     */ 
/*  61 */       if (matcher.find())
/*     */       {
/*     */         try {
/*  64 */           String widthHeight = matcher.group();
/*  65 */           String[] params = widthHeight.replaceAll("\\.", "").split("x");
/*     */ 
/*  67 */           String originFileName = matcher.replaceAll(".");
/*     */ 
/*  69 */           File originFile = new File(fileBasePath + File.separator + originFileName);
/*     */ 
/*  76 */           if (originFile.exists())
/*     */           {
/*  79 */             File scaleFile = ImageUtils.scale(originFile, 
/*  80 */               Integer.valueOf(params[0])
/*  80 */               .intValue(), 
/*  81 */               Integer.valueOf(params[1])
/*  81 */               .intValue());
/*     */ 
/*  83 */             File saveFile = new File(fileBasePath + File.separator + fileName);
/*     */ 
/*  86 */             if (scaleFile.renameTo(saveFile)) {
/*  87 */               file = saveFile;
/*  88 */               response.sendRedirect(contextPath + "/read/" + saveFile
/*  90 */                 .getAbsolutePath().replace("/opt/upload", ""));
/*     */ 
/*  93 */               return;
/*     */             }
/*     */           }
/*     */         } catch (Exception e) {
/*  97 */           logger.error(e.getMessage());
/*     */         }
/*  99 */         System.out.println();
/*     */       }
/*     */     }
/* 102 */     if (file == null) {
/* 103 */       response.sendError(404);
/*     */     } else {
/* 105 */       byte[] fileByte = FileUtils.readFileToByteArray(file);
/* 106 */       response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName
/* 107 */         .getBytes("utf-8"), 
/* 107 */         "ISO-8859-1"));
/* 108 */       response.addHeader("Content-Length", "" + file.length());
/* 109 */       response.setContentType("application/octet-stream");
/*     */ 
/* 111 */       ServletOutputStream sops = response.getOutputStream();
/* 112 */       sops.write(fileByte);
/*     */     }
/*     */   }
/*     */ }
