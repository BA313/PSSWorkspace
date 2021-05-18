import Model.JsonWriter;
import javafx.application.Application;
import java.util.ArrayList;
import Controller.Controller;
import Model.Task;
import View.DayView;
import View.MonthView;
import View.WeekView;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

//TODO Fix date problems

public class PSS extends Application {
    private DayView day;
    private WeekView week;
    private MonthView month;
    private Stage stage;
    private LocalDate date;
    public Controller control;
    private String filepath;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage)  {
        date = LocalDate.now();
        this.stage = stage;
        
        //Initialize controller to handle data fetches 
        control = new Controller();
        
        month = new MonthView(stage, date, control);
        
        //change view when drop box selection changes
        month.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                switchView(newValue);
            }
        });
        
        month.getLeft().setOnMouseClicked(v -> {
            date = date.minusMonths(1);
            switchView("Month");
        });
         
        month.getRight().setOnMouseClicked(v -> {
            date = date.plusMonths(1);
            switchView("Month");
        });
        
        month.getAddTask().setOnAction(v -> {
            month.addTask();
        });

        filepath = control.getFilepath();
        // Save TaskList to a Json file when closing
        stage.setOnCloseRequest(event -> {
            new JsonWriter().writeTaskList(filepath + "Data.json", control.getTasks());

        });

        stage.show();
    }
    
    public void switchView(String view) {
        switch (view) {
        case "Day":
            day = new DayView(stage, date, control);
            
            //change view when drop box selection changes
            day.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if(!oldValue.equals(newValue)) {
                    switchView(newValue);
                }
            });
            
            day.getLeft().setOnMouseClicked(v -> {
                date = date.minusDays(1);
               switchView("Day");
            });
            
            day.getRight().setOnMouseClicked(v -> {
                date =  date.plusDays(1);
                switchView("Day");
            });
            
            day.getAddTask().setOnAction(v -> {
                day.addTask();
            });

            break;
        case "Week":
            week = new WeekView(stage, date, control);

            //change view when drop box selection changes
            week.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if(!oldValue.equals(newValue)) {
                    switchView(newValue);
                }
            });
            
            week.getLeft().setOnMouseClicked(v -> {
                date = date.minusWeeks(1);
                switchView("Week");
            });
             
            week.getRight().setOnMouseClicked(v -> {
                date = date.plusWeeks(1);
                switchView("Week");
            });
            
            week.getAddTask().setOnAction(v -> {
                week.addTask();
            });
            
            break;
        case "Month":
            month = new MonthView(stage, date, control);
            
            //change view when drop box selection changes
            month.getSelect().getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if(!oldValue.equals(newValue)) {
                    switchView(newValue);
                }
            });
            
            month.getLeft().setOnMouseClicked(v -> {
                date = date.minusMonths(1);
                switchView("Month");
            });
             
            month.getRight().setOnMouseClicked(v -> {
                date = date.plusMonths(1);
                switchView("Month");
            });
            
            month.getAddTask().setOnAction(v -> {
                month.addTask();
            });
            
            break;
        }
    }
}
