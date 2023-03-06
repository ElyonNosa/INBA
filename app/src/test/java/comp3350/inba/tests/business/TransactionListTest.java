package comp3350.inba.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        assertNotNull(access.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(1228, 83, "index 2")));

        // add transactions not chronologically
        assertNull(access.insertTransaction(new Transaction(1022, 3, "should be null")));
        assertNull(access.insertTransaction(new Transaction(1155, 523, "should be null")));
        assertNull(access.insertTransaction(new Transaction(1227, 83, "should be null")));

        // add transactions chronologically
        assertNotNull(access.insertTransaction(new Transaction(2222, 3, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(3155, 523, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(4321, 83, "index 5")));

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
        assertNotNull(access.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(1228, 83, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(1384, 74, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(1463, 432, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(2434, 978, "index 5")));

        // check for timestamps within the list
        index = access.getTimestampIndex(1463);
        assertEquals(index, 4);
        index = access.getTimestampIndex(1175);
        assertEquals(index, 1);
        index = access.getTimestampIndex(1043);
        assertEquals(index, 0);
        index = access.getTimestampIndex(1228);
        assertEquals(index, 2);
        index = access.getTimestampIndex(1384);
        assertEquals(index, 3);
        index = access.getTimestampIndex(2434);
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
        assertNotNull(access.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(1228, 83, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(1384, 74, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(1463, 432, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(2434, 978, "index 5")));
        assertNotNull(access.insertTransaction(new Transaction(3783, 132, "index 6")));
        assertNotNull(access.insertTransaction(new Transaction(4127, 278, "index 7")));

        // check for nonexistent timestamps
        index = access.getTimestampIndex(0);
        assertEquals(index, -1);
        index = access.getTimestampIndex(-1);
        assertEquals(index, -1);
        index = access.getTimestampIndex(123);
        assertEquals(index, -1);
        index = access.getTimestampIndex(1042);
        assertEquals(index, -1);
        index = access.getTimestampIndex(7777);
        assertEquals(index, -1);
        index = access.getTimestampIndex(8888);
        assertEquals(index, -1);
        index = access.getTimestampIndex(9999);
        assertEquals(index, -1);

        // add transactions and retest previously nonexistent timestamps
        assertNotNull(access.insertTransaction(new Transaction(7777, 3, "index 8")));
        assertNotNull(access.insertTransaction(new Transaction(8888, 1, "index 9")));
        assertNotNull(access.insertTransaction(new Transaction(9999, 2, "index 10")));
        index = access.getTimestampIndex(7777);
        assertEquals(index, 8);
        index = access.getTimestampIndex(8888);
        assertEquals(index, 9);
        index = access.getTimestampIndex(9999);
        assertEquals(index, 10);

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
        assertNotNull(access.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(1228, 83, "index 2")));
        assertNotNull(access.insertTransaction(A = new Transaction(1384, 74, "index 3")));
        assertNotNull(access.insertTransaction(B = new Transaction(1463, 432, "index 4")));
        assertNotNull(access.insertTransaction(C = new Transaction(2434, 978, "index 5")));
        assertNotNull(access.insertTransaction(new Transaction(3783, 132, "index 6")));
        assertNotNull(access.insertTransaction(new Transaction(4127, 278, "index 7")));
        assertNotNull(access.insertTransaction(new Transaction(9999, 69, "index 8")));

        // check for timestamps within the list
        index = access.getTimestampIndex(1463);
        assertEquals(index, 4);
        index = access.getTimestampIndex(1175);
        assertEquals(index, 1);
        index = access.getTimestampIndex(1043);
        assertEquals(index, 0);
        index = access.getTimestampIndex(1228);
        assertEquals(index, 2);
        index = access.getTimestampIndex(1384);
        assertEquals(index, 3);
        index = access.getTimestampIndex(2434);
        assertEquals(index, 5);

        // remove some transactions from the list
        access.deleteTransaction(A);
        access.deleteTransaction(B);
        access.deleteTransaction(C);

        // check if their timestamps no longer exist
        index = access.getTimestampIndex(1384);
        assertEquals(index, -1);
        index = access.getTimestampIndex(1463);
        assertEquals(index, -1);
        index = access.getTimestampIndex(2434);
        assertEquals(index, -1);

        // check if the transactions following A,B,C have shifted their indices
        index = access.getTimestampIndex(3783);
        assertEquals(index, 3);
        index = access.getTimestampIndex(4127);
        assertEquals(index, 4);
        index = access.getTimestampIndex(9999);
        assertEquals(index, 5);

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
        assertNotNull(access.insertTransaction(new Transaction(1043, 1, "index 0")));
        assertNotNull(access.insertTransaction(new Transaction(1175, 2, "index 1")));
        assertNotNull(access.insertTransaction(new Transaction(1228, 3, "index 2")));
        assertNotNull(access.insertTransaction(new Transaction(3783, 4, "index 3")));
        assertNotNull(access.insertTransaction(new Transaction(4127, 5, "index 4")));
        assertNotNull(access.insertTransaction(new Transaction(9999, 6, "index 5")));

        // get sum of all transactions
        sum = access.getSumInPeriod(0, 10000);
        assertEquals(sum, 21, 0);
        // get sum of first 3 transactions
        sum = access.getSumInPeriod(0, 3782);
        assertEquals(sum, 6, 0);
        // get sum of last 3 transactions
        sum = access.getSumInPeriod(1229, 10000);
        assertEquals(sum, 15, 0);
        // get sum of middle 4 transactions
        sum = access.getSumInPeriod(1044, 9998);
        assertEquals(sum, 14, 0);
        // get sum before the first transaction begins
        sum = access.getSumInPeriod(0, 1000);
        assertEquals(sum, 0, 0);
        // get sum after the first transaction ends
        sum = access.getSumInPeriod(10000, 9999999);
        assertEquals(sum, 0, 0);

        System.out.println("Finished testListGetTimeIndex3");
    }
}
