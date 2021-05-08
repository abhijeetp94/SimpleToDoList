package TheToDoList;

import TheToDoList.DataView.ToDoData;
import TheToDoList.DataView.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class newItemController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descField;
    @FXML
    private DatePicker deadlineField;
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern();

    public ToDoItem processResults(){          // processing the instructions typed
        String title = titleField.getText().trim();
        String description = descField.getText().trim();
        LocalDate deadline = deadlineField.getValue();
        ToDoItem item = new ToDoItem(title, description, deadline);

        ToDoData.getInstance().addToDoItem(item);
        return item;
    }
}
