package com.zhf.tkmapperstudy.base;

import com.alibaba.fastjson.JSON;
import com.zhf.tkmapperstudy.help.exception.CustomException;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import com.zhf.tkmapperstudy.help.util.HelpUtil;
import com.zhf.tkmapperstudy.help.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class BaseLogin {

    @ModelAttribute
    public void preHandle(HttpServletRequest request) {
        String ipAddr = RequestUtil.getIpAddr(request);
        log.info("ipAddr>>>" + ipAddr);
        if (RequestUtil.isIpBlacklist(ipAddr)) {
            log.info("此IP已被拒绝访问");
            throw new CustomException(JSON.toJSONString(JsonResult.errorMessage("此IP已被拒绝访问")));
        }
        int times = 30;
        Long aLong = RequestUtil.loginRequestLimit(ipAddr, 30, times);
        if (RequestUtil.insertRequest(ipAddr, 1)) {
            log.info(ipAddr + "==>qps请求限制已达到");
            // 操作频繁提示 先去除
        }
        if (!(aLong == null)) {
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(aLong, 0, ZoneOffset.ofHours(8));
            String startTime = HelpUtil.lctToString(localDateTime);
            log.info("此IP已被限制访问，限制开始时间：" + startTime + ",限制时间" + times + "分钟");
            throw new CustomException(JSON.toJSONString(JsonResult.errorMessage("此IP已被限制访问，请在" + times +
                    "分钟后再次访问，限制开始时间" + startTime)));
        }
    }
}
