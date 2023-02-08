package comp3350.inba.objects;

import java.util.Objects;

public class Transaction {
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
        return String.format("%s, $%s", category, price);
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
