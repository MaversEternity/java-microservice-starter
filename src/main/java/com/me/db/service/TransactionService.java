package com.me.db.service;

import java.util.function.Supplier;

public interface TransactionService {

    <T> T withTransaction(Supplier<T> task);

     void withTransaction(Runnable task);

}
