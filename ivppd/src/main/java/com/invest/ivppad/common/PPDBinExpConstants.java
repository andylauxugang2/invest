package com.invest.ivppad.common;

/**
 * Created by xugang on 2017/8/7.
 */
public class PPDBinExpConstants {
    private static final int BASE = 2; //基数

    //学历
    public static final long B1 = (long) Math.pow(BASE, 0); // =2^0=1 专科
    public static final long B2 = (long) Math.pow(BASE, 1); // =2^1=2 本科
    public static final long B3 = (long) Math.pow(BASE, 2); // =2^2=4 研究生
    public static final long B4 = (long) Math.pow(BASE, 3); // =2^3=8 硕士
    public static final long B5 = (long) Math.pow(BASE, 4); // =2^4=16 博士
    //标的模型等级
    public static final long B6 = (long) Math.pow(BASE, 5); // =2^5=32 AAA
    public static final long B7 = (long) Math.pow(BASE, 6); // =2^6=64 AA
    public static final long B8 = (long) Math.pow(BASE, 7); // =2^7=128 A
    public static final long B9 = (long) Math.pow(BASE, 8); // =2^8=256 B
    public static final long B10 = (long) Math.pow(BASE, 9); // =2^9=512 C
    public static final long B11 = (long) Math.pow(BASE, 10); // =2^10=1024 D
    public static final long B12 = (long) Math.pow(BASE, 11); // =2^11=2048 E
    public static final long B13 = (long) Math.pow(BASE, 12); // =2^12=4096 F
    //学习形式
    public static final long B14 = (long) Math.pow(BASE, 13); // =2^13=8192 普通
    public static final long B15 = (long) Math.pow(BASE, 14); // =2^14=16384 函授
    public static final long B16 = (long) Math.pow(BASE, 15); // =2^15=32768 网络教育
    public static final long B17 = (long) Math.pow(BASE, 16); //= 2^16=65536 自考
    public static final long B18 = (long) Math.pow(BASE, 17); //= 2^17=131072 成人
    //第三方认证信息
    public static final long B19 = (long) Math.pow(BASE, 18); //= 2^18=262144 学历认证
    public static final long B20 = (long) Math.pow(BASE, 19); //= 2^18=524288 征信认证
    public static final long B21 = (long) Math.pow(BASE, 20); //= 2^18=1048576 视频认证
    public static final long B22 = (long) Math.pow(BASE, 21); //= 2^22=2097152 手机认证
    public static final long B23 = (long) Math.pow(BASE, 22); //= 2^22=4194304 户籍认证
    public static final long B24 = (long) Math.pow(BASE, 23); //= 2^23=8388608 学籍认证
    public static final long B25 = (long) Math.pow(BASE, 24); //= 2^24=
    public static final long B26 = (long) Math.pow(BASE, 25); //= 2^25=
    public static final long B27 = (long) Math.pow(BASE, 26); //= 2^26=
    //毕业学校类型
    public static final long B28 = (long) Math.pow(BASE, 27); // 985 134217728
    public static final long B29 = (long) Math.pow(BASE, 28); // 211 268435456
    public static final long B30 = (long) Math.pow(BASE, 29); // 一本 536870912
    public static final long B31 = (long) Math.pow(BASE, 30); // 二本 1073741824
    public static final long B32 = (long) Math.pow(BASE, 31); // 三本 2147483648
    public static final long B33 = (long) Math.pow(BASE, 32); // 职高 4294967296

    public static final long B34 = (long) Math.pow(BASE, 33); //
    public static final long B35 = (long) Math.pow(BASE, 34); //
    public static final long B36 = (long) Math.pow(BASE, 35); // 星
    public static final long B37 = (long) Math.pow(BASE, 36); // 4星
    public static final long B38 = (long) Math.pow(BASE, 37); // 5星
    public static final long B39 = (long) Math.pow(BASE, 38); // 6星
    public static final long B40 = (long) Math.pow(BASE, 39); // 7星

    public static final long B41 = (long) Math.pow(BASE, 39); // ss

    public static boolean isBinaryBitMatched(long source, long target) {
        return (source & target) == target;
    }

    public static boolean testFlagMatch(long flag, long match) {
        if (flag == 0) {
            return false;
        }
        return isBinaryBitMatched(flag, match);
    }

    //将二进制的某标志位置为1
    public static long onBit(long flag, long bit) {
        return flag |= bit;
    }

    //将二进制的某标志位置为0
    public static long offBit(long flag, long bit) {
        return flag &= ~bit;
    }

    public static void main(String[] args) {
        long nv = B30;
        System.out.println(nv + "=" + Long.toBinaryString(nv));
        System.out.println((long)(124 | 262144 | 56 | 8192 | 402653184)); //402923644
    }
}
