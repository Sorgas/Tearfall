package stonering.menu.utils;

/**
 * Created by Alexander on 28.05.2017.
 */
public class WorldCellInfo {
    public String getCellInfo(int x, int y, int elevation, float temperature) {
        String info = "x: " + x + " y: " + y + "\n";
        info += "elevation: " + elevation + "\n";
        info += "temp(year): " + temperature + "\n";
        return info;
    }

}
