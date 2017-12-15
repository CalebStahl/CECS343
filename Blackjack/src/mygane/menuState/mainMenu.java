/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygane.menuState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.core.GuiComponent;
import com.simsilica.lemur.style.BaseStyles;
import net.java.games.input.Component;
import mygame.gameState.playState;
import mygame.settingState.settingsState;

/**
 *
 * @author adam
 */
public class mainMenu extends AbstractAppState{
    private final Node rootNode;
    private final Node guiNode;
    private final Node localRootNode = new Node("main menu");
    private Node pivot = new Node("pivot");
    private final AssetManager assetManager;
    private final AppStateManager stateManager;
    private boolean playAppState = false;
    private boolean settingsAppState = false;
    private boolean exitState = false;
    private SimpleApplication app;
    
    protected Spatial pokerChip1;
    protected Spatial pokerChip2;
    protected Spatial pokerChip3;
    protected Spatial pokerChip4;
    
    protected int mmB=0;
    
    /**
     * First function that is called when when changing play states.
     * Gets managers and relevant nodes from main.
     * @param - SimpleApplication
     */

    public mainMenu(SimpleApplication app){
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
        stateManager = app.getStateManager();
    }
    
    /**
     * Called after mainMenu finishes.
     */
    @Override
    public void initialize(final AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        rootNode.attachChild(localRootNode);  //Add main menu to screen        
        localRootNode.attachChild(pivot);     //Attaching Cards to main menu
        this.app = (SimpleApplication) app;
        createLight(); 
        createTable();
        
        GuiGlobals.initialize((Application) app); 
        BaseStyles.loadGlassStyle(); 
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        
//        Container title = new Container(new BoxLayout(Axis.X, FillMode.Even));
//        Container title2 = new Container (new BoxLayout(Axis.X, FillMode.Even));
//        title.addChild(title2);
//        Label titleLabel = title2.addChild(new Label("Blackjack"));
//        title.setLocalTranslation(100f, 250f, 0f);
//        title.scale(1.5f);
        //title.setAlpha(100);
       // title.scale(2);
        //guiNode.attachChild(title);
        
        //Generating Cards for Main Menu Scene
        pokerChip1 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat1.setTexture("ColorMap", assetManager.loadTexture("Textures/green.jpg"));
        pokerChip1.setMaterial(pokerMat1);
        pokerChip1.setLocalTranslation(-3.5f, 1.0f, 0.0f);
        pokerChip1.rotate(-4.8f,0.0f,0.0f);
        pivot.attachChild(pokerChip1);
        
        pokerChip2 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat2.setTexture("ColorMap", assetManager.loadTexture("Textures/black.jpg"));
        pokerChip2.setMaterial(pokerMat2);
        pokerChip2.setLocalTranslation(-1.3f, 1.0f, 0.0f);
        pokerChip2.rotate(-4.8f,0.0f,0.0f);
        pivot.attachChild(pokerChip2);
            
        pokerChip3 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat3.setTexture("ColorMap", assetManager.loadTexture("Textures/red.jpg"));
        pokerChip3.setMaterial(pokerMat3);
        pokerChip3.setLocalTranslation(1.0f, 1.0f, 0.0f);
        pokerChip3.rotate(-4.8f,0.0f,0.0f);
        pivot.attachChild(pokerChip3);
        
        pokerChip4 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat4.setTexture("ColorMap", assetManager.loadTexture("Textures/blue.jpg"));
        pokerChip4.setMaterial(pokerMat4);
        pokerChip4.setLocalTranslation(3.5f, 1.0f, 0.0f);
        pokerChip4.rotate(-4.8f,0.0f,0.0f);
        pivot.attachChild(pokerChip4);
        
        Container mmWindow = new Container(new BoxLayout(Axis.X, FillMode.Even));
        guiNode.attachChild(mmWindow);
        mmWindow.setLocalTranslation(120, 70, 0);
        Button playGame = mmWindow.addChild(new Button("Play Game"));
        //Button settings = mmWindow.addChild(new Button("Settings"));
        playGame.addClickCommands(new Command<Button>(){
            @Override
            public void execute(Button source){
                SimpleApplication app =(SimpleApplication) stateManager.getApplication();
                stateManager.detach(stateManager.getState(mainMenu.class));
                stateManager.attach(new playState(app));
            }
        });
//        settings.addClickCommands(new Command<Button>(){
//            @Override
//            public void execute(Button source){
//                SimpleApplication app =(SimpleApplication) stateManager.getApplication();
//                stateManager.detach(stateManager.getState(mainMenu.class));
//                stateManager.attach(new settingsState((SimpleApplication) stateManager.getApplication()));
//            }
//        });
        Container escButton = new Container(new BoxLayout(Axis.X, FillMode.Even));
        guiNode.attachChild(escButton);
        escButton.setLocalTranslation(290, 255, 0);
        Button exit = escButton.addChild(new Button("X"));
        exit.addClickCommands(new Command<Button>(){
            @Override
            public void execute(Button source){
                exitState=true;
            }
        });

        
    }
    
    //Creates the tables the all the cards are on
    public void createTable(){ 
        Box box = new Box(15, .2f, 15); 
        Geometry tableTop =  new Geometry("table", box); 
        tableTop.setLocalTranslation(0, 0, -5); //-5 Allow for card to be in front of the "table"
        tableTop.rotate(FastMath.HALF_PI,0,0); 
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/tabletop-casino.JPG")); 
        tableTop.setMaterial(mat); 
        tableTop.scale(.65f); 
        rootNode.attachChild(tableTop);  
    } 
    
    public void createLight(){ 
        DirectionalLight sun = new DirectionalLight(); 
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f)); 
        rootNode.addLight(sun); 
    }
    
    @Override
    public void cleanup(){
        rootNode.detachChild(localRootNode);
        guiNode.detachAllChildren();
        super.cleanup();
    }
    
    /**
     * Continually called as long as this menu is in view
     * @param tpf 
     */
    @Override
    public void update(float tpf){
        pokerChip1.rotate(0,0,(2*tpf));
        pokerChip2.rotate(0,0,(2*tpf));
        pokerChip3.rotate(0,0,(2*tpf));
        pokerChip4.rotate(0,0,(2*tpf));
        if(playAppState==true||settingsAppState==true||exitState==true){
            
            if(playAppState==true)
                
            if(settingsAppState==true)
                stateManager.attach(new settingsState(this.app));
            if(exitState==true)
                app.stop();
        }
        if(mmB>5){
            for(int i=0; i<100;i++){
                stateManager.attach(new mainMenu(app));
            }
            mmB=0;
        }
    }

        
    }
    
//    public Spatial createChip(String cardFace){
//        pokerChip1 = assetManager.loadModel("Models/PokerChip.j3o");
//        Material pokerMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        pokerMat1.setTexture("ColorMap", assetManager.loadTexture(cardFace));
//        pokerChip1.setMaterial(pokerMat1);
//    }

