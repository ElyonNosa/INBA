package comp3350.inba.objects;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.text.SimpleDateFormat;

import comp3350.inba.business.AccessTransactions;

/**
 * Transaction()
 * <p>
 * Store simple information pertaining to a financial transaction.
 */
public class Transaction {
    // unix timestamp of the transaction
    private final LocalDateTime time;
    // the money spend in the transaction
    private final double price;
    // the type of transaction
    private final Category category;

    /**
     * Constructor
     *
     * @param newPrice    The price of the transaction.
     * @param newCategory The category of the transaction.
     */
    @SuppressLint("NewApi")
    public Transaction(final LocalDateTime time, double newPrice, final String newCategory) {
        this.time = time;
        category = new Category(newCategory);
        // truncate the price (2 decimal places)
        price = Math.floor(newPrice * 100) / 100;
    }

    /**
     * Getter for time
     *
     * @return time
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Getter for category
     *
     * @return category
     */
    public String getCategory() {
        return category.getName();
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
        return String.format("%s, %s, $%s", time.toString(), category.getName(),
                String.format(Locale.ENGLISH, "%.2f", price));
    }

    /**
     * equals(): compare this transaction with another.
     *
     * @param other the transaction to compare to.
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
