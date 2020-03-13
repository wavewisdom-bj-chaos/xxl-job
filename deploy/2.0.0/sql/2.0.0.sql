/**
 * datatable-name:xxl_job
 * datatable-user:xxljobadmin
 * datatable-pwd:xxljobadmin123
 */
user xxl_job;
INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `order`, `address_type`, `address_list`) VALUES (1, 'xxl-job-executor-sample', '示例执行器', 1, 0, NULL);
INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `order`, `address_type`, `address_list`) VALUES (2, 'xxl-job-executor-workflow', '工作流', 1, 0, NULL);
INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `order`, `address_type`, `address_list`) VALUES (4, 'xxl-job-executor-business', '业务模块', 1, 0, NULL);
INSERT INTO `xxl_job_info` VALUES ('64', '4', '0 5 0 * * ?', '用于统计考勤日期', '2019-09-29 10:59:26', '2019-10-31 13:56:46', '蔚文杰', '', 'FIRST', 'statisticAttendanceDateHander', 'hello world', 'SERIAL_EXECUTION', '0', '3', 'BEAN', '', 'GLUE代码初始化', '2019-09-29 10:59:26', '', '1', '1575821100000', '1575907500000');
INSERT INTO `xxl_job_info` VALUES ('69', '2', '0 0 1 * * ?', '考勤统计', '2019-10-31 09:58:35', '2019-10-31 09:59:28', 'yaokun', '', 'FIRST', 'workFlowAttendanceStatisticsHandler', '', 'SERIAL_EXECUTION', '0', '3', 'BEAN', '', 'GLUE代码初始化', '2019-10-31 09:58:35', '', '1', '1575824400000', '1575910800000');
INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`) VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);
INSERT INTO `xxl_job_lock` ( `lock_name`) VALUES ( 'schedule_lock');