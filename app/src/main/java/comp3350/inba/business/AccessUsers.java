package comp3350.inba.business;

import java.util.Collections;
import java.util.List;

import comp3350.inba.application.Service;
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
    public List<String[]> getUsers()
    {
        return Collections.unmodifiableList(Service.getUserPersistence().getUsers());
    }

    /**
     * Insert a transaction to the list.
     * @param currUser the user to insert.
     * @return the user inserted.
     */
    public String[] insertUser(String[] currUser)
    {
        return Service.getUserPersistence().insertUser(currUser);
    }

    /**
     * Update a user in the list
     * @param currUser The user with updated properties.
     * @return The updated user.
     */
    public String[] updateUser(String[] currUser)
    {
        return Service.getUserPersistence().updateUser(currUser);
    }

    /**
     * Remove a user from the list.
     * @param currUser The transaction to delete.
     */
    public void deleteUser(String[] currUser)
    {
        Service.getUserPersistence().deleteUser(currUser);
    }
}
