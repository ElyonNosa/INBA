package comp3350.inba.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import comp3350.inba.R;
import comp3350.inba.application.Service;
import comp3350.inba.business.AccessUsers;
import comp3350.inba.objects.Transaction;
import comp3350.inba.objects.User;

/**
 * LoginActivity.java
 * The page where the user logs in.
 * This class is coupled with activity_login.xml
 */
public class LoginActivity extends AppCompatActivity {
    // the local list of users after retrieving from the "database"
    private List<String[]> usersList;
    private EditText usernameEditText;
    private EditText passwordEditText;
    // instance of user
    private User user;

    SharedPreferences sharePrefs = null;

    boolean isNewAccount = false;
    AccessUsers accessUsers = new AccessUsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        copyDatabaseToDevice();
        //the users "database"
        accessUsers = new AccessUsers();
        usersList = accessUsers.getUsers();
        user = new User(getApplicationContext());

        sharePrefs = getSharedPreferences("night", 0);

        boolean booleanValue = sharePrefs.getBoolean("night_mode", true);
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // obtain the text boxes for user credentials
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        CheckBox newAccountCheckBox = findViewById(R.id.checkBox);
        // listener for new account checkbox
        newAccountCheckBox.setOnCheckedChangeListener((checkBox, b) -> {
            isNewAccount = b;
        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!isNewAccount) {
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
                } else {
                    // attempt account creation
                    if(createNewAccount(username, password)) {
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
                        Toast.makeText(LoginActivity.this, "User ID already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Check if the user already exists. If not, create new account.
     * @param username The username of the new account.
     * @param password The password of the new account.
     * @return True if insertion was a success.
     */
    private boolean createNewAccount(String username, String password) {
        boolean output = true;
        int i;
        // check to see if user id already exists
        for(i = 0; i < usersList.size() && output; ++i) {
            output = !username.equals(usersList.get(i)[0]);
        }
        // create a new account if user id is unique
        if(output) {
            String[] user = {username, username, password};
            output = accessUsers.insertUser(user) != null;
        }

        return output;
    }

    /**
     * validate the login credentials of an existing user
     *
     * @param userid   The userid to be validated.
     * @param password The password to be validated
     * @return true if the userid and password are correct, false otherwise.
     */
    private boolean isValidCredentials(String userid, String password) {
        boolean returnVal = false;
        int i = 0;
        for (i = 0; i < usersList.size() && !returnVal; ++i) {
            if (usersList.get(i)[0].equals(userid) && usersList.get(i)[2].equals(password)) {
                returnVal = true;
            }
        }

        return returnVal;
    }

    private void saveLoginStatus() {
        // Save the user's login status in shared preferences or local database
        user.setLoginStatus(true);
    }

    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            Service.setDBPathName(dataDirectory.toString() + "/" + Service.getDBPathName());

        } catch (final IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}
