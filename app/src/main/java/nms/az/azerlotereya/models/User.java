package nms.az.azerlotereya.models;

/**
 * Created by anar on 9/14/15.
 */
public class User {


    private int userId;
    private String email, name, surname, birthdate, passportId, mobile, pincode, address, father;
    private double balance, balanceWithdraw;


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(int userId, String email, String name, String surname, String father, String birthdate, String passportId, String mobile, String pincode, double balance, double balanceWithdraw, String address) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.father = father;
        this.surname = surname;
        this.birthdate = birthdate;
        this.passportId = passportId;
        this.mobile = mobile;
        this.pincode = pincode;
        this.balance = balance;
        this.balanceWithdraw = balanceWithdraw;
        this.address = address;

    }


    public void setFather(String father) {
        this.father = father;
    }

    public String getFather() {
        return father;
    }

    public double getBalanceWithdraw(){
        return balanceWithdraw;
    }

    public void setBalanceWithdraw(double balanceWithdraw){
        this.balanceWithdraw = balanceWithdraw;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getFullName() {
        return this.name + " " + this.surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
