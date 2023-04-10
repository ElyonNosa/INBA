package comp3350.inba.presentation;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import comp3350.inba.R;

/**
 * SettingsActivity.java
 * The page where the user may browse different settings.
 * This class is coupled with the activity_settings.xml.
 */
public class SettingsActivity extends Activity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init the xml and navigation bar
        setContentView(R.layout.activity_settings);
        navigationBarInit();

        listView = findViewById(R.id.listview);
        // create an array list to hold the different settings
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Set Threshold Limit");
        arrayList.add("Compile Report");
        arrayList.add("Budget Calculator");
        arrayList.add("Change Theme (Iteration 3)");
        arrayList.add("Update Account details (Iteration 3)");
        arrayList.add("Print Monthly Report (Iteration 3)");
        arrayList.add("Submit Feedback (Future Iteration)");
        arrayList.add("Delete Account (Future Iteration)");

        /*
          Adapter function for the list of settings
         */
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set the font size to 20sp
                textView.setTextColor(getColor(R.color.colorWhite));
                return textView;
            }
        };

        listView.setAdapter(arrayAdapter);

        /*
          Do stuff based on what setting was pressed.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                switch (selectedItem) {
                    case "Set Threshold Limit":
                        openThresholdActivity();
                        break;
                    case "Compile Report":
                        openReportActivity();
                        break;
                    case "Budget Calculator":
                        openBudgetCalculatorActivity();;
                        break;
                }
                // Perform other actions based on the clicked item
                Toast.makeText(getApplicationContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        navigationBarInit();
    }

    /**
     * Function for starting the transaction threshold activity.
     */
    protected void openThresholdActivity() {
        Intent intent = new Intent(this, ThresholdActivity.class);
        startActivity(intent);
    }

    /**
     * Function for starting the transaction report activity.
     */
    public void openReportActivity() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    /**
     * Function for starting the transaction report activity.
     */
    public void openBudgetCalculatorActivity() {
        Intent intent = new Intent(this, BudgetCalculatorActivity.class);
        startActivity(intent);
    }

    /**
     * Initialize the navigation bar for the settings page.
     */
    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.buttonSettings);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) // DashboardActivity
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class));
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(), TransactionsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.buttonSettings:
                        return true;
                    case R.id.buttonProfile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}