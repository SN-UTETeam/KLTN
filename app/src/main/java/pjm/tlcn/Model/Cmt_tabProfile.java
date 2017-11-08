package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/8/2017.
 */

public class Cmt_tabProfile {
    private String userid;
    private String comment;
    private String username;
    private String avatarurl;
    private String datetime;

    public Cmt_tabProfile(String userid, String comment, String username, String avatarurl, String datetime) {
        this.userid = userid;
        this.comment = comment;
        this.username = username;
        this.avatarurl = avatarurl;
        this.datetime = datetime;
    }

    public Cmt_tabProfile() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
