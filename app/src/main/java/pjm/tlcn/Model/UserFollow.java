package pjm.tlcn.Model;

/**
 * Created by thienphu on 11/9/2017.
 */

public class UserFollow {

    private String Username;

    private String Avatarurl;
    private String User_id;

    public UserFollow(String user_id,String username, String avatarurl) {
        this.User_id =user_id;
        this.Username = username;
        this.Avatarurl = avatarurl;
    }

    public UserFollow() {

    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAvatarurl() {
        return Avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        Avatarurl = avatarurl;
    }
}
