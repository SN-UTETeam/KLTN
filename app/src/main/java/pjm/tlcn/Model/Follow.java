package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/1/2017.
 */

public class Follow {
    private String user_id;



    public Follow(String user_id) {
        this.user_id = user_id;

    }
    public Follow() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
