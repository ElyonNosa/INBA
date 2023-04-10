package comp3350.inba.presentation;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import comp3350.inba.R;

public class BudgetCalculatorActivity extends Activity {

    private EditText editTextItem;
    private EditText editTextPrice;
    private EditText totalBudgetText;
    private EditText totalDaysText;
    private ArrayList<String> itemList;
    private ArrayList<Double> priceList;
    private ArrayAdapter<String> adapter;

    private double totalBudget;
    private int numDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_calculator);

        editTextItem = findViewById(R.id.editTextItem);
        editTextPrice = findViewById(R.id.editTextPrice);
        totalBudgetText = findViewById(R.id.totalBudgetText);
        totalDaysText = findViewById(R.id.totalDaysText);

        addAndRemoveList();

        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBudget();
            }
        });
    }


    public void calculateBudget()
    {
        TextView showCalculatedBudget = findViewById(R.id.showCalculatedBudget);

        Button buttonConfirm = findViewById(R.id.buttonConfirm);

        String totalBudgetString = totalBudgetText.getText().toString();
        String totalDaysString = totalDaysText.getText().toString();

        if (totalBudgetString.isEmpty() || totalDaysString.isEmpty()) {
            //If empty then return, i.e handle the error
            return;
        }

        totalBudget = Integer.parseInt(totalBudgetString);
        numDays = Integer.parseInt(totalDaysString);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalPrice = 0.0;
                for (double price : priceList) {
                    totalPrice += price;
                }

                double remainingBudget = totalBudget - totalPrice;
                double dailyBudget = remainingBudget / numDays;
                showCalculatedBudget.setText("Total Days: " + numDays + ", Daily budget: $" + String.format("%.2f", dailyBudget));
                showCalculatedBudget.setVisibility(View.VISIBLE);
            }
        });
    }


    public void addAndRemoveList()
    {
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editTextItem.getText().toString();
                String priceString = editTextPrice.getText().toString();

                if (!item.isEmpty() && !priceString.isEmpty()) {
                    double price = Double.parseDouble(priceString);
                    itemList.add(item);
                    priceList.add(price);
                    adapter.notifyDataSetChanged();

                    // Clear the EditText fields
                    editTextItem.setText("");
                    editTextPrice.setText("");
                }
            }
        });


        ListView listViewItems = findViewById(R.id.listViewItems);
        itemList = new ArrayList<>();
        priceList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listViewItems.setAdapter(adapter);

        // A long click listener on the ListView to allow items to be deleted
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                itemList.remove(position);
                priceList.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}

