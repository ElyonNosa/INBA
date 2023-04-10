package comp3350.inba.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.inba.R;
import comp3350.inba.objects.User;

/**
 * ThresholdActivity.java
 * The page where the user adjusts their spending threshold.
 * This class is coupled with activity_threshold.xml
 */
public class ThresholdActivity extends Activity {
    // instance of user
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold);
        user = new User(getApplicationContext());
    }

    public void onClick(View view) {
        // text inputs for weekend and weekday
        EditText wkend = findViewById(R.id.WeekEndThreshold);
        EditText wkday = findViewById(R.id.WeekDayThreshold);

        try {
            // save the thresholds in user profile
            user.setWkendThresh(Double.parseDouble(wkend.getText().toString()));
            user.setWkdayThresh(Double.parseDouble(wkday.getText().toString()));

            System.out.println(user.getWkdayThresh());
        }catch (final Exception e) {
            Messages.fatalError(this, "There is an empty entry");
        }

        // clear the text boxes
        wkend.setText("");
        wkday.setText("");

        finish();
    }

    public void btnExitPressed(View view) {
        finish();
    }
}