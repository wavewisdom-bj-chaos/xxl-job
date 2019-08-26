/**
 * 
 */
package com.xxl.job.admin.listerner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.wave.centimani.tool.component.deploy.ConfigHandler;
//import com.wave.centimani.tool.component.deploy.acm.ACMConfighandler;
//import com.wave.centimani.tool.component.deploy.nacos.NacosConfighandler;
import com.wave.centimani.tool.component.deploy.acm.ACMConfighandler;
import com.wave.centimani.tool.component.deploy.nacos.NacosConfighandler;

/**
* Title: ChaosStartUpListener
* Description: tomcat监听启动类
* Company: wave
* @author Mongo
* @date 2019年8月22日
*/

@WebListener
public class ChaosStartUpListener implements ServletContextListener {

    private static final String ENV_TYPE = System.getProperty("envType");

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
	ConfigHandler configHandler = null;

	switch (ENV_TYPE) {
	case "acm":
	    // 执行acm相关配置
	    configHandler = new ACMConfighandler();
	    break;
	case "nacos":
	    // 执行nacos相关配置
	    configHandler = new NacosConfighandler();
	    break;
	default:
	    break;
	}
	configHandler.loadConfig();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
	// TODO Auto-generated method stub
    }

}
