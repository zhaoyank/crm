package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.entity.TaskExample;
import com.kaishengit.crm.job.SendMessageJob;
import com.kaishengit.crm.mapper.TaskMapper;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.crm.service.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhao
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 根据账号id获取对应的待办事项列表
     * @param accountId
     * @return
     */
    @Override
    public List<Task> findAllTaskByAccountId(Integer accountId) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andAccountIdEqualTo(accountId);
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 保存计划任务
     * @param accountId
     * @param finishTime
     * @param remindTime
     * @param saleId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewTask(Integer accountId, String title, String finishTime, String remindTime, Integer saleId, Integer custId) throws ParseException {
        Task task = new Task();
        task.setDone((byte)0);
        task.setAccountId(accountId);
        task.setCreateTime(new Date());
        task.setTitle(title);
        task.setSaleId(saleId);
        task.setCustId(custId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date finishDate = dateFormat.parse(finishTime);
        task.setFinishTime(finishDate);

        // 判断当设定了提醒时间时,设置提醒时间
        if(StringUtils.isNotEmpty(remindTime)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date remindDate = simpleDateFormat.parse(remindTime);
            task.setRemindTime(remindDate);
        }

        // 插入数据, 获得自动增长主键
        taskMapper.insertSelective(task);

        // 当设定了提醒时间时,创建定时任务
        // 只能在插入数据之后创建, 否则无法获得主键
        if(StringUtils.isNotEmpty(remindTime)) {
            createQuartz(task, accountId, remindTime);
        }

    }

    /**
     * 根据ID删除待办事项
     * @param id
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteTaskById(Integer id) {
        Task task = findTaskById(id);
        taskMapper.deleteByPrimaryKey(id);
        //删除定时任务
        if(task.getRemindTime() != null) {
            deleteQuartz(id);
        }

    }

    /**
     * 根据id查询待办事项
     * @param id
     * @return
     */
    @Override
    public Task findTaskById(Integer id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新task状态
     * @param id
     */
    @Override
    public void updateTaskState(Integer id) {
        Task task = findTaskById(id);
        // 判断当前的task状态, 并改变状态
        if (task.getDone() == 0) {
            task.setDone((byte)1);
        } else {
            task.setDone((byte)0);
        }
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 修改待办事项
     * @param id
     * @param accountId
     * @param title
     * @param finishTime
     * @param remindTime
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTask(Integer id, Integer accountId, String title, String finishTime, String remindTime) throws ParseException {
        Task task = taskMapper.selectByPrimaryKey(id);

        // 删除之前的定时任务
        if(task.getRemindTime() != null) {
            deleteQuartz(id);
        }

        task.setTitle(title);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date finishDate = dateFormat.parse(finishTime);
        task.setFinishTime(finishDate);

        // 当设置提醒时间时
        if(StringUtils.isNotEmpty(remindTime)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date remindDate = simpleDateFormat.parse(remindTime);
            task.setRemindTime(remindDate);
        }

        // 更新
        taskMapper.updateByPrimaryKeySelective(task);

        //添加新的定时任务
        if(StringUtils.isNotEmpty(remindTime)) {
            createQuartz(task, accountId, remindTime);
        }
    }

    /**
     * 根据用户ID和销售机会ID查找所有计划任务
     * @param accountId
     * @param saleId
     * @return
     */
    @Override
    public List<Task> findTaskByAccountAndSaleId(Integer accountId, Integer saleId) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andAccountIdEqualTo(accountId)
                .andSaleIdEqualTo(saleId);
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 根据taskId删除定时任务
     * @param id
     */
    private void deleteQuartz(Integer id) {
        // 获得调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            // 调度器根据id和组名删除任务
            scheduler.deleteJob(new JobKey("taskID:" + id, "sendMessageGroup"));
        } catch (Exception ex) {
            throw new ServiceException("删除定时任务异常");
        }
    }

    /**
     * 创建定时任务
     * @param task
     * @param accountId
     * @param remindTime
     */
    private void createQuartz(Task task, Integer accountId, String remindTime) {
        // 设置定时任务参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.putAsString("accountId",accountId);
        dataMap.put("message",task.getTitle());

        // 创建任务(设置任务类, 参数, id, group)
        JobDetail jobDetail = JobBuilder
                .newJob(SendMessageJob.class)
                .setJobData(dataMap)
                .withIdentity(new JobKey("taskID:"+task.getId(),"sendMessageGroup"))
                .build();

        // 将String类型的时间按格式转化为DateTime类型(2017-10-24 12:30)
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime dateTime = formatter.parseDateTime(remindTime);

        // 拼接cron表达式 ("0 30 12 24 10 ? 2014")
        StringBuilder cron = new StringBuilder("0")
                .append(" ")
                .append(dateTime.getMinuteOfHour())
                .append(" ")
                .append(dateTime.getHourOfDay())
                .append(" ")
                .append(dateTime.getDayOfMonth())
                .append(" ")
                .append(dateTime.getMonthOfYear())
                .append(" ? ")
                .append(dateTime.getYear());
        // 设置触发器
        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron.toString());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(scheduleBuilder)
                .build();

        // 获得调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            // 调度器根据任务和触发器添加定时任务
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException("添加定时任务异常");
        }
    }

}
