package comp3350.inba.tests.objects;

import org.junit.Test;

import comp3350.inba.objects.Transaction;

import static org.junit.Assert.*;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class TransactionTest
{
    @Test
    public void testTransaction()
    {
        Transaction transaction;

        System.out.println("\nStarting testTransaction");

        transaction = new Transaction(LocalDateTime.now(), BigDecimal.valueOf(12.34), "Education");
        // non null test
        assertNotNull(transaction);
        // category test
        assertEquals("Education", transaction.getCategoryName());
        // price test
        assertEquals(0, transaction.getPrice().compareTo(BigDecimal.valueOf(12.34)));

        System.out.println("Finished testTransaction");
    }

    @Test
    public void testTransactionEqual(){
        System.out.println("\nStarting testTransactionEqual");
        Transaction transaction1 = new Transaction(LocalDateTime.now(), BigDecimal.valueOf(0), "Education");
        Transaction transaction2 = new Transaction(transaction1.getTime(), BigDecimal.valueOf(5.99), "Medical");
        assertEquals(transaction1, transaction2);

        System.out.println("\nFinished testTransactionEqual");
    }

    @Test
    public void testTransactionToString(){
        System.out.println("\nStarting testTransactionToString");

        Transaction transaction1 = new Transaction(LocalDateTime.now(), BigDecimal.valueOf(0), "Education");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals(transaction1.getTime().toString() + ", Education, $0.00", transaction1.toString());

        System.out.println("\nFinished testTransactionToString");
    }

}