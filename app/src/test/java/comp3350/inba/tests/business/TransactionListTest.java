package comp3350.inba.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import comp3350.inba.application.Service;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;

public class TransactionListTest {

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

        // add transactions chronologically
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));

        // add transactions not chronologically
        assertNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 3, "should be null")));
        assertNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 30), 523, "should be null")));
        assertNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 40), 83, "should be null")));

        // add transactions chronologically
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 3, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 523, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 83, "index 5")));

        System.out.println("\nStarting testAddChronologically");



        System.out.println("Finished testAddChronologically");
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
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 74, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 432, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 978, "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 4);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, 1);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, 0);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, 2);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 3);
        index = access.getTimestampIndex(LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 5);

        System.out.println("Finished testListGetTimeIndex1");
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
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 74, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 432, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 978, "index 5")));

        // check for nonexistent timestamps
        index = access.getTimestampIndex(LocalDateTime.of(1, 1, 1, 1, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(2038, 3, 12, 15, 9));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(2005, 8, 22, 22, 22));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 19, 41));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(2084, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(2091, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(3000, 4, 13, 17, 1));
        assertEquals(index, -1);

        // add transactions and retest previously nonexistent timestamps
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2084, 4, 13, 17, 1), 3, "index 8")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2091, 4, 13, 17, 1), 1, "index 9")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(3000, 4, 13, 17, 1), 2, "index 10")));
        index = access.getTimestampIndex(LocalDateTime.of(2084, 4, 13, 17, 1));
        assertEquals(index, 6);
        index = access.getTimestampIndex(LocalDateTime.of(2091, 4, 13, 17, 1));
        assertEquals(index, 7);
        index = access.getTimestampIndex(LocalDateTime.of(3000, 4, 13, 17, 1));
        assertEquals(index, 8);

        System.out.println("Finished testListGetTimeIndex2");
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
        assertNotNull(access.insertTransaction(A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 3, "index 0")));
        assertNotNull(access.insertTransaction(B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 523, "index 1")));
        assertNotNull(access.insertTransaction(C = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 83, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 74, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 432, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 978, "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 4);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, 1);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, 0);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, 2);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 3);
        index = access.getTimestampIndex(LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 5);

        // remove some transactions from the list
        access.deleteTransaction(A);
        access.deleteTransaction(B);
        access.deleteTransaction(C);

        // check if their timestamps no longer exist
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, -1);
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, -1);

        // check if the transactions following A,B,C have shifted their indices
        index = access.getTimestampIndex(LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 0);
        index = access.getTimestampIndex(LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 1);
        index = access.getTimestampIndex(LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 2);

        System.out.println("Finished testListGetTimeIndex3");
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
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), 1, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), 2, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), 3, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), 4, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), 5, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), 6, "index 5")));

        // get sum of all transactions
        sum = access.getSumInPeriod(LocalDateTime.of(1984, 1, 1, 0, 0),
                LocalDateTime.of(4984, 1, 1, 0, 0));
        assertEquals(sum, 21, 0);
        // get sum of first 3 transactions
        sum = access.getSumInPeriod(LocalDateTime.of(1984, 3, 13, 17, 1),
                LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(sum, 6, 0);
        // get sum of last 3 transactions
        sum = access.getSumInPeriod(LocalDateTime.of(1984, 4, 13, 17, 42),
                LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(sum, 15, 0);
        // get sum of middle 4 transactions
        sum = access.getSumInPeriod(LocalDateTime.of(1984, 4, 13, 17, 2),
                LocalDateTime.of(2038, 3, 14, 15, 8));
        assertEquals(sum, 14, 0);
        // get sum before the first transaction begins
        sum = access.getSumInPeriod(LocalDateTime.of(1, 1, 1, 0, 0),
                LocalDateTime.of(1984, 1, 1, 0, 0));
        assertEquals(sum, 0, 0);
        // get sum after the first transaction ends
        sum = access.getSumInPeriod(LocalDateTime.of(3333, 1, 1, 0, 0),
                LocalDateTime.of(4444, 1, 1, 0, 0));
        assertEquals(sum, 0, 0);

        System.out.println("Finished testListGetTimeIndex3");
    }
}
