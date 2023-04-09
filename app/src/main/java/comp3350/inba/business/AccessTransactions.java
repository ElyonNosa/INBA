package comp3350.inba.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

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
    // an ordered set of category names
    private static final TreeSet<String> categNames = new TreeSet<>();
    // types of transaction errors
    public enum TransactionError {
        NONE,
        CATEGORY_REQUIRED,
        INVALID_PRICE,
        OVER_LIMIT,
        ALREADY_EXISTS
    }
    // the instance of the transaction persistence
    TransactionPersistence txnPersistance;

    /**
     * Constructor
     */
    public AccessTransactions()
    {
        this(null);
    }

    /**
     * Constructor that allows for predefined category names.
     * @param categNames The predefined list of category names.
     */
    public AccessTransactions(List<String> categNames)
    {
        txnPersistance = Service.getTransactionPersistence();
        if(categNames != null)
        {
            AccessTransactions.categNames.addAll(categNames);
        }
    }

    /**
     * Obtain transaction list from the database.
     * @return The database's list of transactions.
     */
    public List<Transaction> getTransactions(String currUser)
    {
        List<Transaction> output = Collections.unmodifiableList(txnPersistance.getTransactionList(currUser));
        // upon retrieving the list, update the set of category names
        for (Transaction txn : output) {
            categNames.add(txn.getCategoryName());
        }
        return output;
    }

    /**
     * Insert a transaction to the list.
     * @param currentTransaction the transaction to insert.
     * @return the transaction inserted.
     */
    public Transaction insertTransaction(String currUser, Transaction currentTransaction)
    {
        // add the category name to the tree set
        categNames.add(currentTransaction.getCategoryName());
        return txnPersistance.insertTransaction(currUser, currentTransaction);
    }

    /**
     * Update a transaction in the list with an existing timestamp.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    public Transaction updateTransaction(String currUser, Transaction currentTransaction)
    {
        return txnPersistance.updateTransaction(currUser, currentTransaction);
    }

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    public void deleteTransaction(String currUser, Transaction currentTransaction)
    {
        txnPersistance.deleteTransaction(currUser, currentTransaction);
    }

    /**
     * Get index of a transaction with a given timestamp.
     * @param time The timestamp.
     * @return The index of the transaction, or -1 if it doesn't exist.
     */
    public int getTimestampIndex(String currUser, LocalDateTime time) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = txnPersistance.getTransactionList(currUser);
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
    public BigDecimal getSumInPeriod(String currUser, LocalDateTime start, LocalDateTime end) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = txnPersistance.getTransactionList(currUser);
        BigDecimal output = BigDecimal.ZERO;
        int i = 0;
        boolean withinPeriod = true;
        // loop through all transactions
        for (i = 0; i < transactions.size() && withinPeriod; i++) {
            // check if transaction is after or at the start time
            if (transactions.get(i).getTime().isAfter(start.minusSeconds(1))) {
                // check if transaction is before or at the end time
                withinPeriod = transactions.get(i).getTime().isBefore(end.plusSeconds(1));
                if(withinPeriod) {
                    output = output.add(transactions.get(i).getPrice());
                }
            }
        }
        return output;
    }

    /**
     * Return the index of the transaction after a given date.
     * Return the last index if all transactions are before the given date.
     * This function assumes that transactions are in chronological order!
     * @param date The date to test.
     * @return The index of the transaction after the date.
     */
    public int getIndexAfterDate(String currUser, LocalDateTime date) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = txnPersistance.getTransactionList(currUser);
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
    public List<Transaction> getTransactionsByCategory(String currUser, String category)
    {
        // the list of transactions obtained from the database
        List<Transaction> transactions = txnPersistance.getTransactionList(currUser);
        ArrayList<Transaction> output = new ArrayList<>();
        int i = 0;

        for (i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getCategoryName().equals(category)) {
                output.add(transactions.get(i));
            }
        }

        return output;
    }

    /**
     * Delete all user transactions. Used during testing.
     */
    public void deleteAllTransactions(String currUser) {
        // the list of transactions obtained from the database
        List<Transaction> transactions = txnPersistance.getTransactionList(currUser);
        int i = 0;
        for (i = 0; i < transactions.size(); i++) {
            deleteTransaction(currUser, transactions.get(i));
        }
    }

    /**
     * Ensure the transaction has valid properties.
     * @param txn The transaction to check.
     * @param isNewTransaction True if the transaction does not exist in the database.
     * @return The error message if the transaction is invalid, null string otherwise.
     */
    public TransactionError validateTransactionData(Transaction txn, String userid, boolean isNewTransaction) {
        final int LIMIT = 999999999;

        // check for valid category type
        if (txn.getCategoryName() == null || txn.getCategoryName().length() <= 0) {
            return TransactionError.CATEGORY_REQUIRED;
        }

        // check for valid price
        if (txn.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return TransactionError.INVALID_PRICE;
        }

        // check for valid price
        if (txn.getPrice().compareTo(BigDecimal.valueOf(LIMIT)) > 0) {
            return TransactionError.OVER_LIMIT;
        }

        // check if transaction already exists
        if (isNewTransaction && getTimestampIndex(userid, txn.getTime()) != -1) {
            return TransactionError.ALREADY_EXISTS;
        }

        return TransactionError.NONE;
    }

    /**
     * Get a list of the names.
     * @return The category set converted to a string list.
     */
    public List<String> getCategNames() {
        // arraylist has a constructor that allows for creation from tree sets
        return new ArrayList<>(categNames);
    }
}
