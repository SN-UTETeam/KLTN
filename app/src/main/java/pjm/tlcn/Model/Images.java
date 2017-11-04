package pjm.tlcn.Model;

/**
 * Created by thienphu on 10/31/2017.
 */

public class Images {
    private String Id;
    private String UseId;
    private String Status;
    private String Comment;
    private String Imgurl;
    public Images( String Id, String Imgurl){
        this.Id=Id;
        this.Imgurl=Imgurl;


    }

    public Images() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUseId() {
        return UseId;
    }

    public void setUseId(String useId) {
        UseId = useId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String imgurl) {
        Imgurl = imgurl;
    }
}
