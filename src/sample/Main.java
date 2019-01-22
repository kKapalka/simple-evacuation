package sample;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<MouseEvent> {

    Map<String,Boolean> burningBuildings = new HashMap<>();
    List<Path> evacuationPaths = new ArrayList<>();
    Map<String,List<Path>> assignedEvacuationRoutes = new HashMap<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getStylesheets().add(getClass().getResource("styles.css").toString());
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 800  , 500);
        evacuationPaths = (((Pane) root.getChildrenUnmodifiable().get(0)).getChildren()
                .filtered(node->node.getClass()== Path.class).stream().map(object->(Path)object).collect(Collectors.toList()));
        evacuationPaths.forEach(path->path.setVisible(false));
        ((Pane) root.getChildrenUnmodifiable().get(0)).getChildren()
                .filtered(node->node.getClass()== Button.class)
                .forEach(button->{
                    burningBuildings.put(button.getId(),false);
                    assignedEvacuationRoutes.put(button.getId(),evacuationPaths.stream().filter(path->path.getId().contains(button.getId())).collect(Collectors.toList()));
                    button.setOnMouseClicked(this);
                    System.out.println(button.getId()+" - "+assignedEvacuationRoutes.get(button.getId()).size());
                });
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Button room = (Button) mouseEvent.getSource();
        if(room.getStyleClass().contains("fire")){
            room.getStyleClass().remove("fire");
        } else{
            room.getStyleClass().add("fire");
        }
        burningBuildings.replace(room.getId(),!burningBuildings.get(room.getId()));
        toggleEvacuationPaths();
    }
    private void toggleEvacuationPaths(){
        if(burningBuildings.containsValue(true)){
            evacuationPaths.forEach(path->path.setVisible(false));
            if(burningBuildings.get("room1")){
                displayEvacuationPaths(new int[]{0,1,2,1,1});
            }
            if(burningBuildings.get("room2")){
                displayEvacuationPaths(new int[]{1,0,2,1,1});
            }
            if(burningBuildings.get("room3")){
                displayEvacuationPaths(new int[]{1,1,0,1,1});
            }
            if(burningBuildings.get("room4")){
                displayEvacuationPaths(new int[]{1,1,2,0,1});
            }
            if(burningBuildings.get("room5")){
                displayEvacuationPaths(new int[]{1,1,2,1,0});
            }
        } else{
            evacuationPaths.forEach(path->path.setVisible(false));

        }
    }
    private void displayEvacuationPaths(int[] evacuationPathIds){
        for(int i=0;i<evacuationPathIds.length;i++){
            if(!burningBuildings.get("room"+(i+1))){
                assignedEvacuationRoutes.get("room"+(i+1)).get(evacuationPathIds[i]-1).setVisible(true);
            }
        }
    }
}
