package pss;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeekView extends AbstractMenu {
    private final double CELL_HEIGHT = 50, CELL_WIDTH = 100;
    private GridPane calendar;
    private Pane[][] nodes = new Pane[7][24];
    
    WeekView() {
        
    }
    
    WeekView(Stage stage) {
        super();
        
        this.stage = stage;
        scene = new Scene(pane, 1280, 720);
        stage.setTitle("Month View");
        stage.setScene(scene);
        
        buildMenu();
        drawTasks();
    }
    
    WeekView(Stage stage, Date date) {
        super(date);
        
        this.stage = stage;
        scene = new Scene(pane, 1280, 720);
        stage.setTitle("Month View");
        stage.setScene(scene);
        
        buildMenu();
        drawTasks();
    }
    
    public void buildMenu() {    
        select.getSelectionModel().select(1);
        
        /*
         * add highlight to selected day
         * */
        int row = (int) Math.floor((date.get(Calendar.DAY_OF_MONTH) - 1 + firstDay) / 7);
        
        for(int i = 0; i < 7; i++) {
            Rectangle rect = new Rectangle(MINI_SIZE, MINI_SIZE);
            rect.setFill(Color.BLUE);
            rect.setOpacity(0.2);
            miniMonth.add(rect, i, row + 1);
        }
        
        //use a gridpane for the calendar header for consistency with the calendar
        GridPane calendarHeader = new GridPane();
                    
        //label for each day of the week
        Label[] labels = {new Label("Sun"), new Label("Mon"), new Label("Tue"), new Label("Wed"), new Label("Thu"), new Label("Fri"), new Label("Sat")};
                
        //blank rectangle at start of header, so that the header is properly aligned with the calendar table
        Pane formattingBox = new Pane();
        formattingBox.setPrefSize(CELL_HEIGHT, CELL_HEIGHT);
        formattingBox.setMinSize(CELL_HEIGHT, CELL_HEIGHT);
        formattingBox.setMaxSize(CELL_HEIGHT, CELL_HEIGHT);
        formattingBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray;");
        calendarHeader.add(formattingBox, 0, 0);
                
        //loop to make the header
        for(int i = 0; i < 7; i++) {
            //make a box for each day of the week
            Pane headerBox = new Pane();
            headerBox.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            headerBox.setMinSize(CELL_WIDTH, CELL_HEIGHT);
            headerBox.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
            headerBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray");
                    
            //attach the label for each day of the week to the center of a rectangle
            labels[i].setGraphic(headerBox);
            labels[i].setFont(Font.font("Courier", FontWeight.BOLD, 15));
            labels[i].setContentDisplay(ContentDisplay.CENTER);
                    
            //add the label to the header
            calendarHeader.add(labels[i], i + 1, 0);
        }
                
        //gridpane to hold the calendar
        calendar = new GridPane();
                
        //tracker variables for building the time stamp at the start of each calendar line
        int time = 12;
        String amOrPm = "am";
                
        //loop building the calendar
        for(int i = 0; i < 24; i++) {
            //make a box for each hour of the day
            Pane timeBox = new Pane();
            timeBox.setPrefSize(CELL_HEIGHT, CELL_HEIGHT);
            timeBox.setMinSize(CELL_HEIGHT, CELL_HEIGHT);
            timeBox.setMaxSize(CELL_HEIGHT, CELL_HEIGHT);
            timeBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray");
                    
            //switch from am to pm when noon is reached
            if(i == 12)
                amOrPm = "pm";
                    
            //make a label for each hour of the day and add it to the calendar line
            Label timeLabel = new Label(Integer.toString(time) + amOrPm, timeBox);
            timeLabel.setFont(Font.font("Courier", FontWeight.SEMI_BOLD, 15));
            timeLabel.setContentDisplay(ContentDisplay.CENTER);
            calendar.add(timeLabel, 0, i);
                    
            //make the empty cells for each day of the week
            for(int j = 0; j < 7; j++) {
                //used to make calendar look nice
                Pane dayBox = new Pane();
                dayBox.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                dayBox.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                dayBox.setMaxSize(CELL_WIDTH, CELL_HEIGHT);
                dayBox.setStyle("-fx-background-color: whitesmoke; -fx-border-width: 1px; -fx-border-color: darkgray");
                        
                //dashed line in the middle of each cell to indicate each half hour mark
                Line line = new Line(0, CELL_HEIGHT / 2, CELL_WIDTH, CELL_HEIGHT / 2);
                line.getStrokeDashArray().addAll(5.0, 5.0);
                    dayBox.getChildren().add(line);
                        
                //add design pane, and container hbox to the grid
                calendar.add(dayBox, j + 1, i);
                nodes[i][j] = dayBox;
            }
                    
            //once noon or midnight is reached, reset the clock to create a 12 hour clock
            if(time == 12)
                time = 0;
            time++;
        }
                
        //put the calendar in a scroll pane so that its size can be limited, without shrinking it
        ScrollPane calendarScroll = new ScrollPane(calendar);
        calendarScroll.setPrefSize(765, 600);
                
        //translate to line up with the header
        calendarScroll.setTranslateX(-1);
        calendarScroll.setVvalue(0.5);
                
        //blank rectangle at end of header, to align header with scroll bar
        Pane formattingBox2 = new Pane();
        formattingBox2.setPrefSize(14, CELL_HEIGHT);
        formattingBox2.setMinSize(14, CELL_HEIGHT);
        formattingBox2.setMaxSize(14, CELL_HEIGHT);
        formattingBox2.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray");
        calendarHeader.add(formattingBox2, 8, 0);
                
        //combine calendar and header
        VBox finalCalendar = new VBox(0);
        finalCalendar.getChildren().addAll(calendarHeader, calendarScroll);
        finalCalendar.setAlignment(Pos.CENTER_LEFT);
                
        //build final pane
        pane.setLeft(finalCalendar);
        
        pane.requestFocus();
        
        //set alignment and margins
        BorderPane.setMargin(finalCalendar, new Insets(0.0, 5.0, 10.0, 10.0));
    }
    
    public void drawTasks() {
        for(Task tasks : tasks) {
            
        }
    }
}