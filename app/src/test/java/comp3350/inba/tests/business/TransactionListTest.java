package comp3350.inba.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import comp3350.inba.objects.Transaction;
import comp3350.inba.persistence.stubs.TransactionPersistenceStub;

public class TransactionListTest {

    /**
     * Check if list rejects non-chronological transactions
     */
    @Test
    public void testAddChronologically()
    {
        // the transactions database
        TransactionPersistenceStub persistence = new TransactionPersistenceStub(false);

        // add transactions chronologically
        assertNotNull(persistence.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(persistence.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(persistence.insertTransaction(new Transaction(1228, 83, "index 2")));

        // add transactions not chronologically
        assertNull(persistence.insertTransaction(new Transaction(1022, 3, "should be null")));
        assertNull(persistence.insertTransaction(new Transaction(1155, 523, "should be null")));
        assertNull(persistence.insertTransaction(new Transaction(1227, 83, "should be null")));

        // add transactions chronologically
        assertNotNull(persistence.insertTransaction(new Transaction(2222, 3, "index 3")));
        assertNotNull(persistence.insertTransaction(new Transaction(3155, 523, "index 4")));
        assertNotNull(persistence.insertTransaction(new Transaction(4321, 83, "index 5")));

        System.out.println("\nStarting testAddChronologically");



        System.out.println("Finished testAddChronologically");
    }

    /**
     * Find index of transaction with given timestamp
     */
    @Test
    public void testGetTimestampIndex1()
    {
        // the transactions database
        TransactionPersistenceStub persistence = new TransactionPersistenceStub(false);
        // temp index variable
        int index = 0;

        System.out.println("\nStarting testListGetTimeIndex1");

        // add transactions in chronological order
        assertNotNull(persistence.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(persistence.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(persistence.insertTransaction(new Transaction(1228, 83, "index 2")));
        assertNotNull(persistence.insertTransaction(new Transaction(1384, 74, "index 3")));
        assertNotNull(persistence.insertTransaction(new Transaction(1463, 432, "index 4")));
        assertNotNull(persistence.insertTransaction(new Transaction(2434, 978, "index 5")));

        // check for timestamps within the list
        index = persistence.getTimestampIndex(1463);
        assertEquals(index, 4);
        index = persistence.getTimestampIndex(1175);
        assertEquals(index, 1);
        index = persistence.getTimestampIndex(1043);
        assertEquals(index, 0);
        index = persistence.getTimestampIndex(1228);
        assertEquals(index, 2);
        index = persistence.getTimestampIndex(1384);
        assertEquals(index, 3);
        index = persistence.getTimestampIndex(2434);
        assertEquals(index, 5);

        System.out.println("Finished testListGetTimeIndex1");
    }

    /**
     * Find index of transaction with nonexistent timestamp
     */
    @Test
    public void testGetTimestampIndex2()
    {
        // the transactions database
        TransactionPersistenceStub persistence = new TransactionPersistenceStub(false);
        // temp index variable
        int index = 0;

        System.out.println("\nStarting testListGetTimeIndex2");

        // add transactions in chronological order
        assertNotNull(persistence.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(persistence.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(persistence.insertTransaction(new Transaction(1228, 83, "index 2")));
        assertNotNull(persistence.insertTransaction(new Transaction(1384, 74, "index 3")));
        assertNotNull(persistence.insertTransaction(new Transaction(1463, 432, "index 4")));
        assertNotNull(persistence.insertTransaction(new Transaction(2434, 978, "index 5")));
        assertNotNull(persistence.insertTransaction(new Transaction(3783, 132, "index 6")));
        assertNotNull(persistence.insertTransaction(new Transaction(4127, 278, "index 7")));

        // check for nonexistent timestamps
        index = persistence.getTimestampIndex(0);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(-1);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(123);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(1042);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(7777);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(8888);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(9999);
        assertEquals(index, -1);

        // add transactions and retest previously nonexistent timestamps
        assertNotNull(persistence.insertTransaction(new Transaction(7777, 3, "index 8")));
        assertNotNull(persistence.insertTransaction(new Transaction(8888, 1, "index 9")));
        assertNotNull(persistence.insertTransaction(new Transaction(9999, 2, "index 10")));
        index = persistence.getTimestampIndex(7777);
        assertEquals(index, 8);
        index = persistence.getTimestampIndex(8888);
        assertEquals(index, 9);
        index = persistence.getTimestampIndex(9999);
        assertEquals(index, 10);

        System.out.println("Finished testListGetTimeIndex2");
    }

    /**
     * Find index of transaction before and after it was removed.
     */
    @Test
    public void testGetTimestampIndex3()
    {
        // the transactions database
        TransactionPersistenceStub persistence = new TransactionPersistenceStub(false);
        // temp index variable
        int index = 0;

        Transaction A = null;
        Transaction B = null;
        Transaction C = null;

        System.out.println("\nStarting testListGetTimeIndex3");

        // add transactions in chronological order
        assertNotNull(persistence.insertTransaction(new Transaction(1043, 3, "index 0")));
        assertNotNull(persistence.insertTransaction(new Transaction(1175, 523, "index 1")));
        assertNotNull(persistence.insertTransaction(new Transaction(1228, 83, "index 2")));
        assertNotNull(persistence.insertTransaction(A = new Transaction(1384, 74, "index 3")));
        assertNotNull(persistence.insertTransaction(B = new Transaction(1463, 432, "index 4")));
        assertNotNull(persistence.insertTransaction(C = new Transaction(2434, 978, "index 5")));
        assertNotNull(persistence.insertTransaction(new Transaction(3783, 132, "index 6")));
        assertNotNull(persistence.insertTransaction(new Transaction(4127, 278, "index 7")));
        assertNotNull(persistence.insertTransaction(new Transaction(9999, 69, "index 8")));

        // check for timestamps within the list
        index = persistence.getTimestampIndex(1463);
        assertEquals(index, 4);
        index = persistence.getTimestampIndex(1175);
        assertEquals(index, 1);
        index = persistence.getTimestampIndex(1043);
        assertEquals(index, 0);
        index = persistence.getTimestampIndex(1228);
        assertEquals(index, 2);
        index = persistence.getTimestampIndex(1384);
        assertEquals(index, 3);
        index = persistence.getTimestampIndex(2434);
        assertEquals(index, 5);

        // remove some transactions from the list
        persistence.deleteTransaction(A);
        persistence.deleteTransaction(B);
        persistence.deleteTransaction(C);

        // check if their timestamps no longer exist
        index = persistence.getTimestampIndex(1384);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(1463);
        assertEquals(index, -1);
        index = persistence.getTimestampIndex(2434);
        assertEquals(index, -1);

        // check if the transactions following A,B,C have shifted their indices
        index = persistence.getTimestampIndex(3783);
        assertEquals(index, 3);
        index = persistence.getTimestampIndex(4127);
        assertEquals(index, 4);
        index = persistence.getTimestampIndex(9999);
        assertEquals(index, 5);

        System.out.println("Finished testListGetTimeIndex3");
    }

    /**
     * Find the sum of transactions in certain periods.
     */
    @Test
    public void testGetSumInPeriod1()
    {
        // the transactions database
        TransactionPersistenceStub persistence = new TransactionPersistenceStub(false);
        // temp index variable
        double sum = 0;

        System.out.println("\nStarting testListGetTimeIndex3");

        // add transactions in chronological order
        assertNotNull(persistence.insertTransaction(new Transaction(1043, 1, "index 0")));
        assertNotNull(persistence.insertTransaction(new Transaction(1175, 2, "index 1")));
        assertNotNull(persistence.insertTransaction(new Transaction(1228, 3, "index 2")));
        assertNotNull(persistence.insertTransaction(new Transaction(3783, 4, "index 3")));
        assertNotNull(persistence.insertTransaction(new Transaction(4127, 5, "index 4")));
        assertNotNull(persistence.insertTransaction(new Transaction(9999, 6, "index 5")));

        // get sum of all transactions
        sum = persistence.getSumInPeriod(0, 10000);
        assertEquals(sum, 21, 0);
        // get sum of first 3 transactions
        sum = persistence.getSumInPeriod(0, 3782);
        assertEquals(sum, 6, 0);
        // get sum of last 3 transactions
        sum = persistence.getSumInPeriod(1229, 10000);
        assertEquals(sum, 15, 0);
        // get sum of middle 4 transactions
        sum = persistence.getSumInPeriod(1044, 9998);
        assertEquals(sum, 14, 0);
        // get sum before the first transaction begins
        sum = persistence.getSumInPeriod(0, 1000);
        assertEquals(sum, 0, 0);
        // get sum after the first transaction ends
        sum = persistence.getSumInPeriod(10000, 9999999);
        assertEquals(sum, 0, 0);

        System.out.println("Finished testListGetTimeIndex3");
    }
}
