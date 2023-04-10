package comp3350.inba.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.inba.tests.business.AccessTransactionsIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccessTransactionsIT.class
})

public class IntegrationTests {
}
