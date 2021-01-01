package com.lt.config.shiro;

import com.alibaba.fastjson.JSON;
import com.lt.constants.HttpStatus;
import com.lt.vo.AjaxResult;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

//重写shiro的FormAuthenticationFilter
//如果用户没有登录进入这个过滤器
public class ShiroLoginFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        AjaxResult ajaxResult=AjaxResult.fail();
        ajaxResult.put("code", HttpStatus.UNAUTHORIZED);
        ajaxResult.put("msg", "登录认证失效，请重新登录!");
        httpServletResponse.getWriter().write(JSON.toJSON(ajaxResult).toString());
        return false;
    }
}
