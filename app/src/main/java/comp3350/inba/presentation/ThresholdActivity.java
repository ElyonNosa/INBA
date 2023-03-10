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

public class ThresholdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold);
    }

    public void onClick(View view) {
        EditText wkend = findViewById(R.id.WeekEndThreshold);
        EditText wkday = findViewById(R.id.WeekDayThreshold);

        try {
            User.currUser.setWkendThresh(Double.parseDouble(wkend.getText().toString()));
            User.currUser.setWkdayThresh(Double.parseDouble(wkday.getText().toString()));

            System.out.println(User.currUser.getWkdayThresh());
        }catch (final Exception e) {
            Messages.fatalError(this, "There is an empty entry");
        }

        wkend.setText("");
        wkday.setText("");

        finish();
    }

    public void btnExitPressed(View view) {
        finish();
    }
}