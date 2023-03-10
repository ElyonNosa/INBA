package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

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
    List<Transaction> getTransactionList(User currentUser);

    /**
     * Insert a transaction to the list.
     * @param currentTransaction The transaction to insert.
     * @return The inserted transaction.
     */
    Transaction insertTransaction(User currentUser, Transaction currentTransaction);

    /**
     * Update a transaction that exists in the list.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    Transaction updateTransaction(User currentUser, Transaction currentTransaction);

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    void deleteTransaction(User currentUser, Transaction currentTransaction);
}
