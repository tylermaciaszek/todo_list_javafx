package tdlm.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
        
    public void processAddItem() {	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(); 
        ToDoItemInputSingleton dialogForm = ToDoItemInputSingleton.getSingleton();
        dialogForm.init(app.getGUI().getWindow());
	dialogForm.show("test");
        DataManager dataManager = (DataManager)app.getDataComponent();
        //dataManager.addItem(dialogForm.getItem());
        //System.out.print(dataManager.getItems());
        
        
    }
    
    public void processRemoveItem() {
        
    }
    
    public void processMoveUpItem() {
        
    }
    
    public void processMoveDownItem() {
        
    }
    
    public void processEditItem() {
        
    }
   
}
