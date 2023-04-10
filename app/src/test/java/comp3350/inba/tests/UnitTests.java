package comp3350.inba.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.inba.tests.objects.TransactionTest;
import comp3350.inba.tests.business.AccessTransactionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TransactionTest.class,
        AccessTransactionTest.class
})
public class UnitTests {
}
