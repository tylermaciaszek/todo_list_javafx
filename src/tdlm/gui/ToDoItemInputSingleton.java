package tdlm.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import properties_manager.PropertiesManager;
import static saf.settings.AppPropertyType.APP_CSS;
import static saf.settings.AppPropertyType.APP_PATH_CSS;
import tdlm.PropertyType;
import tdlm.controller.ToDoListController;
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
    JFXTextField categoryInput;
    JFXTextField descriptionInput;
    DatePicker initialDate;
    DatePicker endDate;
    //Button submit;
    JFXButton cancel;
    JFXCheckBox completed;
    
    JFXButton submit;
    
    //Boolean
    boolean complete; 
    boolean okClicked;
    
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
    
    public TextField getCategoryInputNode(){
        return categoryInput;
    }
    
    public TextField getDescriptioInputNode(){
        return descriptionInput;
    }
    
    public CheckBox getChecked(){
        return completed;
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
        singleton = null;
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        
        // LABEL TO DISPLAY THE CUSTOM MESSAGE
        category = new Label(props.getProperty(PropertyType.CATEGORY_LABEL));
        description = new Label(props.getProperty(PropertyType.DESCRIPTION_LABEL));
        completeLabel = new Label(props.getProperty(PropertyType.COMPLETE_LABEL));
        initialDateLabel = new Label(props.getProperty(PropertyType.START_DATE_LABEL));
        endDateLabel = new Label(props.getProperty(PropertyType.END_DATE_LABEL));


        //Date Pickers
        initialDate = new DatePicker();
        endDate = new DatePicker();
        
        //Textfields
        categoryInput = new JFXTextField();
        descriptionInput = new JFXTextField();
        
        //Button
        submit = new JFXButton(props.getProperty(PropertyType.SUBMIT_BUTTON));
        cancel = new JFXButton(props.getProperty(PropertyType.CANCEL_BUTTON));
        
        okClicked = false;
        
        //Check Box
        completed = new JFXCheckBox();
        
        //TESTING
        VBox test = new VBox();
        test.getChildren().add(initialDate);
        
        

        // WE'LL PUT EVERYTHING HERE
        dialogForm = new GridPane();
        dialogForm.add(category, 0, 0);
        dialogForm.add(categoryInput, 1, 0);
        dialogForm.add(description, 0, 1);
        dialogForm.add(descriptionInput, 1, 1);
        dialogForm.add(initialDateLabel, 0, 2);
        dialogForm.add(test, 1, 2);
        dialogForm.add(endDateLabel, 0, 3);
        dialogForm.add(endDate, 1, 3);
        dialogForm.add(completeLabel, 0, 4);
        dialogForm.add(completed, 1, 4);
        dialogForm.add(submit, 0, 5);
        dialogForm.add(cancel, 1, 5);
        
        //Style
        dialogForm.setHgap(20);
        dialogForm.setVgap(20);
        dialogForm.setPadding(new Insets(15, 15, 15, 15));
        this.setWidth(320);
        this.setHeight(311);
           
        // AND PUT IT IN THE WINDOW
        Scene formScene = new Scene(dialogForm);
        this.setScene(formScene);
        
        //Make cancel do something
        cancel.setOnAction(e ->{
            okClicked = false;
            this.hide();
        });
        
        //Make submit do something
        submit.setOnAction(e -> {
            okClicked = true;
            data = new ToDoItem();
            data.setCategory(categoryInput.getText());
            data.setDescription(descriptionInput.getText());
            data.setStartDate(initialDate.getValue());
            data.setEndDate(endDate.getValue());
            complete = completed.isSelected();
            data.setCompleted(complete);
            this.hide();
        });
        
    }
    
    public ToDoItem getItem(){
        return this.data;
    }
    
    public boolean getOkClicked(){
        return okClicked;
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
