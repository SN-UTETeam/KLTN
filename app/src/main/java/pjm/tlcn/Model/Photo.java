package pjm.tlcn.Model;

import java.util.List;

/**
 * Created by thienphu on 10/31/2017.
 */

public class Photo {
    private String caption;
    private String date_created;
    private List<Image_path> image_path;
    private String photo_id;
    private String user_id;
    private List<Like> likes;
    private List<Comment> comments;

    public Photo(String caption, String date_created, List<Image_path> image_path, String photo_id, String user_id, List<Like> likes, List<Comment> comments) {
        this.caption = caption;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.likes = likes;
        this.comments = comments;
    }

    public Photo() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public List<Image_path> getImage_path() {
        return image_path;
    }

    public void setImage_path(List<Image_path> image_path) {
        this.image_path = image_path;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
