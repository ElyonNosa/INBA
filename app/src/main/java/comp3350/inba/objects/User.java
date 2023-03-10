package comp3350.inba.objects;

/**
 * This class holds information pertaining to the user
 **/
public class User {

    public static User currUser;
    public static boolean isLoggedIn = false;

    // Class Variables
    private final String userID;
    private String passwd;
    private String userName;
    private double wkdayThresh;
    private double wkendThresh;

    public User(String userName) {
        this.userName = userName;
        this.userID = userName; // will change with DB integration
        wkdayThresh = 0.0;
        wkendThresh = 0.0;
    }

    public User(String uid, String name, String passwd) {
        this.userID = uid;
        this.passwd = passwd;
        this.userName = name;
        wkdayThresh = 0.0;
        wkendThresh = 0.0;
    }

    public void setPasswd(String pw) {
        this.passwd = pw;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getUserName() {
        return userName;
    }

    public double getWkdayThresh() {
        return wkdayThresh;
    }

    public double getWkendThresh() {
        return wkendThresh;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWkdayThresh(double wkdayThresh) {
        this.wkdayThresh = wkdayThresh;
    }

    public void setWkendThresh(double wkendThresh) {
        this.wkendThresh = wkendThresh;
    }
}
