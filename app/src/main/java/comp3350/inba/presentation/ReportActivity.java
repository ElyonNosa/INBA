package comp3350.inba.presentation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.format.DateTimeFormatter;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;


public class ReportActivity extends Activity {


    private final ArrayList<LocalDateTime> localDateTime = new ArrayList<>();
    private final ArrayList<String> category = new ArrayList<>();
    private final ArrayList<BigDecimal> price = new ArrayList<>();

    private ArrayList<LocalDateTime> sortedLocalDateTime = new ArrayList<>();
    private final ArrayList<String> sortedCategory = new ArrayList<>();

    private ArrayList<BigDecimal> sortedPrice = new ArrayList<>();

    PopupMenu popupMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // user instance
        User user = new User(getApplicationContext());
        AccessTransactions accessTransactions = new AccessTransactions();
        List<Transaction> transactionList = accessTransactions.getTransactions(user.getUserID());
        addList(transactionList);

        Button sortButton = findViewById(R.id.sort_button);

        //Set an OnClickListener to show the popup menu when the button is clicked
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        //Define the popup menu options
        popupMenu = new PopupMenu(this, sortButton);
        popupMenu.getMenuInflater().inflate(R.menu.sort_options, popupMenu.getMenu());
        //Handle the sort when clicked on popup.
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sort_by_time:
                        sortListByTime();
                        createTable();
                        return true;
                    case R.id.sort_by_price:
                        sortListByPrice();
                        createTable();
                        return true;
                    default:
                        return false;
                }
            }
        });
        // sort by time by default
        sortListByTime();
        createTable();
    }

    /**
     * Shows the popup menu anchored to the specified view.
     */
    private void showPopupMenu(View view) {
        popupMenu.show();
    }

    /**
     *   Creates a table using the sorted arraylist and display the contents
     */
    public void createTable() {
        TableLayout tableLayout = findViewById(R.id.table_layout);
        tableLayout.removeAllViews(); // remove any existing views in the tableLayout

        // Create header row
        TableRow headerRow = new TableRow(this);
        TextView headerTransactionTime = new TextView(this);
        TextView headerCategory = new TextView(this);
        TextView headerPrice = new TextView(this);
        headerTransactionTime.setText("Transaction Time");
        headerCategory.setText("Category");
        headerPrice.setText("Price");
        headerRow.addView(headerTransactionTime);
        headerRow.addView(headerCategory);
        headerRow.addView(headerPrice);
        tableLayout.addView(headerRow);

        // Create a row for each transaction in sortedPrice
        for (int i = 0; i < sortedPrice.size(); i++) {
            TableRow tableRow = new TableRow(this);
            TextView transactionTime = new TextView(this);
            TextView category = new TextView(this);
            TextView price = new TextView(this);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDataTime = sortedLocalDateTime.get(i).format(formatter);
            transactionTime.setText(formatDataTime);
            category.setText(sortedCategory.get(i));
            price.setText("$" + sortedPrice.get(i));
            tableRow.addView(transactionTime);
            tableRow.addView(category);
            tableRow.addView(price);
            tableLayout.addView(tableRow);
        }
    }


    /**
     * Adds the content of the transaction to a list
     */
    public void addList(List<Transaction> transactionList)
    {
        for (int i =0; i < transactionList.size(); i++) {
            Transaction transaction = transactionList.get(i);
            localDateTime.add(transaction.getTime());
            category.add(transaction.getCategoryName());
            price.add(transaction.getPrice());
        }
    }

    /**
     * Sorts the other lists according to the price.
     */
    public void sortListByPrice()
    {
        sortedLocalDateTime.clear();
        sortedCategory.clear();
        // Assign the sorted ArrayLists to new variables
        sortedPrice = sortByPrice(price);
        for (int i = 0; i < sortedPrice.size(); i++) {
            int index = price.indexOf(sortedPrice.get(i));
            sortedLocalDateTime.add(localDateTime.get(index));
            sortedCategory.add(category.get(index));
        }
    }

    /**
     * Sorts the other lists according to the time.
     */
    public void sortListByTime()
    {
        sortedPrice.clear();
        sortedCategory.clear();
        // Assign the sorted ArrayLists to new variables
        sortedLocalDateTime = sortByTime(localDateTime);
        for (int i = 0; i < sortedLocalDateTime.size(); i++) {
            int index = localDateTime.indexOf(sortedLocalDateTime.get(i));
            sortedPrice.add(price.get(index));
            sortedCategory.add(category.get(index));
        }
    }

    /**
     * Gets unsorted price list and sorts it.
     * @param price Price of the transaction
     * @return The sorted ArrayList of prices
     */
    public ArrayList<BigDecimal> sortByPrice(ArrayList<BigDecimal> price)
    {
        ArrayList<BigDecimal> sortedPrice = new ArrayList<>(price);
        Collections.sort(sortedPrice, Collections.reverseOrder());
        return sortedPrice;
    }

    /**
     * Gets unsorted time list and sorts it.
     * @param localDateTime Price of the transaction
     * @return The sorted ArrayList of prices
     */
    public ArrayList<LocalDateTime> sortByTime(ArrayList<LocalDateTime> localDateTime)
    {
        ArrayList<LocalDateTime> sortedLocalDateTime = new ArrayList<>(localDateTime);
        Collections.sort(sortedLocalDateTime, Collections.reverseOrder());
        return sortedLocalDateTime;
    }

}
