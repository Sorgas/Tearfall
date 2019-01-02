package stonering.screen.util;

/**
 * @author Alexander Kuzyakov on 28.05.2017.
 */
public class WorldCellInfo {
    public String getCellInfo(int x, int y, int elevation, float temperature, float rainfall) {
        String info = "x: " + x + " y: " + y + "\n";
        info += "elevation: " + elevation + "\n";
        info += "temp(year): " + temperature + "\n";
        info += "rainfall: " + rainfall + "\n";
        return info;
    }
}
