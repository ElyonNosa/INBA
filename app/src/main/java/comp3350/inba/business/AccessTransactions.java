package comp3350.inba.business;

import java.util.Collections;
import java.util.List;

import comp3350.inba.application.Service;
import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.TransactionPersistence;

/**
 * AccessTransactions.java
 *
 * Performs actions on the database of transactions.
 */
public class AccessTransactions
{
    // the transaction "database"
    private final TransactionPersistence transactionPersistence;
    // the list of transactions in the "database"
    private List<Transaction> transactions;

    /*
     * Constructor
     */
    public AccessTransactions()
    {
        // retrieve the database
        transactionPersistence = Service.getTransactionPersistence();
        transactions = null;
    }

    /**
     * Obtain transaction list from the database.
     * @return The database's list of transactions.
     */
    public List<Transaction> getTransactions()
    {
        transactions = transactionPersistence.getTransactionList();
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Check if a timestamp exists in the array.
     * @param time The timestamp of the transaction.
     * @return The index of the transaction with the timestamp, or -1 if it doesn't exist.
     */
    public int getTimestampIndex(long time)
    {
        return transactionPersistence.getTimestampIndex(time);
    }

    /**
     * Insert a transaction to the list.
     * @param currentTransaction the transaction to insert.
     * @return the transaction inserted.
     */
    public Transaction insertTransaction(Transaction currentTransaction)
    {
        return transactionPersistence.insertTransaction(currentTransaction);
    }

    /**
     * Update a transaction in the list with an existing timestamp.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    public Transaction updateTransaction(Transaction currentTransaction)
    {
        return transactionPersistence.updateTransaction(currentTransaction);
    }

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    public void deleteTransaction(Transaction currentTransaction)
    {
        transactionPersistence.deleteTransaction(currentTransaction);
    }
}
