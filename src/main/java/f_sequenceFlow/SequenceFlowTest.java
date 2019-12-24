package f_sequenceFlow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

public class SequenceFlowTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition_inputStream() {
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("sequenceFlow.bpmn");
		InputStream inputStreampng = this.getClass().getResourceAsStream("sequenceFlow.png");
		
		Deployment deployment = processEngine.getRepositoryService()
			.createDeployment()
			.name("连线")
			.addInputStream("sequenceFlow.bpmn", inputStreambpmn)
			.addInputStream("sequenceFlow.png", inputStreampng)
			.deploy();
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
		//部署ID：47501
		//部署名称：连线
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "sequenceFlow";
		ProcessInstance pi = processEngine.getRuntimeService()
			.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID：" + pi.getId());
		System.out.println("流程定义ID：" + pi.getProcessDefinitionId());
		//流程实例ID：50001
		//流程定义ID：sequenceFlow:3:47504
	}
	
	/**
	 * 查询当前人的个人任务
	 */
	//42504
	@Test
	public void findMyPersonalTask() {
		String assignee = "田七";
		List<Task> list = processEngine.getTaskService()
			.createTaskQuery()
			.taskAssignee(assignee)
			.orderByTaskCreateTime().asc()
			.list();
		if(list !=null && list.size() >0 ) {
			for (Task task : list) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("执行对象ID：" + task.getExecutionId());
				System.out.println("#############################################################");
			}
		}
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyPersonalTask() {
		//任务ID
		String taskId = "52503";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("message", "重要");
		processEngine.getTaskService()
			.complete(taskId, variables);
		System.out.println("完成任务： 任务ID：" + taskId);
	}
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteDeployment() {
		
		// 删除发布信息
		String deploymentId = "32501";
		// 获取仓库服务对象
		// 1.普通删除，如果当前规则下有正在执行的流程，则抛异常
		//processEngine.getRepositoryService() .deleteDeployment(deploymentId);
		// 2.级联删除，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
		processEngine.getRepositoryService()
			.deleteDeployment(deploymentId, true);
		
		System.out.println("删除成功");
	}
	
	
	
	
	
	
	
	
	
	
}
