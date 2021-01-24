package nms.az.azerlotereya.models;

import java.util.Date;

/**
 * Created by anar on 9/9/15.
 */
public class Ticket {

    private int gameId, status;
    private String ticketNumber, drawNumber, createdAt, barcode;
    private double cost, winning;
    private int numbers[][];

    public Ticket() {
    }

    public Ticket(String ticketNumber, int numbers[][], int gameId, String drawNumber
            , String createdAt, String barcode, double cost, double winning) {

        this.ticketNumber = ticketNumber;
        this.gameId = gameId;
        this.numbers = numbers;
        this.drawNumber = drawNumber;
        this.cost = cost;
        this.createdAt = createdAt;
        this.winning = winning;

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(String drawNumber) {
        this.drawNumber = drawNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getWinning() {
        return winning;
    }

    public void setWinning(double winning) {
        this.winning = winning;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }
}
