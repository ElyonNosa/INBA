package comp3350.inba.application;

import comp3350.inba.persistence.TransactionPersistence;
import comp3350.inba.persistence.stubs.TransactionPersistenceStub;

/**
 * Service.java
 *
 * Ensure that one instance of the database exists at a time.
 */
public class Service {
    // instance of the transaction persistence
    private static TransactionPersistence transactionPersistence = null;

    /**
     * getTransactionPersistence(): Ensure that only one instance of the
     * transaction database exists at a time.
     *
     * @return The database instance.
     */
    public static synchronized TransactionPersistence getTransactionPersistence() {
        // create one if it doesn't exist
        if (transactionPersistence == null) {
            transactionPersistence = new TransactionPersistenceStub();
        }

        return transactionPersistence;
    }
}
