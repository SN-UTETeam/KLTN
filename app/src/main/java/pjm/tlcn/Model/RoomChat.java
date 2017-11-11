package pjm.tlcn.Model;

/**
 * Created by Pjm on 11/11/2017.
 */

public class RoomChat {
    private String room_id;

    public RoomChat(String room_id) {
        this.room_id = room_id;
    }

    public RoomChat() {
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
