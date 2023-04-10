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
import comp3350.inba.presentation.LoginActivity;

@RunWith( AndroidJUnit4.class )
@LargeTest
public class ChangeSettingTest
{
    /*
    ############ change settings System Test ############
    */

    @Rule
    public ActivityTestRule<LoginActivity> activity_rule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testSettings()
    {
        // login
        onView( withId( R.id.username_edit_text )).perform(typeText("rossD"));
        onView( withId( R.id.password_edit_text )).perform( typeText( "rassit" ) );
        closeSoftKeyboard();
        onView( withId( R.id.login_button )).perform( click() );

        // Go to settings
        onView( withId( R.id.buttonSettings )).perform( click() );

        // Verify setting options are displayed
        onView( withId( R.id.listview)).check(  matches( isDisplayed() ));

    }
}
