package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.User;

/**
 * UserPersistence.java
 *
 * Abstract class for user database methods.
 */
public interface UserPersistence {
    /**
     * Get list of users.
     * @return The list of users.
     */
    List<String[]> get_user_list();

    /**
     * Insert a user to the list.
     * @param usr The user to insert.
     * @return The inserted user.
     */
    String[] insert_user(String[] usr);

    /**
     * Update a user that exists in the list.
     *
     * @param usr The user with updated properties.
     * @return The updated user.
     */
    String[] update_user(String[] usr);

    /**
     * Remove a user from the list.
     * @param usr The user to delete.
     */
    void delete_user(String[] usr);
}
