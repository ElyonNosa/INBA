package comp3350.inba.application;

import comp3350.inba.persistence.TransactionPersistence;
import comp3350.inba.persistence.UserPersistence;
import comp3350.inba.persistence.hsqldb.TransactionPersistenceHSQLDB;
import comp3350.inba.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.inba.persistence.stubs.TransactionPersistenceStub;


/**
 * Service.java
 *
 * Ensure that one instance of the database exists at a time. Also store the name of the database.
 */
public class Service {
    // set to true for hsqldb database
    public static final boolean USE_HSQLDB = true;
    // instance of the transaction persistence
    private static TransactionPersistence transactionPersistence = null;
    private static UserPersistence userPersistence = null;
    // name of the database script
    private static String dbName="INBA_DATABASE";

    public static void setDBPathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        dbName = name;
    }

    public static String getDBPathName() {
        return dbName;
    }

    /**
     * Constructor
     * @param generateExamples True if the database should generate examples.
     */
    public Service(boolean generateExamples) {
        if (USE_HSQLDB) {
            userPersistence = new UserPersistenceHSQLDB(dbName);
            transactionPersistence = new TransactionPersistenceHSQLDB(dbName);
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
                transactionPersistence = new TransactionPersistenceHSQLDB(dbName);
            } else {
                transactionPersistence = new TransactionPersistenceStub();
            }
        }

        return transactionPersistence;
    }

    /**
     * getUserPersistence(): Ensure that only one instance of the
     * users database exists at a time.
     *
     * @return The database instance.
     */
    public static synchronized UserPersistence getUserPersistence() {
        // create one if it doesn't exist
        if (userPersistence == null) {
            userPersistence = new UserPersistenceHSQLDB(dbName);
        }

        return userPersistence;
    }

}
