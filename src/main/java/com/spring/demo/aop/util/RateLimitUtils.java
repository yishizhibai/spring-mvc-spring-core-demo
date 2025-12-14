package com.spring.demo.aop.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶算法实现限流（修复Lambda变量有效final问题）
 */
public class RateLimitUtils {
    // 存储每个接口的令牌桶信息：key=接口名，value=令牌桶
    private static final ConcurrentHashMap<String, TokenBucket> TOKEN_BUCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 尝试获取令牌
     * @param methodKey 方法唯一标识（类名+方法名）
     * @param qps 每秒生成令牌数
     * @return 是否获取成功
     */
    public static boolean tryAcquire(String methodKey, int qps) {
        // 防止qps为0或负数，此处qps会被修改，所以要声明为final变量供Lambda使用
        final int finalQps = Math.max(qps, 1);
        // 获取/创建令牌桶（Lambda中引用finalQps）
        TokenBucket tokenBucket = TOKEN_BUCKET_MAP.computeIfAbsent(methodKey, k -> new TokenBucket(finalQps));
        return tokenBucket.tryAcquire();
    }

    /**
     * 令牌桶内部类（线程安全）
     */
    private static class TokenBucket {
        private final int qps; // 每秒生成令牌数
        private final AtomicLong currentTokens; // 当前令牌数
        private long lastRefillTime; // 上次填充令牌时间（毫秒）

        public TokenBucket(int qps) {
            this.qps = qps;
            this.currentTokens = new AtomicLong(qps); // 初始化令牌数为qps
            this.lastRefillTime = System.currentTimeMillis();
        }

        /**
         * 尝试获取1个令牌
         */
        public boolean tryAcquire() {
            // 1. 先填充令牌（根据时间差动态补充）
            refillTokens();

            // 2. 尝试获取令牌（原子操作，防止并发问题）
            return currentTokens.getAndUpdate(current -> current > 0 ? current - 1 : current) > 0;
        }

        /**
         * 填充令牌（核心：根据时间差计算需要补充的令牌数）
         */
        private void refillTokens() {
            long now = System.currentTimeMillis();
            long timeElapsed = now - lastRefillTime;

            // 如果时间间隔小于1毫秒，不填充（避免高频计算）
            if (timeElapsed < 1) {
                return;
            }

            // 计算需要填充的令牌数：时间差（秒）* qps（支持毫秒级精度）
            double elapsedSeconds = (double) timeElapsed / TimeUnit.SECONDS.toMillis(1);
            final long tokensToAdd = (long) (elapsedSeconds * qps);

            // 只有令牌数大于0时才填充
            if (tokensToAdd > 0) {
                // 令牌数不能超过qps上限（Lambda中引用final变量tokensToAdd和qps）
                currentTokens.getAndUpdate(current -> {
                    long newTokens = current + tokensToAdd;
                    return newTokens > qps ? qps : newTokens;
                });
                // 更新上次填充时间
                lastRefillTime = now;
            }
        }
    }
}