package com.me.db.service;

import java.util.function.Supplier;

public interface TransactionService {

    <T> T execute(Supplier<T> task);

     void execute(Runnable task);

}
