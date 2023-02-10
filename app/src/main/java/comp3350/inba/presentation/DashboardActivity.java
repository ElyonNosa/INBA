package comp3350.inba.presentation;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * DashboardActivity.java
 *
 * This class is coupled with activity_dashboard.xml
 */
public class DashboardActivity extends Activity {
    // the transactions database
    private AccessTransactions accessTransactions;
    // the adapter to display transactions in a list view
    private ArrayAdapter<Transaction> transactionArrayAdapter;
    // the local list of transactions
    private List<Transaction> transactionList;

    /**
     * Constructor
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        accessTransactions = new AccessTransactions();
        try {
            // display transactions in list
            transactionList = accessTransactions.getTransactions();
            transactionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
            final ListView listView = findViewById(R.id.transaction_list);
            // adapt the transactions list to the listview
            listView.setAdapter(transactionArrayAdapter);
        }
        catch (final Exception e) {
            Messages.fatalError(this, e.getMessage());
        }
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
    }

    /**
     * This runs when the add button is clicked.
     * @param v View.
     */
    public void buttonAddTransactionOnClick(View v) {
        Intent transactionsIntent = new Intent(DashboardActivity.this, TransactionsActivity.class);
        // open the transactions activity
        DashboardActivity.this.startActivity(transactionsIntent);
    }
}
