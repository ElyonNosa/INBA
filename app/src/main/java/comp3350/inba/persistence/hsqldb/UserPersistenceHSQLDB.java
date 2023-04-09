package comp3350.inba.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import comp3350.inba.objects.User;
import comp3350.inba.persistence.UserPersistence;

public class UserPersistenceHSQLDB implements UserPersistence
{
    /*
    ###################################################################
        Purpose: This Java program implements User Persistence
    ###################################################################
    */

    // ##################### Class Variables ##########################
    private final String dbPath;

    // ###################### Constructor ############################# 
    public UserPersistenceHSQLDB(final String dbPath) 
    {
        this.dbPath = dbPath;
    }

    // ###################### Class method ############################
    private Connection connection() throws SQLException 
    {
        return DriverManager.getConnection(
            "jdbc:hsqldb:file:" + dbPath + ";shutdown=true", 
            "SA", 
            "");
    }


    // ###################### Class method ############################
    private String[] fromResultSet(final ResultSet rs) throws SQLException
    {
        final String User_ID = rs.getString("userID");
        final String User_Name = rs.getString("name");
        final String  User_pass = rs.getString("passwd");

        return new String[]{User_ID, User_Name, User_pass};
    }

    // ############### returns list of all users ######################
    @Override
    public List<String[]> get_user_list()
    {
        final List<String[]> users = new ArrayList<>();
        
        try (final Connection c = connection()) 
        {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM user");

            // loop until all users have been obtained
            while ( rs.next() ) 
            {
                final String[] usr = fromResultSet(rs);
                users.add(usr);
            }
            rs.close();
            st.close();

            return users;
        } 
        catch (final SQLException e) 
        {
            throw new PersistenceException(e);
        }
    }

    // ################ Insert new User into db ####################### 
    @Override
    public String[] insert_user( String[] usr )
    {
        try (final Connection c = connection()) 
        {
            final PreparedStatement st = c.prepareStatement(
                "INSERT INTO user VALUES(?, ?, ?)");

            // the user info
            st.setString( 1, usr[0] );
            st.setString( 2, usr[1] );
            st.setString( 3, usr[2] );
            st.executeUpdate();
            
            return usr;
        } 
        catch (final SQLException e) 
        {
            throw new PersistenceException(e);
        }
    }

    // ####################### Update User ############################
    @Override
    public String[] update_user(String[] usr)
    {
        try (final Connection c = connection()) 
        {
            final PreparedStatement st = c.prepareStatement(
                "UPDATE user SET name = ?, passwd = ? WHERE userID = ?");

            // the user info
            st.setString( 1, usr[0] );
            st.setString( 2, usr[1] );
            st.setString( 3, usr[2] );
            st.executeUpdate();
            
            return usr;
        } 
        catch (final SQLException e) 
        {
            throw new PersistenceException(e);
        }
    }


    // ##################### Delete User ##############################
    @Override
    public void delete_user(String[] usr)
    {
        try ( final Connection c = connection() ) 
        {
            final PreparedStatement sc = c.prepareStatement(
                "DELETE FROM user WHERE userID = ?");
            
            sc.setString( 1, usr[0] );
            sc.executeUpdate();
        } 
        catch (final SQLException e) 
        {
            throw new PersistenceException(e);
        }
    }
} 
// ############################ EOF ###################################