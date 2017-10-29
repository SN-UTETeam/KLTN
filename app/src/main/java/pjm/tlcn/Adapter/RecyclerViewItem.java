package pjm.tlcn.Adapter;

/**
 * Created by thienphu on 10/27/2017.
 */

public class RecyclerViewItem {
    private int drawableId;
    private String name;

    public RecyclerViewItem(int drawableId, String name) {
        this.drawableId = drawableId;
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getName() {
        return name;
    }
}
