/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.GAMEOVER;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.BorderLayout;
import mygane.menuState.mainMenu;

/**
 *
 * @author adam
 */
public class youLose extends AbstractAppState {
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private InputManager inputManager;
    
    private Node guiNode;
    private Node rootNode;
    
    public youLose (SimpleApplication app){
        stateManager = app.getStateManager();
        rootNode  =  app.getRootNode(); 
        guiNode  =  app.getGuiNode(); 
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
    }
    @Override
    public void initialize(final AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        initKeys();
        Container ylScreen = new Container(new BorderLayout());
        ylScreen.setLocalTranslation(50,250, 0);
        ylScreen.addChild(new Label("YOU SUCK!"), BorderLayout.Position.North);
        Button iKnow = ylScreen.addChild(new Button("I Know"));
        iKnow.addClickCommands(new Command<Button>(){
            @Override
            public void execute(Button source){
               SimpleApplication app = (SimpleApplication) stateManager.getApplication();
               stateManager.detach(stateManager.getState(youLose.class));
               stateManager.attach(new mainMenu((SimpleApplication) stateManager.getApplication()));
            }});
    }
    
    @Override
    public void cleanup(){
        super.cleanup();
    }
    @Override
    public void update(float tpf){
        super.update(tpf);
    }
    
    private void initKeys(){
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
    }
}
