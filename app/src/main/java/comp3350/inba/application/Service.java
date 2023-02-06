package comp3350.inba.application;

import comp3350.inba.persistence.TransactionPersistence;
import comp3350.inba.persistence.stubs.TransactionPersistenceStub;

public class Service
{
    private static TransactionPersistence transactionPersistence = null;

    public static synchronized TransactionPersistence getTransactionPersistence()
    {
        if (transactionPersistence == null)
        {
            transactionPersistence = new TransactionPersistenceStub();
        }

        return transactionPersistence;
    }
}
