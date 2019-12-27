
package j_recevieTask;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

/**
 * 接收活动任务
 */
public class ReceiveTaskTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition_inputStream() {
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inputStreampng = this.getClass().getResourceAsStream("receiveTask.png");
		
		Deployment deployment = processEngine.getRepositoryService()
			.createDeployment()
			.name("接收活动任务")
			.addInputStream("receiveTask.bpmn", inputStreambpmn)
			.addInputStream("receiveTask.png", inputStreampng)
			.deploy();
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
		//部署ID：165001
		//部署名称：接收活动任务
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "receiveTask";
		ProcessInstance pi = processEngine.getRuntimeService()
			.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID：" + pi.getId());
		System.out.println("流程定义ID：" + pi.getProcessDefinitionId());
		//流程实例ID：157501
		//流程定义ID：
		
		Execution execution1 = processEngine.getRuntimeService()
			.createExecutionQuery()
			.processInstanceId( pi.getId())
			.activityId("receivetask1")//当前活动的id，对应receiveTask.bpmn文件中的活动节点id属性值
			.singleResult();
		
		/**使用流程变量设置当日销售额，用来传递业务参数*/
		processEngine.getRuntimeService()
			.setVariable(execution1.getId(), "汇总当日销售额", 21000 );
		
		/**
		 * 向后执行一步，如果流程处于等待状态，使得流程继续执行
		 * 在较新版本的activiti中,signal方法换成了trigger方法,用法一样,传参一样
		 * */
		processEngine.getRuntimeService()
			.trigger(execution1.getId());
		
		Execution execution2 = processEngine.getRuntimeService()
				.createExecutionQuery()
				.processInstanceId( pi.getId())
				.activityId("receivetask2")//当前活动的id，对应receiveTask.bpmn文件中的活动节点id属性值
				.singleResult();
		
		Integer value = (Integer)processEngine.getRuntimeService()
			.getVariable(execution2.getId(), "汇总当日销售额");
		
		System.out.println("给经理发送短信： 金额是：" + value);
		
		/**向后执行一步，如果流程处于等待状态，使得流程继续执行*/
		processEngine.getRuntimeService()
			.trigger(execution2.getId());
		
	}
	
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteDeployment() {
		
		// 删除发布信息
		String deploymentId = "162501";
		// 获取仓库服务对象
		// 1.普通删除，如果当前规则下有正在执行的流程，则抛异常
		//processEngine.getRepositoryService() .deleteDeployment(deploymentId);
		// 2.级联删除，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
		processEngine.getRepositoryService()
			.deleteDeployment(deploymentId, true);
		
		System.out.println("删除成功");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
