package com.zhf.tkmapperstudy.base;


import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.zhf.tkmapperstudy.help.enumeration.ErrorType;
import com.zhf.tkmapperstudy.help.exception.CustomException;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import com.zhf.tkmapperstudy.help.util.HelpUtil;
import com.zhf.tkmapperstudy.help.util.RequestUtil;
import com.zhf.tkmapperstudy.help.util.StaticUtil;
import com.zhf.tkmapperstudy.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class BaseController {

    protected UserInfo currentUser;
    @Value("${auth.salt}")
    private String salt;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 整个网站每个页面的拦截请求, 基础信息设置
     */
    @ModelAttribute
    public void preHandle(HttpServletRequest request) throws IOException {
        String authHeader = request.getHeader("Authorization");
        if (!HelpUtil.isEmpty(authHeader)) {
            boolean flag = true;
            String resultInfo = JwtTokenUtil.parseToken(authHeader, salt);
            if (!HelpUtil.isEmpty(resultInfo)) {
                String[] split = resultInfo.split("==>");
                if (split.length > 1) {
                    String userId = split[0];
                    if (RequestUtil.insertRequest(userId, 5)) {
                        log.info(userId + "==>qps请求限制已达到");
                        // 操作频繁提示 先去除
//                        throw new CustomException(JSON.toJSONString(JsonResult.fail(ErrorType.QPS_REQUEST_LIMIT_REACHED)));
                    }
                    String info = redisTemplate.opsForValue().get("userInfo:" + userId);
                    if (!HelpUtil.isEmpty(info)) {
                        String jwtToken = JSON.parseObject(info).getString("jwtToken");
                        if (jwtToken.equals(authHeader)) {
                            StaticUtil.permissions.set(JSON.parseObject(info).getString("permissions"));
                            this.currentUser = JSON.toJavaObject(JSON.parseObject(info).getJSONObject("uInfo"), UserInfo.class);
                            flag = false;
                        }
                    }
                }
            }
            if (flag) {
                throw new CustomException(JSON.toJSONString(JsonResult.fail(ErrorType.SYSTEM_NOT_LOGIN)));
            }
        } else {
            throw new CustomException(JSON.toJSONString(JsonResult.fail(ErrorType.SYSTEM_TOKEN_EMPTY)));
        }
    }
}
