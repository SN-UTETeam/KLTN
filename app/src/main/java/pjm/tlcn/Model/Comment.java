package pjm.tlcn.Model;

/**
 * Created by thienphu on 11/5/2017.
 */

public class Comment {

    private String comment;
    private String datetime;
    private String username;

    public Comment( String comment, String datetime, String username){

        this.comment=comment;
        this.datetime=datetime;
        this.username=username;
    }

    public Comment() {
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
