package comp3350.inba.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Category.java
 * The set of categories that a user has created.
 */
public class Category implements Comparable<Category> {
    // the name of the current category
    private final String name;

    /**
     * Constructor.
     * @param name The name of the category.
     */
    Category(String name)
    {
        this.name = name;
    }

    /**
     * Getter for the category name
     * @return the name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Compare two categories.
     * @param category The category to compare to.
     * @return Positive int if this is greater than the other category. Zero if equal.
     */
    @Override
    public int compareTo(Category category) {
        return this.name.compareTo(category.getName());
    }
}
