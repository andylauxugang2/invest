package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.SortArgs;
import com.lambdaworks.redis.api.sync.RedisKeyCommands;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by admin on 2016/5/16.
 *
 */
public class KeyCommandPlus extends KeyCommand {
    public KeyCommandPlus(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * RENAME
     * 将 key 改名为 newkey 。
     * 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。
     * 当 newkey 已经存在时， RENAME 命令将覆盖旧值。
     * 时间复杂度：O(1)
     * @return 改名成功时提示 OK ，失败时候返回一个错误。
     */
    public Boolean rename(String key, String newKey) {
        checkString("newKey", newKey);
        String result = invokeStringCommand("rename",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).rename(key, newKey));
        return "OK".equals(result);
    }


    /**
     * RENAMENX
     * 当且仅当 newkey 不存在时，将 key 改名为 newkey 。
     * 当 key 不存在时，返回一个错误。
     * 时间复杂度：O(1)
     * @return 修改成功时，返回 true 。如果 newkey 已经存在，返回 false 。
     */
    public Boolean renamenx(String key, String newKey) {
        checkString("newKey", newKey);
        return invokeStringCommand("renamenx",key, false, (cmd) -> (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).renamenx(key, newKey));
    }

    /**
     * SORT
     * 返回或保存给定列表、集合、有序集合 key 中经过排序的元素。
     * 时间复杂度：
     * O(N+M*log(M))， N 为要排序的列表或集合内的元素数量， M 为要返回的元素数量。
     * 如果只是使用 SORT 命令的 GET 选项获取数据而没有进行排序，时间复杂度 O(N)。
     *
     * @return 返回列表形式的排序结果
     */
    public List<String> sort(String key, String pattern, long offset, long count, String[] get, String order, boolean alpha) {
        return invokeStringCommand("sort",key, true, (cmd) -> {
            SortArgs sortArgs = SortArgs.Builder.limit(offset, count);
            if (!StringUtils.isBlank(pattern))
                sortArgs = sortArgs.by(pattern);
            if (get != null && get.length == 0) {
                for (String item : get) {
                    sortArgs = sortArgs.get(item);
                }
            }
            if ("ASC".equals(order))
                sortArgs = sortArgs.asc();
            if ("DESC".equals(order))
                sortArgs = sortArgs.desc();
            if (alpha)
                sortArgs = sortArgs.alpha();
            return (this.<RedisKeyCommands<String, String>>CastCommand(cmd)).sort(key, sortArgs);
        });
    }

    /**
     * SORT
     * 返回或保存给定列表、集合、有序集合 key 中经过排序的元素。
     * 时间复杂度：
     * O(N+M*log(M))， N 为要排序的列表或集合内的元素数量， M 为要返回的元素数量。
     * 如果只是使用 SORT 命令的 GET 选项获取数据而没有进行排序，时间复杂度 O(N)。
     *
     * @return 返回列表形式的排序结果
     */
    public List<byte[]> sortBit(String key, String pattern, long offset, long count, String[] get, String order, boolean alpha) {
        return invokeByteCommand("sortBit",key, true, (cmd) -> {
            SortArgs sortArgs = SortArgs.Builder.limit(offset, count);
            if (!StringUtils.isBlank(pattern))
                sortArgs = sortArgs.by(pattern);
            if (get != null && get.length == 0) {
                for (String item : get) {
                    sortArgs = sortArgs.get(item);
                }
            }
            if ("ASC".equals(order))
                sortArgs = sortArgs.asc();
            if ("DESC".equals(order))
                sortArgs = sortArgs.desc();
            if (alpha)
                sortArgs = sortArgs.alpha();
            return (this.<RedisKeyCommands<String, byte[]>>CastCommand(cmd)).sort(key, sortArgs);
        });
    }
}
