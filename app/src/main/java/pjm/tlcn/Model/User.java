package pjm.tlcn.Model;

/**
 * Created by Pjm on 10/26/2017.
 */

public class User {
    private String Id;
    private String Username;
    private String Email;
    private String PassWord;
    private String PhoneNumber;
    private String Describer;
    private String AvatarUri;

    public User() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public User(String id, String username, String email, String password, String phonenumber, String describer, String avataruri) {
        Id=id;
        Username = username;
        Email = email;
        PassWord = password;
        PhoneNumber = phonenumber;
        Describer = describer;
        AvatarUri = avataruri;
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

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String password) {
        PassWord = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phonenumber) {
        PhoneNumber = phonenumber;
    }

    public String getDescriber() {
        return Describer;
    }

    public void setDescriber(String describer) {
        Describer = describer;
    }

    public String getAvatar() {
        return AvatarUri;
    }

    public void setAvatar(String avatar) {
        AvatarUri = avatar;
    }

}
