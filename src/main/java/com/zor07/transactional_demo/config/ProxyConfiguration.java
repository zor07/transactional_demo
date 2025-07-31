package com.zor07.transactional_demo.config;

import com.zor07.transactional_demo.repository.TransactionLogRepository;
import com.zor07.transactional_demo.repository.UserRepository;
import com.zor07.transactional_demo.service.proxy.ProxyDemoTransactionService;
import com.zor07.transactional_demo.service.proxy.ProxyDemoTransactionServiceImpl;
import com.zor07.transactional_demo.service.proxy.TransactionalProxyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

@Configuration
public class ProxyConfiguration {

    @Bean
    @Primary
    public ProxyDemoTransactionService proxyDemoTransactionService(
            PlatformTransactionManager transactionManager,
            UserRepository userRepository,
            TransactionLogRepository transactionLogRepository) {
        ProxyDemoTransactionService proxyDemoTransactionService = new ProxyDemoTransactionServiceImpl(userRepository, transactionLogRepository);

        return (ProxyDemoTransactionService) Proxy.newProxyInstance(
                ProxyDemoTransactionService.class.getClassLoader(),
                new Class[]{ProxyDemoTransactionService.class},
                new TransactionalProxyHandler(proxyDemoTransactionService, transactionManager)
        );
    }
}
