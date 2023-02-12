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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
        GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(getDataPoint());
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(getDataPoint());

        final String[] MONTHS = {
                "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

        // graph properties
        graph.getGridLabelRenderer().setGridColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setVerticalLabelsColor(0xFFA6ABBD);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.setTitleColor(0xFFA6ABBD);
        // change this title when view transactions is finished!
        graph.setTitle("WIP, moved to iteration 2");

        switch (view.getId()) {
            case R.id.button:

                //Line Graph
                graph.removeAllSeries();
                graph.addSeries(series1);

                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                staticLabelsFormatter.setHorizontalLabels(MONTHS);
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                break;
            case R.id.button2:
                graph.removeAllSeries();
                series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
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
                staticLabelsFormatter2.setHorizontalLabels(MONTHS);
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);
                series2.setDrawValuesOnTop(true);
                series2.setValuesOnTopColor(0xFFA6ABBD);

                graph.addSeries(series2);

                break;
            case R.id.button3:
                System.out.println("Work in progress 2");
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
}

