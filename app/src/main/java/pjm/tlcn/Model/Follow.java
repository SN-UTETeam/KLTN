package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/1/2017.
 */

public class Follow {
    private String userid;
    private String username;


    public Follow(String userid, String username) {
        this.userid = userid;
        this.username = username;
    }
    public Follow() {
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
