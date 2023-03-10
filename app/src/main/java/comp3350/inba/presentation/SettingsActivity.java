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

import comp3350.inba.R;

public class SettingsActivity extends Activity {

    ListView listView;
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

        navigationBarInit();

        listView = (ListView)findViewById(R.id.listview);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Set Threshold Limit");
        arrayList.add("Update Account details");
        arrayList.add("Change Theme");
        arrayList.add("Print Monthly Report");
        arrayList.add("Submit Feedback");
        arrayList.add("Delete Account");

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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("Set Threshold Limit")) {
                    // Open the button
                    openThresholdActivity();
                }
                // Perform other actions based on the clicked item
                Toast.makeText(getApplicationContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void openThresholdActivity(){
        Intent intent = new Intent(this, ThresholdActivity.class);
        startActivity(intent);
    }

    public void openThresholdActivity(){
        Intent intent = new Intent(this, ThresholdActivity.class);
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