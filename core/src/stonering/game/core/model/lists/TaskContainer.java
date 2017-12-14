package stonering.game.core.model.lists;

import stonering.objects.jobs.Task;

import java.util.ArrayList;

/**
 * Created by Alexander on 14.06.2017.
 */
public class TaskContainer {
    private ArrayList<Task> tasks;

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
