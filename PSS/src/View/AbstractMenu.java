package pss;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import java.util.Calendar;
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

public abstract class AbstractMenu {
	//scene and stage for each menu
	protected Scene scene;
	protected Stage stage;
	protected ArrayList<Task> tasks;
	protected GridPane miniMonth;
	protected BorderPane pane, rightPane;
	protected ComboBox<String> select;
	protected Calendar date;
	protected int numOfWeeks, firstDay;
	protected Button left, right, addTask, cancel, create;
	protected final int MINI_SIZE = 35;
	protected TextField name, duration;
	protected CheckBox repeat;
	protected DatePicker startDate, endDate;
	
	AbstractMenu() {
        pane = new BorderPane();
        select = new ComboBox<>();
        select.getItems().addAll("Day", "Week", "Month");
        date = Calendar.getInstance();
        
        Calendar temp = (Calendar) this.date.clone();
        temp.set(Calendar.DAY_OF_MONTH, temp.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastWeek = temp.get(Calendar.WEEK_OF_YEAR);
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeek = temp.get(Calendar.WEEK_OF_YEAR);
        
        if(date.get(Calendar.MONTH) == 11 && lastWeek == 1) {
            lastWeek += 52;
        }
        
        numOfWeeks = lastWeek - firstWeek + 1;
        
        //the controls at the top of the ui to switch views/months
        left = new Button("<");
        right = new Button(">");
        addTask = new Button("Add Task");
        Text titleText = new Text(date.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("en")) + " " + date.get(Calendar.YEAR));
        titleText.setStyle("-fx-font: 20px \"Courier\";");
        
        HBox title = new HBox(10);
        title.getChildren().addAll(left, right, titleText, addTask, select);
        pane.setTop(title);
        BorderPane.setMargin(title, new Insets(10.0, 10.0, 10.0, 10.0));
        buildMiniMonth();
    }
	
	AbstractMenu(Date date) {
	    pane = new BorderPane();
	    rightPane = new BorderPane();
        select = new ComboBox<>();
        select.getItems().addAll("Day", "Week", "Month");
        this.date = Calendar.getInstance();
        this.date.setTime(date);
        
        Calendar temp = (Calendar) this.date.clone();
        temp.set(Calendar.DAY_OF_MONTH, temp.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastWeek = temp.get(Calendar.WEEK_OF_YEAR);
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeek = temp.get(Calendar.WEEK_OF_YEAR);
        
        if(this.date.get(Calendar.MONTH) == 11 && lastWeek == 1) {
            lastWeek += 52;
        }
        
        numOfWeeks = lastWeek - firstWeek + 1;
        
        //the controls at the top of the ui to switch views/months
        left = new Button("<");
        right = new Button(">");
        addTask = new Button("Add Task");
        Text titleText = new Text(this.date.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("en")) + " " + this.date.get(Calendar.YEAR));
        titleText.setStyle("-fx-font: 20px \"Courier\";");
        
        HBox title = new HBox(10);
        title.getChildren().addAll(left, right, titleText, addTask, select);
        pane.setTop(title);
        BorderPane.setMargin(title, new Insets(10.0, 10.0, 10.0, 10.0));
        buildMiniMonth();
    }
	
	public void buildMiniMonth() {
        miniMonth = new GridPane();
        Text[] labels = {new Text("S"), new Text("M"), new Text("T"), new Text("W"), new Text("T"), new Text("F"), new Text("S")};
        
        Calendar prevMonth = (Calendar) date.clone();
        prevMonth.set(Calendar.MONTH, date.get(Calendar.MONTH) - 1);
        
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        date.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH), dayCounter = 1, outOfRangeCounter = 1;
        firstDay = date.get(Calendar.DAY_OF_WEEK) - 1;
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

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
            //make the empty cells for each day of the week
            for(int j = 0; j < 7; j++) {
                StackPane box = new StackPane();
                box.setPrefSize(MINI_SIZE, MINI_SIZE);
                box.setMinSize(MINI_SIZE, MINI_SIZE);
                box.setMaxSize(MINI_SIZE, MINI_SIZE);
                box.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
                
                if(i == 0 && j < firstDay) {
                    Text day = new Text(Integer.toString(prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - firstDay + j + 1));
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
	
	//used to build the ui for each menu
	public abstract void buildMenu();
	
	public abstract void drawTasks();

	//stage getter for each menu
	public Stage getStage() {
		return stage;
	}
	
	//scene getter
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
    
    public void clearCalendar() {
        //remove current calendar and course list
        pane.setLeft(null);
        pane.setRight(null);
    }
    
    public void addTask() {
        //button to close popup
        cancel = new Button("Cancel");
        create = new Button("Create");
        name = new TextField();
        duration = new TextField();
        repeat = new CheckBox("Repeat");
        startDate = new DatePicker(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        endDate = new DatePicker(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        
        endDate.setVisible(false);
        repeat.setSelected(false);
        
        repeat.selectedProperty().addListener(v -> {
           if(repeat.isSelected()) {
               endDate.setVisible(true);
           } else {
               endDate.setVisible(false);
           }
        });
        
        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    duration.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        Label nameLabel = new Label("Name: ", name), durationLabel = new Label("Duration (Minutes): ", duration);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        durationLabel.setContentDisplay(ContentDisplay.RIGHT);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancel, create);
        
        //add error message and button to a vbox
        VBox taskPane = new VBox(10);
        taskPane.setAlignment(Pos.CENTER);
        taskPane.getChildren().addAll(nameLabel, startDate, durationLabel, repeat, endDate, buttons);
        taskPane.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
        
        BorderPane.setMargin(taskPane, new Insets(0.0, 10.0, 10.0, 0.0));
        rightPane.setCenter(taskPane);
        
        //close popup when ok button pressed
        cancel.setOnAction(event -> {
            rightPane.setCenter(null);
        });
    }
    
    public void addTask(Task task) {
        //button to close popup
        cancel = new Button("Cancel");
        create = new Button("Edit");
        name = new TextField(task.getName());
        duration = new TextField(Integer.toString(task.getDuration()));
        repeat = new CheckBox("Repeat");
        startDate = new DatePicker(LocalDate.ofInstant(task.getStartDate().toInstant(), ZoneId.systemDefault()));
        endDate = new DatePicker(LocalDate.ofInstant(task.getEndDate().toInstant(), ZoneId.systemDefault()));
        
        repeat.setSelected(task.getRepeat());
        
        if(task.getRepeat()) {
            endDate.setVisible(true);
        } else {
            endDate.setVisible(false);
        }
        
        repeat.selectedProperty().addListener(v -> {
           if(repeat.isSelected()) {
               endDate.setVisible(true);
           } else {
               endDate.setVisible(false);
           }
        });
        
        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    duration.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        Label nameLabel = new Label("Name: ", name), durationLabel = new Label("Duration (Minutes): ", duration);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        durationLabel.setContentDisplay(ContentDisplay.RIGHT);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancel, create);
        
        //add error message and button to a vbox
        VBox taskPane = new VBox(10);
        taskPane.setAlignment(Pos.CENTER);
        taskPane.getChildren().addAll(nameLabel, startDate, durationLabel, repeat, endDate, buttons);
        taskPane.setStyle("-fx-border-width: 1px; -fx-border-color: darkgray");
        
        BorderPane.setMargin(taskPane, new Insets(0.0, 10.0, 10.0, 0.0));
        rightPane.setCenter(taskPane);
        
        //close popup when ok button pressed
        cancel.setOnAction(event -> {
            rightPane.setCenter(null);
        });
    }
	
	public static void popupError(String errorMsg) {
		//error message for popup
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
		quitError.setOnAction(event -> {
			popup.close();
		});
	}
}
