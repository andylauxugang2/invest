package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.MapScanCursor;
import com.lambdaworks.redis.ScanArgs;
import com.lambdaworks.redis.api.sync.RedisHashCommands;
import java.util.List;
import java.util.Map;

/**
 * Created by yanjie on 2016/5/3.
 *
 */
public class HashCommand extends CommandBase {

    public HashCommand(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * HDEL
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * 时间复杂度:O(N)， N 为要删除的域的数量。
     * @return 被成功移除的域的数量，不包括被忽略的域。
     */
    public Long hdel(String key, String... field) {
        checkStringArray("field", field);
        return this.invokeStringCommand("hdel",key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hdel(key, field));
    }

    /**
     * HEXISTS
     * 查看哈希表 key 中，给定域 field 是否存在。
     * 时间复杂度：O(1)
     * @return 如果哈希表含有给定域，返回 true 。如果哈希表不含有给定域，或 key 不存在，返回 false 。
     */
       public Boolean hexists(String key, String field) {
        checkString("field", field);
        return this.invokeStringCommand("hexists",key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hexists(key, field));
    }

    /**
     * HGET
     * 返回哈希表 key 中给定域 field 的值。
     * 时间复杂度：O(1)
     * @return 返回值:给定域的值。当给定域不存在或是给定 key 不存在时，返回 null 。
     */
    public String hget(String key, String field) {
        checkString("field", field);
        return super.invokeStringCommand("hget", key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hget(key, field));
    }

    /**
     * HGET
     * 返回哈希表 key 中给定域 field 的值。
     * 时间复杂度：O(1)
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 null 。
     */
    public byte[] hgetBit(String key, String field) {
        checkString("field", field);
        return invokeByteCommand("hgetBit", key, true, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hget(key, field));
    }

    /**
     * HINCRBY
     * 为哈希表 key 中的域 field 的值加上增量 increment 。
     * 增量也可以为负数，相当于对给定域进行减法操作。
     * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
     * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     * 时间复杂度：O(1)
     * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
     */
    public Long hincrby(String key, String field, long increment) {
        checkString("field", field);
        return invokeStringCommand("hincrby", key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hincrby(key, field, increment));
    }

    /**
     * HINCRBYFLOAT
     * 为哈希表 key 中的域 field 加上浮点数增量 increment 。
     * 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0 ，然后再执行加法操作。
     * 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作。
     * 当以下任意一个条件发生时，返回一个错误：
     * 域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
     * 域 field 当前的值或给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point number)
     * HINCRBYFLOAT 命令的详细功能和 INCRBYFLOAT 命令类似，请查看 INCRBYFLOAT 命令获取更多相关信息。
     * 时间复杂度：O(1)
     * @return 执行加法操作之后 field 域的值。
     */
    public Double hincrbyfloat(String key, String field, double increment) {
        checkString("field", field);
        return invokeStringCommand("hincrbyfloat",key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hincrbyfloat(key, field, increment));
    }

    /**
     * HLEN
     * 返回哈希表 key 中域的数量。
     * 时间复杂度：O(1)
     * 当 key 不存在时，返回 0 。
     * @return 哈希表中域的数量。
     */
    public Long hlen(String key) {
        return invokeStringCommand("hlen",key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hlen(key));
    }

    /**
     * HMGET
     * 返回哈希表 key 中，一个或多个给定域的值。
     * 如果给定的域不存在于哈希表，那么返回一个 null 值。
     * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 null 值的表。
     * 时间复杂度：O(N)， N 为给定域的数量。
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public List<String> hmget(String key, String[] field) {
        checkStringArray("field", field);
        return invokeStringCommand("hmget",key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hmget(key, field));
    }

    /**
     * HMGET
     * 返回哈希表 key 中，一个或多个给定域的值。
     * 如果给定的域不存在于哈希表，那么返回一个 null 值。
     * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 null 值的表。
     * 时间复杂度：O(N)， N 为给定域的数量。
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public List<byte[]> hmgetBit(String key, String[] field) {
        checkStringArray("field", field);
        return invokeByteCommand("hmgetBit", key, true, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hmget(key, field));
    }

    /**
     * HMSET
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 此命令会覆盖哈希表中已存在的域。
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     * 时间复杂度：O(N)， N 为 field-value 对的数量。
     */
    public Boolean hmset(String key, Map<String, String> map) {
        checkStringMap(map);
       String result= invokeStringCommand("hmset",key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hmset(key, map));
        return "OK".equals(result);
    }

    /**
     * HMSET
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 此命令会覆盖哈希表中已存在的域。
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     * 时间复杂度：O(N)， N 为 field-value 对的数量。
     */
    public Boolean hmset(String key, Map<String, String> map, long seconds) {
        checkStringMap(map);
        String result= invokeStringCommand("hmset",key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hmset(key, map));
        return "OK".equals(result);
    }

    /**
     * HMSET
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 此命令会覆盖哈希表中已存在的域。
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     * 时间复杂度：O(N)， N 为 field-value 对的数量。
     */
    public Boolean hmsetBit(String key, Map<String, byte[]> map) {
        checkByteMap(map);
        String result = invokeByteCommand("hmsetBit",key, false, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hmset(key, map));
        return "OK".equals(result);
    }

    /**
     * HSET
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * 时间复杂度：O(1)
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 true 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 false 。
     */
    public Boolean hset(String key, String field, String value) {
        checkString("field", field);
        checkString("value", value);
       return invokeStringCommand("hset",key,false,cmd->{
            RedisHashCommands<String, String> hashCommands=this.CastCommand(cmd);
            if (hashCommands==null)
                throw new RuntimeException("hashCommands为空!");
           Boolean result= hashCommands.hset(key,field,value);
           if (result==null)
               throw new RuntimeException("result为空");
           return result;
        });
        //return invokeStringCommand(key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hset(key, field, value));
    }

    /**
     * HSET
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * 时间复杂度：O(1)
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 true 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 false 。
     */
    public Boolean hsetBit(String key, String field, byte[] value) {
        checkString("field", field);
        checkByte("value", value);
        return invokeByteCommand("hsetBit",key, false, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hset(key, field, value));
    }

    /**
     * HSETNX
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     * 若域 field 已经存在，该操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * 时间复杂度：O(1)
     * 返回值：设置成功，返回 true 。如果给定域已经存在且没有操作被执行，返回 false 。
     */
    public Boolean hsetnx(String key, String field, String value) {
        checkString("field", field);
        checkString("value", value);
        return invokeStringCommand("hsetnx",key, false, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hsetnx(key, field, value));
    }

    /**
     * HSETNX
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     * 若域 field 已经存在，该操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * 时间复杂度：O(1)
     * 返回值：设置成功，返回 true 。如果给定域已经存在且没有操作被执行，返回 false 。
     */
    public Boolean hsetnxBit(String key, String field, byte[] value) {
        checkString("field", field);
        checkByte("value",value);
        return invokeByteCommand("hsetnxBit",key, false, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hsetnx(key, field, value));
    }

    /**
     * HGETALL
     * 返回哈希表 key 中，所有的域和值。
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * 时间复杂度：O(N)， N 为哈希表的大小。
     * 返回值：以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public Map<String, String> hgetall(String key) {
        return invokeStringCommand("hgetall",key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hgetall(key));
    }

    /**
     * HGETALL
     * 返回哈希表 key 中，所有的域和值。
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * 时间复杂度：O(N)， N 为哈希表的大小。
     * 返回值：以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public Map<String, byte[]> hgetallBit(String key) {
        return invokeByteCommand("hgetallBit",key, true, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hgetall(key));
    }

    /**
     * HKEYS
     * 返回哈希表 key 中的所有域。
     * 时间复杂度：O(N)， N 为哈希表的大小。
     * @return 一个包含哈希表中所有域的表。当 key 不存在时，返回一个空表。
     */
    public List<String> hkeys(String key) {
        return invokeStringCommand("hkeys",key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hkeys(key));
    }

    /**
     * HVALS
     * 返回哈希表 key 中所有域的值。
     * 时间复杂度：O(N)， N 为哈希表的大小。
     * @return 个包含哈希表中所有值的表。当 key 不存在时，返回一个空表。
     */
    public List<String> hvals(String key) {
        return invokeStringCommand("hvals",key, true, (cmd) -> (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hvals(key));
    }

    /**
     * HVALS
     * 返回哈希表 key 中所有域的值。
     * 时间复杂度：O(N)， N 为哈希表的大小。
     * @return 一个包含哈希表中所有值的表。当 key 不存在时，返回一个空表。
     */
    public List<byte[]> hvalsBit(String key) {
        return invokeByteCommand("hvalsBit",key, true, (cmd) -> (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hvals(key));
    }

    /**
     * 命令用于迭代哈希键中的键值对。
     * SCAN 命令， 以及其他增量式迭代命令， 在进行完整遍历的情况下可以为用户带来以下保证： 从完整遍历开始直到完整遍历结束期间， 一直存在于数据集内的所有元素都会被完整遍历返回； 这意味着， 如果有一个元素， 它从遍历开始直到遍历结束期间都存在于被遍历的数据集当中， 那么 SCAN 命令总会在某次迭代中将这个元素返回给用户。
     * 然而因为增量式命令仅仅使用游标来记录迭代状态， 所以这些命令带有以下缺点：
     * 同一个元素可能会被返回多次。 处理重复元素的工作交由应用程序负责， 比如说， 可以考虑将迭代返回的元素仅仅用于可以安全地重复执行多次的操作上。
     * 如果一个元素是在迭代过程中被添加到数据集的， 又或者是在迭代过程中从数据集中被删除的， 那么这个元素可能会被返回， 也可能不会， 这是未定义的（undefined）。
     */
    public MapScanCursor<String, String> hscan(String key, String pattern, MapScanCursor<String, String> cursor) {
        checkString("pattern", pattern);
        return invokeStringCommand("hscan",key, true, (cmd) -> {
            ScanArgs scanArgs = ScanArgs.Builder.limit(100).match(pattern);
            if (cursor == null)
                return (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hscan(key, scanArgs);
            else
                return (this.<RedisHashCommands<String, String>>CastCommand(cmd)).hscan(key, cursor, scanArgs);
        });
    }

    /**
     * 命令用于迭代哈希键中的键值对。
     * SCAN 命令， 以及其他增量式迭代命令， 在进行完整遍历的情况下可以为用户带来以下保证： 从完整遍历开始直到完整遍历结束期间， 一直存在于数据集内的所有元素都会被完整遍历返回； 这意味着， 如果有一个元素， 它从遍历开始直到遍历结束期间都存在于被遍历的数据集当中， 那么 SCAN 命令总会在某次迭代中将这个元素返回给用户。
     * 然而因为增量式命令仅仅使用游标来记录迭代状态， 所以这些命令带有以下缺点：
     * 同一个元素可能会被返回多次。 处理重复元素的工作交由应用程序负责， 比如说， 可以考虑将迭代返回的元素仅仅用于可以安全地重复执行多次的操作上。
     * 如果一个元素是在迭代过程中被添加到数据集的， 又或者是在迭代过程中从数据集中被删除的， 那么这个元素可能会被返回， 也可能不会， 这是未定义的（undefined）。
     */
    public MapScanCursor<String, byte[]> hscanBit(String key, String pattern, MapScanCursor<String, byte[]> cursor) {
        checkString("pattern", pattern);
        return invokeByteCommand("hscanBit",key, true, (cmd) -> {
            ScanArgs scanArgs = ScanArgs.Builder.limit(100).match(pattern);
            if (cursor == null)
                return (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hscan(key, scanArgs);
            else
                return (this.<RedisHashCommands<String, byte[]>>CastCommand(cmd)).hscan(key, cursor, scanArgs);
        });
    }
}
