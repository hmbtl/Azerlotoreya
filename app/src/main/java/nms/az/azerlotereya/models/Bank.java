package nms.az.azerlotereya.models;

/**
 * Created by anar on 10/19/17.
 */

public class Bank {

    private int id;
    private String name;

    public Bank(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
