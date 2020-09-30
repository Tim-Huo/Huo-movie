package com.stylefeng.guns.api.util;

/**
 * @description: 令牌桶
 * @author: Tim_Huo
 * @created: 2020/09/30 05:22
 */
public class TokenBucket {

    private int bucketNums = 100; //桶的容量

    private int rate = 1;         //流的速度

    private int nowTokens;        //当前令牌数量

    private long timestamp = getNowTime(); //时间

    private long getNowTime() {
        return System.currentTimeMillis();
    }

    private int min(int tokens) {
        if(bucketNums > tokens) {
            return tokens;
        } else {
            return bucketNums;
        }
    }

    public boolean getToken() {
        //记录来拿令牌的时间
        long nowTime = getNowTime();
        //添加令牌【判断该有多少个令牌】
        nowTokens = nowTokens + (int)((nowTime - timestamp) * rate);
        //当前令牌数超过桶，就返回桶， 没超过就返回令牌数量
        nowTokens = min(nowTokens);
        System.out.println("当前令牌数量"+nowTokens);
        // 修改拿令牌的时间
        timestamp = nowTime;
        // 判断令牌是否足够
        if(nowTokens < 1){
            return false;
        }else{
            nowTokens -= 1;
            return true;
        }
    }

}
