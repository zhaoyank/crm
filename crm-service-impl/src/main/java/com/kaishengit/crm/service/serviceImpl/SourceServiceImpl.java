package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.Source;
import com.kaishengit.crm.entity.SourceExample;
import com.kaishengit.crm.mapper.SourceMapper;
import com.kaishengit.crm.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhao
 */
@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceMapper sourceMapper;

    /**
     * 查询所有source
     * @return source的List集合
     */
    @Override
    public List<Source> findAllSoure() {
        return sourceMapper.selectByExample(new SourceExample());
    }
}
