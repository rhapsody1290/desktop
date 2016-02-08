package me.ele.panicbuying.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xczhang on 15/11/25 上午11:36.
 */
public class Cart {
    private static int cartMode = 0;
    private static Integer card_id=0;
    public static void setCartMode(int mode) {
        System.out.println("this CartMode:"+mode);
        cartMode=mode;
    }

    public static String genCart(String token){
        int num=card_id+cartMode;
        card_id=card_id+3;
        return (token+num);
    }
}
