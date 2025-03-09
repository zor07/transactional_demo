package com.zor07.transactional_demo.service.propagation.proxy;

import java.math.BigDecimal;

public interface ProxyDemoTransactionService {

    void processTransaction(Long userId, BigDecimal amount, boolean throwError);
}
