package com.j1.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author Chengjiangbo@j1.com
 * @version 2016-05-26
 *
 */
public class Log4jContextListener implements ServletContextListener {
	
	Logger logger = Logger.getLogger(Log4jContextListener.class);

	public void contextDestroyed(ServletContextEvent arg0) {
		

	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		System.out.println(sc.getRealPath("/"));
		String logFilePath = sc.getRealPath("/WEB-INF/conf/log4j.properties");
		
		init(logFilePath, sc);
	}

	private void init(String logFilePath, ServletContext sc) {
		FileInputStream fis = null;
		
		Properties properties = new Properties();
		try {
			fis = new FileInputStream(logFilePath);
			properties.load(fis);
			PropertyConfigurator.configure(properties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
