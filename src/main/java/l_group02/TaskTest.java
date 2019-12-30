package l_group02;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

/**
 * 2
 * 流程变量的方式
 * @author binwenque
 *
 */
public class TaskTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition_inputStream() {
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("task.bpmn");
		InputStream inputStreampng = this.getClass().getResourceAsStream("task.png");
		
		Deployment deployment = processEngine.getRepositoryService()
			.createDeployment()
			.name("任务")
			.addInputStream("task.bpmn", inputStreambpmn)
			.addInputStream("task.png", inputStreampng)
			.deploy();
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
		//部署ID：217501
		//部署名称：任务
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "task";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userIDs", "大大,中中,小小");
		/**启动流程实例的同时，设置流程变量，使用流程变量用来指定任务的办理人，对应task.bpmn中的#{userID}*/
		ProcessInstance pi = processEngine.getRuntimeService()
			.startProcessInstanceByKey(processDefinitionKey, variables);
		System.out.println("流程实例ID：" + pi.getId());
		System.out.println("流程定义ID：" + pi.getProcessDefinitionId());
		//流程实例ID：220001
		//流程定义ID：task:5:217504
	}
	
	/**
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee = "小小";
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
	 * 查询当前人的组任务
	 */
	@Test
	public void findMyGroupTask() {
		String candidateUser = "小小";
		List<Task> list = processEngine.getTaskService()
			.createTaskQuery()
			.taskCandidateUser(candidateUser)
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
	
	/**查询正在执行的任务办理表*/
	@Test
	public void findRunPersonTask() {
		
		String taskId = "197505";
		List<IdentityLink> identityLinksForTask = processEngine.getTaskService()
			.getIdentityLinksForTask(taskId);
		if(identityLinksForTask != null && identityLinksForTask.size() > 0) {
			for (IdentityLink identityLink : identityLinksForTask) {
				System.out.println(identityLink.getTaskId() + "		"
						+ identityLink.getType() + "		"
						+ identityLink.getProcessInstanceId() + "		"
						+ identityLink.getUserId()
						);
			}
		}
	}
	
	/**查询历史任务的办理人表*/
	@Test
	public void findHistoryPersonTask() {
		String processInstanceId = "197501";
		List<HistoricIdentityLink> list = processEngine.getHistoryService()
			.getHistoricIdentityLinksForProcessInstance(processInstanceId);
		if(list != null && list.size() > 0) {
			for (HistoricIdentityLink historicIdentityLink : list) {
				System.out.println(historicIdentityLink.getTaskId() + "		"
						+ historicIdentityLink.getType() + "		"
						+ historicIdentityLink.getProcessInstanceId() + "		"
						+ historicIdentityLink.getUserId()
						);
			}
			
			
		}
	}
	
	/**拾取任务，将组任务降为个人任务，并指定任务的办理人*/
	@Test
	public void claim() {
		String userId = "小小";
		String taskId = "220006";
		//分配的个人任务（可以分配给组任务中的成员，也可以不是）
		processEngine.getTaskService()
			.claim(taskId, userId);
		
	}
	
	/**将个人任务回退到组任务，前提，之前一定是一个组任务*/
	@Test
	public void setAssignee() {
		String taskId = "197505";
		processEngine.getTaskService()
			.setAssignee(taskId, null);
	}
	
	/**向组任务中添加成员*/
	@Test
	public void addGroupUser() {
		String taskId = "197505";
		String userId = "大H";
		processEngine.getTaskService()
			.addCandidateUser(taskId, userId);
	}
	
	/**从组任务中删除成员*/
	@Test
	public void delGroupUser() {
		String taskId = "197505";
		String userId = "小B";
		processEngine.getTaskService()
			.deleteCandidateUser(taskId, userId);
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyPersonalTask() {
		//任务ID
		String taskId = "220006";
		processEngine.getTaskService()
			.complete(taskId);
		System.out.println("完成任务： 任务ID：" + taskId);
	}
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteDeployment() {
		
		// 删除发布信息
		String deploymentId = "212501";
		// 获取仓库服务对象
		// 1.普通删除，如果当前规则下有正在执行的流程，则抛异常
		//processEngine.getRepositoryService() .deleteDeployment(deploymentId);
		// 2.级联删除，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
		processEngine.getRepositoryService()
			.deleteDeployment(deploymentId, true);
		
		System.out.println("删除成功");
	}
	
	
	
	
	
	
	
	
	
	
}
