package org.jakduk.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;

public class TraceInterceptor extends CustomizableTraceInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static Logger logger4j = Logger.getLogger("aop");
	
	@Override
	protected void writeToLog(Log logger, String message, Throwable ex) {
		if (ex != null) {
			logger4j.debug(message, ex);
		} else {
			logger4j.debug(message);
		}
	}
	
	@Override
	protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
		return true;
	}
	

}
