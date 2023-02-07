package comp3350.inba.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(transactionList.get(position).getCategory());
                    text2.setText("" + transactionList.get(position).getPrice());

                    return view;
                }
            };

            final ListView listView = (ListView)findViewById(R.id.listTransactions);
            listView.setAdapter(transactionArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Button updateButton = (Button)findViewById(R.id.buttonTransactionUpdate);
                    Button deleteButton = (Button)findViewById(R.id.buttonTransactionDelete);

                    if (position == selectedTransactionPosition) {
                        listView.setItemChecked(position, false);
                        updateButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        selectedTransactionPosition = -1;
                    } else {
                        listView.setItemChecked(position, true);
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        selectedTransactionPosition = position;
                        selectTransactionAtPosition(position);
                    }
                }
            });

            final EditText editTransactionCategory = (EditText)findViewById(R.id.editTransactionCategory);
            editTransactionCategory.setEnabled(true);
            editTransactionCategory.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {}
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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void selectTransactionAtPosition(int position) {
        Transaction selected = transactionArrayAdapter.getItem(position);

        EditText editID = (EditText)findViewById(R.id.editTransactionCategory);
        EditText editName = (EditText)findViewById(R.id.editTransactionPrice);

        editID.setText(selected.getCategory());
        editName.setText("" + selected.getPrice());
    }

//    public void buttonTransactionStudentsOnClick(View v) {
//        EditText editID = (EditText)findViewById(R.id.editTransactionCategory);
//        String transactionID = editID.getText().toString();
//
//        Intent csIntent = new Intent(TransactionsActivity.this, TransactionsActivity.class);
//        Bundle b = new Bundle();
//        b.putString("transactionID", transactionID);
//        csIntent.putExtras(b);
//        TransactionsActivity.this.startActivity(csIntent);
//    }

    public void buttonTransactionCreateOnClick(View v) {
        Transaction transaction = createTransactionFromEditText();
        String result;

        result = validateTransactionData(transaction, true);
        if (result == null) {
            try {
                transaction = accessTransactions.insertTransaction(transaction);

                transactionList = accessTransactions.getTransactions();
                transactionArrayAdapter.notifyDataSetChanged();
                int pos = transactionList.indexOf(transaction);
                if (pos >= 0) {
                    ListView listView = (ListView)findViewById(R.id.listTransactions);
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
        Transaction transaction = createTransactionFromEditText();
        String result;

        result = validateTransactionData(transaction, false);
        if (result == null) {
            try {
                transaction = accessTransactions.updateTransaction(transaction);

                transactionList = accessTransactions.getTransactions();
                transactionArrayAdapter.notifyDataSetChanged();
                int pos = transactionList.indexOf(transaction);
                if (pos >= 0) {
                    ListView listView = (ListView)findViewById(R.id.listTransactions);
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
        Transaction transaction = createTransactionFromEditText();
        String result;

        try {
            accessTransactions.deleteTransaction(transaction);

            int pos = transactionList.indexOf(transaction);
            if (pos >= 0) {
                ListView listView = (ListView) findViewById(R.id.listTransactions);
                listView.setSelection(pos);
            }
            transactionList = accessTransactions.getTransactions();
            transactionArrayAdapter.notifyDataSetChanged();
        } catch (final Exception e) {
            Messages.warning(this, e.getMessage());
        }
    }

    private Transaction createTransactionFromEditText() {
        EditText editCategory = (EditText)findViewById(R.id.editTransactionCategory);
        EditText editPrice = (EditText)findViewById(R.id.editTransactionPrice);

        Transaction transaction = new Transaction(System.currentTimeMillis() / 1000L,
                Double.parseDouble(editPrice.getText().toString()), editCategory.getText().toString());

        return transaction;
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
            return "Transaction ID already exists.";
        }


        return null;
    }
}
