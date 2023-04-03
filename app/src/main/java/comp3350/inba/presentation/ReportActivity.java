package comp3350.inba.presentation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import comp3350.inba.R;
import comp3350.inba.business.AccessTransactions;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;


public class ReportActivity extends Activity {


    private ArrayList<LocalDateTime> localDateTime = new ArrayList<>();
    private ArrayList<String> category = new ArrayList<>();
    private ArrayList<Double> price = new ArrayList<>();

    private ArrayList<LocalDateTime> sortedLocalDateTime = new ArrayList<>();
    private ArrayList<String> sortedCategory = new ArrayList<>();

    private ArrayList<Double> sortedPrice = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getList();
        sortListByPrice();
    }

    /**
     * Gets the transaction list from AccessTransaction and separates the content of that transaction
     */
    public void getList()
    {
        AccessTransactions accessTransactions = new AccessTransactions();
        List<Transaction> transactionList = accessTransactions.getTransactions(User.currUser);
        for (int i =0; i < transactionList.size(); i++) {
            Transaction transaction = transactionList.get(i);
            localDateTime.add(transaction.getTime());
            category.add(transaction.getCategory());
            price.add(transaction.getPrice());
        }
    }

    /**
     * Sorts the other lists according to the price.
     */
    public void sortListByPrice()
    {
        // Assign the sorted ArrayLists to new variables
        sortedPrice = sortByPrice(price);
        for (int i = 0; i < sortedPrice.size(); i++) {
            int index = price.indexOf(sortedPrice.get(i));
            sortedLocalDateTime.add(localDateTime.get(index));
            sortedCategory.add(category.get(index));
        }
    }

    /**
     * Gets unsorted price list and sorts it.
     * @param price Price of the transaction
     * @return The sorted ArrayList of prices
     */
    public ArrayList<Double> sortByPrice(ArrayList<Double> price)
    {
        ArrayList<Double> sortedPrice = new ArrayList<>(price);
        Collections.sort(sortedPrice, Collections.reverseOrder());
        return sortedPrice;
    }

}
