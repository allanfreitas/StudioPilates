package br.com.luxu.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
//import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class ContextoListener implements ServletContextListener {

	
	@Override
	public void contextInitialized(ServletContextEvent ev) {
    }

	@Override
	public void contextDestroyed(ServletContextEvent ev) {
        try {
            Context ic = new InitialContext();
            Context CONTEXT = (Context) ic.lookup("java:/comp/env");
            BasicDataSource ds = (BasicDataSource) CONTEXT.lookup("<NOME_DO_SEU_JNDI>");
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
