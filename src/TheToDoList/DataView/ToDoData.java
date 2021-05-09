package TheToDoList.DataView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class ToDoData {
    private static ToDoData instance = new ToDoData();
    private static String filename = "datafile.txt";
    private static String fileLocation = "../../Resources/datafile.txt";
    private ObservableList<ToDoItem> itemList;
    private DateTimeFormatter formatter;

    private ToDoData(){
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static ToDoData getInstance(){
        return instance;
    }

    public ObservableList<ToDoItem> getItemList() {
        return itemList;
    }

//    public void setItemList(List<ToDoItem> itemList) {
//        this.itemList = itemList;
//    }

    public void addToDoItem(ToDoItem item){ this.itemList.add(item); }

    public void loadData() throws IOException {
        itemList = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try {
            while ((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");
                String title = itemPieces[0];
                String description = itemPieces[1];
                String deadString = itemPieces[2];
                LocalDate deadline = LocalDate.parse(deadString, formatter);

                ToDoItem item = new ToDoItem(title, description, deadline);
                itemList.add(item);
//                System.out.println(item.getTitle());
            }
        } finally {
            if(br != null){
                br.close();
            }
        }
    }

    public void storeData() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<ToDoItem> iter = itemList.listIterator();
            while(iter.hasNext()){
                ToDoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getTitle(), item.getDescription(), item.getDeadline().format(formatter)));
                bw.newLine();

            }
        } finally {
            if(bw!=null)
                bw.close();
        }
    }

}
