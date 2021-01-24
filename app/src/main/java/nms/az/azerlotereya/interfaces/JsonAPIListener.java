package nms.az.azerlotereya.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anar on 12/22/15.
 */
public interface JsonAPIListener {

    void onNull();

    void onSuccess();

    void onError();

    void onData(JSONObject json) throws JSONException;


}