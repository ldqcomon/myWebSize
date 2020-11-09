package com.it.sf.common;

import com.it.sf.model.UserVo;
import com.it.sf.service.RedisUtilService;
import com.it.sf.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @Auther: ldq
 * @Date: 2020/9/2
 * @Description:
 * @Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RedisUtilServiceTest {
   @Resource
   private RedisUtilService redisUtilService;
    @Test
    public void setValue() {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
//       RedisUtilService redisUtilService = context.getBean(RedisUtilService.class);
        redisUtilService.setValue("user1","tom",0);
    }

    @Test
    public void getValue() {
       // 用它就不要用类上面的两个注解 ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-config.xml");
       // ExtraClass ec = ctx.getBean("extraClass", ExtraClass.class);

    }

    @Test
    public void deleteKey() {
    }

    @Test
    public void keyExist() {
    }
}