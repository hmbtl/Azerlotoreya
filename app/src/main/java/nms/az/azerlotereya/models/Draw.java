package nms.az.azerlotereya.models;

/**
 * Created by anar on 2/17/16.
 */
public class Draw {

    private String number, date, info, time;
    private boolean status;
    private String winnings;

    public Draw() {

    }

    public Draw(String number, String date, String time, String info, String winnings, boolean status) {
        this.number = number;
        this.date = date;
        this.info = info;
        this.time = time;
        this.winnings = winnings;
        this.status = status;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getWinnings() {
        return winnings;
    }

    public void setWinnings(String winnings) {
        this.winnings = winnings;
    }
}
