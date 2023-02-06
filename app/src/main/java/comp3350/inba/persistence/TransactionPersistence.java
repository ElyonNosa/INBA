package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.Transaction;

public interface TransactionPersistence {
    List<Transaction> getTransactionSequential();

    List<Transaction> getTransactionRandom(Transaction currentTransaction);

    Transaction insertTransaction(Transaction currentTransaction);

    Transaction updateTransaction(Transaction currentTransaction);

    void deleteTransaction(Transaction currentTransaction);
}
