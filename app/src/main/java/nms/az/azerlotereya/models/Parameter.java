package nms.az.azerlotereya.models;

/**
 * Created by anar on 9/13/15.
 */
public class Parameter {

    private String key, value;

    public Parameter() {

    }

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
