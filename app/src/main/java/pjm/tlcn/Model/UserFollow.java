package pjm.tlcn.Model;

/**
 * Created by thienphu on 11/9/2017.
 */

public class UserFollow {

    private String Username;

    private String Avatarurl;

    public UserFollow(String username, String avatarurl) {

        Username = username;
        Avatarurl = avatarurl;
    }

    public UserFollow() {
        ////
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
