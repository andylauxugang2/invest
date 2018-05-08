package com.invest.ivpay.common;

/**
 * Created by xugang on 2017/7/28.
 */
public enum BuyCountTypeEnum {

    buycountType1((short) 1, "100捉宝币+20", 120, 100),
    buycountType2((short) 2, "200捉宝币+60", 260, 200),
    buycountType3((short) 3, "300捉宝币+120", 420, 300),
    buycountType4((short) 4, "400捉宝币+200", 600, 400),
    buycountType5((short) 5, "500捉宝币+300", 800, 500),
    buycountType6((short) 6, "800捉宝币+640", 1440, 800),
    buycountType10((short) 10, "其他数目", 0, 0),
    UNKNOWN((short) -1, "未知", -1, -1);

    private short code;
    private String name;
    private int value;
    private int price;

    BuyCountTypeEnum(short code, String name, int value, int price) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.price = price;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getPrice() {
        return price;
    }

    public static BuyCountTypeEnum findByCode(short code) {
        for (BuyCountTypeEnum item : BuyCountTypeEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
