package comp3350.inba.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import comp3350.inba.application.Service;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

public class TransactionListTest {
    User user = new User("UNIT_TESTING!");

    /**
     * Check if list rejects non-chronological transactions
     */
    @Test
    public void testAddChronologically()
    {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);
        // transaction access class
        AccessTransactions access = new AccessTransactions();

        System.out.println("\nStarting testAddChronologically");

        // add transactions chronologically
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));

        // add transactions not chronologically
        assertNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 3, "should be null")));
        assertNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 30), 523, "should be null")));
        assertNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 40), 83, "should be null")));

        // add transactions chronologically
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 3, "index 3")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 523, "index 4")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 83, "index 5")));

        System.out.println("Finished testAddChronologically");

        access.deleteAllTransactions(user);
    }

    /**
     * Find index of transaction with given timestamp
     */
    @Test
    public void testGetTimestampIndex1()
    {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);
        // transaction access class
        AccessTransactions access = new AccessTransactions();
        // temp index variable
        int index = 0;

        System.out.println("\nStarting testListGetTimeIndex1");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 74, "index 3")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 432, "index 4")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 978, "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(user, LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 4);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, 1);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, 0);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, 2);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 3);
        index = access.getTimestampIndex(user, LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 5);

        System.out.println("Finished testListGetTimeIndex1");
        access.deleteAllTransactions(user);
    }

    /**
     * Find index of transaction with nonexistent timestamp
     */
    @Test
    public void testGetTimestampIndex2()
    {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);
        // transaction access class
        AccessTransactions access = new AccessTransactions();
        // temp index variable
        int index = 0;

        System.out.println("\nStarting testListGetTimeIndex2");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 74, "index 3")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 432, "index 4")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 978, "index 5")));

        // check for nonexistent timestamps
        index = access.getTimestampIndex(user, LocalDateTime.of(1, 1, 1, 1, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(2038, 3, 12, 15, 9));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(2005, 8, 22, 22, 22));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 19, 41));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(2084, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(2091, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(3000, 4, 13, 17, 1));
        assertEquals(index, -1);

        // add transactions and retest previously nonexistent timestamps
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2084, 4, 13, 17, 1), 3, "index 8")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2091, 4, 13, 17, 1), 1, "index 9")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(3000, 4, 13, 17, 1), 2, "index 10")));
        index = access.getTimestampIndex(user, LocalDateTime.of(2084, 4, 13, 17, 1));
        assertEquals(index, 6);
        index = access.getTimestampIndex(user, LocalDateTime.of(2091, 4, 13, 17, 1));
        assertEquals(index, 7);
        index = access.getTimestampIndex(user, LocalDateTime.of(3000, 4, 13, 17, 1));
        assertEquals(index, 8);

        System.out.println("Finished testListGetTimeIndex2");
        access.deleteAllTransactions(user);
    }

    /**
     * Find index of transaction before and after it was removed.
     */
    @Test
    public void testGetTimestampIndex3()
    {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);
        // transaction access class
        AccessTransactions access = new AccessTransactions();
        // temp index variable
        int index = 0;

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;

        System.out.println("\nStarting testListGetTimeIndex3");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user, A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(user, B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(user, C = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 74, "index 3")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 432, "index 4")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 978, "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(user, LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 4);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, 1);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, 0);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, 2);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 3);
        index = access.getTimestampIndex(user, LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 5);

        // remove some transactions from the list
        access.deleteTransaction(user, A);
        access.deleteTransaction(user, B);
        access.deleteTransaction(user, C);

        // check if their timestamps no longer exist
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, -1);

        // check if the transactions following A,B,C have shifted their indices
        index = access.getTimestampIndex(user, LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 0);
        index = access.getTimestampIndex(user, LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 1);
        index = access.getTimestampIndex(user, LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 2);

        System.out.println("Finished testListGetTimeIndex3");
        access.deleteAllTransactions(user);
    }

    /**
     * Find the sum of transactions in certain periods.
     */
    @Test
    public void testGetSumInPeriod1()
    {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);
        // transaction access class
        AccessTransactions access = new AccessTransactions();
        // temp index variable
        double sum = 0;

        System.out.println("\nStarting testListGetTimeIndex3");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 1, "index 0")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 2, "index 1")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 3, "index 2")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 4, "index 3")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 5, "index 4")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 6, "index 5")));

        // get sum of all transactions
        sum = access.getSumInPeriod(user, LocalDateTime.of(1984, 1, 1, 0, 0),
                LocalDateTime.of(4984, 1, 1, 0, 0));
        assertEquals(sum, 21, 0);
        // get sum of first 3 transactions
        sum = access.getSumInPeriod(user, LocalDateTime.of(1984, 3, 13, 17, 1),
                LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(sum, 6, 0);
        // get sum of last 3 transactions
        sum = access.getSumInPeriod(user, LocalDateTime.of(1984, 4, 13, 17, 42),
                LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(sum, 15, 0);
        // get sum of middle 4 transactions
        sum = access.getSumInPeriod(user, LocalDateTime.of(1984, 4, 13, 17, 2),
                LocalDateTime.of(2038, 3, 14, 15, 8));
        assertEquals(sum, 14, 0);
        // get sum before the first transaction begins
        sum = access.getSumInPeriod(user, LocalDateTime.of(1, 1, 1, 0, 0),
                LocalDateTime.of(1984, 1, 1, 0, 0));
        assertEquals(sum, 0, 0);
        // get sum after the first transaction ends
        sum = access.getSumInPeriod(user, LocalDateTime.of(3333, 1, 1, 0, 0),
                LocalDateTime.of(4444, 1, 1, 0, 0));
        assertEquals(sum, 0, 0);

        System.out.println("Finished testListGetTimeIndex3");
        access.deleteAllTransactions(user);
    }

    /**
     * Get the index of a transaction after a certain date.
     */
    @Test
    public void testGetIndexAfterDate1()
    {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;

        // transaction access class
        AccessTransactions access = new AccessTransactions();

        System.out.println("\nStarting testGetIndexAfterDate1");
        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user, A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 1, "index 0")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 2, "index 1")));
        assertNotNull(access.insertTransaction(user, B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 3, "index 2")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 4, "index 3")));
        assertNotNull(access.insertTransaction(user, C = new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 5, "index 4")));
        assertNotNull(access.insertTransaction(user, new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 6, "index 5")));

        // perform tests on index after date function
        // input date before all transactions
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1,1,1,0,0)), 0, 0);
        // one minute before the second transaction
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,19)), 1, 0);
        // the exact time of the second transaction (will return index of third transaction)
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,20)), 2, 0);
        // one minute before the fourth transaction
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,41)), 3, 0);
        // the exact time of the fourth transaction (will return index of fifth transaction)
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,42)), 4, 0);
        // input date after all transactions
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(4444,1,1,0,0)), 5, 0);

        // remove some transactions from the list
        access.deleteTransaction(user, A);
        access.deleteTransaction(user, B);
        access.deleteTransaction(user, C);

        // re test the same dates but with updated indices
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1,1,1,0,0)), 0, 0);
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,19)), 0, 0);
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,20)), 1, 0);
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,41)), 1, 0);
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(1984,4,13,17,42)), 2, 0);
        assertEquals(access.getIndexAfterDate(user, LocalDateTime.of(4444,1,1,0,0)), 2, 0);

        System.out.println("Finished testGetIndexAfterDate1");
        access.deleteAllTransactions(user);
    }

    /**
     * Filter the transaction list by a given category.
     */
    @Test
    public void testGetTransactionsByCategory1() {
        // prompt transaction persistence to create database and disable example transactions
        new Service(false);

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;
        Transaction D = null;
        Transaction E = null;
        Transaction F = null;

        ArrayList<Transaction> filteredList;

        // transaction access class
        AccessTransactions access = new AccessTransactions();

        System.out.println("\nStarting testGetTransactionsByCategory1");
        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user, A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 1, "Rasit")));
        assertNotNull(access.insertTransaction(user, B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 2, "Rob")));
        assertNotNull(access.insertTransaction(user, C = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 3, "Rasit")));
        assertNotNull(access.insertTransaction(user, D = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 4, "Rasit")));
        assertNotNull(access.insertTransaction(user, E = new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 5, "Rob")));
        assertNotNull(access.insertTransaction(user, F = new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 6, "Rob")));

        // check if the correct transactions went into the Rasit list
        filteredList = access.getTransactionsByCategory(user, "Rasit");
        assertEquals(filteredList.get(0), A);
        assertEquals(filteredList.get(1), C);
        assertEquals(filteredList.get(2), D);

        // check if the correct transactions went into the Rob list
        filteredList = access.getTransactionsByCategory(user, "Rob");
        assertEquals(filteredList.get(0), B);
        assertEquals(filteredList.get(1), E);
        assertEquals(filteredList.get(2), F);

        System.out.println("Finished testGetTransactionsByCategory1");
        access.deleteAllTransactions(user);
    }
}
