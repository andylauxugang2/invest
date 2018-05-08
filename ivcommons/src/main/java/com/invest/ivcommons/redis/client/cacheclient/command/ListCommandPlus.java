package com.invest.ivcommons.redis.client.cacheclient.command;

import com.lambdaworks.redis.api.sync.RedisListCommands;

/**
 * Created by admin on 2016/5/16.
 *
 */
public class ListCommandPlus extends ListCommand {

    public ListCommandPlus(String cacheName, boolean needThrowException) {
        super(cacheName, needThrowException);
    }

    /**
     * RPOPLPUSH source destination
     * 命令 RPOPLPUSH 在一个原子时间内，执行以下两个动作：
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。
     * 将 source 弹出的元素插入到列表 destination ，作为 destination 列表的的头元素。
     * 举个例子，你有两个列表 source 和 destination ， source 列表有元素 a, b, c ， destination 列表有元素 x, y, z ，执行 RPOPLPUSH source destination 之后， source 列表包含元素 a, b ， destination 列表包含元素 c, x, y, z ，并且元素 c 会被返回给客户端。
     * 如果 source 不存在，值 nil 被返回，并且不执行其他动作。
     * 如果 source 和 destination 相同，则列表中的表尾元素被移动到表头，并返回该元素，可以把这种特殊情况视作列表的旋转(rotation)操作。
     * 时间复杂度：O(1)
     * @return 被弹出的元素。
     */
    public String rpoplpush(String source, String destination) {
        checkString("source", source);
        checkString("destination", destination);
        return invokeStringCommand("rpoplpush",source, false, (cmd) -> (this.<RedisListCommands<String, String>>CastCommand(cmd)).rpoplpush(source, destination));
    }

    /**
     * RPOPLPUSH source destination
     * 命令 RPOPLPUSH 在一个原子时间内，执行以下两个动作：
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。
     * 将 source 弹出的元素插入到列表 destination ，作为 destination 列表的的头元素。
     * 举个例子，你有两个列表 source 和 destination ， source 列表有元素 a, b, c ， destination 列表有元素 x, y, z ，执行 RPOPLPUSH source destination 之后， source 列表包含元素 a, b ， destination 列表包含元素 c, x, y, z ，并且元素 c 会被返回给客户端。
     * 如果 source 不存在，值 nil 被返回，并且不执行其他动作。
     * 如果 source 和 destination 相同，则列表中的表尾元素被移动到表头，并返回该元素，可以把这种特殊情况视作列表的旋转(rotation)操作。
     * 时间复杂度：O(1)
     * @return 被弹出的元素。
     */
    public byte[] rpoplpushBit(String source, String destination) {
        checkString("source", source);
        checkString("destination", destination);
        return invokeByteCommand("rpoplpushBit",source, false, (cmd) -> (this.<RedisListCommands<String, byte[]>>CastCommand(cmd)).rpoplpush(source, destination));
    }
}
