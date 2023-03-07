package comp3350.inba.persistence.stubs;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
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
        // list of example categories to be used in the example transactions
        final String[] EXAMPLE_CATS = {"Amenities", "Education", "Entertainment", "Food",
                "Hardware", "Hobby", "Medical", "Misc", "Transportation", "Utilities"};
         LocalDateTime currTime = LocalDateTime.now();
        this.transactions = new ArrayList<>();
        int i = 0;
        LocalDateTime time = null;
        String category = null;
        // time between example transactions
        final int TIME_INTERVAL = 500000;
        // the number of example transactions to create
        final int NUM_EXAMPLES = 100;
        // the max price of an example transaction
        final double MAX_EXAMPLE_PRICE = 30;

        if (generateExamples) {
            // generate random transactions
            for(i = 0; i < NUM_EXAMPLES; i++) {
                // make the timestamp increase with each example
                time = currTime.minusSeconds((long)TIME_INTERVAL * (NUM_EXAMPLES - i));
                // choose a random category
                category = EXAMPLE_CATS[(int)(Math.random() * EXAMPLE_CATS.length)];

                insertTransaction(new Transaction(time, Math.random() * MAX_EXAMPLE_PRICE, category));
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
     * Insert a transaction to the list only if it is chronological.
     * @param currentTransaction The transaction to insert.
     * @return The inserted transaction. Null if the transaction was not chronologically ordered.
     */
    @Override
    public Transaction insertTransaction(Transaction currentTransaction) {
        Transaction output = null;
        // check if timestamp is greater than timestamp of last transaction
        if (transactions.size() == 0 || currentTransaction.getTime().isAfter(
                transactions.get(transactions.size()-1).getTime())) {
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
}
