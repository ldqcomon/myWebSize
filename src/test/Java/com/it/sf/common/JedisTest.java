package com.it.sf.common;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: ldq
 * @Date: 2020/9/8
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class JedisTest {
    public static void main(String[] args) {
        // 单机版
//        JedisPool jedisPool = new JedisPool("192.168.43.172",7001);
//        Jedis jedis = jedisPool.getResource();
//        String my = jedis.get("ok");
//        log.info("the value of is:{}",my);

        //集群版
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.43.172",7001));
        nodes.add(new HostAndPort("192.168.43.172",7002));
        nodes.add(new HostAndPort("192.168.43.172",7003));
        nodes.add(new HostAndPort("192.168.43.172",7004));
        nodes.add(new HostAndPort("192.168.43.172",7005));
        nodes.add(new HostAndPort("192.168.43.172",7006));
        JedisCluster cluster = new JedisCluster(nodes);
       // cluster.set("ok","ok");
       // cluster.del("ok");
        cluster.set("ok","ok6","NX","PX",10000);
        log.info("the value of key is:{}",cluster.get("ok"));
    }



}
