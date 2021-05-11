package pss;

import javafx.application.Application;
import java.util.ArrayList;
import java.util.Date;
import javafx.stage.Stage;

public class PSS extends Application {
    private DayView day;
    private WeekView week;
    private MonthView month;
    private Stage stage;
    private Date date;
    private ArrayList<Task> tasks = new ArrayList<>();
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        date = new Date();
        this.stage = stage;
        
        Date testStart = new Date(121, 4, 10, 0, 15, 0);
        Date testEnd = Date.from(testStart.toInstant());
        testEnd.setMinutes(testEnd.getMinutes() + 90);
        tasks.add(new Task("A", testStart, testEnd, 90, false));
        
        /*testStart.setHours(3);
        testStart.setMinutes(30);
        testEnd.setHours(3);
        testEnd.setMinutes(45);*/
        Date testStart1 = new Date(121, 4, 10, 3, 30, 0);
        Date testEnd1 = Date.from(testStart1.toInstant());
        testEnd1.setMinutes(testEnd1.getMinutes() + 15);
        tasks.add(new Task("B", testStart1, testEnd1, 15, true));
        
        /*testStart = new Date(121, 4, 10, 5, 45, 0);
        testEnd = Date.from(testStart.toInstant());
        testEnd.setMinutes(testEnd.getMinutes() + 45);
        tasks.add(new Task("C", testStart, testEnd, 45, true));
        
        testStart = new Date(121, 4, 10, 7, 0, 0);
        testEnd = Date.from(testStart.toInstant());
        testEnd.setMinutes(testEnd.getMinutes() + 120);
        tasks.add(new Task("D", testStart, testEnd, 120, true));
        
        testStart = new Date(121, 4, 10, 10, 15, 0);
        testEnd = Date.from(testStart.toInstant());
        testEnd.setMinutes(testEnd.getMinutes() + 90);
        tasks.add(new Task("E", testStart, testEnd, 90, true));
        
        testStart = new Date(121, 4, 10, 15, 45, 0);
        testEnd = Date.from(testStart.toInstant());
        testEnd.setMinutes(testEnd.getMinutes() + 30);
        tasks.add(new Task("F", testStart, testEnd, 30, true));
        
        testStart = new Date(121, 4, 10, 20, 30, 0);
        testEnd = Date.from(testStart.toInstant());
        testEnd.setMinutes(testEnd.getMinutes() + 90);
        tasks.add(new Task("G", testStart, testEnd, 90, true));*/
        
        month = new MonthView(stage, date, tasks);
        
        //change view when drop box selection changes
        month.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                switchView(newValue);
            }
        });
        
        month.getLeft().setOnMouseClicked(v -> {
            date.setMonth(date.getMonth() - 1);
            switchView("Month");
        });
         
        month.getRight().setOnMouseClicked(v -> {
            date.setMonth(date.getMonth() + 1);
            switchView("Month");
        });
        
        month.getAddTask().setOnAction(v -> {
            month.addTask();
        });
        
        stage.show();
    }
    
    public void switchView(String view) {
        switch (view) {
        case "Day":
            day = new DayView(stage, date, tasks);
            
            //change view when drop box selection changes
            day.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if(!oldValue.equals(newValue)) {
                    switchView(newValue);
                }
            });
            
            day.getLeft().setOnMouseClicked(v -> {
               date.setDate(date.getDate() - 1);
               switchView("Day");
            });
            
            day.getRight().setOnMouseClicked(v -> {
                date.setDate(date.getDate() + 1);
                switchView("Day");
            });
            
            day.getAddTask().setOnAction(v -> {
                day.addTask();
            });

            break;
        case "Week":
            week = new WeekView(stage, date, tasks);

            //change view when drop box selection changes
            week.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if(!oldValue.equals(newValue)) {
                    switchView(newValue);
                }
            });
            
            week.getLeft().setOnMouseClicked(v -> {
                date.setDate(date.getDate() - 7);
                switchView("Week");
            });
             
            week.getRight().setOnMouseClicked(v -> {
                date.setDate(date.getDate() + 7);
                switchView("Week");
            });
            
            week.getAddTask().setOnAction(v -> {
                week.addTask();
            });
            
            break;
        case "Month":
            month = new MonthView(stage, date, tasks);
            
            //change view when drop box selection changes
            month.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if(!oldValue.equals(newValue)) {
                    switchView(newValue);
                }
            });
            
            month.getLeft().setOnMouseClicked(v -> {
                date.setMonth(date.getMonth() - 1);
                switchView("Month");
            });
             
            month.getRight().setOnMouseClicked(v -> {
                date.setMonth(date.getMonth() + 1);
                switchView("Month");
            });
            
            month.getAddTask().setOnAction(v -> {
                month.addTask();
            });
            
            break;
        }
    }
}
