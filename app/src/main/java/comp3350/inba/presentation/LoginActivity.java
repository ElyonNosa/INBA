package comp3350.inba.presentation;

import static comp3350.inba.objects.User.currUser;
import static comp3350.inba.objects.User.isLoggedIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import comp3350.inba.R;
import comp3350.inba.business.AccessUsers;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

public class LoginActivity extends Activity {
    //the users "database"
    private AccessUsers accessUsers;
    // the local list of users after retrieving from the "database"
    private List<User> usersList;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        accessUsers = new AccessUsers();
        usersList = accessUsers.getUsers();

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate the user's credentials
                if (isValidCredentials(username, password)) {
                    // Save the user's login status
                    saveLoginStatus();
                    // set current user using credentials
                    User.currUser = new User(username, "", password);

                    // Save the new curr user
                    currUser = new User(username);

                    // Start the MainActivity and finish the LoginActivity
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * validate the login credentials of an existing user
     * @param username The username to be validated.
     * @param password The password to be validated
     * @return true if the username and password are correct, false otherwise.
     */
    private boolean isValidCredentials(String username, String password) {

        for(User currUser : usersList){
            if(currUser.getUserName().equals(username) && currUser.getPasswd().equals(password)){
                return true;
            }
        }


        return false;
    }

    private void saveLoginStatus() {
        // Save the user's login status in shared preferences or local database
        isLoggedIn = true;

    }
}
