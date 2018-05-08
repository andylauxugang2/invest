package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.api.sync.RedisListCommands;

import java.util.List;

/**
 * Created by yanjie on 2016/5/3.
 *
 */
public class ListCommand extends CommandBase {

    public ListCommand(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * LLEN
     * 返回列表 key 的长度。
     * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .
     * 如果 key 不是列表类型，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 列表 key 的长度
     */
    public Long llen(String key) {
        return invokeStringCommand("llen",key, true, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).llen(key));
    }

    /**
     * LINDEX
     * 返回列表 key 中，下标为 index 的元素。
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 如果 key 不是列表类型，返回一个错误。
     * 时间复杂度：
     * O(N)， N 为到达下标 index 过程中经过的元素数量。
     * 因此，对列表的头元素和尾元素执行 LINDEX 命令，复杂度为O(1)。
     *
     * @return 列表中下标为 index 的元素。如果 index 参数的值不在列表的区间范围内(out of range)，返回 null 。
     */
    public String lindex(String key, long index) {
        return invokeStringCommand("lindex",key, true, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lindex(key, index));
    }

    /**
     * LINDEX
     * 返回列表 key 中，下标为 index 的元素。
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 如果 key 不是列表类型，返回一个错误。
     * 时间复杂度：
     * O(N)， N 为到达下标 index 过程中经过的元素数量。
     * 因此，对列表的头元素和尾元素执行 LINDEX 命令，复杂度为O(1)。
     *
     * @return 列表中下标为 index 的元素。如果 index 参数的值不在列表的区间范围内(out of range)，返回 null 。
     */
    public byte[] lindexBit(String key, long index) {
        return invokeByteCommand("lindexBit",key, true, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lindex(key, index));
    }

    /**
     * LINSERT
     * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。
     * 当 pivot 不存在于列表 key 时，不执行任何操作。
     * 当 key 不存在时， key 被视为空列表，不执行任何操作。
     * 如果 key 不是列表类型，返回一个错误。
     * 时间复杂度:
     * O(N)， N 为寻找 pivot 过程中经过的元素数量。
     *
     * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。如果没有找到 pivot ，返回 -1 。如果 key 不存在或为空列表，返回 0
     */
    public Long linsert(String key, boolean before, String pivot, String value) {
        checkString("pivot",pivot);
        checkString("value",value);
        return invokeStringCommand("linsert",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).linsert(key, before, pivot, value));
    }

    /**
     * LINSERT
     * 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。
     * 当 pivot 不存在于列表 key 时，不执行任何操作。
     * 当 key 不存在时， key 被视为空列表，不执行任何操作。
     * 如果 key 不是列表类型，返回一个错误。
     * 时间复杂度:
     * O(N)， N 为寻找 pivot 过程中经过的元素数量。
     *
     * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。如果没有找到 pivot ，返回 -1 。如果 key 不存在或为空列表，返回 0 。
     */
    public Long linsertBit(String key, boolean before, byte[] pivot, byte[] value) {
        checkByte("pivot",pivot);
        checkByte("value",value);
        return invokeByteCommand("linsertBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).linsert(key, before, pivot, value));
    }

    /**
     * POP
     * 移除并返回列表 key 的头元素。
     * 时间复杂度：O(1)
     *
     * @return 列表的头元素。当 key 不存在时，返回 null 。
     */
    public String lpop(String key) {
        return invokeStringCommand("lpop",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lpop(key));
    }

    /**
     * POP
     * 移除并返回列表 key 的头元素。
     * 时间复杂度：O(1)
     *
     * @return 列表的头元素。当 key 不存在时，返回 null 。
     */
    public byte[] lpopBit(String key) {
        return invokeByteCommand("lpopBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lpop(key));
    }

    /**
     * RPOP
     * 移除并返回列表 key 的尾元素。
     * 时间复杂度：O(1)
     *
     * @return 列表的尾元素。当 key 不存在时，返回 null 。
     */
    public String rpop(String key) {
        return invokeStringCommand("rpop",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).rpop(key));
    }

    /**
     * RPOP
     * 移除并返回列表 key 的尾元素。
     * 时间复杂度：O(1)
     *
     * @return 列表的尾元素。当 key 不存在时，返回 null 。
     */
    public byte[] rpopBit(String key) {
        return invokeByteCommand("rpopBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).rpop(key));
    }


    /**
     * LPUSH
     * 将一个或多个值 value 插入到列表 key 的表头
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long lpush(String key, String... value) {
        checkStringArray("value", value);
        return invokeStringCommand("lpush",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lpush(key, value));
    }

    /**
     * LPUSH
     * 将一个或多个值 value 插入到列表 key 的表头
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    public Long lpushBit(String key, byte[]... value) {
        checkByteArray("value", value);
        return invokeByteCommand("lpushBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lpush(key, value));
    }

    /**
     * RPUSH
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 执行 RPUSH 操作后，表的长度。
     */
    public Long rpush(String key, String... value) {
        checkStringArray("value", value);
        return invokeStringCommand("rpush",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).rpush(key, value));
    }

    /**
     * RPUSH
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 执行 RPUSH 操作后，表的长度。
     */
    public Long rpushBit(String key, byte[]... value) {
        checkByteArray("value", value);
        return invokeByteCommand("rpushBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).rpush(key, value));
    }

    /**
     * LPUSHX
     * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。
     * 和 LPUSH 命令相反，当 key 不存在时， LPUSHX 命令什么也不做。
     * 时间复杂度：O(1)
     *
     * @return LPUSHX 命令执行之后，表的长度。
     */
    public Long lpushx(String key, String value) {
        checkString("value", value);
        return invokeStringCommand("lpushx",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lpushx(key, value));
    }

    /**
     * LPUSHX
     * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。
     * 和 LPUSH 命令相反，当 key 不存在时， LPUSHX 命令什么也不做。
     * 时间复杂度：O(1)
     * @return LPUSHX 命令执行之后，表的长度。
     */
    public Long lpushxBit(String key, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("lpushxBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lpushx(key, value));
    }

    /**
     * RPUSHX key value
     * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
     * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做。
     * 时间复杂度：O(1)
     *
     * @return RPUSHX 命令执行之后，表的长度。
     */
    public Long rpushx(String key, String value) {
        checkString("value", value);
        return invokeStringCommand("rpushx",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).rpushx(key, value));
    }

    /**
     * RPUSHX
     * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
     * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做。
     * 时间复杂度：O(1)
     *
     * @return RPUSHX 命令执行之后，表的长度。
     */
    public Long rpushxBit(String key, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("rpushxBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).rpushx(key, value));
    }

    /**
     * LRANGE
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     * 时间复杂度:O(S+N)， S 为偏移量 start ， N 为指定区间内元素的数量。
     *
     * @return 一个列表，包含指定区间内的元素。
     */
    public List<String> lrange(String key, long start, long stop) {
        return invokeStringCommand("lrange",key, true, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lrange(key, start, stop));
    }

    /**
     * LRANGE
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     * 时间复杂度:O(S+N)， S 为偏移量 start ， N 为指定区间内元素的数量。
     * @return 一个列表，包含指定区间内的元素。
     */
    public List<byte[]> lrangeBit(String key, long start, long stop) {
        return invokeByteCommand("lrangeBit",key, true, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lrange(key, start, stop));
    }

    /**
     * LREM
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * count 的值可以是以下几种：
     * count 大于 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count 小于 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count 等于 0 : 移除表中所有与 value 相等的值。
     * 时间复杂度：O(N)， N 为列表的长度。
     * @return 被移除元素的数量。
     * 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0 。
     */
    public Long lrem(String key, int count, String value) {
        checkString("value", value);
        return invokeStringCommand("lrem",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lrem(key, count, value));
    }

    /**
     * LREM
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * count 的值可以是以下几种：
     * count 大于 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count 小于 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count 等于 0 : 移除表中所有与 value 相等的值。
     * 时间复杂度：O(N)， N 为列表的长度。
     * @return 被移除元素的数量。
     * 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0 。
     */
    public Long lremBit(String key, int count, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("lremBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lrem(key, count, value));
    }

    /**
     * LSET
     * 将列表 key 下标为 index 的元素的值设置为 value 。
     * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
     * 关于列表下标的更多信息，请参考 LINDEX 命令。
     * 时间复杂度：对头元素或尾元素进行 LSET 操作，复杂度为 O(1)。其他情况下，为 O(N)， N 为列表的长度。
     */
    public Boolean lset(String key, int index, String value) {
        checkString("value", value);
        String result = invokeStringCommand("lset",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).lset(key, index, value));
        return "OK".equals(result);
    }

    /**
     * LSET
     * 将列表 key 下标为 index 的元素的值设置为 value 。
     * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
     * 关于列表下标的更多信息，请参考 LINDEX 命令。
     * 时间复杂度：对头元素或尾元素进行 LSET 操作，复杂度为 O(1)。其他情况下，为 O(N)， N 为列表的长度。
     */
    public Boolean lsetBit(String key, int index, byte[] value) {
        checkByte("value", value);
        String result = invokeByteCommand("lsetBit",key, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).lset(key, index, value));
        return "OK".equals(result);
    }

    /**
     * LTRIM
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 当 key 不是列表类型时，返回一个错误。
     * LTRIM 命令通常和 LPUSH 命令或 RPUSH 命令配合使用，举个例子：
     * LPUSH log newest_log
     * LTRIM log 0 99
     * 这个例子模拟了一个日志程序，每次将最新日志 newest_log 放到 log 列表中，并且只保留最新的 100 项。注意当这样使用 LTRIM 命令时，时间复杂度是O(1)，因为平均情况下，每次只有一个元素被移除。
     * 注意LTRIM命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表 list ，对该列表执行 LTRIM list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LTRIM 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，或者 start > stop ， LTRIM 返回一个空列表(因为 LTRIM 已经将整个列表清空)。
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     */
    public Boolean ltrim(String key, long start, long stop) {
        String result = invokeStringCommand("ltrim",key, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).ltrim(key, start, stop));
        return "OK".equals(result);
    }
}
