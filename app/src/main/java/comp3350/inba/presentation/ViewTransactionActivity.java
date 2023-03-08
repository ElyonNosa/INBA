package comp3350.inba.presentation;
import androidx.annotation.NonNull;

import android.app.Activity;
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

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewTransactionActivity extends Activity implements View.OnClickListener {

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

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        //Local Variable
        final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
                "Aug", "Sept", "Oct", "Nov", "Dec"};
        //Initializing the graphs
        GraphView graph = findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(getDataPoint());
        //BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(getDataPoint());
        LineChart lineChart = findViewById(R.id.line_chart);
        BarChart barChart = findViewById(R.id.bar_chart);
        PieChart pieChart = findViewById(R.id.pie_chart);

        // graph properties
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(12);

        graph.getGridLabelRenderer().setGridColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.setTitleColor(0xFFA6ABBD);

        switch (view.getId()) {
            //Line Graph
            case R.id.button:
//                graph.removeAllSeries();
//                graph.setVisibility(View.VISIBLE);
//                graph.addSeries(series1);
//
//                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//                staticLabelsFormatter.setHorizontalLabels(MONTHS);
//                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                System.out.println("Line Chart in progress");
                showLineChart(lineChart, MONTHS);
                lineChart.getDescription().setEnabled(false);
                lineChart.setTouchEnabled(true);
                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);
                lineChart.setDrawGridBackground(false);
                lineChart.setPinchZoom(true);



                break;
            //Bar Graph
            case R.id.button2:
                graph.removeAllSeries();
                graph.setVisibility(View.VISIBLE);
//                series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//                    @Override
//                    public int get(DataPoint data) {
//                        if (data.getX() % 2 == 0) {
//                            return Color.GREEN;
//                        } else {
//                            return Color.BLUE;
//                        }
//                    }
//                });
//                graph.addSeries(series2);
//                StaticLabelsFormatter staticLabelsFormatter2 = new StaticLabelsFormatter(graph);
//                staticLabelsFormatter2.setHorizontalLabels(MONTHS);
//                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);
//                series2.setSpacing(0);
//                series2.setDrawValuesOnTop(true);
//                series2.setValuesOnTopColor(0xFFA6ABBD);
//
//                graph.addSeries(series2);
                System.out.println("Bar CHART is currently under progress ");
                showBarChart(barChart, MONTHS);
                barChart.setDrawBarShadow(false);
                barChart.setDrawValueAboveBar(true);
                barChart.getDescription().setEnabled(false);
                barChart.setDrawGridBackground(false);


                break;
            //Pie Chart
            case R.id.button3:
                graph.removeAllSeries();
                graph.setVisibility(View.INVISIBLE);
                 showPieChart(pieChart);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private DataPoint[] getDataPoint() {
        DataPoint[] dp = new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 4),
                new DataPoint(3, 2),
                new DataPoint(4, 5),
                new DataPoint(5, 6),
                new DataPoint(6, 2),
                new DataPoint(7, 7),
                new DataPoint(8, 5),
                new DataPoint(9, 3),
                new DataPoint(10, 6),
                new DataPoint(11, 8)
        };
        return dp;
    }
    private void showLineChart(LineChart lineChart, String[] months)
    {
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

        LineDataSet dataSet = new LineDataSet(entries, "Line Data Set");
        LineData lineData = new LineData(dataSet);

        ValueFormatter formatter = new ValueFormatter() {
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

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(entries.size());
        xAxis.setValueFormatter(formatter);
//        xAxis.setLabelRotationAngle(0); // Set rotation angle to avoid overlapping labels


        // Add data to the chart
        lineChart.setData(lineData);

        // Refresh the chart
        lineChart.invalidate();
    }
    private void showBarChart(BarChart barChart, String[] months)
    {
        ArrayList<BarEntry> entries = new ArrayList<>();
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

        BarDataSet dataSet = new BarDataSet(entries, "Bar Data Set");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        // Set the value formatter for the x-axis labels
        ValueFormatter formatter = new ValueFormatter() {
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

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(formatter);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();

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
        typeAmountMap.put("Feb",50);
        typeAmountMap.put("Mar",40);
        typeAmountMap.put("Apr",20);
        typeAmountMap.put("May",50);
        typeAmountMap.put("June",60);
        typeAmountMap.put("July",20);
        typeAmountMap.put("Aug",70);
        typeAmountMap.put("Sept",50);
        typeAmountMap.put("Oct",30);
        typeAmountMap.put("Nov",60);
        typeAmountMap.put("Dec",80);

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
}

