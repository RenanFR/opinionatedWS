package com.opinionated.ws.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsulTest implements InitializingBean	{
	
//	@Value("${foo}")
//	private String testValue;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsulTest.class);
	

	@Override
	public void afterPropertiesSet() throws Exception {
//		LOGGER.info(testValue);
	}
}
