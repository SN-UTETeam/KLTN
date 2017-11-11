package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/11/2017.
 */

public class Chat {
    private String room_id;
    private String user_id;

    public Chat(String room_id, String user_id) {
        this.room_id = room_id;
        this.user_id = user_id;
    }

    public Chat() {
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
