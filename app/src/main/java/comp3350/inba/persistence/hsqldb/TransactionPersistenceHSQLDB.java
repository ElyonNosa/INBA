package comp3350.inba.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;
import comp3350.inba.persistence.TransactionPersistence;

public class TransactionPersistenceHSQLDB implements TransactionPersistence {

    private final String dbPath;

    public TransactionPersistenceHSQLDB(final String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    /**
     * Get information from a transaction.
     * @param rs ResultSet
     * @return String array of information.
     * @throws SQLException
     */
    private String[] fromResultSet(final ResultSet rs) throws SQLException {
        String[] output = new String[4];
        output[0] = rs.getString("key");
        output[1] = rs.getString("time");
        output[2] = rs.getString("price");
        output[3] = rs.getString("category");

        return output;
    }

    @Override
    public List<Transaction> getTransactionList(User currUser) {
        final List<Transaction> transactions = new ArrayList<>();
        final int TIMESTAMP_LEN = 23;
        String[] args;
        String keyUserID;

        try (final Connection c = connection()) {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM transactions");
            while (rs.next()) {
                args = fromResultSet(rs);
                // obtain the userid from the key
                keyUserID = args[0].substring(0, args[0].length() - TIMESTAMP_LEN);
                // check if the user id matches
                if (currUser.getUserID().equals(keyUserID)) {
                    // add the transaction to the list
                    transactions.add(new Transaction(LocalDateTime.parse(args[1]),
                            Double.parseDouble(args[2]), args[3]));
                }
            }
            rs.close();
            st.close();

            return transactions;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }

    }

    @Override
    public Transaction insertTransaction(User currUser, Transaction currentTransaction) {

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("INSERT INTO transactions VALUES(?, ?, ?, ?)");
            st.setString(1, currUser.getUserName() + currentTransaction.getTime().toString());
            st.setString(2, currentTransaction.getCategory());
            st.setString(3, currentTransaction.getTime().toString());
            st.setString(4, "" + currentTransaction.getPrice());

            st.executeUpdate();

            return currentTransaction;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Transaction updateTransaction(User currUser, Transaction currentTransaction) {

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("UPDATE transactions SET price = ?, category = ? WHERE key = ?");
            st.setString(1, "" + currentTransaction.getPrice());
            st.setString(2, currentTransaction.getCategory());
            st.setString(3, currUser.getUserName() + currentTransaction.getTime().toString());

            st.executeUpdate();

            return currentTransaction;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void deleteTransaction(User currUser, Transaction currentTransaction) {
        try (final Connection c = connection()) {
            final PreparedStatement sc = c.prepareStatement("DELETE FROM transactions WHERE key = ?");
            sc.setString(1, currUser.getUserName() + currentTransaction.getTime().toString());
            sc.executeUpdate();
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
