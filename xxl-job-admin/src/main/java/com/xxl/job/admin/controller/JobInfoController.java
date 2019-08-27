package com.xxl.job.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wave.centimani.tool.component.log.LogEnum;
import com.wave.centimani.tool.component.log.LogsUtil;
import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.exception.XxlJobException;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobUser;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.service.LoginService;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {
    private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);
	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;
	
	@RequestMapping
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	// 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	// 阻塞处理策略-字典

		// 执行器列表
		List<XxlJobGroup> jobGroupList_all =  xxlJobGroupDao.findAll();

		// filter group
		List<XxlJobGroup> jobGroupList = filterJobGroupByRole(request, jobGroupList_all);
		if (jobGroupList==null || jobGroupList.size()==0) {
			throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
		}

		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}

	public static List<XxlJobGroup> filterJobGroupByRole(HttpServletRequest request, List<XxlJobGroup> jobGroupList_all){
		List<XxlJobGroup> jobGroupList = new ArrayList<>();
		if (jobGroupList_all!=null && jobGroupList_all.size()>0) {
			XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
			if (loginUser.getRole() == 1) {
				jobGroupList = jobGroupList_all;
			} else {
				List<String> groupIdStrs = new ArrayList<>();
				if (loginUser.getPermission()!=null && loginUser.getPermission().trim().length()>0) {
					groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
				}
				for (XxlJobGroup groupItem:jobGroupList_all) {
					if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
						jobGroupList.add(groupItem);
					}
				}
			}
		}
		return jobGroupList;
	}
	public static void validPermission(HttpServletRequest request, int jobGroup) {
		XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
		if (!loginUser.validPermission(jobGroup)) {
			throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username="+ loginUser.getUsername() +"]");
		}
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
		
		return xxlJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlJobInfo jobInfo) {
		return xxlJobService.add(jobInfo);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlJobInfo jobInfo) {
		return xxlJobService.update(jobInfo);
	}
    @RequestMapping("/remove")
    @ResponseBody
    public ReturnT<String> remove(int id) {

        return xxlJobService.remove(id);
    }
    
    @RequestMapping("/stop")
    @ResponseBody
    public ReturnT<String> pause(int id) {
        return xxlJobService.stop(id);
    }
    
    @RequestMapping("/start")
    @ResponseBody
    public ReturnT<String> start(int id) {
        return xxlJobService.start(id);
    }
    
    @RequestMapping("/trigger")
    @ResponseBody
    //@PermissionLimit(limit = false)
    public ReturnT<String> triggerJob(int id, String executorParam) {
        // force cover job param
        if (executorParam == null) {
            executorParam = "";
        }
        JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam);
        return ReturnT.SUCCESS;
    }
	/**
	 * wave-内部使用：添加定时任务，并且成功返回时 context字段为 任务ID
	 * @param jobInfo
	 * @return
	 */
    @RequestMapping("/adds")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> adds(@RequestBody XxlJobInfo jobInfo) {
        // LogsUtil.setLogInfo("", LogEnum.IN, "xxl-job", "adds", null, "", jobInfo, "xxl-job-post-adds添加");
        return xxlJobService.add(jobInfo);
    }
    /**
     * wave-内部使用：根据任务ID更新任务信息
     * @param jobInfo
     * @return
     */
    @RequestMapping("/updates")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> updates(@RequestBody XxlJobInfo jobInfo) {
        // LogsUtil.setLogInfo("", LogEnum.IN, "xxl-job", "updates", null, "", jobInfo, "xxl-job-post-updates更新");
        return xxlJobService.update(jobInfo);
    }
    /**
     * wave-内部使用：根据任务ID删除定时任务
     * @param jobInfo
     * @return
     */
    @RequestMapping("/removes")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> removes(@RequestBody XxlJobInfo jobInfo) {
        // LogsUtil.setLogInfo("", LogEnum.IN, "xxl-job", "removes", null, "", jobInfo, "xxl-job-post-removes移除");
        return xxlJobService.remove(jobInfo.getId());
    }
    /**
     * wave-内部使用：根据任务ID停止执行定时任务
     * @param id
     * @return
     */
    @RequestMapping("/stops")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> pauses( @RequestBody XxlJobInfo jobInfo) {
        // LogsUtil.setLogInfo("", LogEnum.IN, "xxl-job", "stops", null, "", jobInfo, "xxl-job-post-stops停止");
        return xxlJobService.stop(jobInfo.getId());
    }
    /**
     * wave-内部使用：根据任务ID使定时任务开始生效执行
     * @param id
     * @return
     */
    @RequestMapping("/starts")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> starts(@RequestBody XxlJobInfo jobInfo) {
        // LogsUtil.setLogInfo("", LogEnum.IN, "xxl-job", "starts", null, "", jobInfo, "xxl-job-post-starts启动");
        return xxlJobService.start(jobInfo.getId());
    }
    /**
     * wave-内部使用：立即出发定时任务执行
     * @param id
     * @param executorParam
     * @return
     */
    @RequestMapping("/triggers")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> triggerJobs(@RequestBody XxlJobInfo jobInfo) {
      //  LogsUtil.setLogInfo("", LogEnum.IN, "xxl-job", "triggers", null, "", jobInfo, "xxl-job-post-triggers立即触发");

        if (jobInfo.getExecutorParam() == null) {
            jobInfo.setExecutorParam(""); ;
        }

        JobTriggerPoolHelper.trigger(jobInfo.getId(), TriggerTypeEnum.MANUAL, -1, null, jobInfo.getExecutorParam());
        return ReturnT.SUCCESS;
    }
    
  
	
}
