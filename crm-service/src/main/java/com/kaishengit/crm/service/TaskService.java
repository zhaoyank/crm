package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Task;

import java.text.ParseException;
import java.util.List;

/**
 * @author zhao
 */
public interface TaskService {

    /**
     * 根据账号id获取对应的待办事项列表
     * @param accountId
     * @return
     */
    List<Task> findAllTaskByAccountId(Integer accountId);

    /**
     * 保存计划任务
     * @param accountId
     * @param title
     * @param finishTime
     * @param remindTime
     * @param saleId
     * @param custId
     */
    void saveNewTask(Integer accountId, String title, String finishTime, String remindTime, Integer saleId, Integer custId) throws ParseException;

    /**
     * 根据id删除待办事项
     * @param id
     */
    void deleteTaskById(Integer id);

    /**
     * 根据id查询待办事项
     * @param id
     * @return
     */
    Task findTaskById(Integer id);

    /**
     * 更新task状态
     * @param id
     */
    void updateTaskState(Integer id);

    /**
     * 修改待办事项
     * @param id
     * @param accountId
     * @param title
     * @param finishTime
     * @param remindTime
     */
    void editTask(Integer id, Integer accountId, String title, String finishTime, String remindTime) throws ParseException;

    /**
     * 根据用户ID和销售机会ID查找所有计划任务
     * @param accountId
     * @param saleId
     * @return
     */
    List<Task> findTaskByAccountAndSaleId(Integer accountId, Integer saleId);

}
