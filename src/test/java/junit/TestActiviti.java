package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.jupiter.api.Test;

/**
 * 流程引擎ProcessEngine对象
 * 所有的操作都离不开引擎对象
 * @author binwenque
 *
 */
public class TestActiviti {
	
	/**
	 * 使用代码创建工作流需要的23张表
	 */
	@Test
	public void createTable() {
		ProcessEngineConfiguration processEngineConfiguration 
			= ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/1221activiti?useUnicode=true"
				+ "&characterEncoding=utf8&serverTimezone=GMT%2B8");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("123456");
		
		/*
		 * DB_SCHEMA_UPDATE_TRUE 如果表不存在，自动创建表
		 * DB_SCHEMA_UPDATE_FALSE 不能自动创建表，需要表存在
		 * DB_SCHEMA_UPDATE_CREATE_DROP 先删除表再创建表
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		
		//工作流的核心对象，ProcessEnginee对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println("ProcessEngine: " + processEngine); 
	}
	
	/**
	 * 使用配置文件创建工作流需要的23张表
	 */
	@Test
	public void createTable_2() {
		
//		ProcessEngineConfiguration processEngineConfiguration 
//			= ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
//		
//		//工作流的核心对象，ProcessEnginee对象
//		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
				.buildProcessEngine();
		
		System.out.println("ProcessEngine: " + processEngine); 
		
	}
	
	
	
	
	
	
}
