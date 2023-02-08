package comp3350.inba.persistence.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.TransactionPersistence;

public class TransactionPersistenceStub implements TransactionPersistence {
    private List<Transaction> transactions;

    public TransactionPersistenceStub() {
        long currTime = System.currentTimeMillis() / 1000L;
        this.transactions = new ArrayList<>();

        transactions.add(new Transaction(currTime, 11.11, "Transportation"));
        transactions.add(new Transaction(currTime - 1, 12.34, "Food"));
        transactions.add(new Transaction(currTime - 2, 7.89, "Education"));
        transactions.add(new Transaction(currTime - 3, 6.66, "Entertainment"));
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
