package comp3350.inba.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.TransactionPersistence;

public class TransactionPersistenceStub implements TransactionPersistence {
    private List<Transaction> transactions;
    private final int NUM_EXAMPLES = 10;

    public TransactionPersistenceStub() {
        long currTime = System.currentTimeMillis() / 1000L;
        this.transactions = new ArrayList<>();
        int i = 0;
        long time = 0;
        String category = null;

        // generate random transactions
        for(i = 0; i < NUM_EXAMPLES; i++) {
            time = currTime - (1013L * (NUM_EXAMPLES - i));
            category = Transaction.CATEGORIES[(int)(Math.random() * Transaction.CATEGORIES.length)];

            transactions.add(new Transaction(time, Math.random() * 30, category));
        }
    }
    @Override
    public List<Transaction> getTransactionSequential() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public List<Transaction> getTransactionRandom(Transaction currentTransaction) {
        List<Transaction> newTransactions = new ArrayList<>();
        int index = transactions.indexOf(currentTransaction);
        if (index >= 0)
        {
            newTransactions.add(transactions.get(index));
        }
        return newTransactions;
    }

    @Override
    public Transaction insertTransaction(Transaction currentTransaction) {
        // don't bother checking for duplicates
        transactions.add(currentTransaction);
        return currentTransaction;
    }

    @Override
    public Transaction updateTransaction(Transaction currentTransaction) {
        int index = transactions.indexOf(currentTransaction);
        if (index >= 0)
        {
            transactions.set(index, currentTransaction);
        }
        return currentTransaction;
    }

    @Override
    public void deleteTransaction(Transaction currentTransaction) {
        int index = transactions.indexOf(currentTransaction);
        if (index >= 0)
        {
            transactions.remove(index);
        }
    }
}
