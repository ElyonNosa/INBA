package comp3350.inba.objects;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * Ensure the transaction has valid properties.
     * @param accessTransactions The transaction persistence.
     * @param isNewTransaction True if the transaction does not exist in the database.
     * @return The error message if the transaction is invalid, null string otherwise.
     */
    public String validateTransactionData(AccessTransactions accessTransactions, boolean isNewTransaction) {
        final int LIMIT = 1000000000;

        // check for valid category type
        if (getCategory() == null || getCategory().length() < 1) {
            return "Transaction Category required";
        }

        // check for valid price
        if (getPrice() <= 0) {
            return "Positive price required";
        }

        // check for valid price
        if (getPrice() >= LIMIT) {
            return "We know you are too poor to afford this!";
        }

        // check if transaction already exists
        if (isNewTransaction && accessTransactions.getTimestampIndex(User.currUser, getTime()) != -1) {
            return "A transaction has already been made within the last second. " +
                    "Please wait 1 second and try again.";
        }

        return null;
    }
}
