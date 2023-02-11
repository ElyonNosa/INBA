package comp3350.inba.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;

/**
 * TransactionsActivity.java
 *
 * This class is coupled to activity_transactions.xm
 */
public class TransactionsActivity extends Activity {
    // the transaction "database"
    private AccessTransactions accessTransactions;
    // the local list of transactions after retrieving from the "database"
    private List<Transaction> transactionList;
    // adapter used for displaying transaction list into listview
    private ArrayAdapter<Transaction> transactionArrayAdapter;
    // index of the current selected transaction
    private int selectedTransactionPosition = -1;
    // the unix timestamp of the current selected transaction
    private long selectedTransactionTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);



        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editTransactionCategory);
        // Get the string array
        String[] categories = getResources().getStringArray(R.array.categories_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        textView.setAdapter(adapter);
        // create instance of the database
        accessTransactions = new AccessTransactions();
        try {
            // get the transaction list from the database
            transactionList = accessTransactions.getTransactions();
            // create the adapter for the transaction list
            transactionArrayAdapter = new ArrayAdapter<Transaction>(this,
                    android.R.layout.simple_list_item_activated_2, android.R.id.text1, transactionList) {

                /**
                 * Format the transaction list in a certain way.
                 * @param position index of the selected element in the list.
                 * @param convertView Convert view.
                 * @param parent Parent view.
                 * @return The formatted view.
                 */
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = view.findViewById(android.R.id.text1);
                    TextView text2 = view.findViewById(android.R.id.text2);

                    // the top text is the category of the transaction
                    text1.setText(transactionList.get(position).getCategory());
                    // the bottom text is the price of the transaction
                    text2.setText(String.format(Locale.ENGLISH, "%.2f",
                            transactionList.get(position).getPrice()));

                    return view;
                }
            };

            // create listview object from the list view in the layout
            final ListView listView = findViewById(R.id.listTransactions);
            // link the list view with the adapter for the transaction list
            listView.setAdapter(transactionArrayAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                // create objects from the buttons in the layout
                Button updateButton = findViewById(R.id.buttonTransactionUpdate);
                Button deleteButton = findViewById(R.id.buttonTransactionDelete);

                // check if a transaction is selected in the list view
                if (position == selectedTransactionPosition) {
                    listView.setItemChecked(position, false);
                    // it should not be possible to update or delete a new transaction
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    // reset values of transaction position and timestamp
                    selectedTransactionPosition = -1;
                    selectedTransactionTime = -1;
                }
                // else a transaction was not selected
                else {
                    listView.setItemChecked(position, true);
                    // allow user to to update or delete the selected transaction
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    // set values of transaction position and timestamp accordingly
                    selectedTransactionPosition = position;
                    selectTransactionAtPosition(position);
                    selectedTransactionTime = transactionList.get(position).getTime();
                }
            });
        } catch (final Exception e) {
            Messages.fatalError(this, e.getMessage());
        }

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set TransactionActivity selected
        bottomNavigationView.setSelectedItemId(R.id.buttonAddTransaction);

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
                   /* case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewActivity.class)); // Replace ViewActivity with the class used to view the graphs
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0,0);
                        return true;*/
                    case R.id.buttonAddTransaction:
                        // true if already on page.
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

    /**
     * selectTransactionAtPosition(): given an index, find the respective transaction in the
     * transaction list and update the EditText views.
     * @param position the index of the selected transaction.
     */
    public void selectTransactionAtPosition(int position) {
        // retrieve transaction from the list
        Transaction selected = transactionArrayAdapter.getItem(position);

        // create objects of the EditText views
        EditText editCategory = findViewById(R.id.editTransactionCategory);
        EditText editPrice = findViewById(R.id.editTransactionPrice);

        // update EditTexts with information of the transaction.
        editCategory.setText(selected.getCategory());
        editPrice.setText(String.format(Locale.ENGLISH, "%.2f", selected.getPrice()));
    }

    /**
     * buttonTransactionCreateOnClick(): this runs when the create button is pressed.
     * @param v View
     */
    public void buttonTransactionCreateOnClick(View v) {
        // create a new transaction from the EditText fields
        Transaction transaction = createTransactionFromEditText(true);
        // check if the transaction is valid
        String result = validateTransactionData(transaction, true);

        if (result == null) {
            try {
                // insert the transaction into the database list
                transaction = accessTransactions.insertTransaction(transaction);
                // update our local list
                transactionList = accessTransactions.getTransactions();
                // refresh the transaction list view
                transactionArrayAdapter.notifyDataSetChanged();
                // set list position to the new transaction
                int pos = transactionList.indexOf(transaction);
                if (pos >= 0) {
                    ListView listView = findViewById(R.id.listTransactions);
                    listView.setSelection(pos);
                }
            } catch (final Exception e) {
                Messages.fatalError(this, e.getMessage());
            }
        } else {
            Messages.warning(this, result);
        }
    }

    /**
     * buttonTransactionUpdateOnClick(): this runs when the update button is pressed.
     * @param v View
     */
    public void buttonTransactionUpdateOnClick(View v) {
        // create a transaction from the EditText fields
        Transaction transaction = createTransactionFromEditText(false);
        // check if the transaction is valid
        String result = validateTransactionData(transaction, false);

        if (result == null) {
            try {
                // overwrite the given transaction into the database list
                transaction = accessTransactions.updateTransaction(transaction);
                // update our local list
                transactionList = accessTransactions.getTransactions();
                // refresh the transaction list view
                transactionArrayAdapter.notifyDataSetChanged();
                // set list position to the updated transaction
                int pos = transactionList.indexOf(transaction);
                if (pos >= 0) {
                    ListView listView = findViewById(R.id.listTransactions);
                    listView.setSelection(pos);
                }
            } catch (final Exception e) {
                Messages.fatalError(this, e.getMessage());
            }
        } else {
            Messages.warning(this, result);
        }
    }

    /**
     * buttonTransactionDeleteOnClick(): this runs when the delete button is pressed.
     * @param v View
     */
    public void buttonTransactionDeleteOnClick(View v) {
        // create a transaction from the EditText fields
        Transaction transaction = createTransactionFromEditText(false);

        try {
            // delete the given transaction into the database list
            accessTransactions.deleteTransaction(transaction);

            // set list position as the index of the deleted transaction
            int pos = transactionList.indexOf(transaction);
            if (pos >= 0) {
                ListView listView = findViewById(R.id.listTransactions);
                listView.setSelection(pos);
            }
            // update our local list
            transactionList = accessTransactions.getTransactions();
            // refresh the transaction list view
            transactionArrayAdapter.notifyDataSetChanged();
        } catch (final Exception e) {
            Messages.warning(this, e.getMessage());
        }
    }

    /**
     * createTransactionFromEditText(): Using the EditText fields, create a new transaction instance.
     * @param isNewTransaction True if this transaction does not yet exist in the database.
     * @return The created transaction.
     */
    private Transaction createTransactionFromEditText(boolean isNewTransaction) {
        // create objects of EditTexts
        EditText editCategory = findViewById(R.id.editTransactionCategory);
        EditText editPrice = findViewById(R.id.editTransactionPrice);
        // transaction properties
        Transaction output = null;
        long time = 0;
        double price = 0;
        try {
            // attempt to parse transaction price to double
            price = Double.parseDouble(editPrice.getText().toString());
            // check if this transaction does not yet exist
            if (isNewTransaction) {
                // set timestamp to current time
                time = System.currentTimeMillis() / 1000L;
            } else {
                // use timestamp of selected transaction
                time = selectedTransactionTime;
            }
            // create the transaction using the obtained properties
            output = new Transaction(time, price, editCategory.getText().toString());
        } catch (Exception e) {
            // Messages.warning(this, e.getMessage());
            // parsing error occurred, leave the transaction as null
        }
        return output;
    }

    /**
     * Ensure the transaction has valid properties.
     * @param transaction The transaction to test.
     * @param isNewTransaction True if the transaction does not exist in the database.
     * @return The error message if the transaction is invalid, null string otherwise.
     */
    private String validateTransactionData(Transaction transaction, boolean isNewTransaction) {
        // check if the transaction is null (incorrectly parsed price)
        if (transaction == null) {
            return "Price is not valid";
        }

        // check for valid category type
        if (transaction.getCategory() == null || transaction.getCategory().length() < 1) {
            return "Transaction Category required";
        }

        // check for valid price
        if (transaction.getPrice() <= 0) {
            return "Positive price required";
        }

        // check if transaction already exists
        if (isNewTransaction && accessTransactions.getTimestampIndex(transaction.getTime()) != -1) {
            return "A transaction has already been made within the last second.\n" +
                    "Please wait 1 second and try again.";
        }

        return null;
    }
}
