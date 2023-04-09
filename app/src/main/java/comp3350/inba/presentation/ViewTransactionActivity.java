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


import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ViewTransactionActivity extends Activity implements View.OnClickListener {

    private ArrayList<LocalDateTime> localDateTime = new ArrayList<>();
    private ArrayList<Double> price = new ArrayList<>();

    private Map<String, Double> monthlyPriceMap = new HashMap<>();

    final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
            "Aug", "Sept", "Oct", "Nov", "Dec"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        navigationBarInit();
        onClick(button1);


    }

    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set View Transaction selected
        bottomNavigationView.setSelectedItemId(R.id.buttonViewTransaction);

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
                        // true if already on page.
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

    @Override
    public void onClick(View view) {
        //Local Variable

        //Initializing the graphs
        LineChart lineChart = findViewById(R.id.line_chart);
        BarChart barChart = findViewById(R.id.bar_chart);
        PieChart pieChart = findViewById(R.id.pie_chart);

        //Fetching the data
        List<Transaction> transactionList = getTransaction();
        addList(transactionList);
        sumPriceByMonth(transactionList);
        sortedMap();

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
     * Gets the transaction from the accessTransaction class
     * @return the transaction
     */
    public List<Transaction> getTransaction()
    {
        AccessTransactions accessTransactions = new AccessTransactions();
        List<Transaction> transactionList = accessTransactions.getTransactions(User.currUser);
        return transactionList;
    }

    /**
     * Adds the content of the transaction to a list
     */
    public void addList(List<Transaction> transactionList)
    {

        for (int i =0; i < transactionList.size(); i++) {
            Transaction transaction = transactionList.get(i);
            localDateTime.add(transaction.getTime());
            price.add(transaction.getPrice());
        }
    }

    /**
     * Sums the transaction based on month
     * @param transactionList The list which requires to be summed.
     */
    public void sumPriceByMonth(List<Transaction> transactionList) {
        monthlyPriceMap.clear();
        DecimalFormat df = new DecimalFormat("#.00");
        for (Transaction transaction : transactionList) {
            String monthYear = transaction.getTime().format(DateTimeFormatter.ofPattern("MM"));
            double price = transaction.getPrice();
            if (monthlyPriceMap.containsKey(monthYear)) {
                double updatedPrice = Double.parseDouble(df.format(monthlyPriceMap.get(monthYear) + price));
                monthlyPriceMap.put(monthYear, updatedPrice);
            } else {
                double formattedPrice = Double.parseDouble(df.format(price));
                monthlyPriceMap.put(monthYear, formattedPrice);
            }
        }

    }

    /**
     * Sort the Map list in ascending.
     * @return The sorted list
     */
    public Map<String, Double> sortedMap()
    {
        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(monthlyPriceMap.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        monthlyPriceMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : sortedList) {
            String formattedPrice = String.format("%.2f", entry.getValue());
            monthlyPriceMap.put(entry.getKey(), Double.parseDouble(formattedPrice));
        }


        return monthlyPriceMap;
    }

    //get the Dataset for Line and Bar graph
    public List<Entry> getDataset() {
        List<Entry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : monthlyPriceMap.entrySet()) {
            String month = entry.getKey();
            Double price = entry.getValue();
            entries.add(new Entry(Float.parseFloat(month), price.floatValue()));
        }

        return entries;
    }

    //Converts list<Entry> to list<BarEntry>
    private  List<BarEntry> cast(List<Entry> entryVal)
    {
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
    private ValueFormatter setLabel(String[] months)
    {
        return new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Convert float value to int index of months array
                int index = (int) value;
                // Check if index is within bounds of months array
                if (index >= 0 && index < months.length) {
                    // Return the corresponding month from the array
                    return months[index];
                }
                return "";
            }
        };
    }

    /**
     * Display the line chart and its properties.
     * @param lineChart Object used to modify the chart
     */
    private void showLineChart(LineChart lineChart)
    {

        List<Entry> entries = getDataset();
        LineDataSet dataSet = new LineDataSet(entries, "Cost");
        LineData lineData = new LineData(dataSet);

        ValueFormatter formatter = setLabel(MONTHS);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(entries.size());
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
     * Align the line chart
     * @param lineChart Object used to modify the chart
     */
    private void modifyLineChart(LineChart lineChart)
    {
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
     * Display the bar chart and its properties.
     * @param barChart Object used to modify the chart
     */
    private void showBarChart(BarChart barChart)
    {
        List<Entry> entryVal = getDataset();
        List<BarEntry> entries = cast(entryVal);

        BarDataSet dataSet = new BarDataSet(entries, "Cost");
        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.WHITE);
        BarData data = new BarData(dataSet);

        // Set the value formatter for the x-axis labels
        ValueFormatter formatter = setLabel(MONTHS);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(formatter);

        barChart.setData(data);
        modifyBarChart(barChart);
    }

    /**
     * Align the bar chart
     * @param barChart Object used to modify the chart
     */
    private void modifyBarChart(BarChart barChart)
    {
        barChart.invalidate();
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
    }

    /**
     * TO display the pieChart
     * @param pieChart Object used to modify the pieChart
     */
    private  void showPieChart(PieChart pieChart){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //PieChart Data
        Map<String, Float> typeAmountMap = initPieChartData();

        //PieChart Colours
        ArrayList<Integer> colors = (ArrayList<Integer>) initChartColours();

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
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

    //Initializing the data for PieChart
    public Map<String, Float> initPieChartData()
    {

        //initializing data
        Map<String, Float> entries = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Double> entry : monthlyPriceMap.entrySet()) {
            String month = MONTHS[i];
            Double price = entry.getValue();
            entries.put(month, price.floatValue());
            i++;
        }

        return entries;
    }

    //Initializing the colours for PieChart
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
}

