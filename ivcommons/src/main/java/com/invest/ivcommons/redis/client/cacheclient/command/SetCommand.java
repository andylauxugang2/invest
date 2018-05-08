package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.ScanArgs;
import com.lambdaworks.redis.ValueScanCursor;
import com.lambdaworks.redis.api.sync.RedisSetCommands;
import java.util.Set;

/**
 * Created by yanjie on 2016/5/3.
 *
 */
public class SetCommand extends CommandBase {

    public SetCommand(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * SADD
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * 当 key 不是集合类型时，返回一个错误。
     * 时间复杂度:O(N)， N 是被添加的元素的数量。
     *
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
     */
    public Long sadd(String key, String... members) {
        checkStringArray("members", members);
        return invokeStringCommand("sadd",key, false, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).sadd(key, members));
    }

    /**
     * SADD
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * 当 key 不是集合类型时，返回一个错误。
     * 时间复杂度:O(N)， N 是被添加的元素的数量。
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
     */
    public Long saddBit(String key, byte[]... members) {
        checkByteArray("members", members);
        return invokeByteCommand("saddBit",key, false, (cmd) -> (this.<RedisSetCommands<String, byte[]>>CastCommand(cmd)).sadd(key, members));
    }

    /**
     * SCARD key
     * 返回集合 key 的基数(集合中元素的数量)。
     * 时间复杂度:O(1)
     * @return 集合的基数。当 key 不存在时，返回 0 。
     */
    public Long scard(String key) {
        return invokeStringCommand("scard",key, true, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).scard(key));
    }

    /**
     * SISMEMBER key member
     * 判断 member 元素是否集合 key 的成员。
     * 时间复杂度:O(1)
     * @return 如果 member 元素是集合的成员，返回 1 。如果 member 元素不是集合的成员，或 key 不存在，返回 0 。
     */
    public Boolean sismember(String key, String member) {
        checkString("members", member);
        return invokeStringCommand("sismember",key, true, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).sismember(key, member));
    }

    /**
     * SISMEMBER
     * 判断 member 元素是否集合 key 的成员。
     * 时间复杂度:O(1)
     * @return 如果 member 元素是集合的成员，返回 1 。如果 member 元素不是集合的成员，或 key 不存在，返回 0 。
     */
    public Boolean sismemberBit(String key, byte[] member) {
        checkByte("members", member);
        return invokeByteCommand("sismemberBit",key, true, (cmd) -> (this.<RedisSetCommands<String, byte[]>>CastCommand(cmd)).sismember(key, member));
    }

    /**
     * SMEMBERS
     * 返回集合 key 中的所有成员。不存在的 key 被视为空集合。
     * 时间复杂度:O(N)， N 为集合的基数。
     * @return 集合中的所有成员。
     */
    public Set<String> smembers(String key) {
        return invokeStringCommand("smembers",key, true, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).smembers(key));
    }

    /**
     * SMEMBERS
     * 返回集合 key 中的所有成员。不存在的 key 被视为空集合。
     * 时间复杂度:O(N)， N 为集合的基数。
     * @return 集合中的所有成员。
     */
    public Set<byte[]> smembersBit(String key) {
        return invokeByteCommand("smembersBit",key, true, (cmd) -> (this.<RedisSetCommands<String, byte[]>>CastCommand(cmd)).smembers(key));
    }

    /**
     * SPOP
     * 移除并返回集合中的一个随机元素。
     * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
     * 时间复杂度:O(1)
     * @return 被移除的随机元素。当 key 不存在或 key 是空集时，返回 null 。
     */
    public String spop(String key) {
        return invokeStringCommand("spop",key, false, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).spop(key));
    }

    /**
     * SPOP
     * 移除并返回集合中的一个随机元素。
     * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
     * 时间复杂度:O(1)
     * @return 被移除的随机元素。当 key 不存在或 key 是空集时，返回 null 。
     */
    public byte[] spopBit(String key) {
        return invokeByteCommand("spopBit",key, false, (cmd) -> (this.<RedisSetCommands<String, byte[]>>CastCommand(cmd)).spop(key));
    }

    /**
     * SRANDMEMBER
     * 如果命令执行时,返回集合中的一个随机元素。
     * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER 则仅仅返回随机元素，而不对集合进行任何改动。
     * 时间复杂度: O(1)
     * @return 返回一个元素；如果集合为空，返回 null 。
     */
    public String srandmember(String key) {
        return invokeStringCommand("srandmember",key, true, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).srandmember(key));
    }

    /**
     * SRANDMEMBER
     * 如果命令执行时,返回集合中的一个随机元素。
     * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER 则仅仅返回随机元素，而不对集合进行任何改动。
     * 时间复杂度: O(1)
     * @return 返回一个元素；如果集合为空，返回 null 。
     */
    public byte[] srandmemberBit(String key) {
        return invokeByteCommand("srandmemberBit",key, true, (cmd) -> (this.<RedisSetCommands<String, byte[]>>CastCommand(cmd)).srandmember(key));
    }

    /**
     * SREM
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * 当 key 不是集合类型，返回一个错误。
     * 时间复杂度:O(N)， N 为给定 member 元素的数量。
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    public Long srem(String key, String... members) {
        checkStringArray("members",members);
        return invokeStringCommand("srem",key, false, (cmd) -> (this.<RedisSetCommands<String, String>>CastCommand(cmd)).srem(key, members));
    }

    /**
     * SREM
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * 当 key 不是集合类型，返回一个错误。
     * 时间复杂度:O(N)， N 为给定 member 元素的数量。
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    public Long sremBit(String key, byte[]... members) {
        checkByteArray("members",members);
        return invokeByteCommand("sremBit",key, false, (cmd) -> (this.<RedisSetCommands<String, byte[]>>CastCommand(cmd)).srem(key, members));
    }

    /**
     * SSCAN 命令用于迭代集合键中的元素。
     */
    public ValueScanCursor<String> sscan(String key, String pattern, ValueScanCursor<String> cursor) {
        checkString("pattern", pattern);
        return invokeStringCommand("sscan",key, true, (cmd) -> {
            ScanArgs scanArgs = ScanArgs.Builder.limit(100).match(pattern);
            if (cursor == null)
                return this.<RedisSetCommands<String, String>>CastCommand(cmd).sscan(key, scanArgs);
            else
                return this.<RedisSetCommands<String, String>>CastCommand(cmd).sscan(key, cursor, scanArgs);
        });
    }

    /**
     * SSCAN 命令用于迭代集合键中的元素。
     */
    public ValueScanCursor<byte[]> sscanBit(String key, String pattern, ValueScanCursor<byte[]> cursor) {
        checkString("pattern", pattern);
        return invokeByteCommand("sscanBit",key, true, (cmd) -> {
            ScanArgs scanArgs = ScanArgs.Builder.limit(100).match(pattern);
            if (cursor == null)
                return this.<RedisSetCommands<String, byte[]>>CastCommand(cmd).sscan(key, scanArgs);
            else
                return this.<RedisSetCommands<String, byte[]>>CastCommand(cmd).sscan(key, cursor, scanArgs);
        });
    }
}
