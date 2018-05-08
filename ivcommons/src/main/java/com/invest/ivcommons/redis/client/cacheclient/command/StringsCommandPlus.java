package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.api.sync.RedisStringCommands;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/5/17.
 *
 */
public class StringsCommandPlus extends StringsCommand {

     public StringsCommandPlus(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * BITOP
     *对一个或多个保存二进制位的字符串 key 进行位元操作，并将结果保存到 destkey 上。
     *operation 可以是 AND 、 OR 、 NOT 、 XOR 这四种操作中的任意一种：
     *BITOP AND destkey key [key ...] ，对一个或多个 key 求逻辑并，并将结果保存到 destkey 。
     *BITOP OR destkey key [key ...] ，对一个或多个 key 求逻辑或，并将结果保存到 destkey 。
     *BITOP XOR destkey key [key ...] ，对一个或多个 key 求逻辑异或，并将结果保存到 destkey 。
     *BITOP NOT destkey key ，对给定 key 求逻辑非，并将结果保存到 destkey 。
     *除了 NOT 操作之外，其他操作都可以接受一个或多个 key 作为输入。
     */
    public Long bitop(String operation,String destkey,String... keys) {
        checkString("destkey", destkey);
        checkStringArray("keys", keys);
        switch (operation) {
            case "AND":
                return invokeStringCommand("bitop",destkey, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).bitopAnd(destkey, keys));
            case "OR":
                return invokeStringCommand("bitop",destkey, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).bitopOr(destkey, keys));
            case "NOT":
                return invokeStringCommand("bitop",destkey, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).bitopNot(destkey, keys[0]));
            case "XOR":
                return invokeStringCommand("bitop",destkey, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).bitopXor(destkey, keys));
        }
        return 0L;
    }

    /**
     * MGET
     *返回所有(一个或多个)给定 key 的值。
     *如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。因此，该命令永不失败。
     *时间复杂度:O(N) , N 为给定 key 的数量。
     *@return 一个包含所有给定 key 的值的列表。
     */
    public List<String> mget(String... keys) {
        checkStringArray("keys", keys);
        return invokeStringCommand("mget",keys[0], true, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).mget(keys));
    }

    /**
     * MGET
     *返回所有(一个或多个)给定 key 的值。
     *如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 null 。因此，该命令永不失败。
     *时间复杂度:O(N) , N 为给定 key 的数量。
     *@return 一个包含所有给定 key 的值的列表。
     */
    public List<byte[]> mgetBit(String... keys) {
        checkStringArray("keys", keys);
        return invokeByteCommand("mgetBit",keys[0], true, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).mget(keys));
    }

    /**
     * MSET
     *同时设置一个或多个 key-value 对。
     *如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作。
     *MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置，某些给定 key 被更新而另一些给定 key 没有改变的情况，不可能发生。
     *时间复杂度：O(N)， N 为要设置的 key 数量。
     *@return 总是返回 OK (因为 MSET 不可能失败)
     */
    public Boolean mset(Map<String,String> map) {
        checkStringMap(map);
        String result = invokeStringCommand("mset","map", false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).mset(map));
        return "OK".equals(result);
    }

    /**
     * MSET
     *同时设置一个或多个 key-value 对。
     *如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作。
     *MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置，某些给定 key 被更新而另一些给定 key 没有改变的情况，不可能发生。
     *时间复杂度：O(N)， N 为要设置的 key 数量。
     *@return 总是返回 OK (因为 MSET 不可能失败)
     */
    public Boolean msetBit(Map<String, byte[]> map) {
        checkByteMap(map);
        String result = invokeByteCommand("msetBit","map", false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).mset(map));
        return "OK".equals(result);
    }

    /**
     * MSETNX
     *同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     *即使只有一个给定 key 已存在， MSETNX 也会拒绝执行所有给定 key 的设置操作。
     *MSETNX 是原子性的，因此它可以用作设置多个不同 key 表示不同字段(field)的唯一性逻辑对象(unique logic object)，所有字段要么全被设置，要么全不被设置。
     *时间复杂度：O(N)， N 为要设置的 key 的数量。
     *@return 当所有 key 都成功设置，返回 1 。如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
     */
    public Boolean msetnx(Map<String, String> map) {
        checkStringMap(map);
        return invokeStringCommand("msetnx","map", false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).msetnx(map));
    }

    /**
     * MSETNX
     *同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     *即使只有一个给定 key 已存在， MSETNX 也会拒绝执行所有给定 key 的设置操作。
     *MSETNX 是原子性的，因此它可以用作设置多个不同 key 表示不同字段(field)的唯一性逻辑对象(unique logic object)，所有字段要么全被设置，要么全不被设置。
     *时间复杂度：O(N)， N 为要设置的 key 的数量。
     *@return 当所有 key 都成功设置，返回 1 。如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
     */
    public Boolean msetnxBit(Map<String, byte[]> map)
    {
        checkByteMap(map);
        return invokeByteCommand("msetnxBit","map", false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).msetnx(map));
    }
}
