package com.framework.controller;

import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping; 
import org.springframework.core.OrderComparator;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.BeansException;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-1-19
 * Time: 11:41:46
 * To change this template use File | Settings | File Templates.
 */
public class MultiControllerUrlHandlerMapping extends AbstractUrlHandlerMapping {

    private Map<String, MultiController> urlMap = new HashMap<String, MultiController>();

    /**
     * @return Returns the urlMap.
     */
    public Map getUrlMap() {
        return urlMap;
    }


    /**
     * @param urlMap The urlMap to set.
     */
    public void setUrlMap(Map<String, MultiController> urlMap) {
        this.urlMap = urlMap;
    }


    public void initApplicationContext() throws BeansException {
        initialUrlMap();
        registerUrlMap();
    }

    protected void registerUrlMap() throws BeansException {
        if (this.urlMap.isEmpty()) {
            logger.info("Neither 'urlMap' nor 'mappings' set on MultiControllerUrlHandlerMapping");
        } else {
            for (String s : this.urlMap.keySet()) {
                String url = s;
                Object handler = this.urlMap.get(url);
                // prepend with slash if it's not present
                if (!url.startsWith("/")) {
                    url = "/" + url;
                }
                registerHandler(url, handler);
            }
        }

    }

    protected void initialUrlMap() throws BeansException {
        //找查所有MultiMethodController类型和子类型的bean到一个map中,bean Name为key值 ,bean实例为value值
        //WebApplicationContext context= getWebApplicationContext();

        Map matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(getWebApplicationContext(), MultiController.class, true, false);
        List controllers;
        if (!matchingBeans.isEmpty()) {
            controllers = new ArrayList(matchingBeans.values());
            Collections.sort(controllers, new OrderComparator());
            for (int i = 0; controllers != null && i < controllers.size(); i++) {
                MultiController controller = (MultiController) controllers.get(i);
                Properties urlPros = controller.getMethodMappings();
                if(urlPros!=null){
                    Iterator itr = urlPros.keySet().iterator();
                    for (; itr.hasNext();) {
                        String url = (String) itr.next();
    //                    System.out.println(url);
    //                    if(!urlMap.containsKey(url)){
                            urlMap.put(url, controller);
    //                    }
                    }
                }
            }
        }
    }
}
