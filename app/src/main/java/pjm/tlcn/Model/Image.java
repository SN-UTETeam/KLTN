package pjm.tlcn.Model;

/**
 * Created by thienphu on 10/31/2017.
 */

public class Image {

    private int comment;
    private String datetime;
    private String imgurl;
    private int likes;
    private String status;
    public Image( int comment, String datetime, String imgurl,int likes, String status){

        this.comment=comment;
        this.datetime=datetime;
        this.imgurl=imgurl;
        this.likes=likes;
        this.status=status;



    }

    public Image() {
    }


    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
