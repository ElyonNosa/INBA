package comp3350.inba.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.TransactionPersistence;

/**
 * TransactionPersistenceStub.java
 *
 * The database containing the transaction list.
 */
public class TransactionPersistenceStub implements TransactionPersistence {
    // the list of transactions
    private final List<Transaction> transactions;

    /**
     * Constructor
     */
    public TransactionPersistenceStub() {
        this(true);
    }

    /**
     * Constructor
     * @param generateExamples True if example transactions should be generated.
     */
    public TransactionPersistenceStub(boolean generateExamples) {
        long currTime = System.currentTimeMillis() / 1000L;
        this.transactions = new ArrayList<>();
        int i = 0;
        long time = 0;
        String category = null;
        // time between example transactions
        final int TIME_INTERVAL = 500000;
        // the number of example transactions to create
        final int NUM_EXAMPLES = 100;

        if (generateExamples) {
            // generate random transactions
            for(i = 0; i < NUM_EXAMPLES; i++) {
                // make the timestamp increase with each example
                time = currTime - ((long)TIME_INTERVAL * (NUM_EXAMPLES - i));
                // choose a random category
                category = Transaction.CATEGORIES[(int)(Math.random() * Transaction.CATEGORIES.length)];

                insertTransaction(new Transaction(time, Math.random() * 30, category));
            }
        }
    }

    /**
     * Getter for the list of transactions.
     * @return the list of transactions.
     */
    @Override
    public List<Transaction> getTransactionList() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Get index of a transaction with a given timestamp.
     * @param time The timestamp.
     * @return The index of the transaction, or -1 if it doesn't exist.
     */
    @Override
    public int getTimestampIndex(long time) {
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
     * Insert a transaction to the list only if it is chronological.
     * @param currentTransaction The transaction to insert.
     * @return The inserted transaction. Null if the transaction was not chronologically ordered.
     */
    @Override
    public Transaction insertTransaction(Transaction currentTransaction) {
        Transaction output = null;
        // check if timestamp is greater than timestamp of last transaction
        if (transactions.size() == 0 || currentTransaction.getTime()
                > transactions.get(transactions.size()-1).getTime()) {
            output = currentTransaction;
            transactions.add(currentTransaction);
        }
        return output;
    }

    /**
     * Update a transaction that exists in the list.
     * @param currentTransaction The transaction with updated properties.
     * @return The updated transaction.
     */
    @Override
    public Transaction updateTransaction(Transaction currentTransaction) {
        int index = transactions.indexOf(currentTransaction);
        if (index >= 0)
        {
            transactions.set(index, currentTransaction);
        }
        return currentTransaction;
    }

    /**
     * Remove a transaction from the list.
     * @param currentTransaction The transaction to delete.
     */
    @Override
    public void deleteTransaction(Transaction currentTransaction) {
        int index = transactions.indexOf(currentTransaction);
        if (index >= 0)
        {
            transactions.remove(index);
        }
    }

    /**
     * Get the sum of prices within a period of time.
     * This function assumes that transactions are in chronological order!
     *
     * @param start The time to start at.
     * @param end   The time to end at.
     */
    @Override
    public double getSumInPeriod(long start, long end) {
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
