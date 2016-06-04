package tdlm.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import tdlm.data.ToDoItem;


/**
 * This class is used to create and make the dialog to get input that creates
 * a new ToDoItem that will be imputed into the table
 *
 * @author Tyler Maciaszek
 * 
 * @version 1.0
 */
public class ToDoItemInputSingleton extends Stage {
    // HERE'S THE SINGLETON OBJECT
    static ToDoItemInputSingleton singleton = null;
    
    // HERE ARE THE DIALOG COMPONENTS
    GridPane dialogForm;
    Label category;
    Label description;
    Label completeLabel;
    Label initialDateLabel;
    Label endDateLabel;
    TextField categoryInput;
    TextField descriptionInput;
    DatePicker initialDate;
    DatePicker endDate;
    Button submit;
    RadioButton rbYes;
    RadioButton rbNo;
    HBox buttonHolder;
    
    //Data Component
    ToDoItem data;

    public TextField getCategoryInput() {
        return categoryInput;
    }

    public TextField getDescriptionInput() {
        return descriptionInput;
    }

    public DatePicker getInitialDate() {
        return initialDate;
    }

    public DatePicker getEndDate() {
        return endDate;
    }
    
    /**
     * Initializes this dialog so that it can be used repeatedly
     * for all kinds of messages. Note this is a singleton design
     * pattern so the constructor is private.
     * 
     */
    private ToDoItemInputSingleton() {}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static ToDoItemInputSingleton getSingleton() {
	if (singleton == null)
	    singleton = new ToDoItemInputSingleton();
	return singleton;
    }
    
    /**
     * This function fully initializes the singleton dialog for use.
     * 
     * @param owner The window above which this dialog will be centered.
     */
    public void init(Stage owner) {
        // MAKE IT MODAL
        if(singleton != null){
            
        }else{
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        }
        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        category = new Label("Category");
        description = new Label("Description");
        completeLabel = new Label("Completed");
        initialDateLabel = new Label("Start Date");
        endDateLabel = new Label("End Date");


        //Date Pickers
        initialDate = new DatePicker();
        endDate = new DatePicker();
        
        //Textfields
        categoryInput = new TextField();
        descriptionInput = new TextField();
        
        //Button
        submit = new Button("Submit");
        
        //RadioButtons
        rbYes = new RadioButton("Yes");
        rbNo = new RadioButton("No");
        ToggleGroup completedButtons;
        completedButtons = new ToggleGroup();
        buttonHolder = new HBox(5);
        rbYes.setToggleGroup(completedButtons);
        rbNo.setToggleGroup(completedButtons);
        buttonHolder.getChildren().addAll(rbYes, rbNo);
        

        // WE'LL PUT EVERYTHING HERE
        dialogForm = new GridPane();
        dialogForm.add(category, 0, 0);
        dialogForm.add(categoryInput, 1, 0);
        dialogForm.add(description, 0, 1);
        dialogForm.add(descriptionInput, 1, 1);
        dialogForm.add(initialDateLabel, 0, 2);
        dialogForm.add(initialDate, 1, 2);
        dialogForm.add(endDateLabel, 0, 3);
        dialogForm.add(endDate, 1, 3);
        dialogForm.add(completeLabel, 0, 4);
        dialogForm.add(buttonHolder, 1, 4);
        dialogForm.add(submit, 0, 5, 2, 1);
        
        //Style
        dialogForm.setHgap(20);
        dialogForm.setVgap(20);
        dialogForm.setPadding(new Insets(15, 15, 15, 15));
        this.setWidth(320);
        this.setHeight(310);
           
        // AND PUT IT IN THE WINDOW
        Scene formScene = new Scene(dialogForm);
        this.setScene(formScene);
        
        //Make submit do something
        submit.setOnAction(e -> {
            this.hide();
        });
        
    }
    
    public ToDoItem getItem(){
        return data;
    }
 
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param title The title to appear in the dialog window.
     * 
     */
    public void show(String title) {
	// SET THE DIALOG TITLE BAR TITLE
	setTitle(title);
	
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}
