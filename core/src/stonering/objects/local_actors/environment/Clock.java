package stonering.objects.local_actors.environment;

import stonering.game.core.model.Turnable;

/**
 * @author Alexander on 07.10.2018.
 */
public class Clock implements Turnable {
    private int time = 0;
    private int timeScale = 5;
    private int riseTime = 480;
    private int setTime = 1080;
    private int dayLength = 1560;
    private int tickCounter = 0;

    private int year;
    private int month;
    private int day;


    @Override
    public void turn() {
        tickCounter++;
        if(tickCounter >= timeScale) {
            tickCounter = 0;
            time++;
            if(time >= dayLength) {
                time = 0;
            }
            if(time == riseTime) {
                System.out.println("sunrise ---------------------------");
            }
            if(time == setTime) {
                System.out.println("sunset ---------------------------");
            }
        }
    }

    private void doSunrize() {

    }

    private void changeDate() {

    }

    public int getTime() {
        return time;
    }

    public int getRiseTime() {
        return riseTime;
    }

    public int getSetTime() {
        return setTime;
    }

    public void init() {
        //TODO
    }


}
