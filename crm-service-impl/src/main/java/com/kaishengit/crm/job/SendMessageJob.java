package com.kaishengit.crm.job;

import com.kaishengit.weixin.util.WeixinUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author zhao
 */
@Component
public class SendMessageJob implements Job {

    private Logger logger = LoggerFactory.getLogger(SendMessageJob.class);
    @Autowired
    private WeixinUtil weixinUtil;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer accountId = dataMap.getIntegerFromString("accountId");
        String message = "您的待办任务[" + dataMap.get("message") + "]最近需要完成";
        weixinUtil.sendTextMessage(Arrays.asList(accountId.toString()),message);

        logger.info("To:{} Message:{}",accountId,message);
    }
}
