package View;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;

import Controller.Controller;
import Model.Anti;
import Model.Recurring;
import Model.Task;
import Model.Transient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

public abstract class AbstractMenu {
	//scene and stage for each view, list of tasks, mini month calendar view, drop box to select view, and info needed to create a new task
	protected Scene scene;
	protected Stage stage;
	protected Controller control;
	protected GridPane miniMonth;
	protected BorderPane pane, rightPane;
	protected ComboBox<String> select;
	protected LocalDate date;
	protected int numOfWeeks, firstDay;
	protected Button left, right, addTask, close, create, cancelTask, delete;
	protected final int MINI_SIZE = 35;
	protected TextField name, duration, frequency;
	protected CheckBox repeat;
	protected DatePicker startDate, endDate;
	protected Spinner<LocalTime> startTime;
	
	//constructor to load into present date
	public AbstractMenu() {
		loader(LocalDate.now());
    }
	
	//constructor to load into specific date
	public AbstractMenu(LocalDate date) {
		loader(date);
    }
	
	private void loader(LocalDate date) {
		pane = new BorderPane();
	    rightPane = new BorderPane();
        select = new ComboBox<>();
        select.getItems().addAll("Day", "Week", "Month");
        this.date = date;
        
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
    
    public LocalTime round15Mins(LocalTime time) {
        int round = time.getMinute() % 15;
        return time = time.plusMinutes(round < 8 ? -round : (15-round));
    }
    
    //show view to add a new task
    public void addTask() {
        //initialize controls
        close = new Button("Close");
        create = new Button("Create");
        name = new TextField();
        duration = new TextField();
        repeat = new CheckBox("Repeat");
        startDate = new DatePicker(date);
        endDate = new DatePicker(date);
        frequency = new TextField();
        
        startTime = new Spinner<>(new SpinnerValueFactory<LocalTime>() {
            @Override
            public void decrement(int num) {
                if(getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    setValue(time.minusMinutes(15));
                }
            }

            @Override
            public void increment(int num) {
                if(getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    setValue(time.plusMinutes(15));
                }
            }
        });
        
        //hide end date selector and set repeat to false
        repeat.setSelected(false);
        startTime.setEditable(true);
        
        startTime.getValueFactory().setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
        startTime.getValueFactory().setValue(round15Mins(LocalTime.now()));
        
        duration.setText("15");
        
        //only allow numbers to be entered into the duration field
        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    duration.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        frequency.setText("1");
        
        //only allow numbers to be entered into the frequency field
        frequency.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    frequency.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        //add labels for name and duration text fields
        Label nameLabel = new Label("Name: ", name), durationLabel = new Label("Duration (Minutes): ", duration),
                startDateLabel = new Label("Start Date: ", startDate), endDateLabel = new Label("End Date: ", endDate),
                startTimeLabel = new Label("Start Time: ", startTime), frequencyLabel = new Label("Frequency (Days): ", frequency);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        durationLabel.setContentDisplay(ContentDisplay.RIGHT);
        startDateLabel.setContentDisplay(ContentDisplay.RIGHT);
        endDateLabel.setContentDisplay(ContentDisplay.RIGHT);
        startTimeLabel.setContentDisplay(ContentDisplay.RIGHT);
        frequencyLabel.setContentDisplay(ContentDisplay.RIGHT);
        
        //group buttons together
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(close, create);
        
        endDateLabel.setVisible(false);
        frequencyLabel.setVisible(false);
        
        //listener to show end date selector if set to repeat
        repeat.selectedProperty().addListener(v -> {
           if(repeat.isSelected()) {
               endDateLabel.setVisible(true);
               frequencyLabel.setVisible(true);
           } else {
               endDateLabel.setVisible(false);
               frequencyLabel.setVisible(false);
           }
        });
        
        //build final pane
        VBox taskPane = new VBox(10);
        taskPane.setAlignment(Pos.CENTER);
        taskPane.getChildren().addAll(nameLabel, startDateLabel, startTimeLabel, durationLabel, repeat, endDateLabel, frequencyLabel, buttons);
        taskPane.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
        
        BorderPane.setMargin(taskPane, new Insets(0.0, 10.0, 10.0, 0.0));
        rightPane.setCenter(taskPane);
        
        //add new task to task array when called
        create.setOnAction(v -> {
        	boolean done = true;
        	try {
        		if(getRepeat()) {
        			control.checkOverlap(createRTask());
        		}else {
        			control.checkOverlap(createTTask());
        		}
        	}catch(Exception e){
        		//check for errors
        		popupError("Error Adding Task: \n" + e.getMessage() + "\nMake sure duration or frequency is not empty");
        		done = false;
        	}
        	//close and refresh page if done
        	if(done) {
	        	buildView();
	        	drawTasks();
	        	rightPane.setCenter(null);
        	}
        });
        
        //close pane when cancel button pressed
        close.setOnAction(event -> rightPane.setCenter(null));
    }
    
    public void editTask(Task task) {
        //initialize controls
        close = new Button("Close");
        cancelTask = new Button("Cancel Task");
        create = new Button("Save");
        delete = new Button("Delete");
        name = new TextField(task.getName());
        duration = new TextField(Integer.toString(task.getDuration()));
        repeat = new CheckBox("Repeat");
        startDate = new DatePicker(task.getStartDate());
        endDate = new DatePicker(task.getEndDate());
        
        startTime = new Spinner<>(new SpinnerValueFactory<LocalTime>() {
            @Override
            public void decrement(int num) {
                if(getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    setValue(time.minusMinutes(15));
                }
            }

            @Override
            public void increment(int num) {
                if(getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    setValue(time.plusMinutes(15));
                }
            }
        });
        
        startTime.setEditable(true);
        startTime.getValueFactory().setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
        startTime.getValueFactory().setValue(task.getStartTime());
        repeat.setSelected(task.getRepeat());
        
        
        //only allow numbers to be entered into the duration field
        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    duration.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        if(task.getType() == Task.RECURRING_TASK) {
            Recurring RTask = (Recurring) task;
            frequency = new TextField(Integer.toString(RTask.getFrequency()));
        //only allow numbers to be entered into the frequency field
        frequency.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    frequency.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        }
        
        //add labels for name and duration text fields
        Label nameLabel = new Label("Name: ", name), durationLabel = new Label("Duration (Minutes): ", duration),
                startDateLabel = new Label("Start Date: ", startDate), endDateLabel = new Label("End Date: ", endDate),
                startTimeLabel = new Label("Start Time: ", startTime), frequencyLabel = new Label("Frequency (Days): ", frequency);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        durationLabel.setContentDisplay(ContentDisplay.RIGHT);
        startDateLabel.setContentDisplay(ContentDisplay.RIGHT);
        endDateLabel.setContentDisplay(ContentDisplay.RIGHT);
        endDateLabel.setVisible(false);
        frequencyLabel.setContentDisplay(ContentDisplay.RIGHT);
        frequencyLabel.setVisible(false);
        
        //group buttons together
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(close,delete, cancelTask, create);
        
        //show/hide end date selector based on whether the task is set to repeat
        if(task.getRepeat()) {
            endDateLabel.setVisible(true);
            frequencyLabel.setVisible(true);
        } else {
            endDateLabel.setVisible(false);
            frequencyLabel.setVisible(false);
        }
        
        //listener to show end date selector if set to repeat
        repeat.selectedProperty().addListener(v -> {
           if(repeat.isSelected()) {
               endDateLabel.setVisible(true);
               frequencyLabel.setVisible(true);
           } else {
               endDateLabel.setVisible(false);
               frequencyLabel.setVisible(false);
           }
        });
        
        //build final pane
        VBox taskPane = new VBox(10);
        taskPane.setAlignment(Pos.CENTER);
        taskPane.getChildren().addAll(nameLabel, startDateLabel, startTimeLabel, durationLabel, repeat, endDateLabel, frequencyLabel, buttons);
        taskPane.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
        
        BorderPane.setMargin(taskPane, new Insets(0.0, 10.0, 10.0, 0.0));
        rightPane.setCenter(taskPane);
        
        //handles saving an edit
        create.setOnAction(v -> {
        	boolean done = true;
        	control.removeTask(task);
        	try {
        		if(getRepeat()) {
        			control.checkOverlap(createRTask());
        		}else {
        			control.checkOverlap(createTTask());
        		}
        	}catch(Exception e){
        		//check for errors
        		popupError("Error Adding Task: \n" + e.getMessage() + "\nMake sure duration or frequency is not empty");
        		done = false;
        	}
        	//close and refresh page if done
        	if(done) {
	        	buildView();
	        	drawTasks();
	        	rightPane.setCenter(null);
        	}
        });
        
        //handles creating antitask
        cancelTask.setOnAction(v -> {
        	if(task.getType() == Task.ANTI_TASK) {
        		Anti temp = (Anti) task;
        		control.unSuppress(temp.getCancelled());
        		control.removeTask(task);
        	}else {
        		control.addTask(createATask(task));
        	}
        	buildView();
        	drawTasks();
        	rightPane.setCenter(null);
        });
        
        delete.setOnAction(v -> {
        	control.removeTask(task);
        	buildView();
        	drawTasks();
        	rightPane.setCenter(null);
        });
        
        //close pane when cancel button pressed
        close.setOnAction(event -> rightPane.setCenter(null));
    }
    
    //popup error message
    public static void popupError(String errorMsg) {
        //error message to show
        Text error = new Text(errorMsg);
        error.setFont(Font.font("Courier", 20));
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
	
	private Task createTTask() {
		Transient newTask = new Transient(getName(), getStartDate(), getEndDate(), getDuration(),
    			getRepeat(), getStartTime());
    	return newTask;
	}
	
	private Task createRTask() {
		Recurring newTask = new Recurring(getName(), getStartDate(), getEndDate(), getDuration(),
    			getRepeat(), getStartTime(), getFrequency());
    	return newTask;
	}
	
	private Task createATask(Task task) {
		Anti newTask = new Anti(getName(), getStartDate(), getEndDate(), getDuration(),
    			getRepeat(), getStartTime(), task);
    	return newTask;
	}

	//getters
	
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
	
	public Button getClose() {
	    return close;
	}
	
	public Button getCancelTask() {
	    return cancelTask;
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
	
	public LocalDate getStartDate() {
	    return startDate.getValue();
	}
	
	public LocalDate getEndDate() {
	    return endDate.getValue();
	}
	
	public LocalTime getStartTime() {
	    return startTime.getValue();
	}
    
    public ComboBox<String> getSelect() {
        return select;
    }
    
    public int getFrequency() {
    	return Integer.parseInt(frequency.getText());
    }
}
