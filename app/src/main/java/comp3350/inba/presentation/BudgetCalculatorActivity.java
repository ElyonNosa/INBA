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

import java.util.ArrayList;

import comp3350.inba.R;

public class BudgetCalculatorActivity extends Activity {

    private EditText editTextItem;
    private EditText editTextPrice;
    private Button buttonAdd;
    private ListView listViewItems;
    private ArrayList<String> itemList;
    private ArrayList<Double> priceList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_calculator);

        editTextItem = findViewById(R.id.editTextItem);
        editTextPrice = findViewById(R.id.editTextPrice);

        buttonAdd = findViewById(R.id.buttonAdd);
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

        listViewItems = findViewById(R.id.listViewItems);
        itemList = new ArrayList<>();
        priceList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listViewItems.setAdapter(adapter);

        // Set a long click listener on the ListView to allow items to be deleted
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

