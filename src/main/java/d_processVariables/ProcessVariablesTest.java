package d_processVariables;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

public class ProcessVariablesTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义（从inputStream）
	 */
	@Test
	public void deploymentProcessDefinition_inputstream() {
		InputStream inputStreambpmn = this.getClass()
				  .getResourceAsStream("/diagrams/processVariables.bpmn");
		InputStream inputStreampng = this.getClass()
				.getResourceAsStream("/diagrams/processVariables.png");
		Deployment deployment = processEngine.getRepositoryService()
			.createDeployment()//创建一个部署对象
			.name("流程定义")//添加部署名称
			.addInputStream("processVariables.bpmn", inputStreambpmn)
			.addInputStream("processVariables.png", inputStreampng)
			.deploy();//完成部署
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
		/*
		 * 	部署ID：5001
			部署名称：流程定义
		 */
	}
	
	/**
	 * 启动流程定义
	 */
	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "processVariables";
		//流程定义的key
		ProcessInstance pi = processEngine.getRuntimeService()
			.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID：" + pi.getId());
		System.out.println("流程定义ID：" + pi.getProcessDefinitionId());
		/* 流程实例ID：22501
			流程定义ID：processVariables:1:5004
		*/
	}
	
	/**
	 * 设置流程变量
	 */
	@Test
	public void setVariables() {
		//正在执行中的任务场景
		TaskService taskService = processEngine.getTaskService();
		//任务ID
		String taskId = "22505";
		
		/**一、设置流程变量，使用基本数据类型**/
		//taskService.setVariableLocal(taskId, "请求天数", 5);//使用setVariableLocal会让流程变量和taskId绑定，如果任务流
			//转到下一个人手中，下一个人将看不到此变量
		//taskService.setVariable(taskId, "请假日期", new Date());
		//taskService.setVariable(taskId, "请假理由", "回家办证，再一起吃个饭");
		
		/**二、设置流程变量，使用javabean类型 *（要求 Person implements Serializable）*/
		//当一个javabean（实现序列化）放置到流程变量中，要求javabean的属性不能再发生变化；否则会抛出异常
		//解决办法：在javabean中添加“序列化固定版本”=>private static final long serialVersionUID = xxx;
		Person p = new Person();
		p.setId(10);
		p.setName("翠花");
		taskService.setVariable(taskId, "人员信息", p);
		System.out.println("设置流程变量完成");
	}
	
	
	/**
	 * 获取流程变量
	 */
	@Test
	public void getVariables() {
		//正在执行中的任务场景
		TaskService taskService = processEngine.getTaskService();
		//任务ID
		String taskId = "22505";
		
		/**一、获取流程变量，获取基本数据类型**/
		//Integer days = (Integer)taskService.getVariable(taskId,"请求天数");//表示使用任务Id，获取所有的流程变量
		//Date date = (Date)taskService.getVariable(taskId,"请假日期");//表示使用任务Id，获取所有的流程变量
		//String reason = (String)taskService.getVariable(taskId,"请假理由");//表示使用任务Id，获取所有的流程变量
		/**二、获取流程变量，获取javabean类型**/
		Person p = (Person)taskService.getVariable(taskId, "人员信息");
		
		//System.out.println("请假天数: " + days);
		//System.out.println("请假日期: " + date);
		//System.out.println("请假理由: " + reason);
		System.out.println(p.getId() + " ==> " + p.getName());
		
	}
	
	/*
	 * 模拟设置和获取流程变量的场景
	 */
	public void setAndGetVariables() {
		
		//正在执行中的流程实例、执行对象
		//RuntimeService runtimeService = processEngine.getRuntimeService();
		
		//正在执行中的任务场景
		//TaskService taskService = processEngine.getTaskService();
		
		//设置流程变量
		//runtimeService.setVariable(executionId, variableName, value);//表示使用执行对象Id， 和流程变量的名称来设置流程变量的值，一次只能设置一个
		//runtimeService.setVariables(executionId, variables);//表示使用执行对象ID和Map集合设置流程变量 ，map集合的key就是流程变量的名称，value就是流程变量的值
	
		
		//taskService.setVariable(taskId, variableName, value);//表示使用任务Id， 和流程变量的名称来设置流程变量的值，一次只能设置一个
		//taskService.setVariables(taskId, variables);//表示使用执行任务ID和Map集合设置流程变量 ，map集合的key就是流程变量的名称，value就是流程变量的值
			
		//runtimeService.startProcessInstanceByKey(processDefinitonKey, variables);//启动流程实例的同时，可以设置流程变量，用map集合
		//taskService.complete(taskId, variables);////完成任务的同时，可以设置流程变量，用map集合
		
		
		//获取流程变量
		//runtimeService.getVariable(executionId, variableName);//表示使用执行对象Id， 和流程变量的名称来获取流程变量的值
		//runtimeService.getVariables(executionId);//表示使用执行对象Id，获取所有的流程变量
		//runtimeService.getVariables(executionId, variableNames);//根据执行对象Id和map-“variableNames”中的变量名获取指定流程变量
		
		//taskService.getVariable(taskId, variableName);//表示使用任务Id， 和流程变量的名称来获取流程变量的值
		//taskService.getVariables(taskId);//表示使用任务Id，获取所有的流程变量
		//taskService.getVariables(taskId, variableNames);//根据任务Id和map-“variableNames”中的变量名获取指定流程变量
		
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyPersonalTask() {
		//任务ID
		String taskId = "27502";
		processEngine.getTaskService()
			.complete(taskId);
		System.out.println("完成任务： 任务ID：" + taskId);
	}
	
	/**
	 * 查询流程变量历史表
	 */
	@Test
	public void findHistoryProcessVariables() {
		
		//String taskId = "15002";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
			.createHistoricVariableInstanceQuery()
			.variableName("请求天数")
			.list();
		if(list !=null && list.size() > 0) {
			for (HistoricVariableInstance hvi : list) {
				System.out.println("##########################################################");
				System.out.println(hvi.getId() + "\n" + hvi.getTaskId() + "\n" + hvi.getProcessInstanceId()
				+ "\n" + hvi.getVariableName() + "\n" + hvi.getValue());
			}
		}
	}
	
	
	/**
	 * 部署流程定义（从zip）
	 */
	/*@Test
	public void deploymentProcessDefinition_zip() {
		InputStream in = this.getClass().getClassLoader()
				  .getResourceAsStream("diagrams/processVariables.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()
			.createDeployment()//创建一个部署对象
			.name("流程定义")//添加部署名称
			.addZipInputStream(zipInputStream)//指定zip格式的文件完成部署
			.deploy();//完成部署
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
	}*/
}
