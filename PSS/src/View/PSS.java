package View;

import javafx.application.Application;
import java.util.ArrayList;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

public class PSS extends Application {
    private DayView day;
    private WeekView week;
    private MonthView month;
    private Stage stage;
    private LocalDate date;
    private ArrayList<Task> tasks = new ArrayList<>();
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        date = LocalDate.now();
        this.stage = stage;
        
        LocalTime start = LocalTime.now().withHour(0).withMinute(15);
        LocalTime end = LocalTime.from(start).plusMinutes(90);
        tasks.add(new Task("A", date, date, 90, false, start, end));
        
        start = LocalTime.now().withHour(3).withMinute(30);
        end = LocalTime.from(start).plusMinutes(15);
        tasks.add(new Task("B", date, date, 15, true, start, end));
        
        start = LocalTime.now().withHour(5).withMinute(45);
        end = LocalTime.from(start).plusMinutes(45);
        tasks.add(new Task("C", date, date, 45, false, start, end));
        
        start = LocalTime.now().withHour(7).withMinute(0);
        end = LocalTime.from(start).plusMinutes(120);
        tasks.add(new Task("D", date, date, 120, true, start, end));
        
        start = LocalTime.now().withHour(10).withMinute(15);
        end = LocalTime.from(start).plusMinutes(90);
        tasks.add(new Task("E", date, date, 90, true, start, end));
        
        start = LocalTime.now().withHour(15).withMinute(45);
        end = LocalTime.from(start).plusMinutes(30);
        tasks.add(new Task("F", date, date, 30, false, start, end));
        
        start = LocalTime.now().withHour(22).withMinute(45);
        end = LocalTime.from(start).plusMinutes(90);
        tasks.add(new Task("G", date, date, 90, false, start, end));
        
        month = new MonthView(stage, date, tasks);
        
        //change view when drop box selection changes
        month.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                switchView(newValue);
            }
        });
        
        month.getLeft().setOnMouseClicked(v -> {
            date.minusMonths(1);
            switchView("Month");
        });
         
        month.getRight().setOnMouseClicked(v -> {
            date.plusMonths(1);
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
               date.minusDays(1);
               switchView("Day");
            });
            
            day.getRight().setOnMouseClicked(v -> {
                date.plusDays(1);
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
                date.minusWeeks(1);
                switchView("Week");
            });
             
            week.getRight().setOnMouseClicked(v -> {
                date.plusWeeks(1);
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
                date.minusMonths(1);
                switchView("Month");
            });
             
            month.getRight().setOnMouseClicked(v -> {
                date.plusMonths(1);
                switchView("Month");
            });
            
            month.getAddTask().setOnAction(v -> {
                month.addTask();
            });
            
            break;
        }
    }
}
