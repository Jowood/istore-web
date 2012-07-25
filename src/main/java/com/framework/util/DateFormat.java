package com.framework.util;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-1-26
 * Time: 0:19:42
 * To change this template use File | Settings | File Templates.
 */
public class DateFormat extends SimpleDateFormat{
    @Override
    public Date parse(String source) throws ParseException {
        if(source.length()==13){
            try{
                long time = Long.parseLong(source);
                return new Date(time);
            }
            catch (NumberFormatException e){
                return null;
            }
        }
        else if(source.length()==10) this.applyPattern("yyyy-MM-dd");
        else if(source.length()==16) this.applyPattern("yyyy-MM-dd HH:mm");
        else if(source.length()==19) this.applyPattern("yyyy-MM-dd HH:mm:ss");
        return super.parse(source);
    }
 
}
