package comp3350.inba.presentation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import comp3350.inba.R;


import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.*;


public class viewTransaction extends AppCompatActivity implements View.OnClickListener {


    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_transaction);

            //Creating buttons to switch between graphs
            Button button1 = findViewById(R.id.button);
            Button button2 = findViewById(R.id.button2);
            Button button3 = findViewById(R.id.button3);

            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);


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
                    /* case R.id.buttonSettings:
                    startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;*/
                        case R.id.buttonProfile:
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });
        }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        //Local Variable
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
                                "Aug", "Sept", "Oct", "Nov", "Dec", ""};
        //========================================================================

        //Initializing the graph
        GraphView graph = findViewById(R.id.graph);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(12);
        graph.getViewport().setXAxisBoundsManual(true);


        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(getDataPoint());
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(getDataPoint());

        PieChart pieChart = findViewById(R.id.pie_chart);

        switch (view.getId()) {
            //Line Graph
            case R.id.button:

                graph.removeAllSeries();
                graph.setVisibility(View.VISIBLE);
                graph.addSeries(series1);

                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                staticLabelsFormatter.setHorizontalLabels(months);
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                break;
            //Bar Graph
            case R.id.button2:
                graph.removeAllSeries();
                graph.setVisibility(View.VISIBLE);
                series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    //Method to alternate the colour of the bar graph
                    public int get(DataPoint data) {
                        if (data.getX() % 2 == 0) {
                            return Color.GREEN;
                        } else {
                            return Color.BLUE;
                        }
                    }
                });
                graph.addSeries(series2);
                StaticLabelsFormatter staticLabelsFormatter2 = new StaticLabelsFormatter(graph);
                staticLabelsFormatter2.setHorizontalLabels(months);
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);

                series2.setSpacing(0);
                series2.setDrawValuesOnTop(true);
                series2.setValuesOnTopColor(Color.WHITE);

                graph.addSeries(series2);

                break;
            //Pie Chart
            case R.id.button3:
                System.out.println("PIE CHART is currently under progress ");
                graph.setVisibility(View.INVISIBLE);
                showPieChart(pieChart);

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    //Method to get data for the graphs. Later can be switched with the database system.
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
                    new DataPoint(11, 8),
            };
            return dp;
    }
    
    private  void showPieChart(PieChart pieChart){

        //======================================================================================================================================
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

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

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

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

}




