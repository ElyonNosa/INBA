package comp3350.inba;

// Espresso imports
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
import comp3350.inba.presentation.DashboardActivity;

@RunWith( AndroidJUnit4.class )
@LargeTest
public class ViewSpendingsTest
{
    /*
    ############ View spending System Test #############
    */

    @Rule
    public ActivityTestRule<DashboardActivity> activity_rule = new ActivityTestRule<>(DashboardActivity.class);

    @Test
    public void testViewSpending()
    {
        final int[] numItems = new int[1];

        // login
        onView( withId( R.id.username_edit_text )).perform( typeText("rossD" ));
        onView( withId( R.id.password_edit_text )).perform( typeText( "rassit" ));
        closeSoftKeyboard();
        onView( withId( R.id.login_button )).perform( click() );

        // verify graph is displayed
        onView( withId( R.id.graph )).check( matches( isDisplayed()));
    }
}
