package comp3350.inba.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import comp3350.inba.application.Service;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

public class TransactionListTest {
    User user = null;
    Service service = null;
    AccessTransactions access = null;
    @Before
    public void beforeTests(){
        // user instance
        user = new User(null);
        // prompt transaction persistence to create database and disable example transactions
        service = new Service(false);
        // transaction access class
        access = new AccessTransactions();
    }

    /**
     * Check if list rejects non-chronological transactions
     */
    @Test
    public void testAddChronologically()
    {

        System.out.println("\nStarting testAddChronologically");

        // add transactions chronologically
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(3), "index 0")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(523), "index 1")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(83), "index 2")));

        // add transactions not chronologically
        assertNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(3), "should be null")));
        assertNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 30), BigDecimal.valueOf(523), "should be null")));
        assertNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 40), BigDecimal.valueOf(83), "should be null")));

        // add transactions chronologically
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(3), "index 3")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(523), "index 4")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(83), "index 5")));

        System.out.println("Finished testAddChronologically");

        access.deleteAllTransactions(user.getUserid());
    }

    /**
     * Find index of transaction with given timestamp
     */
    @Test
    public void testGetTimestampIndex1()
    {
        // temp index variable
        int index = 0;

        System.out.println("\nStarting testListGetTimeIndex1");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(3), "index 0")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(523), "index 1")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(83), "index 2")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(74), "index 3")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(432), "index 4")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(978), "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 4);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, 1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, 0);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, 2);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 3);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 5);

        System.out.println("Finished testListGetTimeIndex1");
        access.deleteAllTransactions(user.getUserid());
    }

    /**
     * Find index of transaction with nonexistent timestamp
     */
    @Test
    public void testGetTimestampIndex2()
    {
        // temp index variable
        int index = 0;

        System.out.println("\nStarting testListGetTimeIndex2");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(3), "index 0")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(523), "index 1")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(83), "index 2")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(74), "index 3")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(432), "index 4")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(978), "index 5")));

        // check for nonexistent timestamps
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1, 1, 1, 1, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2038, 3, 12, 15, 9));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2005, 8, 22, 22, 22));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 19, 41));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2084, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2091, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(3000, 4, 13, 17, 1));
        assertEquals(index, -1);

        // add transactions and retest previously nonexistent timestamps
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2084, 4, 13, 17, 1), BigDecimal.valueOf(3), "index 8")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2091, 4, 13, 17, 1), BigDecimal.valueOf(1), "index 9")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(3000, 4, 13, 17, 1), BigDecimal.valueOf(2), "index 10")));
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2084, 4, 13, 17, 1));
        assertEquals(index, 6);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2091, 4, 13, 17, 1));
        assertEquals(index, 7);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(3000, 4, 13, 17, 1));
        assertEquals(index, 8);

        System.out.println("Finished testListGetTimeIndex2");
        access.deleteAllTransactions(user.getUserid());
    }

    /**
     * Find index of transaction before and after it was removed.
     */
    @Test
    public void testGetTimestampIndex3()
    {
        // temp index variable
        int index = 0;

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;

        System.out.println("\nStarting testListGetTimeIndex3");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user.getUserid(), A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(3), "index 0")));
        assertNotNull(access.insertTransaction(user.getUserid(), B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(523), "index 1")));
        assertNotNull(access.insertTransaction(user.getUserid(), C = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(83), "index 2")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(74), "index 3")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(432), "index 4")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(978), "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 4);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, 1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, 0);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, 2);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 3);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 5);

        // remove some transactions from the list
        access.deleteTransaction(user.getUserid(), A);
        access.deleteTransaction(user.getUserid(), B);
        access.deleteTransaction(user.getUserid(), C);

        // check if their timestamps no longer exist
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 1));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 20));
        assertEquals(index, -1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(index, -1);

        // check if the transactions following A,B,C have shifted their indices
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 42));
        assertEquals(index, 0);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2005, 8, 22, 3, 22));
        assertEquals(index, 1);
        index = access.getTimestampIndex(user.getUserid(), LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(index, 2);

        System.out.println("Finished testListGetTimeIndex3");
        access.deleteAllTransactions(user.getUserid());
    }

    /**
     * Find the sum of transactions in certain periods.
     */
    @Test
    public void testGetSumInPeriod1()
    {
        // temp index variable
        BigDecimal sum = BigDecimal.ZERO;

        System.out.println("\nStarting testListGetTimeIndex3");

        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(1), "index 0")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(2), "index 1")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(3), "index 2")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(4), "index 3")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(5), "index 4")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(6), "index 5")));

        // get sum of all transactions
        sum = access.getSumInPeriod(user.getUserid(), LocalDateTime.of(1984, 1, 1, 0, 0),
                LocalDateTime.of(4984, 1, 1, 0, 0));
        assertEquals(sum.compareTo(BigDecimal.valueOf(21)), 0);
        // get sum of first 3 transactions
        sum = access.getSumInPeriod(user.getUserid(), LocalDateTime.of(1984, 3, 13, 17, 1),
                LocalDateTime.of(1984, 4, 13, 17, 41));
        assertEquals(sum.compareTo(BigDecimal.valueOf(6)), 0);
        // get sum of last 3 transactions
        sum = access.getSumInPeriod(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 42),
                LocalDateTime.of(2038, 3, 14, 15, 9));
        assertEquals(sum.compareTo(BigDecimal.valueOf(15)), 0);
        // get sum of middle 4 transactions
        sum = access.getSumInPeriod(user.getUserid(), LocalDateTime.of(1984, 4, 13, 17, 2),
                LocalDateTime.of(2038, 3, 14, 15, 8));
        assertEquals(sum.compareTo(BigDecimal.valueOf(14)), 0);
        // get sum before the first transaction begins
        sum = access.getSumInPeriod(user.getUserid(), LocalDateTime.of(1, 1, 1, 0, 0),
                LocalDateTime.of(1984, 1, 1, 0, 0));
        assertEquals(sum.compareTo(BigDecimal.ZERO), 0);
        // get sum after the first transaction ends
        sum = access.getSumInPeriod(user.getUserid(), LocalDateTime.of(3333, 1, 1, 0, 0),
                LocalDateTime.of(4444, 1, 1, 0, 0));
        assertEquals(sum.compareTo(BigDecimal.ZERO), 0);

        System.out.println("Finished testListGetTimeIndex3");
        access.deleteAllTransactions(user.getUserid());
    }

    /**
     * Get the index of a transaction after a certain date.
     */
    @Test
    public void testGetIndexAfterDate1()
    {

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;


        System.out.println("\nStarting testGetIndexAfterDate1");
        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user.getUserid(), A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(1), "index 0")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(2), "index 1")));
        assertNotNull(access.insertTransaction(user.getUserid(), B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(3), "index 2")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(4), "index 3")));
        assertNotNull(access.insertTransaction(user.getUserid(), C = new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(5), "index 4")));
        assertNotNull(access.insertTransaction(user.getUserid(), new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(6), "index 5")));

        // perform tests on index after date function
        // input date before all transactions
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1,1,1,0,0)), 0, 0);
        // one minute before the second transaction
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,19)), 1, 0);
        // the exact time of the second transaction (will return index of third transaction)
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,20)), 2, 0);
        // one minute before the fourth transaction
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,41)), 3, 0);
        // the exact time of the fourth transaction (will return index of fifth transaction)
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,42)), 4, 0);
        // input date after all transactions
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(4444,1,1,0,0)), 5, 0);

        // remove some transactions from the list
        access.deleteTransaction(user.getUserid(), A);
        access.deleteTransaction(user.getUserid(), B);
        access.deleteTransaction(user.getUserid(), C);

        // re test the same dates but with updated indices
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1,1,1,0,0)), 0, 0);
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,19)), 0, 0);
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,20)), 1, 0);
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,41)), 1, 0);
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(1984,4,13,17,42)), 2, 0);
        assertEquals(access.getIndexAfterDate(user.getUserid(), LocalDateTime.of(4444,1,1,0,0)), 2, 0);

        System.out.println("Finished testGetIndexAfterDate1");
        access.deleteAllTransactions(user.getUserid());
    }

    /**
     * Filter the transaction list by a given category.
     */
    @Test
    public void testGetTransactionsByCategory1() {

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;
        Transaction D = null;
        Transaction E = null;
        Transaction F = null;

        List<Transaction> filteredList;

        System.out.println("\nStarting testGetTransactionsByCategory1");
        // add transactions in chronological order
        assertNotNull(access.insertTransaction(user.getUserid(), A = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 1), BigDecimal.valueOf(1), "Rasit")));
        assertNotNull(access.insertTransaction(user.getUserid(), B = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 20), BigDecimal.valueOf(2), "Rob")));
        assertNotNull(access.insertTransaction(user.getUserid(), C = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 41), BigDecimal.valueOf(3), "Rasit")));
        assertNotNull(access.insertTransaction(user.getUserid(), D = new Transaction(LocalDateTime.of(1984, 4, 13, 17, 42), BigDecimal.valueOf(4), "Rasit")));
        assertNotNull(access.insertTransaction(user.getUserid(), E = new Transaction(LocalDateTime.of(2005, 8, 22, 3, 22), BigDecimal.valueOf(5), "Rob")));
        assertNotNull(access.insertTransaction(user.getUserid(), F = new Transaction(LocalDateTime.of(2038, 3, 14, 15, 9), BigDecimal.valueOf(6), "Rob")));

        // check if the correct transactions went into the Rasit list
        filteredList = access.getTransactionsByCategory(user.getUserid(), "Rasit");
        assertEquals(filteredList.get(0), A);
        assertEquals(filteredList.get(1), C);
        assertEquals(filteredList.get(2), D);

        // check if the correct transactions went into the Rob list
        filteredList = access.getTransactionsByCategory(user.getUserid(), "Rob");
        assertEquals(filteredList.get(0), B);
        assertEquals(filteredList.get(1), E);
        assertEquals(filteredList.get(2), F);

        System.out.println("Finished testGetTransactionsByCategory1");
        access.deleteAllTransactions(user.getUserid());
    }
}
