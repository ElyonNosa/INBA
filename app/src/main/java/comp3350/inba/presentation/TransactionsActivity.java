package comp3350.inba.presentation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Category;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

/**
 * TransactionsActivity.java
 * The page where we create, read, update and delete transactions.
 * This class is coupled to activity_transactions.xml
 */
public class TransactionsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    // the transaction "database"
    private AccessTransactions accessTransactions;
    // the local list of transactions after retrieving from the "database"
    private List<Transaction> transactionList;
    // adapter used for displaying transaction list into listview
    private ArrayAdapter<Transaction> transactionArrayAdapter;
    // index of the current selected transaction
    private int selectedTransactionPosition = -1;
    // the LocalDateTime timestamp of the current selected transaction
    private LocalDateTime selectedTransactionTime;
    // the text view for category suggestions
    private AutoCompleteTextView textViewCategories;
    // the list view containing the transactions
    private ListView listViewTransactions;
    // the spinner for the category filter
    private Spinner spinnerCategories;
    // string to indicate that no category filer is selected
    private static final String NO_FILTER = "No filter";
    // string of the current category filter
    private String categoryFilter = NO_FILTER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        // create instance of access transactions
        accessTransactions = new AccessTransactions();

        // disable traditional input for the date input text box
        ((EditText)findViewById(R.id.editTextDate)).setInputType(InputType.TYPE_NULL);
        // Get a reference to the AutoCompleteTextView in the layout
        textViewCategories = findViewById(R.id.editTransactionCategory);
        // create an adapter from the existing list of categories, assign this to the autocomplete view
        textViewCategories.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, Category.getCategorySet()));

        // get a reference to the Spinner in the layout
        spinnerCategories = findViewById(R.id.spinnerCategoryFilter);
        // enable listener for spinner
        spinnerCategories.setOnItemSelectedListener(this);
        // create an adapter from the existing list of categories, assign this to the autocomplete view
        spinnerCategories.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, getCategoryFilterArray()));

        // get the transaction list from the database
        transactionList = accessTransactions.getTransactions(User.currUser);
        try {
            updateListView();
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
                    case R.id.buttonViewTransaction:
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class)); // Replace ViewActivity with the class used to view the graphs
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonAddTransaction:
                        // true if already on page.
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
     * Update the list view based on the currently saved category filter
     */
    private void updateListView() {
        // check to see if "No filter" is selected
        if (categoryFilter.equals(NO_FILTER)) {
            // use the normal list
            transactionList = accessTransactions.getTransactions(User.currUser);
        } else {
            // use the transaction list filtered by the category
            transactionList = accessTransactions.getTransactionsByCategory(User.currUser, categoryFilter);
        }
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
                // a string containing category, followed by price
                String categoryPrice = transactionList.get(position).getCategory() + ": $"
                        + String.format(Locale.ENGLISH, "%.2f",
                        transactionList.get(position).getPrice());

                // the top text is the category and price of the transaction
                text1.setText(categoryPrice);
                // the bottom text is the timestamp of the transaction
                text2.setText(transactionList.get(position).getTime().toString());

                return view;
            }
        };

        // create listview object from the list view in the layout
        listViewTransactions = findViewById(R.id.listTransactions);
        // link the list view with the adapter for the transaction list
        listViewTransactions.setAdapter(transactionArrayAdapter);

        listViewTransactions.setOnItemClickListener((parent, view, position, id) -> {
            // create objects from the buttons in the layout
            Button updateButton = findViewById(R.id.buttonTransactionUpdate);
            Button deleteButton = findViewById(R.id.buttonTransactionDelete);

            // check if a transaction is selected in the list view
            if (position == selectedTransactionPosition) {
                listViewTransactions.setItemChecked(position, false);
                // it should not be possible to update or delete a new transaction
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
                // reset values of transaction position and timestamp
                selectedTransactionPosition = -1;
                selectedTransactionTime = null;
            }
            // else a transaction was not selected
            else {
                listViewTransactions.setItemChecked(position, true);
                // allow user to to update or delete the selected transaction
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
                // set values of transaction position and timestamp accordingly
                selectedTransactionPosition = position;
                selectTransactionAtPosition(position);
                selectedTransactionTime = transactionList.get(position).getTime();
            }
        });
    }

    /**
     * selectTransactionAtPosition(): given an index, find the respective transaction in the
     * transaction list and update the EditText views.
     *
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
     *
     * @param v View
     */
    public void buttonTransactionCreateOnClick(View v) {
        // create a new transaction from the EditText fields
        Transaction transaction = createTransactionFromEditText(true);
        String result;

        // check for non null
        if (transaction != null) {
            // validate the transaction
            result = transaction.validateTransactionData(accessTransactions, true);
            // check if return is null (there are no errors)
            if (result == null) {
                try {
                    // insert the transaction into the database list
                    transaction = accessTransactions.insertTransaction(User.currUser, transaction);
                    // update our local list
                    transactionList = accessTransactions.getTransactions(User.currUser);
                    // refresh the transaction list view
                    transactionArrayAdapter.notifyDataSetChanged();
                    // set list position to the new transaction
                    int pos = transactionList.indexOf(transaction);
                    if (pos >= 0) {
                        ListView listView = findViewById(R.id.listTransactions);
                        listView.setSelection(pos);
                    }
                    // create an adapter from the existing list of categories, assign this to the autocomplete view
                    textViewCategories.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Category.getCategorySet()));
                    // create an adapter from the existing list of categories, assign this to the category filter spinner
                    spinnerCategories.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getCategoryFilterArray()));
                    // these are done in case of a new category addition, we want to update the autocomplete and filter list
                    // refresh list
                    updateListView();
                } catch (final Exception e) {
                    Messages.fatalError(this, e.getMessage());
                }
            } else {
                Messages.warning(this, result);
            }
        }
    }

    /**
     * buttonTransactionUpdateOnClick(): this runs when the update button is pressed.
     *
     * @param v View
     */
    public void buttonTransactionUpdateOnClick(View v) {
        // create a transaction from the EditText fields
        Transaction transaction = createTransactionFromEditText(false);
        String result;

        // check for non null
        if (transaction != null) {
            // validate the transaction
            result = transaction.validateTransactionData(accessTransactions, false);
            // check if return is null (there are no errors)
            if (result == null) {
                try {
                    // overwrite the given transaction into the database list
                    transaction = accessTransactions.updateTransaction(User.currUser, transaction);
                    // update our local list
                    transactionList = accessTransactions.getTransactions(User.currUser);
                    // refresh the transaction list view
                    transactionArrayAdapter.notifyDataSetChanged();
                    // set list position to the updated transaction
                    int pos = transactionList.indexOf(transaction);
                    if (pos >= 0) {
                        ListView listView = findViewById(R.id.listTransactions);
                        listView.setSelection(pos);
                    }
                    // refresh list
                    updateListView();
                } catch (final Exception e) {
                    Messages.fatalError(this, e.getMessage());
                }
            } else {
                Messages.warning(this, result);
            }
        }
    }


    /**
     * buttonTransactionDeleteOnClick(): this runs when the delete button is pressed.
     *
     * @param v View
     */
    public void buttonTransactionDeleteOnClick(View v) {
        // create a transaction from the EditText fields
        Transaction transaction = createTransactionFromEditText(false);

        try {
            // delete the given transaction into the database list
            accessTransactions.deleteTransaction(User.currUser, transaction);

            // set list position as the index of the deleted transaction
            int pos = transactionList.indexOf(transaction);
            if (pos >= 0) {
                ListView listView = findViewById(R.id.listTransactions);
                listView.setSelection(pos);
            }
            // update our local list
            transactionList = accessTransactions.getTransactions(User.currUser);
            // refresh the transaction list view
            transactionArrayAdapter.notifyDataSetChanged();
            // refresh list
            updateListView();
        } catch (final Exception e) {
            Messages.warning(this, e.getMessage());
        }
    }

    /**
     * After clicking on the date text, show calendar dialog then scroll to the selected date.
     * @param v The view.
     */
    public void textEnterDateOnClick(View v) {
        // create object of EditText
        EditText dateText = findViewById(R.id.editTextDate);
        final Calendar cldr = Calendar.getInstance();
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(TransactionsActivity.this,
                (view, year, month, day) -> {
                    String output = year + "-" + (month + 1) + "-" + day;
                    dateText.setText(output);
                    // create a date using the selected day, month, year.
                    // get the index of the transaction after this date.
                    // use the index to select a position in the list.
                    listViewTransactions.smoothScrollToPosition(accessTransactions.getIndexAfterDate(User.currUser,
                            LocalDateTime.of(year, month+1, day, 0, 0)));
                }, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    /**
     * createTransactionFromEditText(): Using the EditText fields, create a new transaction instance.
     *
     * @param isNewTransaction True if this transaction does not yet exist in the database.
     * @return The created transaction.
     */
    private Transaction createTransactionFromEditText(boolean isNewTransaction) {
        // create objects of EditTexts
        EditText editCategory = findViewById(R.id.editTransactionCategory);
        EditText editPrice = findViewById(R.id.editTransactionPrice);
        // transaction properties
        Transaction output = null;
        LocalDateTime time;
        double price = 0;
        try {
            // attempt to parse transaction price to double
            price = Double.parseDouble(editPrice.getText().toString());
            // check if this transaction does not yet exist
            if (isNewTransaction) {
                // set timestamp to current time
                time = LocalDateTime.now();
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
     * Initialize the nav bar for this page.
     */
    protected void navigationBarInit() {
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set TransactionActivity selected
        bottomNavigationView.setSelectedItemId(R.id.buttonAddTransaction);

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
                        // Intent to start new Activity
                        startActivity(new Intent(getApplicationContext(), ViewTransactionActivity.class));
                        // Can Adjust Transition Speed, both enter and exit
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.buttonAddTransaction:
                        // true if already on page.
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
     * Getter for the category string arraylist with an additional "No filter" category.
     * @return The string arraylist to be used in the category filter spinner.
     */
    private ArrayList<String> getCategoryFilterArray() {
        ArrayList<String> output = Category.getCategorySet();
        // add the "No filter" category to the filter list
        output.add(0, NO_FILTER);
        return output;
    }

    /**
     * onItemSelected(): this runs when the user pressed an entry in the spinner.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // change the text color of the spinner to white
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        categoryFilter = parent.getItemAtPosition(position).toString();
        updateListView();
    }

    /**
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
