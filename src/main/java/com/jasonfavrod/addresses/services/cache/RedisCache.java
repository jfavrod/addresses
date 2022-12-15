package com.jasonfavrod.addresses.services.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

@Service
public class RedisCache implements CacheService {
    private JedisPool jedisPool;

    @Autowired
    public RedisCache(@Value("${redis.hostname}") String hostname, @Value("${redis.port}") String port) {
        setPool(hostname, Integer.parseInt(port));
    }

    public String get(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void set(String key, String value) {
        /** 30 Minutes */
        int defaultSecondsToExpire = 60 * 30;
        set(key, value, defaultSecondsToExpire);
    }

    public void set(String key, String value, int secondsToExpire) {
        var setParams = new SetParams();
        setParams.ex(secondsToExpire);

        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(key, value, setParams);
        }
    }

    private void setPool(String hostname, int port) {
        jedisPool = new JedisPool(hostname, port);
    }
}
