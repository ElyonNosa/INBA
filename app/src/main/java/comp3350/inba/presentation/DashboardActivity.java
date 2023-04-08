package comp3350.inba.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.DefaultLabelFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;
import comp3350.inba.application.Service;

/**
 * DashboardActivity.java
 * The home page that shows a graph and recent transactions.
 * This class is coupled with activity_dashboard.xml
 */
public class DashboardActivity extends Activity {
    // predefined category names to put on the graph and serve as suggestions for the user
    static final String[] PREDEFINED_CATEG_NAMES = {"Amenities", "Education", "Entertainment",
            "Food", "Hardware", "Hobby", "Medical", "Misc", "Transportation", "Utilities"};
    // the transactions database
    private AccessTransactions accessTransactions;
    // the adapter to display transactions in a list view
    private ArrayAdapter<Transaction> transactionArrayAdapter;
    // the local list of transactions
    private List<Transaction> transactionList;
    // instance of user
    private User user;

    /**
     * Constructor
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User(getApplicationContext());

        // login if not currently logged in
        if(!user.getLoginStatus()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_dashboard);
        copyDatabaseToDevice();
        // create instance of accessTransactions using the predefined list of category names
        accessTransactions = new AccessTransactions(Arrays.asList(PREDEFINED_CATEG_NAMES));
        try {
            // display transactions in list
            transactionList = accessTransactions.getTransactions(user.getUserID());
            transactionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
            final ListView listView = findViewById(R.id.transaction_list);
            // adapt the transactions list to the listview
            listView.setAdapter(transactionArrayAdapter);
        }
        catch (final Exception e) {
            Messages.fatalError(this, e.getMessage());
        }

        navigationBarInit();

    }//onCreate

    // will need to ask the DB if we are logged in
    private boolean isLoggedIn() {
        return user.getLoginStatus();
    }

    /**
     * Update the graph that displays transaction totals
     */
    protected void updateGraph() {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // add data points to the graph
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(transactionsToGraphView());
        graph.addSeries(series);// styling

        // apply a different color to each category
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) (data.getX()*(122)* accessTransactions.getCategNames().size())%255, 80, 140);
            }
        });

        // series properties
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(0xFFA6ABBD);

        // graph label properties
        graph.getGridLabelRenderer().setGridColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setNumHorizontalLabels(accessTransactions.getCategNames().size());
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graph.setTitle("All Time Transactions:");
        graph.setTitleColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setLabelsSpace(50);
        graph.getGridLabelRenderer().setTextSize(35);
        graph.getGridLabelRenderer().setPadding(50);

        // check if there are a sufficient number of categories
        if(accessTransactions.getCategNames().size() > 0) {
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
                        if ((output = accessTransactions.getCategNames().get(index)).length() > TRUNCATE_LEN) {
                            // truncate the string
                            output = accessTransactions.getCategNames().get(index).substring(0, TRUNCATE_LEN);
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
    }

    /**
     * Convert the total spendings of transactions per category into an array of data points.
     * @return The data points of the total spendings.
     */
    protected DataPoint[] transactionsToGraphView() {
        DataPoint[] output = new DataPoint[accessTransactions.getCategNames().size()];
        // the running price totals per category
        double[] categoryTotals = new double[accessTransactions.getCategNames().size()];
        int i = 0;
        int j = 0;
        boolean found = false;
        Transaction temp = null;

        // loop through all transactions
        for (i = 0; i < transactionList.size(); i++) {
            temp = transactionList.get(i);
            found = false;
            // loop through all predefined categories
            for (j = 0; j < accessTransactions.getCategNames().size() && !found; j++) {
                // check if the transaction category matches with a predefined category
                if (accessTransactions.getCategNames().get(j).equals(temp.getCategoryName())) {
                    // increase the total price of this category
                    categoryTotals[j] += temp.getPrice();
                    found = true;
                }
            }
        }

        // loop through all predefined categories
        for (i = 0; i < categoryTotals.length; i++) {
            // make a data point per category
            output[i] = new DataPoint(i, categoryTotals[i]);
        }

        return output;
    }

    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) // DashboardActivity
                {
                    case R.id.home:
                        // true if already on page.
                        return true;
                    case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class)); // Replace ViewActivity with the class used to view the graphs
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(),TransactionsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonSettings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonProfile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
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
        transactionList = accessTransactions.getTransactions(user.getUserid());
        // update the transaction list
        transactionArrayAdapter.notifyDataSetChanged();

        updateGraph();

        updateMonthlyTotal();
    }

    /**
     * Print total monthly spending on title text.
     */
    private void updateMonthlyTotal() {
        final int SECONDS_PER_MONTH = 2629744;
        TextView title = findViewById(R.id.textTitle);
        LocalDateTime now = LocalDateTime.now();
        // get sum of transactions between now and 1 month ago
        double total = accessTransactions.getSumInPeriod(user.getUserid(), now.minusSeconds(SECONDS_PER_MONTH), now);
        String text = "Monthly Total: $" + String.format(Locale.ENGLISH, "%.2f", total);
        title.setText(text);
    }


    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            Service.setDBPathName(dataDirectory.toString() + "/" + Service.getDBPathName());

        } catch (final IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}
