package com.it.sf.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Auther: ldq
 * @Date: 2020/9/12
 * @Description:
 * @Version: 1.0
 */
public class PropUtils {
    private static Logger logger = LoggerFactory.getLogger(PropUtils.class);
    private static Properties properties;
    static {
        InputStream in = null;
        try {
            properties = new Properties();
            in = PropUtils.class.getResourceAsStream("/conf/web-service.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  String  getProp(String key){
        return properties.getProperty(key);
    }

}
