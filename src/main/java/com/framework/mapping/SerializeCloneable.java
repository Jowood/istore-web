package com.framework.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * 对象序列化和克隆
 * User: 姜敏
 * Date: 2009-8-16
 * Time: 18:07:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class SerializeCloneable implements Serializable,Cloneable{
    protected Log logger = LogFactory.getLog(getClass());

    @Override
    public Object clone() throws CloneNotSupportedException {
        ByteArrayOutputStream byteOut = null; //二进制输出流
        ObjectOutputStream objectOut = null;  //对象输出流
        ByteArrayInputStream byteIn = null;   //二进制输入流
        ObjectInputStream objectIn = null;    //对象输入流
        try {
            byteOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(this);
            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            objectIn = new ObjectInputStream(byteIn);
            return objectIn.readObject();
        }
        catch (IOException e) {
            logger.error(e.getMessage());
            throw new CloneNotSupportedException(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new CloneNotSupportedException(e.getMessage());
        }
        finally {
            try {
                if (objectIn != null) {
                    objectIn.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            try {
                if (byteIn != null) {
                    byteIn.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            try {
                if (objectOut != null) {
                    objectOut.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            try {
                if (byteOut != null) {
                    byteOut.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

}
