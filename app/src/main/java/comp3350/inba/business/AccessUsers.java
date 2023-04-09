package comp3350.inba.business;

import java.util.Collections;
import java.util.List;

import comp3350.inba.application.Service;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

/**
 * AccessUsers.java
 *
 * Performs actions on the database of users.
 */

public class AccessUsers {
    //Constructor
    public AccessUsers(){
        //do nothing
    }

    /**
     * Obtain list of users from the database.
     * @return The database's list of users.
     */
    public List<User> getUsers()
    {
        return Collections.unmodifiableList(Service.getUserPersistence().get_user_list());
    }

    /**
     * Insert a transaction to the list.
     * @param currUser the user to insert.
     * @return the user inserted.
     */
    public User insertUser(User currUser)
    {
        return Service.getUserPersistence().insert_user(currUser);
    }

    /**
     * Update a user in the list
     * @param currUser The user with updated properties.
     * @return The updated user.
     */
    public User updateUser(User currUser)
    {
        return Service.getUserPersistence().update_user(currUser);
    }

    /**
     * Remove a user from the list.
     * @param currUser The transaction to delete.
     */
    public void deleteUser(User currUser)
    {
        Service.getUserPersistence().delete_user(currUser);
    }
}
