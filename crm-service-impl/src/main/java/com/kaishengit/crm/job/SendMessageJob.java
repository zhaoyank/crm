package com.kaishengit.crm.job;

import com.alibaba.fastjson.JSON;
import com.kaishengit.weixin.util.WeixinUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务
 * @author zhao
 */
public class SendMessageJob implements Job {

    private Logger logger = LoggerFactory.getLogger(SendMessageJob.class);

    /**
     * 执行定时任务
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer accountId = dataMap.getIntegerFromString("accountId");
        String message = "您的待办任务[<a href=\"http://locahost:8080/task\">" + dataMap.get("message") + "</a>]最近需要完成";

        // 获取Spring容器,从Spring容器中获取JmsTemplate
        try {
            ApplicationContext applicationContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("springApplicationContext");
            JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");

            // 将消息发送到队列
            jmsTemplate.send("WeixinMessage-queue", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", accountId);
                    map.put("message", message);
                    String json = JSON.toJSONString(map);
                    TextMessage textMessage = session.createTextMessage(json);
                    return textMessage;
                }
            });
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        logger.info("To:{} Message:{}",accountId,message);
    }
}
