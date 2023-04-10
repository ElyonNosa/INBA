package comp3350.inba.presentation;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ViewTransationActivity.java
 * The page where we see graphs containing transaction data.
 * This class is coupled with activity_view_transaction.xml.
 */
public class ViewTransactionActivity extends Activity implements View.OnClickListener {
    // the sum of prices for each month
    private ArrayList<BigDecimal> monthlyPriceArray;
    final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
            "Aug", "Sept", "Oct", "Nov", "Dec"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        User user = new User(getApplicationContext());
        // instance of AccessTransactions
        AccessTransactions accessTransactions = new AccessTransactions();
        // fetch data
        monthlyPriceArray = new ArrayList<>(Arrays.asList(accessTransactions.getSumByMonth(
                user.getUserID(), LocalDateTime.now().getYear())));

        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        navigationBarInit();
        // click button 1 by default
        onClick(button1);
    }

    /**
     * Initialize the navbar for this page.
     */
    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set View Transaction selected
        bottomNavigationView.setSelectedItemId(R.id.buttonViewTransaction);

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
                        // true if already on page.
                        return true;
                    case R.id.buttonAddTransaction:
                        startActivity(new Intent(getApplicationContext(), TransactionsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.buttonSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
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

    /**
     * onClick(): what happens when you click on the graph buttons.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        //Local Variable

        //Initializing the graphs
        LineChart lineChart = findViewById(R.id.line_chart);
        BarChart barChart = findViewById(R.id.bar_chart);
        PieChart pieChart = findViewById(R.id.pie_chart);

        switch (view.getId()) {
            //Line Graph
            case R.id.button:

                // Hide the other two charts
                barChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
                // Show the line chart
                lineChart.setVisibility(View.VISIBLE);

                showLineChart(lineChart);
                break;

            //Bar Graph
            case R.id.button2:
                // Hide the other two charts
                lineChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);

                // Show the line chart
                barChart.setVisibility(View.VISIBLE);

                showBarChart(barChart);
                break;

            //Pie Chart
            case R.id.button3:
                // Hide the other two charts
                barChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);

                // Show the line chart
                pieChart.setVisibility(View.VISIBLE);

                showPieChart(pieChart);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    /**
     * Obtain data formatted for the graphs.
     *
     * @return
     */
    public List<Entry> getDataset() {
        List<Entry> entries = new ArrayList<>();
        int i = 0;
        for (i = 0; i < Month.values().length; ++i) {
            entries.add(new Entry(i, monthlyPriceArray.get(i).floatValue()));
        }

        return entries;
    }

    //Converts list<Entry> to list<BarEntry>
    private List<BarEntry> cast(List<Entry> entryVal) {
        // Create a new list of BarEntry objects
        List<BarEntry> barEntries = new ArrayList<>();
        // Iterate over the entries list, converting each Entry to a BarEntry
        for (Entry entry : entryVal) {
            BarEntry barEntry = new BarEntry(entry.getX(), entry.getY());
            barEntries.add(barEntry);
        }

        return barEntries;
    }

    // Used to add a label to the x axis of Bar and Line graph
    private ValueFormatter setLabel(String[] months) {
        return new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int)value;
                // Check if index is within bounds of months array
                if (index >= 0 && index < MONTHS.length) {
                    // Return the corresponding month from the array
                    return MONTHS[index];
                }
                return "";
            }
        };
    }

    /**
     * Display the line chart and its properties.
     *
     * @param lineChart Object used to modify the chart
     */
    private void showLineChart(LineChart lineChart) {
        List<Entry> entries = getDataset();
        LineDataSet dataSet = new LineDataSet(entries, "Cost");
        LineData lineData = new LineData(dataSet);
        // Set the value formatter for the x-axis labels
        ValueFormatter formatter = setLabel(MONTHS);
        XAxis xAxis = lineChart.getXAxis();

        // 10 labels actually prints 12 for some weird reason
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(formatter);

        dataSet.setColor(Color.parseColor("#80FF7F50"));
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);

        // Add data to the chart
        lineChart.setData(lineData);
        modifyLineChart(lineChart);
    }

    /**
     * Display the bar chart and its properties.
     *
     * @param barChart Object used to modify the chart
     */
    private void showBarChart(BarChart barChart) {
        List<Entry> entryVal = getDataset();
        List<BarEntry> entries = cast(entryVal);
        BarDataSet dataSet = new BarDataSet(entries, "Cost");
        BarData data = new BarData(dataSet);
        // Set the value formatter for the x-axis labels
        ValueFormatter formatter = setLabel(MONTHS);
        XAxis xAxis = barChart.getXAxis();

        xAxis.setLabelCount(MONTHS.length);
        xAxis.setValueFormatter(formatter);

        dataSet.setColor(Color.YELLOW);
        dataSet.setDrawValues(true);
        dataSet.setValueTextColor(Color.WHITE);

        barChart.setData(data);
        modifyBarChart(barChart);
    }

    /**
     * Align the bar chart
     *
     * @param barChart Object used to modify the chart
     */
    private void modifyBarChart(BarChart barChart) {
        barChart.invalidate();
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
    }

    /**
     * Align the line chart
     *
     * @param lineChart Object used to modify the chart
     */
    private void modifyLineChart(LineChart lineChart) {
        // Refresh the chart
        lineChart.invalidate();
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
    }

    /**
     * TO display the pieChart
     *
     * @param pieChart Object used to modify the pieChart
     */
    private void showPieChart(PieChart pieChart) {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";
        //PieChart Colours
        ArrayList<Integer> colors = (ArrayList<Integer>) initChartColours();
        int i = 0;
        // create entries using monthly sums
        for (i = 0; i < MONTHS.length; ++i) {
            if(monthlyPriceArray.get(i).compareTo(BigDecimal.ZERO) > 0)
            {
                pieEntries.add(new PieEntry(monthlyPriceArray.get(i).floatValue(), MONTHS[i]));
            }
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        //setting text size and color of the label
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setValueTextColor(Color.BLACK);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    //Initializing the colours for PieChart
    public List<Integer> initChartColours() {
        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        return colors;
    }
}

