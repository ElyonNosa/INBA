package comp3350.inba.tests.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import comp3350.inba.application.Service;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.TransactionPersistence;
import comp3350.inba.persistence.hsqldb.TransactionPersistenceHSQLDB;
import comp3350.inba.tests.utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccessTransactionsIT {
    private AccessTransactions accessTransactions;
    private File tempDB;

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2002, 2, 20, 19,45);
    private static final String TEST_USER = "IT_TEST_DO_NOT_USE";


    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final TransactionPersistence persistence = new TransactionPersistenceHSQLDB(this.tempDB.getAbsolutePath().replace(".script", ""));
        this.accessTransactions = new AccessTransactions(persistence);
        // insert test transactions
        accessTransactions.insertTransaction(TEST_USER, new Transaction(TEST_DATE, BigDecimal.TEN, "Rob"));
        accessTransactions.insertTransaction(TEST_USER, new Transaction(TEST_DATE.plusSeconds(1), BigDecimal.ONE, "Franklin"));
        accessTransactions.insertTransaction(TEST_USER, new Transaction(TEST_DATE.plusSeconds(2), BigDecimal.valueOf(21), "Rasit"));
    }

    @Test
    public void testGetTransactions() {
        final List<Transaction> txns = accessTransactions.getTransactions(TEST_USER);
        assertEquals("Rob", txns.get(0).getCategoryName());
        assertEquals(1, txns.size());

        System.out.println("Finished test AccessTransactions");
    }

    @Test
    public void testDeleteTransaction() {
        final Transaction txn = new Transaction(TEST_DATE.plusSeconds(1), BigDecimal.ONE, "Franklin");
        List<Transaction> txns = accessTransactions.getTransactions(TEST_USER);
        assertEquals(3, txns.size());
        accessTransactions.deleteTransaction(TEST_USER, txn);
        txns = accessTransactions.getTransactions(TEST_USER);
        assertEquals(2, txns.size());
    }

    @Test
    public void testInsertTransaction() {
        final Transaction txn = new Transaction(TEST_DATE.plusSeconds(3), BigDecimal.valueOf(77), "Guderian");
        accessTransactions.insertTransaction(TEST_USER, txn);
        assertEquals(5, accessTransactions.getTransactions(TEST_USER).size());
    }

    @Test
    public void testUpdateTransaction() {
        List<Transaction> txns;
        final Transaction update = new Transaction(TEST_DATE, BigDecimal.valueOf(111), "Rob");
        accessTransactions.updateTransaction(TEST_USER, update);
        txns = accessTransactions.getTransactions(TEST_USER);
        assertEquals(BigDecimal.valueOf(111), txns.get(0).getPrice());
    }

    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }
}

