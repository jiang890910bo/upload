/*     */ package com.j1.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ public class ClassUtils
/*     */ {
/* 101 */   static String NAME_PREFIX = "class ";
/*     */ 
/*     */   private static List<Class> getAllImplClassesByInterface(Class c)
/*     */   {
/*  27 */     List returnClassList = new ArrayList();
/*     */ 
/*  29 */     if (c.isInterface()) {
/*  30 */       String packageName = c.getPackage().getName();
/*     */       try {
/*  32 */         List allClass = getClassesByPackageName(packageName);
/*  33 */         for (int i = 0; i < allClass.size(); i++)
/*     */         {
/*  38 */           if ((c.isAssignableFrom((Class)allClass.get(i))) && 
/*  39 */             (!c.equals(allClass.get(i))))
/*  40 */             returnClassList.add(allClass.get(i));
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException e)
/*     */       {
/*  45 */         e.printStackTrace();
/*     */       } catch (IOException e) {
/*  47 */         e.printStackTrace();
/*     */       }
/*     */     } else {
/*  50 */       returnClassList.add(c);
/*     */     }
/*  52 */     return returnClassList;
/*     */   }
/*     */ 
/*     */   private static List<Class> getClassesByPackageName(String packageName)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  59 */     ClassLoader classLoader = Thread.currentThread()
/*  59 */       .getContextClassLoader();
/*  60 */     String path = packageName.replace('.', '/');
/*  61 */     Enumeration resources = classLoader.getResources(path);
/*  62 */     List<File> dirs = new ArrayList();
/*  63 */     while (resources.hasMoreElements()) {
/*  64 */       URL resource = (URL)resources.nextElement();
/*  65 */       dirs.add(new File(resource.getFile()));
/*     */     }
/*  67 */     ArrayList classes = new ArrayList();
/*  68 */     for (File directory : dirs) {
/*  69 */       classes.addAll(findClasses(directory, packageName));
/*     */     }
/*  71 */     return classes;
/*     */   }
/*     */ 
/*     */   private static List<Class> findClasses(File directory, String packageName)
/*     */     throws ClassNotFoundException
/*     */   {
/*  77 */     List classes = new ArrayList();
/*  78 */     if (!directory.exists()) {
/*  79 */       return classes;
/*     */     }
/*  81 */     File[] files = directory.listFiles();
/*  82 */     for (File file : files) {
/*  83 */       if (file.isDirectory())
/*     */       {
/*  85 */         assert (!file.getName().contains("."));
/*  86 */         classes.addAll(findClasses(file, packageName + '.' + file
/*  87 */           .getName()));
/*  88 */       } else if (file.getName().endsWith(".class")) {
/*  89 */         classes.add(Class.forName(packageName + "." + file
/*  91 */           .getName().substring(0, file
/*  92 */           .getName().length() - 6)));
/*     */       }
/*     */     }
/*  95 */     return classes;
/*     */   }
/*     */ 
/*     */   public static Set<Class<?>> getClassAtPackageByParent(String packageName, Class<?> parentClass)
/*     */   {
/* 113 */     Set msgHandlers = new HashSet();
/* 114 */     for (Class cls : getClassesByPackage(packageName)) {
/* 115 */       int mod = cls.getModifiers();
/* 116 */       if ((!Modifier.isAbstract(mod)) && (!Modifier.isInterface(mod)))
/*     */       {
/* 118 */         if ((parentClass.isAssignableFrom(cls)) && (!cls.isInterface()))
/* 119 */           msgHandlers.add(cls); 
/*     */       }
/*     */     }
/* 121 */     return msgHandlers;
/*     */   }
/*     */ 
/*     */   public static Type getGenericsType(Method method)
/*     */   {
/* 131 */     Type[] types = method.getGenericParameterTypes();
/* 132 */     for (int i = 0; i < types.length; i++) {
/* 133 */       ParameterizedType pt = (ParameterizedType)types[i];
/* 134 */       if (pt.getActualTypeArguments().length > 0)
/* 135 */         return pt.getActualTypeArguments()[0];
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getClassName(Type type)
/*     */   {
/* 147 */     String fullName = type.toString();
/* 148 */     if (fullName.startsWith(NAME_PREFIX))
/* 149 */       return fullName.substring(NAME_PREFIX.length());
/* 150 */     return fullName;
/*     */   }
/*     */ 
/*     */   public static Method getMethod(Method[] methods, String methodName)
/*     */   {
/* 162 */     for (Method method : methods) {
/* 163 */       if (method.getName().equalsIgnoreCase(methodName))
/* 164 */         return method;
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isPrimitiveType(Class<?> clazz)
/*     */   {
/* 182 */     return (clazz != null) && (
/* 180 */       (clazz
/* 180 */       .isPrimitive()) || (clazz.equals(String.class)) || 
/* 181 */       (Iterable.class
/* 181 */       .isAssignableFrom(clazz)) || 
/* 182 */       (clazz
/* 182 */       .getName().equals("java.lang.Class")));
/*     */   }
/*     */ 
/*     */   public static Set<Class<?>> getClassesByPackage(String pack)
/*     */   {
/* 194 */     Set classes = new LinkedHashSet();
/*     */ 
/* 196 */     boolean recursive = true;
/*     */ 
/* 198 */     String packageName = pack;
/* 199 */     String packageDirName = packageName.replace('.', '/');
/*     */     try
/*     */     {
/* 204 */       Enumeration dirs = Thread.currentThread().getContextClassLoader()
/* 204 */         .getResources(packageDirName);
/*     */ 
/* 206 */       while (dirs.hasMoreElements())
/*     */       {
/* 208 */         URL url = (URL)dirs.nextElement();
/*     */ 
/* 210 */         String protocol = url.getProtocol();
/*     */ 
/* 212 */         if ("file".equals(protocol))
/*     */         {
/* 214 */           String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
/*     */ 
/* 216 */           findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
/*     */         }
/* 218 */         else if ("jar".equals(protocol))
/*     */         {
/*     */           try
/*     */           {
/* 225 */             JarFile jar = ((JarURLConnection)url.openConnection())
/* 225 */               .getJarFile();
/*     */ 
/* 227 */             Enumeration entries = jar.entries();
/*     */ 
/* 229 */             while (entries.hasMoreElements())
/*     */             {
/* 231 */               JarEntry entry = (JarEntry)entries.nextElement();
/* 232 */               String name = entry.getName();
/*     */ 
/* 234 */               if (name.charAt(0) == '/')
/*     */               {
/* 236 */                 name = name.substring(1);
/*     */               }
/*     */ 
/* 239 */               if (name.startsWith(packageDirName)) {
/* 240 */                 int idx = name.lastIndexOf('/');
/*     */ 
/* 242 */                 if (idx != -1)
/*     */                 {
/* 245 */                   packageName = name.substring(0, idx)
/* 245 */                     .replace('/', '.');
/*     */                 }
/*     */ 
/* 248 */                 if ((idx != -1) || (recursive))
/*     */                 {
/* 250 */                   if ((name.endsWith(".class")) && 
/* 251 */                     (!entry
/* 251 */                     .isDirectory()))
/*     */                   {
/* 253 */                     String className = name.substring(packageName
/* 254 */                       .length() + 1, name
/* 255 */                       .length() - 6);
/*     */                     try
/*     */                     {
/* 258 */                       classes.add(
/* 259 */                         Class.forName(packageName + '.' + className));
/*     */                     }
/*     */                     catch (ClassNotFoundException e)
/*     */                     {
/* 264 */                       e.printStackTrace();
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (IOException e) {
/* 272 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (IOException e) {
/* 277 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 280 */     return classes;
/*     */   }
/*     */ 
/*     */   public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes)
/*     */   {
/* 294 */     File dir = new File(packagePath);
/*     */ 
/* 296 */     if ((!dir.exists()) || (!dir.isDirectory()))
/*     */     {
/* 298 */       return;
/*     */     }
/*     */ 
/* 301 */     File[] dirfiles = dir.listFiles(new FileFilter()
/*     */     {
/*     */       public boolean accept(File file)
/*     */       {
/* 305 */         return (recursive && file.isDirectory()) ||  (file.getName().endsWith(".class"));
/*     */       }
/*     */     });
/* 309 */     for (File file : dirfiles)
/*     */     {
/* 311 */       if (file.isDirectory()) {
/* 312 */         findAndAddClassesInPackageByFile(packageName + "." + file
/* 313 */           .getName(), file
/* 314 */           .getAbsolutePath(), recursive, classes);
/*     */       }
/*     */       else {
/* 317 */         String className = file.getName().substring(0, file
/* 318 */           .getName().length() - 6);
/*     */         try
/*     */         {
/* 324 */           classes.add(Thread.currentThread().getContextClassLoader()
/* 325 */             .loadClass(packageName + '.' + className));
/*     */         }
/*     */         catch (ClassNotFoundException e)
/*     */         {
/* 328 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }
