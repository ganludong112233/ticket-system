package com.workorder.ticket.service.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * 本Service提供一些对于所有key的通用操作 以及部分类型的常用操作
 */
public interface RedisService {
    
    public RedisTemplate<String, String> getRedisTemplate();

    boolean exists(String key);

    Set<String> keys(String pattern);

    /**
     * add the delta to the key
     * 
     * @param key
     * @param delta
     * @return the new value else -1 if the key is not existed
     */
    long increment(String key, long delta);

    /**
     * 移除KEY
     * 
     * @param key 移除某类KEY
     * @return true or false
     */
    boolean removeKey(String key);

    /**
     * 移除别表kes
     * 
     * @param keys
     */
    void removeKeys(Collection<String> keys);

    /**
     * string 类型的key 操作实现
     * 
     * @param key key
     * @return 结果
     */
    String getForString(String key);

    /**
     * string 类型的设置方法
     * 
     * @param key key
     * @param value value
     * @return 是否操作成功
     */
    boolean setForString(String key, String value);
    
    /**
     * string 类型的设置方法
     * 
     * @param key key
     * @param value value
     * @param timeout 过期时间（秒）
     * @return 是否操作成功
     */
    boolean setForString(String key, String value, long timeout);
    
    /**
     * String 设置不存在
     * 
     * @param key key
     * @param value value
     * @return 是否操作成功
     */
    boolean setNotExsit(String key, String value);

    /**
     * zset 类型的存储
     * 
     * @param key key
     * @param value value
     * @param score 排序值
     * @return 是否操作成功
     */
    boolean setForZSet(String key, String value, double score);

    /**
     * zset 类型， 移除指定值，这个值可以是多个
     * 
     * @param key key
     * @param values values
     * @return 是否操作成功
     */
    boolean removeForZSet(String key, String... values);

    /**
     * zset 类型获取指定范围内的数据
     * 
     * @param key key
     * @param start start
     * @param end end
     * @return 结果
     */
    Set<String> getForZSet(String key, long start, long end);

    /**
     * zset 获取所有的数据
     * 
     * @param key key
     * @return value
     */
    Set<String> getForZSet(String key);

    /**
     * 获取有序集合的某个值的score
     * 
     * @param key key
     * @param value value
     * @return 对应的score
     */
    Double getScoreForZSet(String key, String value);

    /**
     * zset 类型获取指定范围内的数据
     * 
     * @param key key
     * @param start start
     * @param end end
     * @return 结果
     */
    Set<String> getReverseForZSet(String key, long start, long end);

    /**
     * zset 类型获取指定范围内的数据
     * 
     * @param key key
     * @param start start
     * @param end end
     * @return 结果
     */
    Set<ZSetOperations.TypedTuple<String>> getReverseForZSetWithScore(String key, long start,
            long end);

    /**
     * zset 获取指定score范围数据
     * 
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScores(String key, double min,
            double max);

    /**
     * zset 获取指定score范围数据量
     * 
     * @param key
     * @param min
     * @param max
     * @return
     */
    Long countZSetByScore(String key, double min, double max);

    /**
     * zset 获取key的数量
     * 
     * @param key
     * @return
     */
    Long countZSetByKey(String key);

    /**
     * zset 获取所有的数据
     * 
     * @param key key
     * @return value
     */
    Set<String> getReverseForZSet(String key);

    /**
     * zset 类型获取获取指定KEY，指定值的排名
     * 
     * @param key key
     * @param value value
     * @return 结果, 确保结果不为NULL对象，便于调用方处理
     */
    Long getRankForZSet(String key, String value);

    /**
     * zset 类型获取获取指定KEY，指定值的排名
     * 
     * @param key key
     * @param value value
     * @return 结果, 确保结果不为NULL对象，便于调用方处理
     */
    Long getReverseRankForZSet(String key, String value);

    /**
     * zset 删除范围内的数据
     * 
     * @param key key
     * @param min 范围最小值
     * @param max 范围最大值
     * @return 删除条数
     */
    Long removeRangeByScore(String key, double min, double max);

    /**
     * hash 类型获取获取指定KEY的所有内容
     * 
     * @param key key
     * @return 结果, 确保结果不为NULL对象，便于调用方处理
     */
    Map<String, String> getAllForHash(String key);

    /**
     * hash, 获取值，这个仅用于单个KEY的获取
     * 
     * @param key key
     * @param hashKey hashKey
     * @return value value
     */
    String getForHash(String key, String hashKey);

    /**
     * hash， 设置值， 一般情况，如果要添加多个的话，采用这种方式比较合适，对于单个，则采用另一个方法
     * 
     * @param key key
     * @param contentMap 内容
     */
    void putAllForHash(String key, Map<String, String> contentMap);

    /**
     * hash, 设置值，这个仅用于单个KEY的更新
     * 
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    void putForHash(String key, String hashKey, String value);
    
    /**
     * hash, 设置值，这个仅用于单个KEY的更新,当且仅当hashkey不存在时
     * 
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    boolean putHashIfAbsent(String key, String hashKey, String value);
    
    /**
     * hash 返回哈希表key中域的数量
     * @param key
     * @return Long
     */
    Long hashLen(String key);
    
    /**
     * hash 返回哈希表key中所有域的值
     * @param key
     * @return List<String>
     */
    List<String> hashVals(String key);
    
    /**
     * Hash 删除hash的filed
     * @param key
     * @param field
     * @return Long
     */
    Long removeHashField(String key,String field);

    /**
     * 判断一个SET中是否有指定元素
     * 
     * @param key key
     * @param member member
     * @return true or false
     */
    boolean hasMemberForSet(String key, String member);

    /**
     * 将元素添加到Set里
     * 
     * @param key key
     * @param member member
     * @return true or false
     */
    boolean addMemberForSet(String key, String member);

    /**
     * 将多个元素添加到Set里
     * 
     * @param key key
     * @param members members
     * @return true or false
     */
    boolean addMembersForSet(String key, String[] members);

    /**
     * 将元素从Set里移除
     * 
     * @param key key
     * @param member member
     * @return true or false
     */
    boolean removeMemberForSet(String key, String member);

    /**
     * 获取某个key的所有成员
     * 
     * @param key
     * @return
     */
    Set<String> members(String key);

    /**
     * 将item添加到list里
     * 
     * @param key key
     * @param item item
     * @return true or false
     */
    boolean pushForList(String key, String item);

    /**
     * 将多个item添加到list里
     * 
     * @param key key
     * @param List item
     * @return true or false
     */
    void pushAllForList(String key, List<String> items);
    
    /**
     * 从list中pull一个元素
     * 
     * @param key key
     * @return item
     */
    String pullForList(String key);
    
    /**
     * 查看list所有元素
     * 
     * @param key key
     * @return item
     */
    List<String> getAllForList(String key);
    
    /**
     * 获取list长度（不存在默认为0）
     * 
     * @param key key
     * @return int
     */
    Long getLenForList(String key);

    /**
     * 从list中pull一个元素
     * 
     * @param key key
     * @param times 阻塞的时间，以秒为单位
     * @return item
     */
    String blockingPullForList(String key, long times);

    /**
     * 获取zset大小
     * 
     * @param key
     * @return
     */
    Long getSizeForSet(String key);

    /**
     * 批量入库set
     * 
     * @param key
     * @param values
     * @return
     */
    boolean setForZSets(String key, Set<ZSetOperations.TypedTuple<String>> values);

    /**
     * 设置缓存过期时间
     * 
     * @param key
     * @param timeout
     * @return
     */
    boolean setExpire(String key, long timeout);

    /**
     * 设置超时时间
     * 
     * @param key
     * @param timeout
     * @return
     */
    boolean setExpire(String key, long timeout, TimeUnit unit);

    /**
     * 列表的存储
     * 
     * @param key key
     * @param values value
     * @return 是否操作成功
     */
    boolean setForList(String key, List<String> values);

    /**
     * 获取列表
     * 
     * @param key key
     */
    List<String> getForList(String key);

    /**
     * 将hash转换为对象
     * 
     * @param key key
     * @param field field
     * @param clazz 对象类型
     * @param <T> 返回对象
     * @return
     */
    <T> T getObjectForHash(String key, String field, Class<T> clazz);

    /**
     * string 转换为对象
     * 
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getObjectForString(String key, Class<T> clazz);

    /**
     * 获取锁
     * 
     * @param key
     * @return
     */
    boolean getLock(String key);

    /**
     * 释放锁
     * 
     * @param key
     * @return
     */
    boolean releaseLock(String key);

    /**
     * 移除从小到大排名介于start和end元素，包括start和end
     * 
     * @param key
     * @param start
     * @param end
     * @return
     */
    Long removeRangeByRank(String key, long start, long end);

    /**
     * 返回从小到大排名介于start和end元素的member，包括start和end
     * 
     * @param key
     * @param start
     * @param end
     */
    Set<String> getRangeByRank(String key, long start, long end);
    
    Set<String> trimCacheList2N(String key, int maxSize);
    
}
