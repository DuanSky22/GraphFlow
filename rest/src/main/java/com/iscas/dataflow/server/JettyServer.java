package com.iscas.dataflow.server;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.iscas.dataflow.util.AppConfiguration;

/**
 * Jetty服务器，提供对实时库和历史库数据的访问能力
 * @author DuanSky
 * @version 1.0
 */
public class JettyServer {
	public static void main(String[] args) throws Exception {
		Server server = new Server(getPort());
		server.setHandler(getServletContextHandler(getContext()));
		server.start();
		server.join();
	}

	private static int getPort() throws IOException {
		Properties prop = new Properties();
		prop.load(new InputStreamReader(JettyServer.class.getClassLoader()
				.getResourceAsStream("application.properties")));
		return Integer.valueOf(prop.getProperty("server.port", "9090"));
	}

	private static ServletContextHandler getServletContextHandler(
			WebApplicationContext context) {
		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setErrorHandler(null);
		DispatcherServlet dispatcher = new DispatcherServlet();
		dispatcher.setApplicationContext(context);
		contextHandler.addServlet(new ServletHolder(dispatcher), "/*");
		contextHandler.addEventListener(new ContextLoaderListener(context));
		return contextHandler;
	}

	private static WebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(AppConfiguration.class.getCanonicalName());
		//context.getEnvironment().setActiveProfiles("prod");
		return context;
	}
}
