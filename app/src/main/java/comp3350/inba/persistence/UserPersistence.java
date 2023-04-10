package comp3350.inba.persistence;

import java.util.List;

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
    List<String[]> getUsers();

    /**
     * Insert a user to the list.
     * @param usr The user to insert.
     * @return The inserted user.
     */
    String[] insertUser(String[] usr);

    /**
     * Update a user that exists in the list.
     *
     * @param usr The user with updated properties.
     * @return The updated user.
     */
    String[] updateUser(String[] usr);

    /**
     * Remove a user from the list.
     * @param usr The user to delete.
     */
    void deleteUser(String[] usr);
}
