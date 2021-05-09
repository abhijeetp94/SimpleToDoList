package TheToDoList;

import TheToDoList.DataView.ToDoData;
import TheToDoList.DataView.ToDoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class Controller {
    private ObservableList<ToDoItem> toDoList;
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
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleButton filterButton;

    private FilteredList<ToDoItem> filteredList;
    private Predicate<ToDoItem> getAllItems, getTodayItems;

    public void initialize(){                                            // Initialize method to initialize the application
        contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Edit");
        toDoList = ToDoData.getInstance().getItemList();
        contextMenu.getItems().setAll(deleteItem, editItem);

        // event handler for delete and Edit item
        // =============================================================================================================================================
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ToDoItem item = toDoItemListView.getSelectionModel().getSelectedItem();
                deleteSelectedItem(item);
            }
        });
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ToDoItem item = toDoItemListView.getSelectionModel().getSelectedItem();
                handleEditItem(item);
            }
        });
        // =============================================================================================================================================


        // event handler for selected item
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

        // predicate to get all available items
        getAllItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return true;
            }
        };
        // predicate to get item due today
        getTodayItems = new Predicate<ToDoItem>() {
            @Override
            public boolean test(ToDoItem toDoItem) {
                return toDoItem.getDeadline().equals(LocalDate.now());
            }
        };
        filteredList = new FilteredList<>(toDoList, getAllItems);
        SortedList<ToDoItem> sortedList = new SortedList<>(filteredList, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem o1, ToDoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });
        toDoItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoItemListView.setItems(sortedList);
        editDetails.setDisable(true);
        toDoItemListView.getSelectionModel().selectFirst();  // Select the first Item while opening

        // custom cell factory for the list view
        // ==============================================================================================================================================
        toDoItemListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> toDoItemListView) {
                ListCell<ToDoItem> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(ToDoItem toDoItem, boolean b) {
                        super.updateItem(toDoItem, b);
                        if(b){
                            setText(null);
//                            setBackground(super.getBackground());
                        }
                        else {
                            setText(toDoItem.getTitle());
                            setFont(new Font("Times New Roman", 16));
                            if(toDoItem.getDeadline().compareTo(LocalDate.now()) <= 0 && !(isEmpty())){
                                setTextFill(Color.RED);
//                                setTextFill(Color.WHITE);setFont(new Font("Times New Roman bold"));
                            } else if(toDoItem.getDeadline().equals(LocalDate.now().plusDays(1)) && !(isEmpty()) ){
                                setTextFill(Color.DEEPPINK);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isEmpty) -> {
                            if(isEmpty){
                                cell.setContextMenu(null);
                            }
                            else {
                                cell.setContextMenu(contextMenu);
                            }
                        }
                );
                return cell;
            }
        });
        // ==============================================================================================================================================
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
            toDoItemListView.getSelectionModel().select(item);  // selecting the newly added item
        }

    }

    // method to delete the selected item
    public void deleteSelectedItem(ToDoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Delete: " + item.getTitle());
        alert.setContentText("Are you sure?");

        Optional<ButtonType> res = alert.showAndWait();
        if(res.isPresent() && res.get()==ButtonType.OK){
            toDoList.remove(item);
        }
    }

    // handle delete key pressed
    public void handleKeyPressed(KeyEvent e){
        if(toDoItemListView.getSelectionModel().getSelectedItem() != null){
            if(e.getCode() == KeyCode.DELETE){
                ToDoItem item = toDoItemListView.getSelectionModel().getSelectedItem();
                deleteSelectedItem(item);
            }
        }
    }

    // event handler for filtering the list
    public void handleFilterButton(){
        ToDoItem item = toDoItemListView.getSelectionModel().getSelectedItem();
        if(filterButton.isSelected()){
            filteredList.setPredicate(getTodayItems);
            if(filteredList.isEmpty()){
                selectedTextArea.clear();
                dueDateLabel.setText("NA");
            }
            if(filteredList.contains(item)){
                toDoItemListView.getSelectionModel().select(item);
            }
            else{
                toDoItemListView.getSelectionModel().selectFirst();
            }
        }
        else {
            filteredList.setPredicate(getAllItems);
            toDoItemListView.getSelectionModel().select(item);
        }
    }

    // event handler to exit the button
    public void handleExit(){
        Platform.exit();
    }

    // edit items
    public void handleEditItem(ToDoItem item){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPageBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Item");
        dialog.getDialogPane().setHeaderText("Edit Item");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newItemDialog.fxml"));
        try {

            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException ie){
            ie.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        newItemController controller = loader.getController();
        controller.setData(item);
        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get()==ButtonType.OK){
            ToDoItem updatedItem = controller.updateResults();
            int index = toDoList.indexOf(item);
            toDoList.set(index, updatedItem);

        }

    }

}