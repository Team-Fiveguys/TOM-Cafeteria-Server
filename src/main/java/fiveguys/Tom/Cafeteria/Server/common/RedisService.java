package fiveguys.Tom.Cafeteria.Server.common;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean tryLock(String key, String value, long timeout, TimeUnit unit) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        return success != null && success;
    }

    public void setValue(String key, String value){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setValue(String key, String value, Duration duration){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public String getValue(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(key);
    }

    public void deleteValues(String key){
        redisTemplate.delete(key);
    }

    public void setList(String key, List list){
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.rightPushAll(key, list);
    }
    public void addList(String key, String value){
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key, value);
    }
}
