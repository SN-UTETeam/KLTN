package pjm.tlcn.Model;

/**
 * Created by thienphu on 11/5/2017.
 */

public class Comment {

    private String comment;
    private String date_created;
    private String user_id;

    public Comment( String comment, String date_created, String user_id){

        this.comment=comment;
        this.date_created=date_created;
        this.user_id=user_id;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
