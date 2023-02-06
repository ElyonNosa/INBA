package comp3350.inba.business;

import java.util.Collections;
import java.util.List;

import comp3350.inba.application.Service;
import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.TransactionPersistence;

public class AccessTransactions
{
    private TransactionPersistence transactionPersistence;
    private List<Transaction> transactions;
    private Transaction transaction;
    private int currentTransaction;

    public AccessTransactions()
    {
        transactionPersistence = Service.getTransactionPersistence();
        transactions = null;
        transaction = null;
        currentTransaction = 0;
    }

    public List<Transaction> getTransactions()
    {
        transactions = transactionPersistence.getTransactionSequential();
        return Collections.unmodifiableList(transactions);
    }

    public Transaction getSequential()
    {
        String result = null;
        if (transactions == null)
        {
            transactions = transactionPersistence.getTransactionSequential();
            currentTransaction = 0;
        }
        if (currentTransaction < transactions.size())
        {
            transaction = (Transaction) transactions.get(currentTransaction);
            currentTransaction++;
        }
        else
        {
            transactions = null;
            transaction = null;
            currentTransaction = 0;
        }
        return transaction;
    }

    public Transaction getRandom(long time)
    {
        transactions = transactionPersistence.getTransactionRandom(new Transaction(time));
        currentTransaction = 0;
        if (currentTransaction < transactions.size())
        {
            transaction = transactions.get(currentTransaction);
            currentTransaction++;
        }
        else
        {
            transactions = null;
            transaction = null;
            currentTransaction = 0;
        }
        return transaction;
    }

    public Transaction insertTransaction(Transaction currentTransaction)
    {
        return transactionPersistence.insertTransaction(currentTransaction);
    }

    public Transaction updateTransaction(Transaction currentTransaction)
    {
        return transactionPersistence.updateTransaction(currentTransaction);
    }

    public void deleteTransaction(Transaction currentTransaction)
    {
        transactionPersistence.deleteTransaction(currentTransaction);
    }
}
