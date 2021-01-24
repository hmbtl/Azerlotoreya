package nms.az.azerlotereya.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anar on 7/13/15.
 */
public class Game {

    private int gameId;
    private String name, totalWinnings;
    private String lastGameDate, nextGameDate;
    private int[] lastWinners;

    private int selectedDraw;

    private double cost;
    private boolean status, drawStatus, enabled;
    private String drawNumber, drawDate, drawInfo, winnings;

    private List<Draw> draws;

    public Game() {
    }

    /*
        public Game(int gameId, String name, double cost, boolean status, boolean enabled,
                    String drawNumber, String drawDate, String drawInfo, boolean drawStatus, String winnings, int[] lastWinners) {
            this.gameId = gameId;
            this.name = name;
            this.cost = cost;
            this.status = status;
            this.enabled = enabled;
            this.drawNumber = drawNumber;
            this.drawDate = drawDate;
            this.drawInfo = drawInfo;
            this.drawStatus = drawStatus;
            this.winnings = winnings;
            this.lastWinners = lastWinners;
        }
    */
    public List<Draw> getDraws() {
        return draws;
    }

    public void setDraws(List<Draw> draws) {
        this.draws = draws;
    }

    public Game(int gameId, String name, double cost, boolean status, boolean enabled, List<Draw> draws) {
        this.gameId = gameId;
        this.name = name;
        this.cost = cost;
        this.status = status;
        this.enabled = enabled;
        this.draws = draws;
    }


    public int getSelectedDraw() {
        return selectedDraw;
    }

    public void setSelectedDraw(int selectedDraw) {
        this.selectedDraw = selectedDraw;
    }

    public int[] getLastWinners() {
        return lastWinners;
    }

    public void setLastWinners(int[] lastWinners) {
        this.lastWinners = lastWinners;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getCost() {
        return cost;
    }

    public boolean isStatus() {
        return status;
    }


    public boolean isEnabled() {
        return enabled;
    }


    public String[] getDrawsList() {
        List<String> drawNumbers = new ArrayList<>();

        for (Draw draw : draws
                ) {
            Log.e("draw number",draw.getNumber());
            drawNumbers.add(draw.getNumber());
        }

        String[] drawString = new String[drawNumbers.size()];
        drawString = drawNumbers.toArray(drawString);

        return drawString;
    }


}
