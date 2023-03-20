package comp3350.inba.presentation;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.inba.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button thresholdButton = findViewById(R.id.threshold);
        thresholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openThresholdActivity();
            }
        });

        Button reportButton = findViewById(R.id.Report);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReportActivity();
            }
        });

        navigationBarInit();
    }

    public void openThresholdActivity(){
        Intent intent = new Intent(this, ThresholdActivity.class);
        startActivity(intent);
    }

    public void openReportActivity()
    {
        Intent intent = new Intent( this, ReportActivity.class);
        startActivity(intent);
    }

    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.buttonSettings);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) // DashboardActivity
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class));
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonSettings:
                        return true;
                    case R.id.buttonProfile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}