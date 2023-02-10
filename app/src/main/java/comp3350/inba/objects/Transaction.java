package comp3350.inba.objects;

import android.annotation.SuppressLint;

import java.util.Locale;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Transaction()
 * <p>
 * Store simple information pertaining to a financial transaction.
 */
public class Transaction {
    // list of predefined transaction categories
    public final static String[] CATEGORIES = {"Amenities", "Education", "Entertainment", "Food",
            "Hardware", "Hobby", "Medical", "Misc", "Transportation", "Utilities"};
    // unix timestamp of the transaction
    private final long time;
    // the money spend in the transaction
    private final double price;
    // the type of transaction
    private final String category;

    /**
     * Constructor
     *
     * @param newTime The timestamp of the transaction.
     */
    public Transaction(final long newTime) {
        this(newTime, 11.11, "Example");
    }

    /**
     * Constructor
     *
     * @param newTime     The timestamp of the transaction.
     * @param newPrice    The price of the transaction.
     * @param newCategory The category of the transaction.
     */
    public Transaction(final long newTime, final double newPrice, final String newCategory) {
        time = newTime;
        category = newCategory;
        price = newPrice;
    }

    /**
     * Getter for time
     *
     * @return time
     */
    public long getTime() {
        return time;
    }

    /**
     * Getter for category
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter for price
     *
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * toString(): Create a string containing information of the transaction.
     *
     * @return String of transaction information.
     */
    public String toString() {
        // create format for the date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // print date, category and price
        return String.format("%s, %s, $%s", jdf.format(new Date(time * 1000L)), category,
                String.format(Locale.ENGLISH, "%.2f", price));
    }

    /**
     * equals(): compare this transaction with another.
     *
     * @param otherthe transaction to compare to.
     * @return true if they are the same transaction.
     */
    public boolean equals(Object other) {
        boolean equals = false;
        // ensure that the other object is of transaction class
        if (other instanceof Transaction) {
            final Transaction otherTransaction = (Transaction) other;
            // compare the timestamp of each
            equals = Objects.equals(this.time, otherTransaction.time);
        }
        return equals;
    }
}
