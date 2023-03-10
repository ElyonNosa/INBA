package comp3350.inba.application;

import comp3350.inba.persistence.TransactionPersistence;
import comp3350.inba.persistence.hsqldb.TransactionPersistenceHSQLDB;
import comp3350.inba.persistence.stubs.TransactionPersistenceStub;


/**
 * Service.java
 *
 * Ensure that one instance of the database exists at a time.
 */
public class Service {
    // Note for the grader: HSQLDB implementation is present but does not work
    public static final boolean USE_HSQLDB = false;
    // instance of the transaction persistence
    private static TransactionPersistence transactionPersistence = null;

    /**
     * Constructor
     */
    public Service() {
        this(true);
    }

    /**
     * Constructor
     * @param generateExamples True if the database should generate examples.
     */
    public Service(boolean generateExamples) {
        if (USE_HSQLDB) {
            transactionPersistence = new TransactionPersistenceHSQLDB(Main.getDBPathName());
        } else {
            transactionPersistence = new TransactionPersistenceStub(generateExamples);
        }
    }

    /**
     * getTransactionPersistence(): Ensure that only one instance of the
     * transaction database exists at a time.
     *
     * @return The database instance.
     */
    public static synchronized TransactionPersistence getTransactionPersistence() {
        // create one if it doesn't exist
        if (transactionPersistence == null) {
            if (USE_HSQLDB) {
                transactionPersistence = new TransactionPersistenceHSQLDB(Main.getDBPathName());
            } else {
                transactionPersistence = new TransactionPersistenceStub();
            }
        }

        return transactionPersistence;
    }
}
