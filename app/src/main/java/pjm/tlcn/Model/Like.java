package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/10/2017.
 */

public class Like {
    private String user_id;

    public Like(String user_id) {
        this.user_id = user_id;
    }

    public Like() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
