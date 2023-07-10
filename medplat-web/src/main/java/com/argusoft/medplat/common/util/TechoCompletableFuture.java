package com.argusoft.medplat.common.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * An util class to create thread pool
 * @author harshit
 * @since 31/08/2020 4:30
 */
public class TechoCompletableFuture {

    private TechoCompletableFuture() {
            
    }

    public static ThreadPoolTaskExecutor mobileGetDetailThreadPool;
    public static ThreadPoolTaskExecutor mobileGetDetailAshaThreadPool;

    static {
        mobileGetDetailThreadPool = new ThreadPoolTaskExecutor();
        mobileGetDetailThreadPool.setCorePoolSize(50);
        mobileGetDetailThreadPool.setMaxPoolSize(100);
        mobileGetDetailThreadPool.setKeepAliveSeconds(1000*60);
//        mobileGetDetailThreadPool.setTaskDecorator(new TenantAwareTaskDecorator());
        mobileGetDetailThreadPool.initialize();

        mobileGetDetailAshaThreadPool = new ThreadPoolTaskExecutor();
        mobileGetDetailAshaThreadPool.setCorePoolSize(25);
        mobileGetDetailAshaThreadPool.setMaxPoolSize(50);
        mobileGetDetailAshaThreadPool.setKeepAliveSeconds(1000*60);
//        mobileGetDetailAshaThreadPool.setTaskDecorator(new TenantAwareTaskDecorator());
        mobileGetDetailAshaThreadPool.initialize();
    }
}

