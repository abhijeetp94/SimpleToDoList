package TheToDoList.DataView;

import java.time.LocalDate;

public class ToDoItem {
    private String title;
    private String description;
    private LocalDate deadline;

    public ToDoItem(String title, String description, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
}
