package comp3350.inba.presentation;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp3350.inba.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set profile selected
        bottomNavigationView.setSelectedItemId(R.id.buttonProfile);

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
                        startActivity(new Intent(getApplicationContext(), viewTransaction.class)); // Replace ViewActivity with the class used to view the graphs
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    /* case R.id.buttonSettings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;*/
                    case R.id.buttonProfile:
                        return true;
                }
                return false;
            }
        });
    }


    protected void updateGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // add data points to the graph
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(transactionsToGraphView());
        graph.addSeries(series);// styling

        // apply a different color to each category
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) (data.getX()*(122)*Category.getCategorySet().size())%255, 80, 140);
            }
        });

        // series properties
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(0xFFA6ABBD);

        // graph label properties
        graph.getGridLabelRenderer().setGridColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setLabelsSpace(50);
        graph.getGridLabelRenderer().setTextSize(35);
        graph.getGridLabelRenderer().setPadding(50);


        // custom label formatter to show categories
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                final int TRUNCATE_LEN = 9;
                int index = 0;
                String output = null;
                if (isValueX) {
                    // convert the x value to an index number
                    index = (int) Double.parseDouble(super.formatLabel(value, isValueX));
                    // check if category string length is more than desired
                    if ((output = Category.getCategorySet().get(index)).length() > TRUNCATE_LEN) {
                        // truncate the string
                        output = Category.getCategorySet().get(index).substring(0,TRUNCATE_LEN);
                    }
                    // return category of a given index
                    return output;
                } else {
                    // show normal y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    /**
     * Convert the total spendings of transactions per category into an array of data points.
     * @return The data points of the total spendings.
     */
    private DataPoint[] transactionsToGraphView() {
        DataPoint[] output = new DataPoint[Category.getCategorySet().size()];
        // the running price totals per category
        double[] categoryTotals = new double[Category.getCategorySet().size()];
        int i = 0;
        int j = 0;
        boolean found = false;
        Transaction temp = null;

        // loop through all transactions
        for (i = 0; i < transactionList.size(); i++) {
            temp = transactionList.get(i);
            found = false;
            // loop through all predefined categories
            for (j = 0; j < Category.getCategorySet().size() && !found; j++) {
                // check if the transaction category matches with a predefined category
                if(Category.getCategorySet().get(j).equals(temp.getCategory())) {
                    // increase the total price of this category
                    categoryTotals[j] += temp.getPrice();
                    found = true;
                }
            }
        }

        // loop through all predefined categories
        for (i = 0; i < Category.getCategorySet().size(); i++) {
            // make a data point per category

            output[i] = new DataPoint(i, categoryTotals[i]);
        }

        return output;
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
        transactionList = accessTransactions.getTransactions();
        // update the transaction list
        transactionArrayAdapter.notifyDataSetChanged();
        updateGraph();
    }
}
