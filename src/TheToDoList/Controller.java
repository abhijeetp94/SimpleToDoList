package TheToDoList;

import TheToDoList.DataView.ToDoData;
import TheToDoList.DataView.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {
    private List<ToDoItem> toDoList;
    @FXML
    private ListView<ToDoItem> toDoItemListView;
    @FXML
    private TextArea selectedTextArea;
    @FXML
    private Label dueDateLabel;
    @FXML
    private Button editDetails, successDetails, cancelDetails;

    public void initialize(){                                            // Initialize method to initialize the application
        toDoList = ToDoData.getInstance().getItemList();
//        toDoList.add(new ToDoItem("Submit Assignment",
//                "Submit the assignment of chapter 4 of maths",
//                LocalDate.of(2021, Month.MAY, 9)));
//        toDoList.add(new ToDoItem("Make Project",
//                "Start Building the project",
//                LocalDate.of(2021, Month.MAY, 11)));
//        toDoList.add(new ToDoItem("Give Contest",
//                "There is a codeforces contest of division 2 you should give it.",
//                LocalDate.of(2021, Month.MAY, 8)));
//        toDoList.add(new ToDoItem("Write Blog",
//                "Write a weblog for the blogspot account of Astrowing MNNIT.",
//                LocalDate.of(2021, Month.MAY, 12))); // Hard Coded info
//        dueDateLabel.setText("NA");
//        toDoItemListView.getItems().setAll(toDoList);
//        ToDoData.getInstance().setItemList(toDoList);
//        ToDoData.getInstance().loadData();


        // =============================================================================================================================================
        toDoItemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observableValue, ToDoItem toDoItem, ToDoItem t1) { // Custom Listener for List View Selection
                ToDoItem item = toDoItemListView.getSelectionModel().getSelectedItem();
                if(item != null){
                    selectedTextArea.setText(item.getDescription());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dueDateLabel.setText(df.format(item.getDeadline())); // setting the text for selected Item
                    editDetails.setDisable(false);
                }
            }
        });
        // =============================================================================================================================================

        toDoItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoItemListView.getItems().setAll(ToDoData.getInstance().getItemList());
        editDetails.setDisable(true);
        toDoItemListView.getSelectionModel().selectFirst();  // Select the first Item while opening
    }


    @FXML
    public void onButtonClicked(ActionEvent e){
        if (e.getSource().equals(editDetails)){                // Event Handler or Listener for edit details button
            selectedTextArea.setEditable(true);
            editDetails.setVisible(false);
            successDetails.setVisible(true);
            cancelDetails.setVisible(true);
        }
        if (e.getSource().equals(successDetails)){             // Event Handler or Listener for Done button
            String s = selectedTextArea.getText();
            toDoItemListView.getSelectionModel().getSelectedItem().setDescription(s);
            int index = toDoList.indexOf(toDoItemListView.getSelectionModel().getSelectedItem());
            toDoList.get(index).setDescription(s);
            selectedTextArea.setEditable(false);
            editDetails.setVisible(true);
            successDetails.setVisible(false);
            cancelDetails.setVisible(false);
        }
        if (e.getSource().equals(cancelDetails)){               // Event Handler or Listener for Cancel button
            selectedTextArea.setText(toDoItemListView.getSelectionModel().getSelectedItem().getDescription());
            selectedTextArea.setEditable(false);
            editDetails.setVisible(true);
            successDetails.setVisible(false);
            cancelDetails.setVisible(false);
        }

    }


}