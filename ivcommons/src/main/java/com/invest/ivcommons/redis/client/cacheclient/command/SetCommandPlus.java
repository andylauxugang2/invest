package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.api.sync.RedisSetCommands;

import java.util.Set;

/**
 * Created by admin on 2016/5/17.
 *
 */
public class SetCommandPlus extends SetCommand {

    public SetCommandPlus(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * SMOVE source destination member
     * 将 member 元素从 source 集合移动到 destination 集合。
     * SMOVE 是原子性操作。
     * 如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
     * 当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
     * 当 source 或 destination 不是集合类型时，返回一个错误。
     * 时间复杂度:O(1)
     *
     * @return 如果 member 元素被成功移除，返回 true 。如果 member 元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回 false 。
     */
    public Boolean smove(String source, String destination, String value) {
        checkString("source", source);
        checkString("destination", destination);
        checkString("value", value);
        return invokeStringCommand("smove",source, false, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).smove(source, destination, value));
    }

    /**
     * SMOVE source destination member
     * 将 member 元素从 source 集合移动到 destination 集合。
     * SMOVE 是原子性操作。
     * 如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
     * 当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
     * 当 source 或 destination 不是集合类型时，返回一个错误。
     * 时间复杂度:O(1)
     *
     * @return 如果 member 元素被成功移除，返回 true 。如果 member 元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回 false 。
     */
    public Boolean smoveBit(String source, String destination, byte[] value) {
        checkString("source", source);
        checkString("destination", destination);
        checkByte("value", value);
        return invokeByteCommand("smoveBit",source, false, (cmd) -> this.<RedisSetCommands<String, byte[]>>CastCommand(cmd).smove(source, destination, value));
    }

    /**
     * SDIFF
     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。
     * 不存在的 key 被视为空集。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 一个包含差集成员的列表。
     */
    public Set<String> sdiff(String... keys) {
        checkStringArray("keys", keys);
        return invokeStringCommand("sdiff",keys[0], true, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).sdiff(keys));
    }

    /**
     * SDIFF
     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。
     * 不存在的 key 被视为空集。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 一个包含差集成员的列表。
     */
    public Set<byte[]> sdiffBit(String... keys) {
        checkStringArray("keys", keys);
        return invokeByteCommand("sdiffBit",keys[0], true, (cmd) -> this.<RedisSetCommands<String, byte[]>>CastCommand(cmd).sdiff(keys));
    }


    /**
     * SDIFFSTORE
     * 这个命令的作用和 SDIFF 类似，但它将结果保存到 destination 集合，而不是简单地返回结果集。
     * 如果 destination 集合已经存在，则将其覆盖。
     * destination 可以是 key 本身。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 结果集中的元素数量。
     */
    public Long sdiffstore(String destination, String... keys) {
        checkString("destination", destination);
        checkStringArray("keys", keys);
        return invokeStringCommand("sdiffstore",destination, false, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).sdiffstore(destination, keys));
    }

    /**
     * SDIFFSTORE
     * 这个命令的作用和 SDIFF 类似，但它将结果保存到 destination 集合，而不是简单地返回结果集。
     * 如果 destination 集合已经存在，则将其覆盖。
     * destination 可以是 key 本身。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 结果集中的元素数量。
     */
    public Set<String> sinter(String... keys) {
        checkStringArray("keys", keys);
        return invokeStringCommand("sinter",keys[0], false, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).sinter(keys));
    }

    /**
     * SINTER
     * 返回一个集合的全部成员，该集合是所有给定集合的交集。
     * 不存在的 key 被视为空集。
     * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     * 时间复杂度:O(N * M)， N 为给定集合当中基数最小的集合， M 为给定集合的个数。
     *
     * @return 交集成员的列表。
     */
    public Set<byte[]> sinterBit(String... keys) {
        checkStringArray("keys", keys);
        return invokeByteCommand("sinterBit",keys[0], true, (cmd) -> this.<RedisSetCommands<String, byte[]>>CastCommand(cmd).sinter(keys));
    }

    /**
     * SINTERSTORE
     * 这个命令类似于 SINTER 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。
     * 如果 destination 集合已经存在，则将其覆盖。
     * destination 可以是 key 本身。
     * 时间复杂度:O(N * M)， N 为给定集合当中基数最小的集合， M 为给定集合的个数。
     *
     * @return 结果集中的成员数量。
     */
    public Long sinterstore(String destination, String... keys) {
        checkString("destination", destination);
        checkStringArray("keys", keys);
        return invokeStringCommand("sinterstore",destination, false, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).sinterstore(destination, keys));
    }

    /**
     * SUNION
     * 返回一个集合的全部成员，该集合是所有给定集合的并集。
     * 不存在的 key 被视为空集。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 并集成员的列表。
     */
    public Set<String> sunion(String... keys) {
        checkStringArray("keys", keys);
        return invokeStringCommand("sunion",keys[0], true, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).sunion(keys));
    }

    /**
     * SUNION
     * 返回一个集合的全部成员，该集合是所有给定集合的并集。
     * 不存在的 key 被视为空集。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 并集成员的列表。
     */
    public Set<byte[]> sunionBit(String... keys) {
        checkStringArray("keys", keys);
        return invokeByteCommand("sunionBit",keys[0], true, (cmd) -> this.<RedisSetCommands<String, byte[]>>CastCommand(cmd).sunion(keys));
    }

    /**
     * 这个命令类似于 SUNION 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。
     * 如果 destination 已经存在，则将其覆盖。
     * destination 可以是 key 本身。
     * 时间复杂度:O(N)， N 是所有给定集合的成员数量之和。
     *
     * @return 结果集中的元素数量。
     */
    public Long sunionstore(String destination, String... keys) {
        checkString("destination", destination);
        checkStringArray("keys", keys);
        return invokeStringCommand("sunionstore",destination, false, (cmd) -> this.<RedisSetCommands<String, String>>CastCommand(cmd).sunionstore(destination, keys));
    }
}
