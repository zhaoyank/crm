package com.kaishengit.crm.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhao
 */
public class SendMessageJob implements Job {

    private Logger logger = LoggerFactory.getLogger(SendMessageJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer accountId = dataMap.getIntegerFromString("accountId");
        String message = (String) dataMap.get("message");

        logger.info("To:{} Message:{}",accountId,message);
    }
}
