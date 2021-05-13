package View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import java.util.Locale;

import Controller.Controller;
import Model.Task;

import java.lang.Math;
import java.time.format.TextStyle;
import javafx.scene.paint.Color;
import java.time.LocalDate;

public class DayView extends AbstractMenu {
    private final double CELL_HEIGHT = 50, CELL_WIDTH = 100;
    private GridPane calendar;
    
    DayView() {
        
    }
    
    //constructor to load into present day
    DayView(Stage stage, Controller control) {
        super();
        
        this.stage = stage;
        this.control = control;
        scene = new Scene(pane, 1280, 720);
        stage.setTitle("Month View");
        stage.setScene(scene);
        
        buildView();
        drawTasks();
    }
    
    //constructor to load into specific date
    public DayView(Stage stage, LocalDate date, Controller control) {
        super(date);
        
        this.stage = stage;
        this.control = control;
        scene = new Scene(pane, 1280, 720);
        stage.setTitle("Month View");
        stage.setScene(scene);
        
        buildView();
        drawTasks();
    }
    
    public void buildView() {  
        //set drop down selector to show day view
        select.getSelectionModel().select(0);
        
        //get grid index of the current day, then highlight that day on the mini month calendar
        int row = (int) Math.floor((date.getDayOfMonth() - 1 + firstDay) / 7);
        int column = (date.getDayOfMonth() - 1 + firstDay) - (7 * row);
        Rectangle rect = new Rectangle(MINI_SIZE, MINI_SIZE);
        rect.setFill(Color.BLUE);
        rect.setOpacity(0.2);
        miniMonth.add(rect, column, row + 1);
        
        //use a gridpane for the calendar header for consistency with the calendar
        GridPane calendarHeader = new GridPane();
                    
        //label for each day of the week
        Label label = new Label(date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("en")));
                
        //blank rectangle at start of header, so that the header is properly aligned with the calendar table
        Pane formattingBox = new Pane();
        formattingBox.setPrefSize(CELL_HEIGHT, CELL_HEIGHT);
        formattingBox.setMinSize(CELL_HEIGHT, CELL_HEIGHT);
        formattingBox.setMaxSize(CELL_HEIGHT, CELL_HEIGHT);
        formattingBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray;");
        calendarHeader.add(formattingBox, 0, 0);
                
        //make a box for each day of the week
        Pane headerBox = new Pane();
        headerBox.setPrefSize(7 * CELL_WIDTH, CELL_HEIGHT);
        headerBox.setMinSize(7 * CELL_WIDTH, CELL_HEIGHT);
        headerBox.setMaxSize(7 * CELL_WIDTH, CELL_HEIGHT);
        headerBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray");
                
        //attach the label for each day of the week to the center of a rectangle
        label.setGraphic(headerBox);
        label.setFont(Font.font("Courier", FontWeight.BOLD, 15));
        label.setContentDisplay(ContentDisplay.CENTER);
                
        //add the label to the header
        calendarHeader.add(label, 1, 0);
                
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
            //used to make calendar look nice
            Pane dayBox = new Pane();
            dayBox.setPrefSize(7 * CELL_WIDTH, CELL_HEIGHT);
            dayBox.setMinSize(7 * CELL_WIDTH, CELL_HEIGHT);
            dayBox.setMaxSize(7 * CELL_WIDTH, CELL_HEIGHT);
            dayBox.setStyle("-fx-background-color: whitesmoke; -fx-border-width: 1px; -fx-border-color: darkgray");
                    
            //dashed line in the middle of each cell to indicate each half hour mark
            Line line = new Line(0, CELL_HEIGHT / 2, 7 * CELL_WIDTH, CELL_HEIGHT / 2);
            line.getStrokeDashArray().addAll(5.0, 5.0);
                dayBox.getChildren().add(line);
                    
            //add design pane, and container hbox to the grid
            calendar.add(dayBox, 1, i);
                    
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
                
        //build final pane and take focus off buttons so they don't load highlighted
        pane.setLeft(finalCalendar);
        pane.requestFocus();
        
        //set alignment and margins
        BorderPane.setMargin(finalCalendar, new Insets(0.0, 5.0, 10.0, 10.0));
    }
    //TODO CHANGE
    //add boxes for tasks on calendar
    public void drawTasks() {
        for(Task task : control.getTasks()) {
            //get grid index, height of box, and offset of the start of the box
            int row = task.getStartTime().getHour();
            double height = (CELL_HEIGHT / 4) * (task.getDuration() / 15) + 1;
            double offset = (CELL_HEIGHT / 4) * (task.getStartTime().getMinute() / 15) - 1;
            
            //rectangle to add to calendar
            Rectangle rect = new Rectangle(7 * CELL_WIDTH, height);
            rect.setFill(Color.BLUE);
            rect.setOpacity(0.5);
            
            //label with task name, and on click listener to show task details/edit task
            Label label = new Label(task.getName(), rect);
            label.setContentDisplay(ContentDisplay.CENTER);
            label.setOnMouseClicked(v -> addTask(task));
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
            
            //need to add box to a pane first or it wouldn't draw right
            Pane p = new Pane();
            p.getChildren().add(label);
            label.setTranslateY(offset);
            
            //add box to calendar, the math expression determines the column span and thankfully applies the right offset to the rectangle
            calendar.add(p, 1, row, 1, (int)Math.floor((height + offset) / 50) + 1);
        }
    }
}