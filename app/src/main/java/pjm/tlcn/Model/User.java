package pjm.tlcn.Model;

/**
 * Created by Pjm on 10/26/2017.
 */

public class User {
    private String User_id;
    private String Username;
    private String Email;
    private String Password;
    private String Phonenumber;
    private String Describer;
    private String Avatarurl;

    public User(String user_id, String username, String email, String password, String phonenumber, String describer, String avatarurl) {
        User_id = user_id;
        Username = username;
        Email = email;
        Password = password;
        Phonenumber = phonenumber;
        Describer = describer;
        Avatarurl = avatarurl;
    }

    public User() {
        ////
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getDescriber() {
        return Describer;
    }

    public void setDescriber(String describer) {
        Describer = describer;
    }

    public String getAvatarurl() {
        return Avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        Avatarurl = avatarurl;
    }
}


