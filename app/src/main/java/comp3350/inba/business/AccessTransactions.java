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
    /*
     * Constructor
     */
    public AccessTransactions()
    {
        // do nothing
    }

    /**
     * Obtain transaction list from the database.
     * @return The database's list of transactions.
     */
    public List<Transaction> getTransactions()
    {
        return Collections.unmodifiableList(Service.getTransactionPersistence().getTransactionList());
    }

    /**
     * Insert a transaction to the list.
     * @param currentTransaction the transaction to insert.
     * @return the transaction inserted.
     */
    public Transaction insertTransaction(Transaction currentTransaction)
    {
        return Service.getTransactionPersistence().insertTransaction(currentTransaction);
    }

    /**
     * Update a transaction in the list with an existing timestamp.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    public Transaction updateTransaction(Transaction currentTransaction)
    {
        return Service.getTransactionPersistence().updateTransaction(currentTransaction);
    }

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    public void deleteTransaction(Transaction currentTransaction)
    {
        Service.getTransactionPersistence().deleteTransaction(currentTransaction);
    }

    /**
     * Get index of a transaction with a given timestamp.
     * @param time The timestamp.
     * @return The index of the transaction, or -1 if it doesn't exist.
     */
    public int getTimestampIndex(long time) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = Service.getTransactionPersistence().getTransactionList();
        int output = -1;
        int i = 0;
        // start at end of array to reduce complexity
        for(i = transactions.size() - 1; i >= 0 && output == -1; --i) {
            // check if the timestamps match
            if (time == transactions.get(i).getTime()) {
                output = i;
            }
        }

        return output;
    }

    /**
     * Get the sum of prices within a period of time.
     * This function assumes that transactions are in chronological order!
     *
     * @param start The time to start at.
     * @param end   The time to end at.
     */
    public double getSumInPeriod(long start, long end) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = Service.getTransactionPersistence().getTransactionList();
        double output = 0;
        int i = 0;
        boolean withinPeriod = true;
        // loop through all transactions
        for (i = 0; i < transactions.size() && withinPeriod; i++) {
            // check if transaction is after start time
            if (transactions.get(i).getTime() >= start) {
                // check if transaction is before end time
                withinPeriod = (transactions.get(i).getTime() <= end);
                if(withinPeriod) {
                    output += transactions.get(i).getPrice();
                }
            }
        }
        // truncate output to 2 places after decimal
        output = Math.floor(output * 100) / 100;
        return output;
    }
}
