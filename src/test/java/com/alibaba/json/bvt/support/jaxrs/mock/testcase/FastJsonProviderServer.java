/**
 * <p>Title: FastJsonProviderServer.java</p>
 * <p>Description: FastJsonProviderServer</p>
 * <p>Package: com.alibaba.json.bvt.support.jaxrs.mock.testcase</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.jaxrs.mock.testcase;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;

/**
 * <p>Title: FastJsonProviderServer</p>
 * <p>Description: </p>
 *
 * @since 2016年4月21日
 * @author Victor.Zxy
 * @version 1.0
 */
public class FastJsonProviderServer {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8088);

		// Register and map the dispatcher servlet
		final ServletHolder servletHolder = new ServletHolder(new CXFServlet());
		final ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.addServlet(servletHolder, "/rest/*");
		context.addEventListener(new ContextLoaderListener());
		context.setInitParameter(
				"contextConfigLocation",
				"classpath*:/com/alibaba/json/bvt/support/jaxrs/mock/resource/applicationContext-rest.xml");
		server.setHandler(context);
		server.start();
		server.join();
	}

}
