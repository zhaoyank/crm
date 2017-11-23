package com.kaishengit.weixin.mq;

import com.alibaba.fastjson.JSON;
import com.kaishengit.weixin.util.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhao
 */
@Component
public class WeixinConsumer {

    @Autowired
    private WeixinUtil weixinUtil;

    @JmsListener(destination = "WeixinMessage-queue")
    public void sendMessageToWeixin(String json) {

        Map<String, Object> map = JSON.parseObject(json, HashMap.class);
        weixinUtil.sendTextMessage(Arrays.asList("ZhaoYan"),(String) map.get("message"));

    }

}
