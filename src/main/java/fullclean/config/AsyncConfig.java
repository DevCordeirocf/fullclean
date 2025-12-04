package fullclean.config;

import fullclean.security.TenantContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    static class TenantTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            String tenant = TenantContext.getTenantId();
            return () -> {
                try {
                    if (tenant != null) {
                        TenantContext.setTenantId(tenant);
                    }
                    runnable.run();
                } finally {
                    TenantContext.clear();
                }
            };
        }
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setTaskDecorator(new TenantTaskDecorator());
        executor.initialize();
        return executor;
    }
}
