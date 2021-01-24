package nms.az.azerlotereya.models;

/**
 * Created by anar on 11/22/16.
 */

public class Transaction {

    private String date;
    private int method, type;
    private double amount;
    private String orderNumber, cardNumber;


    public Transaction(){

    }

    public Transaction(int type, int method, double amount, String orderNumber, String cardNumber,String date){
        this.type = type;
        this.method = method;
        this.amount = amount;
        this.date = date;
        this.orderNumber = orderNumber;
        this.cardNumber = cardNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
