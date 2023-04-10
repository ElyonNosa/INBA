package comp3350.inba.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
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
public class SettingsActivity extends AppCompatActivity {

    ListView listView;
    SwitchCompat switchCompat;
    SharedPreferences sharePrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init the xml and navigation bar
        setContentView(R.layout.activity_settings);

        switchCompat = findViewById(R.id.theme_switch);
        sharePrefs = getSharedPreferences("night", 0);

        boolean booleanValue = sharePrefs.getBoolean("night_mode",true);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharePrefs.edit();
                    editor.putBoolean("night_mode",true);
                    editor.commit();

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    SharedPreferences.Editor editor = sharePrefs.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();
                }
            }
        });


        navigationBarInit();

        listView = findViewById(R.id.listview);
        // create an array list to hold the different settings
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Set Threshold Limit");
        arrayList.add("Compile Report");
        arrayList.add("Logout");

        /*
          Adapter function for the list of settings
         */
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Set the font size to 20sp
                textView.setTextColor(getColor(R.color.colorAccent));
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
                    case "Logout":
                        // TODO: log out the user
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

    public void openReportActivity() {
        Intent intent = new Intent(this, ReportActivity.class);
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
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class));
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.buttonSettings:
                        return true;
                    case R.id.buttonProfile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}