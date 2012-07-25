package com.framework.system;

import com.framework.util.WebUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 上下文过滤器
 * User: 姜敏
 * Date: 2009-8-18
 * Time: 19:32:21
 * To change this template use File | Settings | File Templates.
 */

public class WebFilter extends CharacterEncodingFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //System.out.println("WebFilter.doFilterInternal");
        super.doFilterInternal(request, response, filterChain);
    }

    @Override
    protected void initFilterBean() throws ServletException {
        WebUtil.setServletContext(super.getServletContext());
        super.initFilterBean();
    }

    @Override
    public void destroy() {
        WebUtil.removeRequest();
        WebUtil.removeResponse();
        WebUtil.removeServletContext();
        super.destroy();
    }




}