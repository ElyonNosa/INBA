package comp3350.inba.objects;

import android.annotation.SuppressLint;

import java.util.Locale;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    public final static String[] CATEGORIES = {"Amenities", "Education", "Entertainment", "Food", "Hardware", "Hobby", "Medical", "Misc", "Transportation", "Utilities"};
    private final long time;
    private final double price;
    private final String category;

    public Transaction(final long newTime)
    {
        this(newTime, 11.11, "Example");
    }
    public Transaction(final long newTime, final double newPrice, final String newCategory)
    {
        time = newTime;
        category = newCategory;
        price = newPrice;
    }

    public long getTime () {return time;}
    public String getCategory () {return category;}
    public double getPrice () {return price;}

    public String toString()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s, %s, $%s", jdf.format(new Date(time * 1000L)), category, String.format(Locale.ENGLISH,"%.2f", price));
    }

    public boolean equals(Object other)
    {
        boolean equals = false;
        if (other instanceof Transaction) {
            final Transaction otherTransaction = (Transaction) other;
            equals = Objects.equals(this.time, otherTransaction.time);
        }
        return equals;
    }
}
