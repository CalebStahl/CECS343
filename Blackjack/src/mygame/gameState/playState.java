/* 
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor. 
 */ 
package mygame.gameState; 
 
import com.jme3.app.Application; 
import com.jme3.app.state.AbstractAppState; 
import com.jme3.app.SimpleApplication; 
import com.jme3.app.state.AppStateManager; 
import com.jme3.asset.AssetManager; 
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight; 
import com.jme3.material.Material; 
import com.jme3.math.FastMath; 
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
import com.simsilica.lemur.style.BaseStyles; 
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import mygame.Hand;
import mygane.menuState.mainMenu;
import mygame.Player;
 
 
/** 
 * 
 * @author adam 
 */ 
public class playState extends AbstractAppState { 
    private final Node rootNode; 
    private final Node guiNode; 
    private final Node localRootNode = new Node("main game"); 
    private final AssetManager assetManager; 
    private final InputManager inputManager;
    private final AppStateManager stateManager;
    private Container betGUI;
    private Container actionGUI;
    private Container escGUI;
    private Container wallet;
    private List<Spatial> savedGUI = new ArrayList();
    private List<Boolean> phases;
     
    protected Spatial card; 
    protected Spatial pokerChip1; 
    protected Spatial pokerChip2; 
    protected Spatial pokerChip3; 
    protected Spatial pokerChip4;
    
    private double bet = 0;
    private double multiplier = 0;
    private Player user = new Player("Adam");
    private Hand pHand = new Hand();
    private Hand dHand = new Hand();
    //private double 
     
     
    public playState(SimpleApplication app){        
        stateManager = app.getStateManager();
        rootNode  =  app.getRootNode(); 
        guiNode  =  app.getGuiNode(); 
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
    } 
     
    @Override 
    public void initialize(AppStateManager stateManager, Application app){ 
        super.initialize(stateManager, app); 
        rootNode.attachChild(localRootNode);
        initKeys();
        
        phases = new ArrayList();
        initGame();
        

        
         
        //Establishing Basic GUI Theme: CHANGE LATER?
        GuiGlobals.initialize((Application) app); 
        BaseStyles.loadGlassStyle(); 
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        //GUI_Main= new Container(new BorderLayout());
        //guiNode.attachChild(GUI_Main); 
        //GUI_Main.setLocalTranslation(30, 100, 0); 
        betGUI = getBetMenu(); 
        escGUI = getEscMenu();
        actionGUI = getActionMenu();
        createTable(); 
        createLight(); 
         
        //Wallet GUI Spatial 
        wallet  =  new Container (new BorderLayout()); 
        wallet.addChild(new Label("Wallet"), BorderLayout.Position.North); 
        Label walNum = wallet.addChild(new Label("$ 10,000"), BorderLayout.Position.South); 
        wallet.setLocalTranslation(0, 340, 0); 
        guiNode.attachChild(wallet); 
                         
        Node pivot = new Node("pivot"); 
        rootNode.attachChild(pivot); 
        
        //Card Model for demonstration
        String cardName = "Textures/Cards/"; 
        cardName = cardName.concat("2_of_spades.png"); 
        card = assetManager.loadModel("Models/basicCard.j3o"); 
        Material cardMat = assetManager.loadMaterial("Materials/CardMat.j3m"); 
        cardMat.setTexture("ColorMap2", assetManager.loadTexture(cardName)); 
        card.setMaterial(cardMat); 
        card.setLocalTranslation(-1.0f, 2.5f, 0.0f); 
        pivot.attachChild(card); 
        pivot.scale(0.60f); 
    } 
     
    @Override 
    public void cleanup(){ 
        rootNode.detachChild(localRootNode); 
        super.cleanup(); 
    } 
    
    @Override
    public void update(float tpf){
        //super.update();
        //Betting Phase
        if(phases.get(0)==false){
            //if(GUI isn't attached)
            if (guiNode.getChildIndex(betGUI)==-1){                
                guiNode.attachChild(betGUI);
            }
            
        }//Dealing Phase
        else if(phases.get(0)==true && phases.get(1)==false){
            //If Action GUI isn't attached
            if(guiNode.getChildIndex(actionGUI)==-1){
                guiNode.detachChild(betGUI);
                guiNode.attachChild(actionGUI);
            }
            pHand.DrawCard();
            dHand.DrawCard();
            pHand.DrawCard();
            dHand.DrawCard();
            //
            if(dHand.isSplittable()){
                //create splittable button
                //add split phase
            }
           
            phases.add(1, true);
            //
        }//Action Phase
        else if(phases.get(1)==true && phases.get(2)==false){
            if(isHit==true){
                pHand.DrawCard();
                isHit=false;
            }
            else if(isStay==true){
                phases.add(2, true);
                isStay=false;
                
            }
            if(pHand.isSplittable()){
                //attach split button
                //
            }
        }//Dealer Phase
        else if(phases.get(3)==true && phases.get(4)==true){
            while(dHand.getTotal()>17)
                dHand.DrawCard();
        }
    }
    
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
    
    //Light needed for spatials to be illuminated
    public void createLight(){ 
        DirectionalLight sun = new DirectionalLight(); 
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f)); 
        rootNode.addLight(sun); 
    } 
    
    //
    public Container getBetMenu(){ 
        Container betWinMain =  new Container(new BorderLayout());
        betWinMain.setLocalTranslation(90,50,0);
        Container betWindow = new Container(new BoxLayout(Axis.X, FillMode.Even)); 
        betWinMain.addChild(betWindow, BorderLayout.Position.Center); 
        betWinMain.addChild(new Label("Bet"), BorderLayout.Position.North); 
        //mainWindow.addChild(betWinMain, BorderLayout.Position.East); 
        Button oneK = betWindow.addChild(new Button("$ 1,000")); 
        Button twoK = betWindow.addChild(new Button("$ 2,000"));
        Button fiveK = betWindow.addChild(new Button("$ 5,000")); 
        Button tenK = betWindow.addChild(new Button("$ 10,000")); 
        fiveK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               if(user.getWallet()>1000){
                bet=5000;
                phases.add(0, true);
               }
               else{
                   System.out.println("");
               }
        }});
        twoK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               bet=10000;
        }             
        });
        fiveK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               bet=5000;
        }             
        }); 
        tenK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               bet=10000;
        }             
        }); 
        return betWinMain;
    } 
    
    public Container getActionMenu(){
        Container actWinMain = new Container(new BorderLayout());
        actWinMain.setLocalTranslation(90,50,0);
        Container actionWindow = new Container(new BoxLayout(Axis.X, FillMode.Even)); 
        actWinMain.addChild(new Label("Actions"), BorderLayout.Position.North); 
        actWinMain.addChild(actionWindow, BorderLayout.Position.Center); 
        //mainWindow.addChild(actWinMain, BorderLayout.Position.West); 
        Button hit = actionWindow.addChild(new Button("HIT")); 
        Button stand = actionWindow.addChild(new Button("STAND")); 
        Button split = actionWindow.addChild(new Button("SPLIT")); 
        //Button dbl = actionWindow.addChild(new Button("DOUBLE"));
        hit.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                System.out.println("This world is yours."); 
            } 
        });
        stand.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                System.out.println("This world is yours."); 
            } 
        });
        split.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                System.out.println("This world is yours."); 
            } 
        });
        return actWinMain;
    }
    
    private void initKeys(){
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.addMapping("ESCAPE", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(actionListener, "ESCAPE");
         
    }
    
    //Needs to be changed so that instead of adding escGUI,
    //It will add the last GUI elements that were in scene
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("ESCAPE") && !keyPressed) {
                if(guiNode.getChildIndex(escGUI)==-1){
                    savedGUI = guiNode.getChildren();
                    guiNode.detachAllChildren();
                    guiNode.attachChild(escGUI);
                }
                else{
                   guiNode.detachChild(escGUI);
                   for(int i=0; i<savedGUI.size(); i++){
                        guiNode.attachChild((Spatial) savedGUI.remove(i));}
                }
                
            }
        }
    };
    
    private Container getEscMenu(){
        Container escWindow = new Container(new BorderLayout());
        escWindow.setLocalTranslation(180, 250,0); 
        Container escButtons = new Container(new BoxLayout(Axis.Y, FillMode.Even));
        escWindow.addChild(new Label("Escape Menu"), BorderLayout.Position.North);
        Button _MainMenu = escButtons.addChild(new Button("Main Menu"));
        Button _Settings = escButtons.addChild(new Button("SETTINGS"));
        Button _SaveGame = escButtons.addChild(new Button("Save Game"));
        Button _Cancel = escButtons.addChild(new Button("Cancel"));
        escWindow.addChild(escButtons);
        _MainMenu.addClickCommands(new Command<Button>(){
            @Override public void execute(Button source){
                stateManager.detach(stateManager.getState(playState.class));
                stateManager.attach(new mainMenu(app));
            }
        });
        _Cancel.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                if(bet==0){
                    guiNode.detachChild(escGUI);
                    guiNode.attachChild(betGUI);
                }
                 if(bet>0){
                    guiNode.detachChild(escGUI);
                    for(int i=0; i<savedGUI.size(); i++){
                        System.out.println("I assume that this is not working?");
                        guiNode.attachChild(savedGUI.remove(i));
                    }
                }
            } 
        });
        return escWindow;
    }        
    
    void initGame(){
        for( int i =0; i<5; i++)
            phases.add(false);
        
    }
//        public Spatial createChip(){
//          pokerChip1 = assetManager.loadModel("Models/PokerChip.j3o");
//          Material pokerMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//          pokerMat1.setTexture("ColorMap", assetManager.loadTexture("Textures/green.jpg"));
//          pokerChip1.setMaterial(pokerMat1);
//          pokerChip1.setLocalTranslation(-5.0f, 0.0f, 0.0f);
//          pokerChip1.rotate(-5.0f,0.0f,0.0f);
//          pivot.attachChild(pokerChip1);
//        
//          pokerChip2 = assetManager.loadModel("Models/PokerChip.j3o");
//          Material pokerMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//          pokerMat2.setTexture("ColorMap", assetManager.loadTexture("Textures/black.jpg"));
//          pokerChip2.setMaterial(pokerMat2);
//          pokerChip2.setLocalTranslation(-2.5f, 0.0f, 0.0f);
//          pokerChip2.rotate(-5.0f,0.0f,0.0f);
//          pivot.attachChild(pokerChip2);
//            
//          pokerChip3 = assetManager.loadModel("Models/PokerChip.j3o");
//          Material pokerMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//          pokerMat3.setTexture("ColorMap", assetManager.loadTexture("Textures/red.jpg"));
//          pokerChip3.setMaterial(pokerMat3);
//          pokerChip3.setLocalTranslation(0.0f, 0.0f, 0.0f);
//          pokerChip3.rotate(-5.0f,0.0f,0.0f);
//          pivot.attachChild(pokerChip3);
//        
//          pokerChip4 = assetManager.loadModel("Models/PokerChip.j3o");
//          Material pokerMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//          pokerMat4.setTexture("ColorMap", assetManager.loadTexture("Textures/blue.jpg"));
//          pokerChip4.setMaterial(pokerMat4);
//          pokerChip4.setLocalTranslation(2.5f, 0.0f, 0.0f);
//          pokerChip4.rotate(-5.0f,0.0f,0.0f);
//          pivot.attachChild(pokerChip4);
//    }
} 