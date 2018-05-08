package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.ScriptOutputType;
import com.lambdaworks.redis.SetArgs;
import com.lambdaworks.redis.api.sync.RedisScriptingCommands;
import com.lambdaworks.redis.api.sync.RedisStringCommands;

/**
 * Created by admin on 2016/5/17.
 *
 */
public class StringsCommand extends CommandBase {


    static final String setcas = "local value = redis.call('get',KEYS[1])\n" +
            "if value==false or value==nil then\n" +
            "\treturn false\n" +
            "end\n" +
            "if value==ARGV[1] then\n" +
            "redis.call('set',KEYS[1],ARGV[2])\n" +
            "return ARGV[2]\n" +
            "end\n" +
            "return value";

    static final String setexcas = "local value = redis.call('get',KEYS[1])\n" +
            "if value==false or value==nil then\n" +
            "\treturn false\n" +
            "end\n" +
            "if value==ARGV[1] then\n" +
            "redis.call('set',KEYS[1],ARGV[2],'EX',ARGV[3])\n" +
            "return ARGV[2]\n" +
            "end\n" +
            "return value";

    public StringsCommand(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * APPEND
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     * 时间复杂度：平摊O(1)
     *
     * @return 追加 value 之后， key 中字符串的长度。
     */
    public Long append(String key, String value) {
        checkString("value", value);
        return invokeStringCommand("append", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).append(key, value));
    }

    /**
     * APPEND
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     * 时间复杂度：平摊O(1)
     *
     * @return 追加 value 之后， key 中字符串的长度。
     */
    public Long appendBit(String key, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("appendBit", key, false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).append(key, value));
    }


    /**
     * BITCOUNT
     * 计算给定字符串中，被设置为 1 的比特位的数量。
     * 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
     * start 和 end 参数的设置和 GETRANGE 命令类似，都可以使用负数值： 比如 -1 表示最后一个字节， -2 表示倒数第二个字节，以此类推。
     * 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。
     * 时间复杂度：O(N)
     *
     * @return 被设置为 1 的位的数量。
     */
    public Long bitcount(String key, int start, int end) {
        return invokeStringCommand("bitcount", key, true, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).bitcount(key, start, end));
    }

    /**
     * DECR key
     * 将 key 中储存的数字值减一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 关于递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
     * 时间复杂度：O(1)
     *
     * @return 执行 DECR 命令之后 key 的值。
     */
    public Long decr(String key) {
        return invokeStringCommand("decr", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).decr(key));
    }

    /**
     * DECRBY
     * 将 key 所储存的值减去减量 decrement 。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 关于更多递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
     * 时间复杂度：O(1)
     *
     * @return 减去 decrement 之后，key 的值。
     */
    public Long decrby(String key, long value) {
        return invokeStringCommand("decrby", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).decrby(key, value));
    }

    /**
     * GET
     * 返回 key 所关联的字符串值。
     * 如果 key 不存在那么返回特殊值 nil 。
     * 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值。
     * 时间复杂度：O(1)
     *
     * @return 当 key 不存在时，返回 null ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    public String get(String key) {
        return invokeStringCommand("get", key, true, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).get(key));
    }


    /**
     * GET
     * 返回 key 所关联的字符串值。
     * 如果 key 不存在那么返回特殊值 nil 。
     * 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值。
     * 时间复杂度：O(1)
     *
     * @return 当 key 不存在时，返回 null ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    public byte[] getBit(String key) {
        return invokeByteCommand("getBit", key, true, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).get(key));
    }


    /**
     * GETBIT
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
     * 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0 。
     * 时间复杂度：O(1)
     *
     * @return 字符串值指定偏移量上的位(bit)。
     */
    public Long getbit(String key, long offest) {
        return invokeStringCommand("getbit", key, true, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).getbit(key, offest));
    }

    /**
     * GETRANGE key start end
     * 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。
     * 负数偏移量表示从字符串最后开始计数， -1 表示最后一个字符， -2 表示倒数第二个，以此类推。
     * GETRANGE 通过保证子字符串的值域(range)不超过实际字符串的值域来处理超出范围的值域请求。
     * 时间复杂度：O(N)， N 为要返回的字符串的长度。
     * 复杂度最终由字符串的返回值长度决定，但因为从已有字符串中取出子字符串的操作非常廉价(cheap)，所以对于长度不大的字符串，该操作的复杂度也可看作O(1)。
     *
     * @return 截取得出的子字符串。
     */
    public String getrange(String key, long start, long end) {
        return invokeStringCommand("getrange", key, true, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).getrange(key, start, end));
    }

    /**
     * GETSET
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * 当 key 存在但不是字符串类型时，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 返回给定 key 的旧值。 key 没有旧值时，也即是， key 不存在时，返回 null 。
     */
    public String getset(String key, String value) {
        checkString("value", value);
        return invokeStringCommand("getset", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).getset(key, value));
    }

    /**
     * GETSET
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * 当 key 存在但不是字符串类型时，返回一个错误。
     * 时间复杂度：O(1)
     *
     * @return 返回给定 key 的旧值。 key 没有旧值时，也即是， key 不存在时，返回 null 。
     */
    public byte[] getsetBit(String key, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("getsetBit", key, false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).getset(key, value));
    }

    /**
     * INCR
     * 将 key 中储存的数字值增一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 这是一个针对字符串的操作，因为 Redis 没有专用的整数类型，所以 key 内储存的字符串被解释为十进制 64 位有符号整数来执行 INCR 操作。
     * 时间复杂度：O(1)
     *
     * @return 执行 INCR 命令之后 key 的值。
     */
    public Long incr(String key) {
        return invokeStringCommand("incr", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).incr(key));
    }

    /**
     * INCRBY
     * 将 key 所储存的值加上增量 increment 。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 关于递增(increment) / 递减(decrement)操作的更多信息，参见 INCR 命令。
     * 时间复杂度：O(1)
     *
     * @return 加上 increment 之后， key 的值。
     */
    public Long incrby(String key, long value) {
        return invokeStringCommand("incrby", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).incrby(key, value));
    }

    /**
     * INCRBYFLOAT
     * 为 key 中所储存的值加上浮点数增量 increment 。
     * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
     * 如果命令执行成功，那么 key 的值会被更新为（执行加法之后的）新值，并且新值会以字符串的形式返回给调用者。
     * 无论是 key 的值，还是增量 increment ，都可以使用像 2.0e7 、 3e5 、 90e-2 那样的指数符号(exponential notation)来表示，但是，执行 INCRBYFLOAT 命令之后的值总是以同样的形式储存，也即是，它们总是由一个数字，一个（可选的）小数点和一个任意位的小数部分组成（比如 3.14 、 69.768 ，诸如此类)，小数部分尾随的 0 会被移除，如果有需要的话，还会将浮点数改为整数（比如 3.0 会被保存成 3 ）。
     * 除此之外，无论加法计算所得的浮点数的实际精度有多长， INCRBYFLOAT 的计算结果也最多只能表示小数点的后十七位。
     * 当以下任意一个条件发生时，返回一个错误：
     * key 的值不是字符串类型(因为 Redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
     * key 当前的值或者给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point number）
     * 时间复杂度：O(1)
     *
     * @return 执行命令之后 key 的值。
     */
    public Double incrbyfloat(String key, double value) {
        return invokeStringCommand("incrbyfloat", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).incrbyfloat(key, value));
    }

    /**
     * PSETEX
     * 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
     * 时间复杂度：O(1)
     *
     * @return 设置成功时返回 OK 。
     */
    public Boolean psetex(String key, long milliseconds, String value) {
        checkString("value", value);
        String result = invokeStringCommand("psetex", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).psetex(key, milliseconds, value));
        return "OK".equals(result);
    }

    /**
     * PSETEX
     * 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
     * 时间复杂度：O(1)
     *
     * @return 设置成功时返回 OK 。
     */
    public Boolean psetexBit(String key, long milliseconds, byte[] value) {
        checkByte("value", value);
        String result = invokeByteCommand("psetexBit", key, false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).psetex(key, milliseconds, value));
        return "OK".equals(result);
    }

    /**
     * SET key value
     * 将字符串值 value 关联到 key 。
     * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     *
     * @return SET 在设置操作成功完成时，才返回 OK 。
     */
    public Boolean set(String key, String value) {
        checkString("value", value);
        String result = invokeStringCommand("set", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).set(key, value));
        return "OK".equals(result);
    }

    /**
     * SET key value
     * 将字符串值 value 关联到 key 。
     * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     *
     * @return SET 在设置操作成功完成时，才返回 OK 。
     */
    public Boolean setBit(String key, byte[] value) {
        checkByte("value", value);
        String result = invokeByteCommand("setBit", key, false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).set(key, value));
        return "OK".equals(result);
    }

    /**
     * SETBIT
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
     * 位的设置或清除取决于 value 参数，可以是 0 也可以是 1 。
     * 当 key 不存在时，自动生成一个新的字符串值。
     * 字符串会进行伸展(grown)以确保它可以将 value 保存在指定的偏移量上。当字符串值进行伸展时，空白位置以 0 填充。
     * offset 参数必须大于或等于 0 ，小于 2^32 (bit 映射被限制在 512 MB 之内)。
     */
    public Long setbit(String key, long offest, int value) {
        return invokeStringCommand("setbit", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).setbit(key, offest, value));
    }

    /**
     * SETEX
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     * 这个命令类似于以下两个命令：
     * SET key value
     * EXPIRE key seconds  # 设置生存时间
     * 不同之处是， SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
     * 时间复杂度：O(1)
     *
     * @return 设置成功时返回 OK 。当 seconds 参数不合法时，返回一个错误。
     */
    public Boolean setex(String key, long seconds, String value) {
        checkString("value", value);
        String result = invokeStringCommand("setex", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).setex(key, seconds, value));
        return "OK".equals(result);
    }

    /**
     * SETEX
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     * 这个命令类似于以下两个命令：
     * SET key value
     * EXPIRE key seconds  # 设置生存时间
     * 不同之处是， SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
     * 时间复杂度：O(1)
     *
     * @return 设置成功时返回 OK 。当 seconds 参数不合法时，返回一个错误。
     */
    public Boolean setexBit(String key, long seconds, byte[] value) {
        checkByte("value", value);
        String result = invokeByteCommand("setexBit", key, false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).setex(key, seconds, value));
        return "OK".equals(result);
    }

    /**
     * SETNX
     * SETNX key value
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
     * 时间复杂度：O(1)
     *
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    public Boolean setnx(String key, String value) {
        checkString("value", value);
        return invokeStringCommand("setnx", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).setnx(key, value));
    }

    public Boolean setnx(String key, long seconds, String value) {
        checkString("value", value);

        return invokeStringCommand("setnx", key, false, (cmd) -> {
            SetArgs args = (new SetArgs()).ex(seconds).nx();
            String result = this.<RedisStringCommands<String, String>>CastCommand(cmd).set(key, value, args);
            return "OK".equals(result);
        });
    }

    /**
     * SETNX
     * SETNX
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
     * 时间复杂度：O(1)
     *
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    public Boolean setnxBit(String key, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("setnxBit", key, false, (cmd) -> this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).setnx(key, value));
    }

    public Boolean setnxBit(String key, long seconds, byte[] value) {
        checkByte("value", value);
        return invokeByteCommand("setnxBit", key, false, (cmd) -> {
            SetArgs args = (new SetArgs()).ex(seconds).nx();
            String result = this.<RedisStringCommands<String, byte[]>>CastCommand(cmd).set(key, value, args);
            return "OK".equals(result);
        });
    }

    /**
     * SETRANGE
     * 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始。
     * 不存在的 key 当作空白字符串处理。
     * SETRANGE 命令会确保字符串足够长以便将 value 设置在指定的偏移量上，如果给定 key 原来储存的字符串长度比偏移量小(比如字符串只有 5 个字符长，但你设置的 offset 是 10 )，那么原字符和偏移量之间的空白将用零字节(zerobytes, "\x00" )来填充。
     * 注意你能使用的最大偏移量是 2^29-1(536870911) ，因为 Redis 字符串的大小被限制在 512 兆(megabytes)以内。如果你需要使用比这更大的空间，你可以使用多个 key 。
     * 当生成一个很长的字符串时，Redis 需要分配内存空间，该操作有时候可能会造成服务器阻塞(block)。
     * 时间复杂度：对小(small)的字符串，平摊复杂度O(1)。(关于什么字符串是”小”的，请参考 APPEND 命令),否则为O(M)， M 为 value 参数的长度。
     *
     * @return 被 SETRANGE 修改之后，字符串的长度。
     */
    public Long setrange(String key, long offest, String value) {
        checkString("value", value);
        return invokeStringCommand("setrange", key, false, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).setrange(key, offest, value));
    }

    /**
     * STRLEN
     * 返回 key 所储存的字符串值的长度。
     * 当 key 储存的不是字符串值时，返回一个错误。
     * 复杂度：O(1)
     *
     * @return 字符串值的长度。当 key 不存在时，返回 0 。
     */
    public Long strlen(String key) {
        return invokeStringCommand("strlen", key, true, (cmd) -> this.<RedisStringCommands<String, String>>CastCommand(cmd).strlen(key));
    }

    public String setcas(String key, String expect, String update) {
        return invokeStringCommand("setcas", key, true, (cmd) -> this.<RedisScriptingCommands<String, String>>CastCommand(cmd).<String>eval(setcas, ScriptOutputType.VALUE, new String[]{key}, expect, update));
    }

    public String setexcas(String key, String expect, String update, int timeout) {
        return invokeStringCommand("setcas", key, true, (cmd) -> this.<RedisScriptingCommands<String, String>>CastCommand(cmd).<String>eval(setexcas, ScriptOutputType.VALUE, new String[]{key}, expect, update, String.valueOf(timeout)));
    }
}
