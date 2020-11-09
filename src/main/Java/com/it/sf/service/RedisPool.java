package com.it.sf.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: ldq
 * @Date: 2020/9/2
 * @Description:
 * @Version: 1.0
 */
@Slf4j
//@Service("redisPool")
@Component
@PropertySource({"classpath:redis.properties"})
public class RedisPool {
    @Resource(name = "jedisPool")
    private ShardedJedisPool shardedJedisPool;
    private static JedisCluster jedisCluster;
    @Value("${redis.clusters.host}")
    private String clusterHost;
    @Value("${redis.cluster.port1}")
    private String port1;
    @Value("${redis.cluster.port2}")
    private String port2;
    @Value("${redis.cluster.port3}")
    private String port3;
    @Value("${redis.cluster.timeout}")
    private String clusterTimeout;
    @Value("${redis.cluster.response.timeout}")
    private String responseTimeout;
    @Value("${redis.cluster.max.attempt}")
    private String maxAttempt;
    @Value("${redis.cluster.max.idle}")
    private String maxIdle;
    @Value("${redis.cluster.min.idle}")
    private String minIdle;
    @Value("${redis.cluster.max.total}")
    private String maxTotal;
    @Value("${redis.cluster.testOnBorrow}")
    private String testOnBorrow;
    @Value("${redis.cluster.testOnReturn}")
    private String testOnReturn;

    @PostConstruct
    public void init() {
        log.info("创建了对象,{}", clusterHost);
    }

    static {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(Integer.parseInt("200"));
        poolConfig.setMinIdle(Integer.parseInt("200"));
        poolConfig.setMaxTotal(Integer.parseInt("200"));
        poolConfig.setTestOnBorrow(Boolean.parseBoolean("true"));
        poolConfig.setTestOnReturn(Boolean.parseBoolean("true"));
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("127.0.0.1", Integer.parseInt("7001")));
        nodes.add(new HostAndPort("127.0.0.1", Integer.parseInt("7002")));
        nodes.add(new HostAndPort("127.0.0.1", Integer.parseInt("7003")));
        nodes.add(new HostAndPort("127.0.0.1", Integer.parseInt("7004")));
        nodes.add(new HostAndPort("127.0.0.1", Integer.parseInt("7005")));
        nodes.add(new HostAndPort("127.0.0.1", Integer.parseInt("7006")));
        jedisCluster = new JedisCluster(nodes, Integer.parseInt("1000"), Integer.parseInt("1000"), Integer.parseInt("5"), poolConfig);

    }

    public ShardedJedis getInstance() {
        ShardedJedis resource = shardedJedisPool.getResource();
        return resource;
    }

    public static JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void closeJedis(JedisCluster jedisCluster) {
        try {
            if (jedisCluster != null) {
                jedisCluster.close();
            }
        } catch (Exception e) {
            log.error("jedisCluster close error:{}", e);
        }
    }

    public void closeJedis(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("shardedJedis close error:{}", e);
        }
    }
}
