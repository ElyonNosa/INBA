package comp3350.inba.presentation;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.Category;
import comp3350.inba.objects.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static comp3350.inba.objects.User.currUser;


public class ProfileActivity extends AppCompatActivity {

    // the transactions database
    private AccessTransactions accessTransactions;
    // the adapter to display transactions in a list view
    private ArrayAdapter<Transaction> transactionArrayAdapter;
    // the local list of transactions
    private List<Transaction> transactionList;

    /**
     * Constructor
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get all of the text in from the layout
        TextView monthlySpendingLimit = findViewById(R.id.v_mn_spending_limit);
        TextView weeklySpendingLimit = findViewById(R.id.v_wk_spending_limit);
        TextView userName = findViewById(R.id.profile_name);
        TextView email = findViewById(R.id.email_address);

        // Set the profile tab to take the currUser's data
        monthlySpendingLimit.setText("$"+String.format("%.2f",currUser.getWkendThresh()));
        weeklySpendingLimit.setText("$"+String.format("%.2f",currUser.getWkendThresh()));
        userName.setText(currUser.getUserName());
        email.setText("Email: "+currUser.getUserName()+"@gmail.com");

        accessTransactions = new AccessTransactions();
        try {
            // display transactions in list
            transactionList = accessTransactions.getTransactions(User.currUser);
            transactionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
            final ListView listView = findViewById(R.id.transaction_list);
            // adapt the transactions list to the listview
            listView.setAdapter(transactionArrayAdapter);
        }
        catch (final Exception e) {
            Messages.fatalError(this, e.getMessage());
        }

        onResume();
        navigationBarInit();

    }//OnCreate

    // Pie chart setup
    private  void showPieChart(PieChart pieChart){

        //======================================================================================================================================
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //PieChart Data
        Map<String, Integer> typeAmountMap = initPieChartData();

        //PieChart Colours
        ArrayList<Integer> colors = (ArrayList<Integer>) initChartColours();

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public Map<String, Integer> initPieChartData()
    {

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Jan",10);
        typeAmountMap.put("Feb",20);
        typeAmountMap.put("Mar",30);
        typeAmountMap.put("Apr",40);
        typeAmountMap.put("May",50);
        typeAmountMap.put("June",20);
        typeAmountMap.put("July",50);
        typeAmountMap.put("Aug",40);
        typeAmountMap.put("Sept",90);
        typeAmountMap.put("Oct",30);
        typeAmountMap.put("Nov",70);
        typeAmountMap.put("Dec",60);

        return typeAmountMap;
    }

    //Used to allocate different colours to pirChart
    public List<Integer> initChartColours()
    {
        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        return  colors;
    }


    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set profile selected
        bottomNavigationView.setSelectedItemId(R.id.buttonProfile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class)); // Replace ViewActivity with the class used to view the graphs
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(), TransactionsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.buttonSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.buttonProfile:
                        return true;
                }
                return false;
            }
        });
    }


    /**
     * Destructor
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * This function runs when the user returns to this page.
     */
    @Override
    protected void onResume() {
        super.onResume();
        transactionList = accessTransactions.getTransactions(User.currUser);
        // update the transaction list
        transactionArrayAdapter.notifyDataSetChanged();
        PieChart pieChart = findViewById(R.id.pie_chart);
        showPieChart(pieChart);
    }
}
