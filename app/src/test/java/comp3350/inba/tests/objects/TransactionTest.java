package comp3350.inba.tests.objects;

import org.junit.Test;

import comp3350.inba.objects.Transaction;

import static org.junit.Assert.*;

public class TransactionTest
{
    @Test
    public void testTransaction1()
    {
        Transaction transaction;

        System.out.println("\nStarting testCourse");

        transaction = new Transaction(System.currentTimeMillis() / 1000, 12.34, "Education");
        assertNotNull(transaction);
        assertEquals("Education", transaction.getCategory());
        assertEquals(12.34, transaction.getPrice(), 0.0);

        System.out.println("Finished testTransaction");
    }

}