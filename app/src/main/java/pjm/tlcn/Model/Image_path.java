package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/19/2017.
 */

public class Image_path {
    private String type;
    private String path;

    public Image_path() {
    }

    public Image_path(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
