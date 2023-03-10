package comp3350.inba.objects;

import java.util.ArrayList;
import java.util.TreeSet;

public class Category {
    // an ordered set of category names
    private static TreeSet<String> categorySet = new TreeSet<>();
    // the name of the current category
    private final String name;

    /**
     * Constructor.
     * @param name The name of the category.
     */
    Category(String name)
    {
        this.name = name;
        // add the category name to the tree set
        categorySet.add(name);
    }

    /**
     * Getter for the category name
     * @return the name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Get an arraylist of the names.
     * @return The category set converted to a string arraylist.
     */
    public static ArrayList<String> getCategorySet() {
        return new ArrayList<>(categorySet);
    }
}
