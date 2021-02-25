package com.boot.yuncourier.service.impl.user;

import com.boot.yuncourier.dao.user.TransactionMapper;
import com.boot.yuncourier.entity.user.Transaction;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.user.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: TransactionServiceImpl-交易記錄service
 * @Date: 2020-02-01
 */

@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;
    @Override
    public int addTransactionByTransaction(Transaction transaction) {
        return transactionMapper.addTransactionByTransaction(transaction);
    }
    @Override
    public List<Transaction> geyPayListByUser(User user) {
        return transactionMapper.geyPayListByUser(user);
    }
    @Override
    public List<Transaction> getTransactionListByUser(User user) {
        return transactionMapper.getTransactionListByUser(user);
    }
    @Override
    public int deleteTransactionByTransaction(Transaction transaction) {
        return transactionMapper.deleteTransactionByTransaction(transaction);
    }

}
