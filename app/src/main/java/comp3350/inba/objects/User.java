package comp3350.inba.objects;

/**
 * This class holds information pertaining to the user
**/
public class User {
    private String userName;
    private double wkdayThresh;
    private double wkendThresh;

    public User(){
        userName = "";
        wkdayThresh = 0.0;
        wkendThresh = 0.0;
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
