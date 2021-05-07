package TheToDoList;

import TheToDoList.DataView.ToDoItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {
    private List<ToDoItem> toDoList;
    @FXML
    private ListView<ToDoItem> toDoItemListView;

    public void initialize(){
        toDoList = new ArrayList<>();
        toDoList.add(new ToDoItem("Submit Assignment",
                "Submit the assignment of chapter 4 of maths",
                LocalDate.of(2021, Month.MAY, 9)));
        toDoList.add(new ToDoItem("Make Project",
                "Start Building the project",
                LocalDate.of(2021, Month.MAY, 11)));
        toDoList.add(new ToDoItem("Give Contest",
                "There is a codeforces contest of division 2 you should give it.",
                LocalDate.of(2021, Month.MAY, 8)));
        toDoList.add(new ToDoItem("Write Blog",
                "Write a weblog for the blogspot account of Astrowing MNNIT.",
                LocalDate.of(2021, Month.MAY, 12)));

        toDoItemListView.getItems().setAll(toDoList);
        toDoItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    @FXML
    public void handleSelectedItem(){
        ToDoItem item = toDoItemListView.getSelectionModel().getSelectedItem();
        System.out.println("Selected Item is " + item.getTitle());
    }


}