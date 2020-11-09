package com.it.sf.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

import java.io.IOException;

/**
 * @Auther: ldq
 * @Date: 2020/9/2
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class RedisUtilService {
    @Autowired
    private RedisPool redisPool;
    private static JedisCluster jedisCluster;

    // 保存key
    public String setValue(String key, String value, int timeout) {
        //SetNx：SET if Not eXists，如果存在key，则返回0；如果不存在key，设置成功返回1
        // SetEx：会覆盖同一个key的值
        ShardedJedis jedis = null;
        String result = "";
        try {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            jedis = redisPool.getInstance();
            if (timeout == 0) {
                result = jedis.set(key, value);
            } else {
                result = jedis.setex(key, 60, value);
            }
            log.info("setValue result:{}", result);
        } catch (Exception e) {
            log.error("setValue error:{}", e);
        } finally {
            redisPool.closeJedis(jedis);
        }
        return result;
    }

    // 分布式锁+超时时间设置
    public String setValueNx(String key, String value, int timeout) {
        ShardedJedis jedis = null;
        String result = "";
        try {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            jedis = redisPool.getInstance();
            result = jedis.set(key, value, "nx", "px", timeout * 1000);
            return result;
        } catch (Exception e) {
            log.info("setValueNx error:{}", e);
            return null;
        } finally {
            jedis.close();
        }
    }

    //获取key
    public String getValue(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = redisPool.getInstance();
            return jedis.get(key);
        } catch (Exception e) {
            log.error("getValue error:{}", e);
            return null;
        } finally {
            redisPool.closeJedis(jedis);
        }
    }

    //删除key
    public void deleteKey(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = redisPool.getInstance();
            Long decr = jedis.decr(key);
            log.info("delete key result:{}", decr);
        } catch (Exception e) {
            log.error("deleteKey error:{}", e);
        } finally {
            redisPool.closeJedis(jedis);
        }
    }

    // 判断key是否存在
    public boolean keyExist(String key) {
        try {
            jedisCluster = RedisPool.getJedisCluster();
            return jedisCluster.exists(key);
        } catch (Exception e) {
            log.error("get keyExist error:{}", e);
        }
        return false;
    }

    public  String getByCluster(String key) {
        try {
            jedisCluster = RedisPool.getJedisCluster();
            return jedisCluster.get(key);
        } catch (Exception e) {
            log.error("get the key:{} error:{}", key, e.getMessage());
        }
        return null;
    }

    // 集群模式下不需要关闭,自己会关闭连接的,否则会将集群连接都关闭掉(error:no reachable node in cluster)
    public  String setByCluster(String key, String value, int timeout) {
        try {
            jedisCluster = RedisPool.getJedisCluster();
            if (timeout > 0) {
                return jedisCluster.setex(key, timeout, value);
            } else {
                return jedisCluster.set(key, value);
            }
        } catch (Exception e) {
            log.error("set the key:{} error:{}", key, e.getMessage());
        }
        return null;
    }

    // 分布式锁加上时间限制
    public  String setNxByCluster(String key, String value, int timeout) {
        try {
            jedisCluster = RedisPool.getJedisCluster();
            return jedisCluster.set(key,value,"nx","px",timeout*1000);
        } catch (Exception e) {
            log.error("set the key:{} error:{}", key, e.getMessage());
        }
        return null;
    }


}
