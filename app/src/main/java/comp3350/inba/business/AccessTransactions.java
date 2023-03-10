package comp3350.inba.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.inba.application.Service;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;
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
    public List<Transaction> getTransactions(User currUser)
    {
        return Collections.unmodifiableList(Service.getTransactionPersistence().getTransactionList(currUser));
    }

    /**
     * Insert a transaction to the list.
     * @param currentTransaction the transaction to insert.
     * @return the transaction inserted.
     */
    public Transaction insertTransaction(User currUser, Transaction currentTransaction)
    {
        return Service.getTransactionPersistence().insertTransaction(currUser, currentTransaction);
    }

    /**
     * Update a transaction in the list with an existing timestamp.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    public Transaction updateTransaction(User currUser, Transaction currentTransaction)
    {
        return Service.getTransactionPersistence().updateTransaction(currUser, currentTransaction);
    }

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    public void deleteTransaction(User currUser, Transaction currentTransaction)
    {
        Service.getTransactionPersistence().deleteTransaction(currUser, currentTransaction);
    }

    /**
     * Get index of a transaction with a given timestamp.
     * @param time The timestamp.
     * @return The index of the transaction, or -1 if it doesn't exist.
     */
    public int getTimestampIndex(LocalDateTime time) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = Service.getTransactionPersistence().getTransactionList(User.currUser);
        int output = -1;
        int i = 0;
        // start at end of array to reduce complexity
        for(i = transactions.size() - 1; i >= 0 && output == -1; --i) {
            // check if the timestamps match
            if (time.equals(transactions.get(i).getTime())) {
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
    public double getSumInPeriod(LocalDateTime start, LocalDateTime end) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = Service.getTransactionPersistence().getTransactionList(User.currUser);
        double output = 0;
        int i = 0;
        boolean withinPeriod = true;
        // loop through all transactions
        for (i = 0; i < transactions.size() && withinPeriod; i++) {
            // check if transaction is after or at the start time
            if (transactions.get(i).getTime().isAfter(start.minusSeconds(1))) {
                // check if transaction is before or at the end time
                withinPeriod = transactions.get(i).getTime().isBefore(end.plusSeconds(1));
                if(withinPeriod) {
                    output += transactions.get(i).getPrice();
                }
            }
        }
        // truncate output to 2 places after decimal
        output = Math.floor(output * 100) / 100;
        return output;
    }

    /**
     * Return the index of the transaction after a given date.
     * Return the last index if all transactions are before the given date.
     * This function assumes that transactions are in chronological order!
     * @param date The date to test.
     * @return The index of the transaction after the date.
     */
    public int getIndexAfterDate(LocalDateTime date) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = Service.getTransactionPersistence().getTransactionList(User.currUser);
        int i = 0;
        boolean found = false;
        // loop through all items in the transaction list
        for (i = 0; i < transactions.size() && !found; i++) {
            found = transactions.get(i).getTime().isAfter(date);
        }
        // decrement i upon leaving for loop (unless i is 0)
        if (i > 0) {
            --i;
        }
        return i;
    }

    /**
     * Return the transaction list filtered by category.
     * @param category The category to filter by.
     * @return The filtered list.
     */
    public ArrayList<Transaction> getTransactionsByCategory(String category)
    {
        // the list of transactions obtained from the database
        List<Transaction> transactions = Service.getTransactionPersistence().getTransactionList(User.currUser);
        ArrayList<Transaction> output = new ArrayList<>();
        int i = 0;

        for (i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getCategory().equals(category)) {
                output.add(transactions.get(i));
            }
        }

        return output;
    }
}
