package nms.az.azerlotereya.models;

/**
 * Created by anar on 10/10/15.
 */
public class Template {

    private int id, gameId;
    private String name, date;
    private int[][] numbers;

    public Template() {
    }

    public Template(int id, String name, int[][] numbers, int gameId, String date) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.date = date;
        this.numbers = numbers;


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }
}
