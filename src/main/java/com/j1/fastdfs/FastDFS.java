/*     */ package com.j1.fastdfs;
/*     */ 
/*     */ import com.j1.file.attr.FileAttrReader;
/*     */ import com.j1.util.FileUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.csource.common.NameValuePair;
/*     */ import org.csource.fastdfs.ClientGlobal;
/*     */ import org.csource.fastdfs.StorageClient;
/*     */ import org.csource.fastdfs.StorageServer;
/*     */ import org.csource.fastdfs.TrackerClient;
/*     */ import org.csource.fastdfs.TrackerServer;
/*     */ 
/*     */ public class FastDFS
/*     */ {
/*  17 */   private static Logger logger = Logger.getLogger(FastDFS.class);
/*     */ 
/*  19 */   private static FastDFS _INSTANCE = null;
/*     */ 
/*     */   private FastDFS() {
/*     */     try {
/*  23 */       ClientGlobal.init(FastDFS.class.getClassLoader()
/*  24 */         .getResource("fastdfs-client.conf")
/*  24 */         .getPath());
/*     */     } catch (Exception e) {
/*  26 */       e.printStackTrace();
/*  27 */       logger.error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static FastDFS instance() {
/*  32 */     if (_INSTANCE == null)
/*  33 */       _INSTANCE = new FastDFS();
/*  34 */     return _INSTANCE;
/*     */   }
/*     */ 
/*     */   public String upload(String filename) throws Exception {
/*  38 */     TrackerClient tracker = new TrackerClient();
/*  39 */     TrackerServer trackerServer = tracker.getConnection();
/*  40 */     StorageServer storageServer = null;
/*     */ 
/*  42 */     StorageClient storageClient = new StorageClient(trackerServer, storageServer);
/*     */ 
/*  44 */     String[] fileIds = storageClient.upload_file(filename, 
/*  45 */       FileUtils.extName(filename), 
/*  46 */       FileAttrReader.readFileAttr(filename));
/*     */ 
/*  48 */     for (String fileId : fileIds) {
/*  49 */       logger.info(fileId);
/*     */     }
/*     */ 
/*  52 */     return fileIds[0] + "/" + fileIds[1];
/*     */   }
/*     */ 
/*     */   public String upload(String localFile, String extName) throws Exception {
/*  56 */     TrackerClient tracker = new TrackerClient();
/*  57 */     TrackerServer trackerServer = tracker.getConnection();
/*  58 */     StorageServer storageServer = null;
/*     */ 
/*  60 */     StorageClient storageClient = new StorageClient(trackerServer, storageServer);
/*     */ 
/*  62 */     String[] fileIds = storageClient.upload_file(localFile, extName, 
/*  63 */       FileAttrReader.readFileAttr(localFile));
/*     */ 
/*  65 */     for (String fileId : fileIds) {
/*  66 */       logger.info(fileId);
/*     */     }
/*     */ 
/*  69 */     return fileIds[0] + "/" + fileIds[1];
/*     */   }
/*     */ 
/*     */   public String upload(String groupName, String masterFileName, String prefixname, byte[] fileBuff, String extName, NameValuePair[] metaList)
/*     */     throws Exception
/*     */   {
/*  75 */     TrackerClient tracker = new TrackerClient();
/*  76 */     TrackerServer trackerServer = tracker.getConnection();
/*  77 */     StorageServer storageServer = null;
/*  78 */     StorageClient storageClient = new StorageClient(trackerServer, storageServer);
/*     */ 
/*  80 */     String[] fileIds = storageClient.upload_file(groupName, masterFileName, prefixname, fileBuff, extName, metaList);
/*     */ 
/*  83 */     for (String fileId : fileIds) {
/*  84 */       logger.info(fileId);
/*     */     }
/*     */ 
/*  87 */     return fileIds[0] + "/" + fileIds[1];
/*     */   }
/*     */ 
/*     */   public String upload(byte[] fileBuff, String filename) throws Exception {
/*  91 */     TrackerClient tracker = new TrackerClient();
/*  92 */     TrackerServer trackerServer = tracker.getConnection();
/*  93 */     StorageServer storageServer = null;
/*     */ 
/*  95 */     StorageClient storageClient = new StorageClient(trackerServer, storageServer);
/*     */ 
/*  97 */     String[] fileIds = storageClient.upload_file(fileBuff, 
/*  98 */       FileUtils.extName(filename), 
/*  99 */       FileAttrReader.readFileAttr(filename));
/*     */ 
/* 101 */     for (String fileId : fileIds) {
/* 102 */       logger.info(fileId);
/*     */     }
/*     */ 
/* 105 */     return fileIds[0] + "/" + fileIds[1];
/*     */   }
/*     */ 
/*     */   public byte[] download(String groupName, String fileName) throws IOException
/*     */   {
/* 110 */     TrackerClient tracker = new TrackerClient();
/* 111 */     TrackerServer trackerServer = tracker.getConnection();
/* 112 */     StorageServer storageServer = null;
/*     */ 
/* 114 */     StorageClient storageClient = new StorageClient(trackerServer, storageServer);
/*     */ 
/* 117 */     byte[] bytes = null;
/*     */     try
/*     */     {
/* 120 */       bytes = storageClient.download_file(groupName, fileName);
/*     */     } catch (Exception e) {
/* 122 */       bytes = null;
/* 123 */       e.printStackTrace();
/*     */     }
/* 125 */     return bytes;
/*     */   }
/*     */ 
/*     */   public boolean download(String groupName, String fileName, String localFile) {
/* 129 */     TrackerClient tracker = new TrackerClient();
/*     */     try {
/* 131 */       TrackerServer trackerServer = tracker.getConnection();
/* 132 */       StorageServer storageServer = null;
/*     */ 
/* 134 */       StorageClient storageClient = new StorageClient(trackerServer, storageServer);
/*     */ 
/* 137 */       storageClient.download_file(groupName, fileName, localFile);
/* 138 */       return true;
/*     */     } catch (Exception e) {
/* 140 */       logger.error(e.getMessage());
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ }
