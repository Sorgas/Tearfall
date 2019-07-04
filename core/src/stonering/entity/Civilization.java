package stonering.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Represents civilization on world map.
 *
 * @author Alexander on 30.09.2018.
 */
public class Civilization {
    private String title;
    private List<Vector2> worldCells;
    private List<City> cities;
    private City capital;

    public void turn() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Vector2> getWorldCells() {
        return worldCells;
    }

    public void setWorldCells(List<Vector2> worldCells) {
        this.worldCells = worldCells;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public City getCapital() {
        return capital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
    }
}
