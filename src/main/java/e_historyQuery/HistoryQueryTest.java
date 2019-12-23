package e_historyQuery;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;

public class HistoryQueryTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 查询历史流程实例
	 */
	@Test
	public void findHistoryProcessInstance() {
		String processInstanceId = "10001";
		HistoricProcessInstance hpi = processEngine.getHistoryService()
			.createHistoricProcessInstanceQuery()
			.processInstanceId(processInstanceId)
			.orderByProcessInstanceStartTime().asc()
			.singleResult();
		System.out.println(hpi.getId() + "	" + hpi.getProcessDefinitionId() + "	" + hpi.getStartTime() + "	" + hpi.getEndTime());
		
	}
	
	/**
	 * 查询历史活动
	 */
	@Test
	public void findHistoryActiviti() {
		
		String processInstanceId = "10001";
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
			.createHistoricActivityInstanceQuery()
			.processInstanceId(processInstanceId)
			.orderByHistoricActivityInstanceStartTime().asc()
			.list();
		
		if(list != null && list.size() >0 ) {
			for (HistoricActivityInstance hai : list) {
				System.out.println(hai.getAssignee() + "	" + hai.getProcessInstanceId() + "	" + hai.getEndTime());
				System.out.println("======================================================");
			}
		}
		
	}
	
	/**
	 * 查询历史任务
	 */
	@Test
	public void findHistoryTask() {
		
		//String taskAssignee = "张晓晓";
		String processInstanceId = "10001";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
			.createHistoricTaskInstanceQuery()
			//.taskAssignee(taskAssignee)
			.processInstanceId(processInstanceId)
			.list();
		if(list != null && list.size() >0 ) {
			for (HistoricTaskInstance hai : list) {
				System.out.println(hai.getId() + "	" + hai.getName() + "   " + hai.getAssignee() + "	" + hai.getEndTime());
				System.out.println("======================================================");
			}
		}
		
		
		
	}
	
	
	/**
	 * 查询流程变量历史表
	 */
	@Test
	public void findHistoryProcessVariables() {
		
		//String taskId = "15002";
		String processInstanceId = "10001";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
			.createHistoricVariableInstanceQuery()
			.processInstanceId(processInstanceId)
			//.variableName("请求天数")
			.list();
		if(list !=null && list.size() > 0) {
			for (HistoricVariableInstance hvi : list) {
				System.out.println("##########################################################");
				System.out.println(hvi.getId() + "\n" + hvi.getTaskId() + "\n" + hvi.getProcessInstanceId()
				+ "\n" + hvi.getVariableName() + "\n" + hvi.getValue());
			}
		}
	}
	
	
	
	
	
	
}
