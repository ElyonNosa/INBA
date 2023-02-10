package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.Transaction;

/**
 * TransactionPersistence.java
 *
 * Abstract class for transaction database methods.
 */
public interface TransactionPersistence {
    /**
     * Get list of transactions.
     * @return Next transaction.
     */
    List<Transaction> getTransactionList();

    /**
     * Get index of a transaction with a given timestamp.
     * @param time The timestamp.
     * @return The index of the transaction, or -1 if it doesn't exist.
     */
    int getTimestampIndex(long time);

    /**
     * Insert a transaction to the list.
     * @param currentTransaction The transaction to insert.
     * @return The inserted transaction.
     */
    Transaction insertTransaction(Transaction currentTransaction);

    /**
     * Update a transaction that exists in the list.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    Transaction updateTransaction(Transaction currentTransaction);

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    void deleteTransaction(Transaction currentTransaction);
}
