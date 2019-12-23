# 部署对象和流程定义相关的表

select * from act_re_deployment; 	#部署对象表

select * from act_re_procdef; 	#流程定义表

select * from act_ge_bytearray;		#资源文件表

select * from act_ge_property;		#主键生成策略表alter

#####################################################################################################################

#流程实例，执行对象，任务
select * from act_ru_execution;		#正在执行的执行对象表alter

select * from act_hi_procinst;		#流程实例的历史表alter

select * from act_ru_task;		#正在执行的任务表（只有节点是UserTask的时候，该表中存在数据）

select * from act_hi_taskinst;		#任务历史表（只有节点是UserTask的时候，该表中存在数据）

select * from act_hi_actinst;		#所有活动节点的历史表

#####################################################################################################################
#流程变量

select * from act_ru_variable;		#正在执行的流程变量表

select * from act_hi_varinst;		#历史的流程变量表



























