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
        Date testStart = new Date(), testEnd = new Date();
        testEnd.setHours(testStart.getHours() + 1);
        this.stage = stage;
        
        tasks.add(new Task("A", testStart, testEnd, 90, false));
        testStart.setHours(testEnd.getHours() + 1);
        testEnd.setHours(testStart.getHours() + 1);
        tasks.add(new Task("B", testStart, testEnd, 90, true));
        testStart.setHours(testEnd.getHours() + 1);
        testEnd.setHours(testStart.getHours() + 1);
        tasks.add(new Task("C", testStart, testEnd, 90, false));
        testStart.setHours(testEnd.getHours() + 1);
        testEnd.setHours(testStart.getHours() + 1);
        tasks.add(new Task("D", testStart, testEnd, 90, true));
        testStart.setHours(testEnd.getHours() + 1);
        testEnd.setHours(testStart.getHours() + 1);
        tasks.add(new Task("E", testStart, testEnd, 90, false));
        testStart.setHours(testEnd.getHours() + 1);
        testEnd.setHours(testStart.getHours() + 1);
        tasks.add(new Task("F", testStart, testEnd, 90, false));
        testStart.setHours(testEnd.getHours() + 1);
        testEnd.setHours(testStart.getHours() + 1);
        tasks.add(new Task("E", testStart, testEnd, 90, false));
        
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
            day = new DayView(stage, date);
            
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
            week = new WeekView(stage, date);

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
