/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.settingState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

/**
 *
 * @author adam
 */
public class settingsState extends AbstractAppState{
    private final Node rootNode;
    private final Node guiNode;
    private final Node localRootNode = new Node("settings");
    private final AssetManager assetManager;
    
    public settingsState(SimpleApplication app){
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
    } 
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        rootNode.attachChild(localRootNode);
        
        GuiGlobals.initialize((Application) app); 
        BaseStyles.loadGlassStyle(); 
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
    }
    
    @Override
    public void cleanup(){
        rootNode.detachChild(localRootNode);
        super.cleanup();
        
    }
}
