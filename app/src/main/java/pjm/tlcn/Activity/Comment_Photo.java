package pjm.tlcn.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by My PC on 4/12/2017.
 */

public class Comment_Photo {
    private int id;
    private  String name;
    private double rating;
    private  String comment;
    private  String commenttrim;
    private  String avatar;
    private  int itemid;
    private  String reviewurl;

    public Comment_Photo(JSONObject object){
        try {
            this.id = object.getInt("id");
            this.name = object.getString("name");
            this.rating=object.getDouble("rating");
            this.comment=object.getString("comment");
            this.commenttrim=object.getString("commenttrim");
            this.avatar=object.getString("avater");
            this.itemid=object.getInt("itemid");
            this.reviewurl=object.getString("reviewurl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Comment_Photo (int id, String name, double rating, String comment, String commenttrim, String avatar
    , int itemid, String reviewurl) {
        this.id = id;
        this.name=name;
        this.rating=rating;
        this.comment=comment;
        this.commenttrim=commenttrim;
        this.avatar=avatar;
        this.itemid=itemid;
        this.reviewurl=reviewurl;
    }

    //getter and setter
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenttrim() {
        return commenttrim;
    }

    public void setCommenttrim(String commenttrim) {
        this.commenttrim = commenttrim;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getReviewurl() {
        return reviewurl;
    }

    public void setReviewurl(String reviewurl) {
        this.reviewurl = reviewurl;
    }
}
