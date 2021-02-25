package com.boot.yuncourier.service.user;


import com.boot.yuncourier.entity.user.Transaction;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: TramsactionService-交易記錄接口
 * @Date: 2020-02-01
 */

public interface TransactionService {
    int addTransactionByTransaction (Transaction transaction);
    List<Transaction> geyPayListByUser(User user);
    List<Transaction> getTransactionListByUser(User user);
    int deleteTransactionByTransaction(Transaction transaction);
}
