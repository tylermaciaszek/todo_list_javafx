package tdlm.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import tdlm.controller.ToDoListController;
import tdlm.data.DataManager;
import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import tdlm.PropertyType;
import tdlm.data.ToDoItem;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    static final String CLASS_PROMPT_LABEL = "prompt_label";
    static final String EMPTY_TEXT = "";
    static final int LARGE_TEXT_FIELD_LENGTH = 20;
    static final int SMALL_TEXT_FIELD_LENGTH = 5;

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;
    
    // THIS CONTROLLER PROVIDES THE RESPONSES TO INTERACTIONS
    ToDoListController toDoListController;
    
    // THIS IS OUR WORKSPACE HEADING
    Label headingLabel;
    
    // THIS HAS OUR TODO LIST DETAILS
    VBox detailsBox;
    Label detailsLabel;
    HBox nameAndOwnerBox;
    HBox nameBox;
    Label nameLabel;
    TextField nameTextField;
    HBox ownerBox;
    Label ownerLabel;
    TextField ownerTextField;

     // THIS REGION IS FOR MANAGING TODO ITEMS
    VBox itemsBox;
    Label itemsLabel;
    HBox itemsToolbar;
    Button addItemButton;
    Button removeItemButton;
    Button moveUpItemButton;
    Button moveDownItemButton;
    TableView<ToDoItem> itemsTable;
    TableColumn itemCategoryColumn;
    TableColumn itemDescriptionColumn;
    TableColumn itemStartDateColumn;
    TableColumn itemEndDateColumn;
    TableColumn itemCompletedColumn;

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;    
    
    //Added by Tyler for the dialog form
    Label descriptionInput;
    Label categoryInput;
    LocalDate createdInput;
    LocalDate endInput;
    boolean completedInput;

    
    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();

        // INIT ALL WORKSPACE COMPONENTS
	layoutGUI();
        
        // AND SETUP EVENT HANDLING
	setupHandlers();
    }
    
    private void layoutGUI() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
	// FIRST THE LABEL AT THE TOP
        headingLabel = new Label();
        headingLabel.setText(props.getProperty(PropertyType.WORKSPACE_HEADING_LABEL));        

        // THEN THE TODO LIST DETAILS
        detailsBox = new VBox();
        detailsLabel = new Label();
        detailsLabel.setText(props.getProperty(PropertyType.DETAILS_HEADING_LABEL));
        
        // THIS WILL CONTAIN BOTH
        nameAndOwnerBox = new HBox();
        
        // THIS JUST THE NAME
        nameBox = new HBox();
        nameLabel = new Label();
        nameLabel.setText(props.getProperty(PropertyType.NAME_PROMPT));
        nameTextField = new TextField();
        nameBox.getChildren().addAll(nameLabel, nameTextField);

        // THIS JUST THE OWNER
        ownerBox = new HBox();
        ownerLabel = new Label(props.getProperty(PropertyType.OWNER_PROMPT));
        ownerTextField = new TextField();
        ownerBox.getChildren().addAll(ownerLabel, ownerTextField);
        
        // ARRANGE THE CONTENTS OF BOTH ON A SINGLE LINE
        nameAndOwnerBox.getChildren().addAll(nameBox, ownerBox);
        
        // NOW ORGANIZE THE CONTENTS OF detailsBox
        detailsBox.getChildren().add(detailsLabel);
        detailsBox.getChildren().add(nameAndOwnerBox);
 
        // NOW THE CONTROLS FOR ADDING LECTURES
        itemsBox = new VBox();
        itemsLabel = new Label(props.getProperty(PropertyType.ITEMS_HEADING_LABEL));
        itemsToolbar = new HBox();
        addItemButton = gui.initChildButton(itemsToolbar, PropertyType.ADD_ICON.toString(), PropertyType.ADD_ITEM_TOOLTIP.toString(), false);
        removeItemButton = gui.initChildButton(itemsToolbar, PropertyType.REMOVE_ICON.toString(), PropertyType.REMOVE_ITEM_TOOLTIP.toString(), true);
        moveUpItemButton = gui.initChildButton(itemsToolbar, PropertyType.MOVE_UP_ICON.toString(), PropertyType.MOVE_UP_ITEM_TOOLTIP.toString(), true);
        moveDownItemButton = gui.initChildButton(itemsToolbar, PropertyType.MOVE_DOWN_ICON.toString(), PropertyType.MOVE_DOWN_ITEM_TOOLTIP.toString(), true);
        itemsTable = new TableView();
        itemsBox.getChildren().add(itemsLabel);
        itemsBox.getChildren().add(itemsToolbar);
        itemsBox.getChildren().add(itemsTable);
        
        // NOW SETUP THE TABLE COLUMNS
        itemCategoryColumn = new TableColumn(props.getProperty(PropertyType.CATEGORY_COLUMN_HEADING));
        itemDescriptionColumn = new TableColumn(props.getProperty(PropertyType.DESCRIPTION_COLUMN_HEADING));
        itemStartDateColumn = new TableColumn(props.getProperty(PropertyType.START_DATE_COLUMN_HEADING));
        itemEndDateColumn = new TableColumn(props.getProperty(PropertyType.END_DATE_COLUMN_HEADING));
        itemCompletedColumn = new TableColumn(props.getProperty(PropertyType.COMPLETED_COLUMN_HEADING));
        
        // AND LINK THE COLUMNS TO THE DATA
        itemCategoryColumn.setCellValueFactory(new PropertyValueFactory<String, String>("category"));
        itemDescriptionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("description"));
        itemStartDateColumn.setCellValueFactory(new PropertyValueFactory<LocalDate, String>("startDate"));
        itemEndDateColumn.setCellValueFactory(new PropertyValueFactory<LocalDate, String>("endDate"));
        itemCompletedColumn.setCellValueFactory(new PropertyValueFactory<Boolean, String>("completed"));
        itemsTable.getColumns().add(itemCategoryColumn);
        itemsTable.getColumns().add(itemDescriptionColumn);
        itemsTable.getColumns().add(itemStartDateColumn);
        itemsTable.getColumns().add(itemEndDateColumn);
        itemsTable.getColumns().add(itemCompletedColumn);
        DataManager dataManager = (DataManager)app.getDataComponent();
        itemsTable.setItems(dataManager.getItems());

	// AND NOW SETUP THE WORKSPACE
	workspace = new VBox();
        workspace.getChildren().add(headingLabel);
        workspace.getChildren().add(detailsBox);
        workspace.getChildren().add(itemsBox);
    }
        
    //Set up dialogForm
    
    
    
    //Show the dialogForm
   
    
        
    
    public void setDebugText(String text) {
	debugText.setText(text);
    }
    
    
    private void setupHandlers() {
	// MAKE THE CONTROLLER
	toDoListController = new ToDoListController(app);
	
	// NOW CONNECT THE BUTTONS TO THEIR HANDLERS
        addItemButton.setOnAction(e->{
            try {
                toDoListController.processAddItem();
            } catch (Exception ex) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        removeItemButton.setOnAction(e->{
            toDoListController.processRemoveItem();
        });
        moveUpItemButton.setOnAction(e->{
            toDoListController.processMoveUpItem();
        });
        moveDownItemButton.setOnAction(e->{
            toDoListController.processMoveDownItem();
        });
        
        itemsTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                toDoListController.processEditItem();
            }
        });
    }
    
    public void setImage(ButtonBase button, String fileName) {
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + fileName;
        Image buttonImage = new Image(imagePath);
	
	// SET THE IMAGE IN THE BUTTON
        button.setGraphic(new ImageView(buttonImage));	
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
        
        // FIRST THE WORKSPACE PANE
        workspace.getStyleClass().add(CLASS_BORDERED_PANE);
        
        // THEN THE HEADING
	headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        
        // THEN THE DETAILS PANE AND ITS COMPONENTS
        detailsBox.getStyleClass().add(CLASS_BORDERED_PANE);
        detailsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        nameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        ownerLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        itemsBox.getStyleClass().add(CLASS_BORDERED_PANE);
        itemsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
    }

    public TableView<ToDoItem> getItemsTable() {
        return itemsTable;
    }
    
    

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
	DataManager dataManager = (DataManager)app.getDataComponent();

    }
}
