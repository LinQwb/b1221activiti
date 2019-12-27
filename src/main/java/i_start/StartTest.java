package i_start;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

/**
 * 开始结束
 */
public class StartTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition_inputStream() {
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("start.bpmn");
		InputStream inputStreampng = this.getClass().getResourceAsStream("start.png");
		
		Deployment deployment = processEngine.getRepositoryService()
			.createDeployment()
			.name("开始活动")
			.addInputStream("start.bpmn", inputStreambpmn)
			.addInputStream("start.png", inputStreampng)
			.deploy();
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
		//部署ID：125001
		//部署名称：开始活动
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "start";
		ProcessInstance pi = processEngine.getRuntimeService()
			.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID：" + pi.getId());
		System.out.println("流程定义ID：" + pi.getProcessDefinitionId());
		//流程实例ID：112501
		//流程定义ID：parallelGateWay:1:110004
		
		ProcessInstance rpi = processEngine.getRuntimeService()
			.createProcessInstanceQuery()//创建流程实例查询对象
			.processInstanceId(pi.getId())
			.singleResult();
		//说明流程实例结束了
		if(rpi == null) {
			//查询历史，获取流程的相关信息
			HistoricProcessInstance hpi = processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId(pi.getId())
				.singleResult();
			System.out.println(hpi.getId() + "   " + hpi.getStartTime() + 
					"   " + hpi.getEndTime() + "   " + hpi.getDurationInMillis());
		}
		
	}
	
}
