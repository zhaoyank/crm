package com.kaishengit.weixin.util;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.kaishengit.weixin.exception.WeixinException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhao
 */
@Component
public class WeixinUtil {

    public static final String ACCESS_TOKEN_TYPE_NORMAL = "normal";
    public static final String ACCESS_TOKEN_TYPE_CONTACTS = "contact";


    private static final String GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    private static final String POST_CREATE_ACCOUNT_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";

    private static final String GET_DELETE_ACCOUNT_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=%s&userid=%s";

    private static final String POST_CREATE_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=%s";

    private static final String GET_DELETE_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";

    private static final String POST_UPDATE_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=%s";

    private static final String POST_TEXT_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    @Value("${weixin.corpid}")
    private String corpId;
    @Value("${weixin.secret}")
    private String secret;
    @Value("${weixin.crm.secret}")
    private String contactsSecret;
    @Value("${weixin.crm.agentid}")
    private String agentId;

    /**
     * AccessToken 的缓存
     */
    private LoadingCache<String, String > accessTokenCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String type) throws Exception {
                    String url;
                    if (ACCESS_TOKEN_TYPE_CONTACTS.equals(type)) {
                        url = String.format(GET_ACCESS_TOKEN_URL, corpId, contactsSecret);
                    } else {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpId, secret);
                    }
                    String resultJson = sendHttpGetRequest(url);

                    Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
                    if (resultMap.get("errcode").equals(0)) {
                        return (String) resultMap.get("access_token");
                    }
                    throw new WeixinException(resultJson);
                }
            });

    /**
     * 发送文本消息
     * @param userIdList
     * @param message
     */
    public void sendTextMessage(List<String> userIdList, String message) {
        String accessToken = getAccessToken(ACCESS_TOKEN_TYPE_NORMAL);
        String url = String.format(POST_TEXT_MESSAGE_URL, accessToken);

        Map<String, Object> paramMap = Maps.newHashMap();
        StringBuffer ids = new StringBuffer();
        for (String id : userIdList) {
            ids.append(id).append("|");
        }
        String userIds = ids.toString().substring(0,ids.lastIndexOf("|"));
        paramMap.put("touser", userIds);
        paramMap.put("msgtype", "text");
        paramMap.put("agentid", agentId);

        Map<String, Object> textMap = Maps.newHashMap();
        textMap.put("content", message);
        paramMap.put("text", textMap);

        String requestJson = JSON.toJSONString(paramMap);
        String resultJson = sendHttpPostRequest(url, requestJson);
        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("发送微信文本信息失败;" + resultJson);
        }
    }

    /**
     * 删除账号
     * @param id
     */
    public void deleteAccount(Integer id) {
        String accessToken = getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS);
        String url = String.format(GET_DELETE_ACCOUNT_URL,accessToken, id);

        String resultJson = sendHttpGetRequest(url);
        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("微信端删除账号异常;" + resultJson);
        }
    }


    /**
     * 添加账号
     * @param userId
     * @param name
     * @param mobile
     * @param deptIdList
     */
    public void createAccount(Integer userId, String name, String mobile, List<Integer> deptIdList) {
        String accessToken = getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS);
        String url = String.format(POST_CREATE_ACCOUNT_URL,accessToken);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("userid", userId);
        paramMap.put("name", name);
        paramMap.put("mobile", mobile);
        paramMap.put("department",deptIdList);
        String requestJson = JSON.toJSONString(paramMap);

        String resultJson = sendHttpPostRequest(url, requestJson);
        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("添加新账号到微信失败;" + resultJson);
        }
    }


    /**
     * 更新部门
     * @param id
     * @param deptName
     * @param pId
     */
    public void updateDept(Integer id, String deptName, Integer pId) {
        String accessToken = getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS);
        String url = String.format(POST_UPDATE_DEPT_URL, accessToken);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("name", deptName);
        paramMap.put("id",id);
        paramMap.put("parentid", pId);
        String requestJson = JSON.toJSONString(paramMap);

        String resultJson = sendHttpPostRequest(url, requestJson);
        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("微信端更新部门失败," + resultJson);
        }
    }


    /**
     * 删除部门
     * @param id 部门ID
     */
    public void deleteDept(Integer id) {
        String accessToken = getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS);
        String url = String.format(GET_DELETE_DEPT_URL, accessToken, id);

        String resultJson = sendHttpGetRequest(url);
        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("微信端删除部门失败;" + resultJson);
        }
    }

    /**
     * 添加部门
     * @param id
     * @param deptName
     * @param pId
     */
    public void createDept(Integer id, String deptName, Integer pId) {
        String accessToken = getAccessToken(ACCESS_TOKEN_TYPE_CONTACTS);
        String url = String.format(POST_CREATE_DEPT_URL,accessToken);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("name", deptName);
        paramMap.put("parentid", pId);
        paramMap.put("id", id);

        String requestJson = JSON.toJSONString(paramMap);
        String resultJson = sendHttpPostRequest(url, requestJson);

        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if(!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("添加部门到企业微信失败:" + resultJson);
        }

    }


    /**
     * 获取access_token
     * @param type
     * @return
     */
    public String getAccessToken(String type) {
        try {
            return accessTokenCache.get(type);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new WeixinException("获取access_token失败");
        }
    }

    /**
     * HTTP GET 请求
     * @param url 请求路径
     * @return 返回JSON
     */
    private String sendHttpGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeixinException("Http GET 请求异常");
        }
    }

    /**
     * HTTP POST 请求
     * @param url 请求地址
     * @param json post 参数
     * @return 返回JSON
     */
    private String sendHttpPostRequest(String url, String json) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new WeixinException("Http POST 请求异常");
        }
    }

}
