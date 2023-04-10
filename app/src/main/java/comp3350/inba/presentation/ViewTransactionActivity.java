package comp3350.inba.presentation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import comp3350.inba.R;

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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewTransactionActivity extends AppCompatActivity implements View.OnClickListener {

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
        final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
                "Aug", "Sept", "Oct", "Nov", "Dec"};
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

                showLineChart(lineChart, MONTHS);
                break;

            //Bar Graph
            case R.id.button2:
                // Hide the other two charts
                lineChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);

                // Show the line chart
                barChart.setVisibility(View.VISIBLE);

                showBarChart(barChart, MONTHS);
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

    public List<Entry> getDataset() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 10));
        entries.add(new BarEntry(1f, 20));
        entries.add(new BarEntry(2f, 30));
        entries.add(new BarEntry(3f, 40));
        entries.add(new BarEntry(4f, 50));
        entries.add(new BarEntry(5f, 20));
        entries.add(new BarEntry(6f, 50));
        entries.add(new BarEntry(7f, 40));
        entries.add(new BarEntry(8f, 90));
        entries.add(new BarEntry(9f, 30));
        entries.add(new BarEntry(10f, 70));
        entries.add(new BarEntry(11f, 60));

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

    //TODO: Need to resolve the bug in label. It is printing duplicate months.
    private void showLineChart(LineChart lineChart, String[] months)
    {

        List<Entry> entries = getDataset();
        LineDataSet dataSet = new LineDataSet(entries, "Cost");
        LineData lineData = new LineData(dataSet);

        ValueFormatter formatter = setLabel(months);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(entries.size());
        xAxis.setValueFormatter(formatter);
//        xAxis.setLabelRotationAngle(0);

        // Add data to the chart
        lineChart.setData(lineData);
        modifyLineChart(lineChart);
    }
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


    private void showBarChart(BarChart barChart, String[] months)
    {
        List<Entry> entryVal = getDataset();
        List<BarEntry> entries = cast(entryVal);

        BarDataSet dataSet = new BarDataSet(entries, "Cost");
        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.WHITE);
        BarData data = new BarData(dataSet);

        // Set the value formatter for the x-axis labels
        ValueFormatter formatter = setLabel(months);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(formatter);

        barChart.setData(data);
        modifyBarChart(barChart);
    }

    private void modifyBarChart(BarChart barChart)
    {
        barChart.invalidate();
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
    }
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
        //======================================================================================================================================

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

    //Used to allocate different colours to pieChart
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

