package pjm.tlcn.Model;

/**
 * Created by Pjm on 10/26/2017.
 */

public class User {
    private String Id;
    private String Username;
    private String Email;
    private String Password;
    private String Phonenumber;
    private String Describer;
    private String Avatarurl;

    public User(String id, String username, String email, String password, String phonenumber, String describer, String avatarurl) {
        Id = id;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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


