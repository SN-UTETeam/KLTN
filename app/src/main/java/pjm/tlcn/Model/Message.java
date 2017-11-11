package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/11/2017.
 */

public class Message {
    private String user_id;
    private String user_avatar;
    private String message;
    private String datecreated;
    private String image_url;

    public Message(String user_id, String user_avatar, String message, String datecreated, String image_url) {
        this.user_id = user_id;
        this.user_avatar = user_avatar;
        this.message = message;
        this.datecreated = datecreated;
        this.image_url = image_url;
    }

    public Message() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
