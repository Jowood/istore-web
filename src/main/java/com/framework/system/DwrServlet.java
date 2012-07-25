package com.framework.system;

import org.directwebremoting.spring.DwrSpringServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import com.framework.util.WebUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-3-15
 * Time: 18:18:18
 * To change this template use File | Settings | File Templates.
 */
public class DwrServlet extends DwrSpringServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doGet(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        WebUtil.setRequest(request);
        WebUtil.setResponse(response);
        super.doPost(request, response);    
    }

    
}
