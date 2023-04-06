package comp3350.inba;

// Espresso imports
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// J unit Imports
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.anything;

// Android imports
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

// Presentation layer
import comp3350.inba.presentation.DashboardActivity;
//import comp3350.inba.presentation.TransactionsActivity;

@RunWith( AndroidJUnit4.class )
@LargeTest
public class TransactionsTests
{
    /*
    ############ Add Transaction System Test ############
    */

    @Rule
    public ActivityTestRule<DashboardActivity> activity_rule = new ActivityTestRule<>(DashboardActivity.class);
    //public ActivityTestRule<TransactionsActivity> other_activity_rule = new ActivityTestRule<>(TransactionsActivity.class);

    @Test
    public void createTransaction()
    {
        // login
        onView( withId( R.id.username_edit_text )).perform(typeText("rossD"));
        onView( withId( R.id.password_edit_text )).perform( typeText( "rassit" ) );
        closeSoftKeyboard();
        onView( withId( R.id.login_button )).perform( click() );

        // add a transaction
        onView( withId( R.id.buttonAddTransaction )).perform( click() );
        onView( withId( R.id.editTransactionCategory )).perform(typeText("something fun"));
        onView( withId( R.id.editTransactionPrice )).perform(typeText("20.34"));
        onView( withId( R.id.buttonTransactionCreate )).perform( click() );

        // Verify it was added
        onData(anything()).inAdapterView(withId(R.id.listTransactions)).atPosition( 0 ).perform(click());
        onView(withId(R.id.editTransactionCategory)).check(matches(withText("something fun")));
    }
}