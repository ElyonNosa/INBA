package comp3350.inba.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;

public class TransactionsActivity extends Activity {

    private AccessTransactions accessTransactions;
    private List<Transaction> transactionList;
    private ArrayAdapter<Transaction> transactionArrayAdapter;
    private int selectedTransactionPosition = -1;

    private long selectedTransactionTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        accessTransactions = new AccessTransactions();

        try
        {
            transactionList = accessTransactions.getTransactions();
            transactionArrayAdapter = new ArrayAdapter<Transaction>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, transactionList)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView text1 = view.findViewById(android.R.id.text1);
                    TextView text2 = view.findViewById(android.R.id.text2);

                    text1.setText(transactionList.get(position).getCategory());
                    text2.setText(String.valueOf(transactionList.get(position).getPrice()));

                    return view;
                }
            };

            final ListView listView = findViewById(R.id.listTransactions);
            listView.setAdapter(transactionArrayAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Button updateButton = findViewById(R.id.buttonTransactionUpdate);
                Button deleteButton = findViewById(R.id.buttonTransactionDelete);

                if (position == selectedTransactionPosition) {
                    listView.setItemChecked(position, false);
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    selectedTransactionPosition = -1;
                    selectedTransactionTime = -1;
                } else {
                    listView.setItemChecked(position, true);
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    selectedTransactionPosition = position;
                    selectTransactionAtPosition(position);
                    selectedTransactionTime = transactionList.get(position).getTime();
                }
            });
        }
        catch (final Exception e)
        {
            Messages.fatalError(this, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void selectTransactionAtPosition(int position) {
        Transaction selected = transactionArrayAdapter.getItem(position);

        EditText editCategory = findViewById(R.id.editTransactionCategory);
        EditText editPrice = findViewById(R.id.editTransactionPrice);

        editCategory.setText(selected.getCategory());
        editPrice.setText(String.valueOf(selected.getPrice()));
    }

    public void buttonTransactionCreateOnClick(View v) {
        Transaction transaction = createTransactionFromEditText(true);
        String result = validateTransactionData(transaction, true);

        if (result == null) {
            try {
                transaction = accessTransactions.insertTransaction(transaction);

                transactionList = accessTransactions.getTransactions();
                transactionArrayAdapter.notifyDataSetChanged();
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

    public void buttonTransactionUpdateOnClick(View v) {
        Transaction transaction = createTransactionFromEditText(false);
        String result = validateTransactionData(transaction, false);

        if (result == null) {
            try {
                transaction = accessTransactions.updateTransaction(transaction);

                transactionList = accessTransactions.getTransactions();
                transactionArrayAdapter.notifyDataSetChanged();
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

    public void buttonTransactionDeleteOnClick(View v) {
        Transaction transaction = createTransactionFromEditText(false);

        try {
            accessTransactions.deleteTransaction(transaction);

            int pos = transactionList.indexOf(transaction);
            if (pos >= 0) {
                ListView listView = findViewById(R.id.listTransactions);
                listView.setSelection(pos);
            }
            transactionList = accessTransactions.getTransactions();
            transactionArrayAdapter.notifyDataSetChanged();
        } catch (final Exception e) {
            Messages.warning(this, e.getMessage());
        }
    }

    private Transaction createTransactionFromEditText(boolean isNewTransaction) {
        EditText editCategory = findViewById(R.id.editTransactionCategory);
        EditText editPrice = findViewById(R.id.editTransactionPrice);

        long time = 0;
        if(isNewTransaction) {
            time = System.currentTimeMillis() / 1000L;
        }
        else {
            time = selectedTransactionTime;
        }

        return new Transaction(time, Double.parseDouble(editPrice.getText().toString()), editCategory.getText().toString());
    }

    private String validateTransactionData(Transaction transaction, boolean isNewTransaction) {
        if (transaction.getCategory().length() == 0) {
            return "Transaction Category required";
        }

        if (transaction.getPrice() < 0) {
            return "Positive price required";
        }

        if (isNewTransaction && accessTransactions.getRandom(transaction.getTime()) != null) {
//            return "Transaction ID " + transaction.getTransactionCategory() + " already exists.";
            return "A transaction has already been made this second. Please wait 1 second and try again. ";
        }


        return null;
    }
}
