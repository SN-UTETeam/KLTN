package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/20/2017.
 */

public class Item_GridPhoto {
    private String photo_id;
    private String path;

    public Item_GridPhoto(String photo_id, String path) {
        this.photo_id = photo_id;
        this.path = path;
    }

    public Item_GridPhoto() {
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
