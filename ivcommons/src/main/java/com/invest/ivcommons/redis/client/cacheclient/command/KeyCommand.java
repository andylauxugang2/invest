package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.KeyScanCursor;
import com.lambdaworks.redis.ScanArgs;
import com.lambdaworks.redis.ScanCursor;
import com.lambdaworks.redis.api.sync.RedisKeyCommands;
import java.util.Date;

/**
 * Created by admin on 2016/5/16.
 *
 */
public class KeyCommand extends CommandBase {

    public KeyCommand(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * DEL
     * 删除给定的一个或多个 key,不存在的 key 会被忽略。
     * 时间复杂度：O(1)
     * 返回值：删除是否成功
     */
    public Boolean del(String key) {
        Long result = invokeStringCommand("del",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).del(key));
        if (result==null)
            return false;
        return result != 0;
    }

    /**
     * EXISTS
     * 检查给定 key 是否存在。
     * 时间复杂度：O(1)
     * @return 若 key 存在，返回 true ，否则返回 false 。
     */
    public Boolean exists(String key) {
        Long result = invokeStringCommand("exists",key, true, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).exists(key));
        if (result==null)
            return false;
        return result != 0;
    }

    /**
     * EXPIRE
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
     * 生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，如果一个命令只是修改(alter)一个带生存时间的 key 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。
     * 比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
     * 另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
     * RENAME 命令的另一种可能是，尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ，这时旧的 another_key (以及它的生存时间)会被删除，然后旧的 key 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。
     * 使用 PERSIST 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。
     * 更新生存时间
     * 可以对一个已经带有生存时间的 key 执行 EXPIRE 命令，新指定的生存时间会取代旧的生存时间。
     * 在新的 Redis 2.6 版本中，延迟被降低到 1 毫秒之内,也即是，就算 key 已经过期，但它还是可能在过期之后一毫秒之内被访问到
     * @return 设置成功返回 true 。当 key 不存在，返回 false 。
     */
    public Boolean expire(String key, int seconds) {
        return invokeStringCommand("expire",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).expire(key, seconds));
    }


    /**
     * EXPIREAT
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。
     * 不同在于 EXPIREAT 命令接受的时间参数是 具体的过期时间。
     * 时间复杂度：O(1)
     *
     * @return 如果生存时间设置成功，返回 true 。当 key 不存在或没办法设置生存时间，返回 false 。
     */
    public Boolean expireat(String key, Date expireTime) {
        if (expireTime==null)
            throw new NullPointerException("expireTime");
        return invokeStringCommand("expireat",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).expireat(key, expireTime));
    }

    /**
     * PERSIST key
     * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
     * 时间复杂度：O(1)
     *
     * @return 当生存时间移除成功时，返回 true,如果 key 不存在或 key 没有设置生存时间，返回 false 。
     */
    public Boolean persist(String key) {
        return invokeStringCommand("persist",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).persist(key));
    }

    /**
     * PEXPIRE
     * 这个命令和 EXPIRE 命令的作用类似，但是它以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位。
     * 时间复杂度：O(1)
     * @return 设置成功，返回 true,key 不存在或设置失败，返回 false
     */
    public Boolean pexpire(String key, int milliseconds) {
        return invokeStringCommand("pexpire",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).pexpire(key, milliseconds));
    }

    /**
     * RANDOMKEY
     * 从当前数据库中随机返回(不删除)一个 key 。
     * 时间复杂度：O(1)
     * @return 当数据库不为空时，返回一个 key 。当数据库为空时，返回 null 。
     */
    public String randomkey() {
        return invokeStringCommand("randomkey","randomkey", true, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).randomkey());
    }


    /**
     * TTL
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     * 时间复杂度：O(1)
     * @return 以秒为单位，返回 key 的剩余生存时间,不存在时,返回null
     */
    public Long ttl(String key) {
        return invokeStringCommand("ttl",key, true, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).ttl(key));
    }

    /**
     * TYPE key
     * 返回 key 所储存的值的类型。
     * 时间复杂度O(1)：
     * @return None (key不存在) String (字符串),List(列表),Set(集合),SortedSet(有序集),Hash(哈希表)
     */
    public String type(String key) {
        return invokeStringCommand("type",key, true, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).type(key));
    }

    /**
     * SCAN
     * 命令都支持增量式迭代， 它们每次执行都只会返回少量元素， 所以这些命令可以用于生产环境， 而不会出现像 KEYS 命令、 SMEMBERS 命令带来的问题 —— 当 KEYS 命令被用于处理一个大的数据库时， 又或者 SMEMBERS 命令被用于处理一个大的集合键时， 它们可能会阻塞服务器达数秒之久。
     * COUNT 参数的默认值为 10 。
     * 在迭代一个足够大的、由哈希表实现的数据库、集合键、哈希键或者有序集合键时， 如果用户没有使用 MATCH 选项， 那么命令返回的元素数量通常和 COUNT 选项指定的一样， 或者比 COUNT 选项指定的数量稍多一些。
     * 在迭代一个编码为整数集合（intset，一个只由整数值构成的小集合）、 或者编码为压缩列表（ziplist，由不同值构成的一个小哈希或者一个小有序集合）时， 增量式迭代命令通常会无视 COUNT 选项指定的值， 在第一次迭代就将数据集包含的所有元素都返回给用户。
     */
    public KeyScanCursor<String> scan(String pattern, ScanCursor cursor) {
        checkString("pattern", pattern);
        return invokeStringCommand("scan","scan", true, (cmd) -> {
            ScanArgs scanArgs = ScanArgs.Builder.limit(100).match(pattern);
            if (cursor == null)
                return (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).scan(scanArgs);
            else
                return (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).scan(cursor, scanArgs);
        });
    }


}
