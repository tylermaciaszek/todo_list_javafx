package tdlm.controller;

import java.io.File;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import properties_manager.PropertiesManager;
import tdlm.data.DataManager;
import tdlm.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppFileComponent;
import saf.controller.AppFileController;
import saf.ui.AppMessageDialogSingleton;
import tdlm.PropertyType;
import tdlm.data.ToDoItem;
import tdlm.data.DataManager;
import tdlm.gui.ToDoItemInputSingleton;

/**
 * This class responds to interactions with todo list editing controls.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class ToDoListController {
    AppTemplate app;
    PropertiesManager props = PropertiesManager.getPropertiesManager();

    
    public ToDoListController(AppTemplate initApp) {
	app = initApp;
    }
        
    public void processAddItem() throws Exception {	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        ToDoItemInputSingleton dialogForm = ToDoItemInputSingleton.getSingleton();
        dialogForm.init(app.getGUI().getWindow());
	dialogForm.show(props.getProperty(PropertyType.ADD_NEW_ITEM));
        DataManager dataManager = (DataManager) app.getDataComponent();
        if(dialogForm.getOkClicked()){
        ToDoItem data = dialogForm.getItem();
        dataManager.addItem(data);
        workspace.getItemsTable().setItems(dataManager.getItems());
        AppFileController fileController = new AppFileController(app);
        fileController.markAsEdited(app.getGUI());
        }
    }
    
    public void processRemoveItem() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        workspace.getItemsTable().getItems().remove(workspace.getItemsTable().getSelectionModel().getSelectedIndex());
        //buttonDisable();
        AppFileController fileController = new AppFileController(app);
        fileController.markAsEdited(app.getGUI());
    }
    
    public void processMoveUpItem() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        int selectedIndex = workspace.getItemsTable().getSelectionModel().getSelectedIndex();
        ToDoItem oldItem = workspace.getItemsTable().getItems().get((selectedIndex-1));
        ToDoItem selectedItem = workspace.getItemsTable().getSelectionModel().getSelectedItem();
        workspace.getItemsTable().getItems().remove(selectedIndex);
        workspace.getItemsTable().getItems().remove(selectedIndex-1);
        workspace.getItemsTable().getItems().add(selectedIndex-1, selectedItem);
        workspace.getItemsTable().getItems().add(selectedIndex, oldItem);
        buttonDisable(selectedIndex-1);
        AppFileController fileController = new AppFileController(app);
        fileController.markAsEdited(app.getGUI());
    }
    
    public void processMoveDownItem() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        int selectedIndex = workspace.getItemsTable().getSelectionModel().getSelectedIndex();
        int newIndex = workspace.getItemsTable().getSelectionModel().getSelectedIndex()+1;
        ToDoItem oldItem = workspace.getItemsTable().getItems().get((newIndex));
        ToDoItem selectedItem = workspace.getItemsTable().getSelectionModel().getSelectedItem();
        workspace.getItemsTable().getItems().remove(oldItem);
        workspace.getItemsTable().getItems().remove(selectedItem);
        workspace.getItemsTable().getItems().add(selectedIndex, oldItem); 
        workspace.getItemsTable().getItems().add(newIndex, selectedItem);
        buttonDisable(newIndex);
        AppFileController fileController = new AppFileController(app);
        fileController.markAsEdited(app.getGUI());
    }
    
    public void processEditItem() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        TableView table = workspace.getItemsTable();
        ToDoItem editItem = (ToDoItem) table.getSelectionModel().getSelectedItem();
        DataManager dataManager = (DataManager) app.getDataComponent();
        ToDoItemInputSingleton editItemDialog = ToDoItemInputSingleton.getSingleton();
        editItemDialog.init(app.getGUI().getWindow());
        editItemDialog.getCategoryInputNode().setText(editItem.getCategory());
        editItemDialog.getDescriptioInputNode().setText(editItem.getDescription());
        editItemDialog.getInitialDate().setValue(editItem.getStartDate());
        editItemDialog.getEndDate().setValue(editItem.getEndDate());
        editItemDialog.getChecked().setSelected(editItem.getCompleted());
        editItemDialog.show(props.getProperty(PropertyType.EDIT_ITEM));
        if(editItemDialog.getOkClicked()){
        dataManager.getItems().set(table.getSelectionModel().getSelectedIndex(), editItemDialog.getItem());
        AppFileController fileController = new AppFileController(app);
        fileController.markAsEdited(app.getGUI());
        }
    }
    
    public void buttonDisable(int selectInt){
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        workspace.getItemsTable().getSelectionModel().clearSelection();
        workspace.getItemsTable().getSelectionModel().select(selectInt);
        if(selectInt == 0)
        workspace.getMoveDownItemButton().setDisable(true);
        else if(selectInt == workspace.getItemsTable().getItems().size())
        workspace.getMoveUpItemButton().setDisable(true);
    } 
}
