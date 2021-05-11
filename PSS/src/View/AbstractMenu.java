package pss;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import java.util.Locale;
import java.util.Date;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;

public abstract class AbstractMenu {
	//scene and stage for each view, list of tasks, mini month calendar view, drop box to select view, and info needed to create a new task
	protected Scene scene;
	protected Stage stage;
	protected ArrayList<Task> tasks;
	protected GridPane miniMonth;
	protected BorderPane pane, rightPane;
	protected ComboBox<String> select;
	protected LocalDate date;
	protected int numOfWeeks, firstDay;
	protected Button left, right, addTask, cancel, create;
	protected final int MINI_SIZE = 35;
	protected TextField name, duration;
	protected CheckBox repeat;
	protected DatePicker startDate, endDate;
	
	//constructor to load into present date
	AbstractMenu() {
        pane = new BorderPane();
        select = new ComboBox<>();
        select.getItems().addAll("Day", "Week", "Month");
        date = LocalDate.now();
        
        //save current day, get first and last weeks of month to get number of weeks in month, restore current date
        LocalDate start = date.withDayOfMonth(1), end = date.withDayOfMonth(date.lengthOfMonth());
        int lastWeek = end.get(WeekFields.ISO.weekOfYear()), firstWeek = start.get(WeekFields.ISO.weekOfYear());
        
        //need to check if december rolls into next year, or the math breaks
        if(date.getMonthValue() == 12 && lastWeek == 1) {
            lastWeek += 52;
        }
        
        //get the number of weeks in the month
        numOfWeeks = lastWeek - firstWeek + 1;
        
        //the controls at the top of the ui to switch views/months and create new task
        left = new Button("<");
        right = new Button(">");
        addTask = new Button("Add Task");
        
        //show current month and year in title
        Text titleText = new Text(date.getMonth().getDisplayName(TextStyle.FULL, new Locale("en")) + " " + date.getYear());
        titleText.setStyle("-fx-font: 20px \"Courier\";");
        
        //set the title to the top of the screen and build the mini month calendar
        HBox title = new HBox(10);
        title.getChildren().addAll(left, right, titleText, addTask, select);
        pane.setTop(title);
        BorderPane.setMargin(title, new Insets(10.0, 10.0, 10.0, 10.0));
        buildMiniMonth();
    }
	
	//constructor to load into specific date
	AbstractMenu(Date date) {
	    pane = new BorderPane();
	    rightPane = new BorderPane();
        select = new ComboBox<>();
        select.getItems().addAll("Day", "Week", "Month");
        this.date = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        
        WeekFields weekFields = WeekFields.of(new Locale("en"));
        LocalDate start = this.date.withDayOfMonth(1), end = this.date.withDayOfMonth(this.date.lengthOfMonth());
        int lastWeek = end.get(weekFields.weekOfYear()), firstWeek = start.get(weekFields.weekOfYear());
        
        //need to check if december rolls into next year, or the math breaks
        if(this.date.getMonthValue() == 12 && lastWeek == 1) {
            lastWeek += 52;
        }
        
        //get the number of weeks in the month
        numOfWeeks = lastWeek - firstWeek + 1;
        
        //the controls at the top of the ui to switch views/months and create new task
        left = new Button("<");
        right = new Button(">");
        addTask = new Button("Add Task");
        
        //show current month and year in title
        Text titleText = new Text(this.date.getMonth().getDisplayName(TextStyle.FULL, new Locale("en")) + " " + this.date.getYear());
        titleText.setStyle("-fx-font: 20px \"Courier\";");
        
        //set the title to the top of the screen and build the mini month calendar
        HBox title = new HBox(10);
        title.getChildren().addAll(left, right, titleText, addTask, select);
        pane.setTop(title);
        BorderPane.setMargin(title, new Insets(10.0, 10.0, 10.0, 10.0));
        buildMiniMonth();
    }
	
	//show a small month view in the corner to keep track of current date
	public void buildMiniMonth() {
	    //labels for the days of the week
        miniMonth = new GridPane();
        Text[] labels = {new Text("S"), new Text("M"), new Text("T"), new Text("W"), new Text("T"), new Text("F"), new Text("S")};
        
        //get the last month to draw the carry over days from the last month on the first week of the calendar
        LocalDate prevMonth = (date.getMonthValue() == 1) ? date.withYear(date.getYear() - 1).withMonth(12) : date.withMonth(date.getMonthValue() - 1);
        
        //save current day, get the number of days in the month, the first day of the month, counters for drawing the calendar, and restore current day
        int daysInMonth = date.lengthOfMonth(), dayCounter = 1, outOfRangeCounter = 1;
        LocalDate start = date.withDayOfMonth(1);
        firstDay = start.getDayOfWeek().getValue();

        //loop to make the header
        for(int i = 0; i < labels.length; i++) {
            StackPane headerBox = new StackPane();
            headerBox.setPrefSize(MINI_SIZE, MINI_SIZE);
            headerBox.setMinSize(MINI_SIZE, MINI_SIZE);
            headerBox.setMaxSize(MINI_SIZE, MINI_SIZE);
            headerBox.setStyle("-fx-background-color: lightgray; -fx-border-width: 1px; -fx-border-color: darkgray");
            
            headerBox.getChildren().add(labels[i]);
            miniMonth.add(headerBox, i, 0);
        }
                
        //loop building the calendar
        for(int i = 0; i < numOfWeeks; i++) {                    
            for(int j = 0; j < 7; j++) {
                StackPane box = new StackPane();
                box.setPrefSize(MINI_SIZE, MINI_SIZE);
                box.setMinSize(MINI_SIZE, MINI_SIZE);
                box.setMaxSize(MINI_SIZE, MINI_SIZE);
                box.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
                
                if(i == 0 && j < firstDay) {
                    Text day = new Text(Integer.toString(prevMonth.lengthOfMonth() - firstDay + j + 1));
                    day.setFill(Color.GRAY);
                    box.getChildren().add(day);
                } else if(dayCounter > daysInMonth) {
                    Text day = new Text(Integer.toString(outOfRangeCounter++));
                    day.setFill(Color.GRAY);
                    box.getChildren().add(day);
                } else {
                    Text day = new Text(Integer.toString(dayCounter++));
                    box.getChildren().add(day);
                }
                
                miniMonth.add(box, j, i + 1);
            }
        }
        
        miniMonth.setAlignment(Pos.BOTTOM_RIGHT);
        rightPane.setBottom(miniMonth);
        pane.setRight(rightPane);
        BorderPane.setMargin(miniMonth, new Insets(0.0, 10.0, 10.0, 0.0));
        
    }
	
	//clear the current views
    public void clearCalendar() {
        pane.setLeft(null);
        pane.setRight(null);
    }
    
    //show view to add a new task
    public void addTask() {
        //initialize controls
        cancel = new Button("Cancel");
        create = new Button("Create");
        name = new TextField();
        duration = new TextField();
        repeat = new CheckBox("Repeat");
        startDate = new DatePicker(date);
        endDate = new DatePicker(date);
        
        //hide end date selector and set repeat to false
        endDate.setVisible(false);
        repeat.setSelected(false);
        
        //listener to show end date selector if set to repeat
        repeat.selectedProperty().addListener(v -> {
           if(repeat.isSelected()) {
               endDate.setVisible(true);
           } else {
               endDate.setVisible(false);
           }
        });
        
        //only allow numbers to be entered into the duration field
        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    duration.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        //add labels for name and duration text fields
        Label nameLabel = new Label("Name: ", name), durationLabel = new Label("Duration (Minutes): ", duration);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        durationLabel.setContentDisplay(ContentDisplay.RIGHT);
        
        //group buttons together
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancel, create);
        
        //build final pane
        VBox taskPane = new VBox(10);
        taskPane.setAlignment(Pos.CENTER);
        taskPane.getChildren().addAll(nameLabel, startDate, durationLabel, repeat, endDate, buttons);
        taskPane.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
        
        BorderPane.setMargin(taskPane, new Insets(0.0, 10.0, 10.0, 0.0));
        rightPane.setCenter(taskPane);
        
        //close pane when cancel button pressed
        cancel.setOnAction(event -> rightPane.setCenter(null));
    }
    
    //display a task with option to edit task info
    public void addTask(Task task) {
        //initialize controls
        cancel = new Button("Cancel");
        create = new Button("Edit");
        name = new TextField(task.getName());
        duration = new TextField(Integer.toString(task.getDuration()));
        repeat = new CheckBox("Repeat");
        startDate = new DatePicker(LocalDate.ofInstant(task.getStartDate().toInstant(), ZoneId.systemDefault()));
        endDate = new DatePicker(LocalDate.ofInstant(task.getEndDate().toInstant(), ZoneId.systemDefault()));
        repeat.setSelected(task.getRepeat());
        
        //show/hide end date selector based on whether the task is set to repeat
        if(task.getRepeat()) {
            endDate.setVisible(true);
        } else {
            endDate.setVisible(false);
        }
        
        //listener to show end date selector if set to repeat
        repeat.selectedProperty().addListener(v -> {
           if(repeat.isSelected()) {
               endDate.setVisible(true);
           } else {
               endDate.setVisible(false);
           }
        });
        
        //only allow numbers to be entered into the duration field
        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    duration.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        //add labels for name and duration text fields
        Label nameLabel = new Label("Name: ", name), durationLabel = new Label("Duration (Minutes): ", duration);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        durationLabel.setContentDisplay(ContentDisplay.RIGHT);
        
        //group buttons together
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancel, create);
        
        //build final pane
        VBox taskPane = new VBox(10);
        taskPane.setAlignment(Pos.CENTER);
        taskPane.getChildren().addAll(nameLabel, startDate, durationLabel, repeat, endDate, buttons);
        taskPane.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
        
        BorderPane.setMargin(taskPane, new Insets(0.0, 10.0, 10.0, 0.0));
        rightPane.setCenter(taskPane);
        
        //close pane when cancel button pressed
        cancel.setOnAction(event -> rightPane.setCenter(null));
    }
    
    //popup error message
    public static void popupError(String errorMsg) {
        //error message to show
        Text error = new Text(errorMsg);
        error.setFont(Font.font("Courier", FontWeight.BOLD, 20));
        error.setStyle("-fx-fill: white; -fx-stroke: royalblue; -fx-stroke-width: 3px");
        
        //button to close popup
        Button quitError = new Button("Ok");
        
        //add error message and button to a vbox
        VBox errorPane = new VBox(10);
        errorPane.setAlignment(Pos.CENTER);
        errorPane.getChildren().addAll(error, quitError);
        
        //create new stage for the error, and display the new stage
        Stage popup = new Stage();
        Scene popupScene = new Scene(errorPane, 600, 400);
        popup.setScene(popupScene);
        popup.setTitle("Error");
        popup.show();
        
        //close popup when ok button pressed
        quitError.setOnAction(event -> popup.close());
    }
	
	//used to build the ui for each view
	public abstract void buildView();
	
	//used to draw the tasks on the calendars
	public abstract void drawTasks();

	/*
	 * getters
	 * */
	
	public Stage getStage() {
		return stage;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public BorderPane getPane() {
        return pane;
    }
	
	public Button getLeft() {
	    return left;
	}
	
	public Button getRight() {
	    return right;
	}
	
	public Button getAddTask() {
	    return addTask;
	}
	
	public Button getCancel() {
	    return cancel;
	}
	
	public Button getCreate() {
	    return create;
	}
	
	public String getName() {
	    return name.getText();
	}
	
	public int getDuration() {
	    return Integer.parseInt(duration.getText());
	}
	
	public boolean getRepeat() {
	    return repeat.isSelected();
	}
    
    public ComboBox<String> getSelect() {
        return select;
    }
}
