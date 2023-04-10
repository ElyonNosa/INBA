package comp3350.inba;

// Espresso imports
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// J unit Imports
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertTrue;

// Android imports
import android.view.View;
import android.widget.ListView;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

// Presentation layer
import comp3350.inba.presentation.LoginActivity;

@RunWith( AndroidJUnit4.class )
@LargeTest
public class MultiCategoryTransactionTest
{
    /*
    ############ Add multi category Transaction System Test ############
    */

    @Rule
    public ActivityTestRule<LoginActivity> activity_rule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testMultiCreateTransaction()
    {
        // login
        onView( withId( R.id.username_edit_text )).perform(typeText("rossD"));
        onView( withId( R.id.password_edit_text )).perform( typeText( "rassit" ) );
        closeSoftKeyboard();
        onView( withId( R.id.login_button )).perform( click() );

        String cat_1 = "Type A";
        String cat_2 = "Type B";

        String amount_1 = "20.02";
        String amount_2 = "30.03";

        // Test transaction of Category type A
        test_transaction( cat_1, amount_1 );

        // Test transaction of Category type B
        test_transaction( cat_2, amount_2 );
    }



    private void test_transaction( String cat, String amount )
    {
        final int[] numItems = new int[1];

        // add transaction
        onView( withId( R.id.buttonAddTransaction )).perform( click() );
        onView( withId( R.id.editTransactionCategory )).perform(typeText(cat));
        closeSoftKeyboard();
        onView( withId( R.id.editTransactionPrice )).perform(typeText(amount));
        closeSoftKeyboard();
        onView( withId( R.id.buttonTransactionCreate )).perform( click() );

        // count the number of items in the list
        onView(withId(R.id.listTransactions)).check(matches(new TypeSafeMatcher<View>() {
            /**
             * Generates a description of the object.  The description may be part of a
             * a description of a larger object of which this is just a component, so it
             * should be worded appropriately.
             *
             * @param description The description to be built or appended to.
             */
            @Override
            public void describeTo(Description description) {
                // do nothing
            }

            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                //here we assume the adapter has been fully loaded already
                numItems[0] = listView.getAdapter().getCount();

                return true;
            }
        }));

        // Verify it was added as the last item in the list
        onData(anything()).inAdapterView(withId(R.id.listTransactions)).atPosition(numItems[0] - 1).perform(click());
        onView(withId(R.id.editTransactionCategory)).check(matches(withText( cat )));
        onView(withId(R.id.editTransactionPrice)).check(matches(withText( amount )));
        // delete once done
        onView( withId( R.id.buttonTransactionDelete )).perform( click() );
    }
}
