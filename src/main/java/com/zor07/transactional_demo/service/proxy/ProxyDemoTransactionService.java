package com.zor07.transactional_demo.service.proxy;

import java.math.BigDecimal;

public interface ProxyDemoTransactionService {

    void processTransaction(Long userId, BigDecimal amount, boolean throwError);
}
