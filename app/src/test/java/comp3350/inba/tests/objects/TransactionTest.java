package comp3350.inba.tests.objects;

import org.junit.Test;

import comp3350.inba.objects.Transaction;

import static org.junit.Assert.*;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class TransactionTest
{
    @Test
    public void testTransaction()
    {
        Transaction transaction;

        System.out.println("\nStarting testTransaction");

        transaction = new Transaction(LocalDateTime.now(), 12.34, "Education");
        // non null test
        assertNotNull(transaction);
        // category test
        assertEquals("Education", transaction.getCategory());
        // price test
        assertEquals(12.34, transaction.getPrice(), 0.0);

        System.out.println("Finished testTransaction");
    }

    @Test
    public void testTransactionEqual(){
        System.out.println("\nStarting testTransactionEqual");
        Transaction transaction1 = new Transaction(LocalDateTime.now(), 0.00, "Education");
        Transaction transaction2 = new Transaction(transaction1.getTime(), 5.99, "Medical");
        assertTrue(transaction1.equals(transaction2));

        System.out.println("\nFinished testTransactionEqual");
    }

    @Test
    public void testTransactionToString(){
        System.out.println("\nStarting testTransactionToString");

        Transaction transaction1 = new Transaction(LocalDateTime.now(), 0.00, "Education");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals(transaction1.getTime().toString() + ", Education, $0.00", transaction1.toString());

        System.out.println("\nFinished testTransactionToString");
    }

}