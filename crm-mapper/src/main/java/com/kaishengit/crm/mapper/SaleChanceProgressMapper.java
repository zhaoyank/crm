package com.kaishengit.crm.mapper;

import com.kaishengit.crm.entity.SaleChanceProgress;
import com.kaishengit.crm.entity.SaleChanceProgressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SaleChanceProgressMapper {
    long countByExample(SaleChanceProgressExample example);

    int deleteByExample(SaleChanceProgressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SaleChanceProgress record);

    int insertSelective(SaleChanceProgress record);

    List<SaleChanceProgress> selectByExampleWithBLOBs(SaleChanceProgressExample example);

    List<SaleChanceProgress> selectByExample(SaleChanceProgressExample example);

    SaleChanceProgress selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SaleChanceProgress record, @Param("example") SaleChanceProgressExample example);

    int updateByExampleWithBLOBs(@Param("record") SaleChanceProgress record, @Param("example") SaleChanceProgressExample example);

    int updateByExample(@Param("record") SaleChanceProgress record, @Param("example") SaleChanceProgressExample example);

    int updateByPrimaryKeySelective(SaleChanceProgress record);

    int updateByPrimaryKeyWithBLOBs(SaleChanceProgress record);

    int updateByPrimaryKey(SaleChanceProgress record);
}