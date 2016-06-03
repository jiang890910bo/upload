package com.j1.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import org.apache.log4j.Logger;

public class FileUtils
{
  private static Logger logger = Logger.getLogger(FileUtils.class);

  public static final String TEMP_ROOT = FileUtils.class.getResource(File.separator)
    .getPath() + "._temp";

  public static String extName(String filepath)
  {
    if (filepath == null)
      return "";
    int lastDotIndex = filepath.lastIndexOf(".");
    if (lastDotIndex == -1)
      return "";
    return filepath.substring(lastDotIndex + 1);
  }

  public static File generateTempFile(String extName)
  {
    File file = new File(TEMP_ROOT + File.separator + 
      System.currentTimeMillis() + "." + extName);
    try {
      if (file.exists())
        file.delete();
      file.createNewFile();
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
    return file;
  }

  public static File storeTempFile(byte[] bytes, String extName) throws IOException
  {
    File file = generateTempFile(extName);
    write(file, new String(bytes));
    return file;
  }

  public static String read(InputStream reader) {
    StringBuffer result = new StringBuffer();
    try
    {
      int i;
      while ((i = reader.read()) != -1) {
        result.append(i);
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result.toString();
  }

  public static byte[] read(File file) {
    return readFile(file).getBytes();
  }

  public static String readFile(File file) {
    StringBuffer result = new StringBuffer();
    if ((!file.exists()) || (!file.canRead()))
      return result.toString();
    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader isr = new InputStreamReader(fis);
      BufferedReader reader = new BufferedReader(isr);
      String line = null;
      while ((line = reader.readLine()) != null) {
        result.append(line);
        result.append(System.getProperty("line.separator"));
      }
      reader.close();
      isr.close();
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result.toString();
  }

  public static void write(File file, String content) throws IOException {
    if ((!file.exists()) || (!file.canWrite())) {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
    try {
      FileOutputStream fos = new FileOutputStream(file);
      OutputStreamWriter osw = new OutputStreamWriter(fos);
      osw.write(content);
      osw.flush();
      osw.close();
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static
  {
    File tempRoot = new File(TEMP_ROOT);
    if (!tempRoot.exists())
      tempRoot.mkdirs();
  }
}
