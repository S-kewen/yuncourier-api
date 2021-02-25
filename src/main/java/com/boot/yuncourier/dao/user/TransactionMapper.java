package com.boot.yuncourier.dao.user;


import com.boot.yuncourier.entity.user.Transaction;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: TransactionMapper-交易記錄dao
 * @Date: 2020-02-01
 */
@Mapper
public interface TransactionMapper {
    int addTransactionByTransaction(Transaction Transaction);
    List<Transaction> geyPayListByUser(User user);
    List<Transaction> getTransactionListByUser(User user);
    int deleteTransactionByTransaction(Transaction transaction);
}