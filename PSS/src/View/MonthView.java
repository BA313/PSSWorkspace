package pss;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import java.util.Date;
import java.util.Calendar;

public class MonthView extends AbstractMenu {
    private final double HEADER_HEIGHT = 50, CELL_WIDTH = 765 / 7, CELL_HEIGHT;
    private GridPane calendar;
    private VBox[][] nodes;
    private BorderPane[][] extras;
    
    MonthView() {
        numOfWeeks = 5;
        CELL_HEIGHT = 600 / numOfWeeks;
    }
    
    MonthView(Stage stage, ArrayList<Task> tasks) {
        super();
        
        this.stage = stage;
        this.tasks = tasks;
        scene = new Scene(pane, 1280, 720);
        stage.setTitle("Month View");
        stage.setScene(scene);
        CELL_HEIGHT = 600 / numOfWeeks;
        nodes = new VBox[7][numOfWeeks + 1];
        extras = new BorderPane[7][numOfWeeks + 1];
        
        buildMenu();
        drawTasks();
    }
    
    MonthView(Stage stage, Date date, ArrayList<Task> tasks) {
        super(date);
        
        this.stage = stage;
        this.tasks = tasks;
        scene = new Scene(pane, 1280, 720);
        stage.setTitle("Month View");
        stage.setScene(scene);
        CELL_HEIGHT = 600 / numOfWeeks;
        nodes = new VBox[7][numOfWeeks + 1];
        extras = new BorderPane[7][numOfWeeks + 1];
        
        buildMenu();
        drawTasks();
    }
    
    public void buildMenu() {  
        Calendar prevMonth = (Calendar) date.clone();
        prevMonth.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
        
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        date.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH), firstDay = date.get(Calendar.DAY_OF_WEEK) - 1, dayCounter = 1, outOfRangeCounter = 1;
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        
        select.getSelectionModel().select(2);
        
        //use a gridpane for the calendar header for consistency with the calendar
        GridPane calendarHeader = new GridPane();
                    
        //label for each day of the week
        Label[] labels = {new Label("Sun"), new Label("Mon"), new Label("Tue"), new Label("Wed"), new Label("Thu"), new Label("Fri"), new Label("Sat")};

        //loop to make the header
        for(int i = 0; i < 7; i++) {
            //make a box for each day of the week
            Pane headerBox = new Pane();
            headerBox.setPrefSize(CELL_WIDTH, HEADER_HEIGHT);
            headerBox.setMinSize(CELL_WIDTH, HEADER_HEIGHT);
            headerBox.setMaxSize(CELL_WIDTH, HEADER_HEIGHT);
            headerBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray");
                    
            //attach the label for each day of the week to the center of a rectangle
            labels[i].setGraphic(headerBox);
            labels[i].setFont(Font.font("Courier", FontWeight.BOLD, 15));
            labels[i].setContentDisplay(ContentDisplay.CENTER);
                    
            //add the label to the header
            calendarHeader.add(labels[i], i, 0);
        }
                
        //gridpane to hold the calendar
        calendar = new GridPane();
                
        //loop building the calendar
        for(int i = 0; i < numOfWeeks; i++) {                    
            //make the empty cells for each day of the week
            for(int j = 0; j < 7; j++) {
              //used to make calendar look nice
                VBox dayBox = new VBox(1);
                dayBox.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                dayBox.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                dayBox.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
                dayBox.setStyle("-fx-background-color: whitesmoke; -fx-border-width: 1px; -fx-border-color: darkgray");
                BorderPane dateAndExtra = new BorderPane();
                Text extra = new Text();
                StackPane extraPane = new StackPane();
                
                extraPane.setVisible(false);
                extraPane.setStyle("-fx-background-color: lightgray");
                extraPane.getChildren().add(extra);
                StackPane.setMargin(extra, new Insets(5, 5, 5, 5));
                
                if(i == 0 && j < firstDay) {
                    Text dateText = new Text(Integer.toString(prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - firstDay + j + 1));
                    dateText.setFill(Color.GRAY);
                    dateAndExtra.setRight(dateText);
                    dateAndExtra.setLeft(extraPane);
                    
                    BorderPane.setMargin(dateText, new Insets(5, 5, 0, 0));
                    dayBox.getChildren().add(dateAndExtra);
                } else if(dayCounter > daysInMonth) {
                    Text dateText = new Text(Integer.toString(outOfRangeCounter++));
                    dateText.setFill(Color.GRAY);
                    dateAndExtra.setRight(dateText);
                    dateAndExtra.setLeft(extraPane);
                    
                    BorderPane.setMargin(dateText, new Insets(5, 5, 0, 0));
                    dayBox.getChildren().add(dateAndExtra);
                } else {
                    Text dateText = new Text(Integer.toString(dayCounter++));
                    dateAndExtra.setRight(dateText);
                    dateAndExtra.setLeft(extraPane);
                    
                    BorderPane.setMargin(dateText, new Insets(5, 5, 0, 0));
                    dayBox.getChildren().add(dateAndExtra);
                }
                
                //add design pane, and container hbox to the grid
                calendar.add(dayBox, j, i);
                nodes[i][j] = dayBox;
                extras[i][j] = dateAndExtra;
            }
        }
                
        //combine calendar and header
        VBox finalCalendar = new VBox(0);
        finalCalendar.getChildren().addAll(calendarHeader, calendar);
        finalCalendar.setAlignment(Pos.CENTER_LEFT);
                
        //build final pane
        pane.setLeft(finalCalendar);
        
        pane.requestFocus();
        
        //set alignment and margins
        BorderPane.setMargin(finalCalendar, new Insets(0.0, 0.0, 10.0, 10.0));
    }
    
    public void drawTasks() {
        for(Task task : tasks) {
            int row = (int) Math.floor((date.get(Calendar.DAY_OF_MONTH) - 1 + firstDay) / 7);
            int column = (date.get(Calendar.DAY_OF_MONTH) - 1 + firstDay) - (7 * row);
            
            if(nodes[row][column].getChildren().size() > 6) {
                StackPane stack = (StackPane) extras[row][column].getLeft();
                stack.setVisible(true);
                Text text = (Text) stack.getChildren().get(0);
                int num = 0;
                
                if(text.getText().length() > 1) {
                    num = Integer.parseInt(text.getText().substring(1));
                }
                
                num++;
                text.setText("+" + num);
            } else {
                Rectangle rect = new Rectangle(CELL_WIDTH, 10);
                rect.setFill(Color.BLUE);
                rect.setOpacity(0.2);
                Label label;
                
                if(task.getName().length() > 10) {
                    label = new Label(task.getName().substring(0, 6) + "...", rect);
                } else {
                    label = new Label(task.getName(), rect);
                }
                
                label.setContentDisplay(ContentDisplay.CENTER);
                nodes[row][column].getChildren().add(label);
                
                label.setOnMouseClicked(v -> {
                    addTask(task);
                });
            }
        }
    }
}