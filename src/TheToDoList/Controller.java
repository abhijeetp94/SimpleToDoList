package TheToDoList;

import TheToDoList.DataView.ToDoData;
import TheToDoList.DataView.ToDoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @FXML
    private BorderPane mainPageBorderPane;

    public void initialize(){                                            // Initialize method to initialize the application
        toDoList = ToDoData.getInstance().getItemList();

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
    public void onButtonClicked(ActionEvent e){      // Button Click Listener
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

    public void handleNewItem(){                             // Event handler for adding new items
        Dialog<ButtonType> dialog = new Dialog<>();          // a dialog class to pop up a modal
        dialog.initOwner(mainPageBorderPane.getScene().getWindow()); // set the dialog in the scene
        dialog.setTitle("New Item");
        FXMLLoader fxmlLoader = new FXMLLoader();   // new way to load fxml file a better way
        fxmlLoader.setLocation(getClass().getResource("newItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());  // load the file
        } catch (IOException e){
            System.out.println("Cannot open dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);   // add two new buttons in the modal
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();          // Show and wait opens up the dialog and restricts user from interacting with other UI
        if(result.isPresent() && (result.get() == ButtonType.OK)){   // Handle if Ok button is pressed
            newItemController controller = fxmlLoader.getController();
            ToDoItem item = controller.processResults();  // processing the result from the dialog controller
            toDoItemListView.getItems().setAll(toDoList);   // setting newly added item to list view
//            toDoItemListView.getItems().indexOf(item);
            toDoItemListView.getSelectionModel().select(item);  // selecting the newly added item
            System.out.println("Okay button Pressed");
        }
        else if(result.isPresent() && (result.get() == ButtonType.CANCEL)){
            System.out.println("Cancel button Pressed");
        }
    }


}