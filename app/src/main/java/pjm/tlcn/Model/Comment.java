package pjm.tlcn.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thienphu on 11/5/2017.
 */

public class Comment implements Parcelable{

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

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", user_id='" + user_id + '\'' +
                ", date_created='" + date_created + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(user_id);
        dest.writeString(date_created);

    }

    protected Comment(Parcel in){
        comment = in.readString();
        user_id = in.readString();
        date_created = in.readString();

    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
