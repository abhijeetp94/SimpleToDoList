package TheToDoList;

import TheToDoList.DataView.ToDoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.getIcons().add(new Image("./Resources/tea.png"));
        primaryStage.setTitle("The To Do List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try{
            ToDoData.getInstance().storeData();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {
        try {
            ToDoData.getInstance().loadData();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}
