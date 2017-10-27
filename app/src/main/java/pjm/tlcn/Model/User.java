package pjm.tlcn.Model;

/**
 * Created by Pjm on 10/26/2017.
 */

public class User {

    private String Username;
    private String Email;
    private String PassWord;
    private Integer PhoneNumber;
    private String Describer;
    private String Avatar;

    public User() {
    }

    public User(String username, String email, String passWord, Integer phoneNumber, String describer, String avatar) {

        Username = username;
        Email = email;
        PassWord = passWord;
        PhoneNumber = phoneNumber;
        Describer = describer;
        Avatar = avatar;
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

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public Integer getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDescriber() {
        return Describer;
    }

    public void setDescriber(String describer) {
        Describer = describer;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
