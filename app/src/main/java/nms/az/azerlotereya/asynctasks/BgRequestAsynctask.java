package nms.az.azerlotereya.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

import nms.az.azerlotereya.R;
import nms.az.azerlotereya.customviews.ProgressCustom;
import nms.az.azerlotereya.interfaces.JsonAPIListener;
import nms.az.azerlotereya.tools.Constants;
import nms.az.azerlotereya.tools.JSONParser;
import nms.az.azerlotereya.tools.Utilities;

/**
 * Created by anar on 12/21/15.
 */
public class BgRequestAsynctask extends AsyncTask<String, String, String> {


    private JsonAPIListener callback;

    private ProgressCustom pDialog;
    private HashMap<String, String> params;
    private String url, method;
    private String message;
    private Activity context;
    private boolean isDialog = true;


    public BgRequestAsynctask(Activity context, String method, String url,
                              HashMap<String, String> params, JsonAPIListener cb) {
        this.params = params;
        this.context = context;
        this.url = url;
        this.method = method;
        this.callback = cb;
    }

    public BgRequestAsynctask(Activity context, String method, String url,
                              HashMap<String, String> params, boolean isDialog, JsonAPIListener cb) {
        this.params = params;
        this.context = context;
        this.url = url;
        this.method = method;
        this.isDialog = isDialog;
        this.callback = cb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        url = Constants.BASE_URL + url;

        if(isDialog) {
            pDialog = new ProgressCustom(context);
            pDialog.show();
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        JSONParser jsonParser = new JSONParser();
        String status = null;

        try {

            JSONObject json = jsonParser.sendRequest(method, url, params);

            //Log.e("jsonResult", json.toString());

            if (json == null) {
                return null;
            }

            status = json.getString("status");

            if (status.equals("success") && json.has("data")) {
                JSONObject data = json.getJSONObject("data");
                if (callback != null)
                    callback.onData(data);
            }

            if (status.equals("error"))
                message = json.getString("message");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        if(isDialog){
            pDialog.dismiss();
        }

        // If not connected to server
        if (status == null) {
            Utilities.showMessageToast(context, R.string.internet_connection_error);

            if (callback != null)
                callback.onNull();

        } else {
            if (status.equals("success")) {
                if (callback != null)
                    callback.onSuccess();

            } else {
                //Utilities.showMessageToast(context, message);
                if (callback != null)
                    callback.onError();
            }
        }
    }

}