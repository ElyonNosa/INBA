package comp3350.inba.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import comp3350.inba.R;
import comp3350.inba.objects.User;

/**
 * LoginActivity.java
 * The page where the user logs in.
 * This class is coupled with activity_login.xml
 */
public class LoginActivity extends Activity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    // instance of user
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = new User(getApplicationContext());

        // obtain the text boxes for user credentials
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

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
                    user.setUserid(username);
                    user.setUserName(username);

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

    private boolean isValidCredentials(String username, String password) {
        // Validate the user's credentials
        // Return true if the username and password are correct, false otherwise
        return true;
    }

    private void saveLoginStatus() {
        // Save the user's login status in shared preferences or local database
        user.setLoginStatus(true);
    }
}
