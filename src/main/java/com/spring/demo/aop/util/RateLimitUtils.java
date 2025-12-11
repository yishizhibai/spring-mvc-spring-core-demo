package com.spring.demo.aop.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶算法实现限流
 */
public class RateLimitUtils {
    // 存储每个接口的令牌桶信息：key=接口名，value=令牌桶
    private static final ConcurrentHashMap<String, TokenBucket> TOKEN_BUCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 尝试获取令牌
     */
    public static boolean tryAcquire(String methodKey, int qps) {
        // 获取/创建令牌桶
        TokenBucket tokenBucket = TOKEN_BUCKET_MAP.computeIfAbsent(methodKey, k -> new TokenBucket(qps));
        return tokenBucket.tryAcquire();
    }

    /**
     * 令牌桶内部类
     */
    private static class TokenBucket {
        private final int qps; // 每秒生成令牌数
        private final AtomicLong currentTokens; // 当前令牌数
        private long lastRefillTime; // 上次填充令牌时间

        public TokenBucket(int qps) {
            this.qps = qps;
            this.currentTokens = new AtomicLong(qps);
            this.lastRefillTime = System.currentTimeMillis();
        }

        /**
         * 尝试获取1个令牌
         */
        public boolean tryAcquire() {
            // 1. 填充令牌（根据时间差补充）
            refillTokens();

            // 2. 尝试获取令牌
            if (currentTokens.get() > 0) {
                return currentTokens.decrementAndGet() >= 0;
            }
            return false;
        }

        /**
         * 填充令牌
         */
        private void refillTokens() {
            long now = System.currentTimeMillis();
            long timeElapsed = now - lastRefillTime;
            if (timeElapsed < TimeUnit.SECONDS.toMillis(1)) {
                return; // 1秒内未到，不填充
            }

            // 计算需要填充的令牌数
            long tokensToAdd = (timeElapsed / TimeUnit.SECONDS.toMillis(1)) * qps;
            if (tokensToAdd > 0) {
                currentTokens.set(Math.min(currentTokens.get() + tokensToAdd, qps)); // 令牌数不超过qps
                lastRefillTime = now;
            }
        }
    }
}