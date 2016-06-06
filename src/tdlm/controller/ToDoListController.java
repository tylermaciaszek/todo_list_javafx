package tdlm.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TablePosition;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import tdlm.data.DataManager;
import tdlm.gui.Workspace;
import saf.AppTemplate;
import saf.ui.AppMessageDialogSingleton;
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
    
    public ToDoListController(AppTemplate initApp) {
	app = initApp;
    }
        
    public void processAddItem() throws Exception {	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        ToDoItemInputSingleton dialogForm = ToDoItemInputSingleton.getSingleton();
        dialogForm.init(app.getGUI().getWindow());
	dialogForm.show("Add New Item");
        DataManager dataManager = (DataManager) app.getDataComponent();
        ToDoItem data = dialogForm.getItem();
        dataManager.addItem(data);
        workspace.getItemsTable().setItems(dataManager.getItems());
    }
    
    public void processRemoveItem() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        workspace.getItemsTable().getItems().remove(workspace.getItemsTable().getSelectionModel().getSelectedIndex());
        buttonDisable();
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
        buttonDisable();
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
        buttonDisable();
    }
    
    public void processEditItem() {
        
    }
    
    public void buttonDisable(){
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        workspace.getItemsTable().getSelectionModel().clearSelection();
        workspace.getMoveDownItemButton().setDisable(true);
        workspace.getMoveUpItemButton().setDisable(true);
        workspace.getRemoveItemButton().setDisable(true);
    } 
}
