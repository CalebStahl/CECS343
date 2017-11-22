/* 
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor. 
 */ 
package mygame.gameState; 
 
import com.google.common.base.Joiner;
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
import mygame.Deck;
import mygame.Hand;
import mygame.PHASES;
import mygane.menuState.mainMenu;
import mygame.Player;
import mygame.getGUI;
import mygame.settingState.settingsState;
 
 
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
    
    private List<Spatial> savedGUI = new ArrayList();
    private PHASES phase;
    
    private boolean isHit;
    private boolean isSplit;
    private boolean isStay;
    private Player user;
    private Hand pHand;
    private Hand dHand;
    private Deck deck;
    
    
    protected Label walNum;     //Lists money in user's wallet
    
    private int bet = 0;
    private double multiplier;
    
    //private double 
    private enum PLAYERS{P1, P2, P3,PN}
     
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
        localRootNode.scale(0.6f);
        initKeys();
        initGame();
        user = new Player("Adam");
        multiplier =1;
        getCountingGUI();
        //Establishing Basic GUI Theme: CHANGE LATER Green?
        GuiGlobals.initialize((Application) app); 
        BaseStyles.loadGlassStyle(); 
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
         
        betGUI = getBetMenu(); 
        escGUI = getEscMenu();
        actionGUI = getActionMenu();
        createTable(); 
        createLight(); 
         
        //Wallet GUI Spatial 
        wallet  =  new Container (new BorderLayout()); 
        wallet.addChild(new Label("Wallet"), BorderLayout.Position.North);
        walNum = wallet.addChild(new Label(labelWallet()), BorderLayout.Position.South);
        wallet.setLocalTranslation(0, 250, 0); 
        guiNode.attachChild(wallet); 
                         
        getGUI.counter(); 
    } 
    
    private String labelWallet(){
        StringBuilder walletLabel=new StringBuilder("$   "+ Long.toString(user.getWallet()));
        int intFound = walletLabel.lastIndexOf(" ");
        for(int i=(walletLabel.length()-3); i>(intFound+1); i-=3){
            walletLabel.insert(i, ',');
        }
        return walletLabel.toString();
    }
    
    /**
     * Called when playState is deatched from stateManager.
     */
    @Override 
    public void cleanup(){ 
        rootNode.detachAllChildren(); 
        guiNode.detachAllChildren();
        super.cleanup(); 
    } 
    
    //Game Logic
    @Override
    public void update(float tpf){
        super.update(tpf);
        if(null!=phase) //Betting Phase
        switch (phase) {
        //Dealing Phase
            case BET:
                if (guiNode.getChildIndex(betGUI)==-1){
                    guiNode.attachChild(betGUI);
                }   break;
//        if(phase==PHASES.BROKE){
//            SimpleApplication app =(SimpleApplication) stateManager.getApplication();
//            stateManager.attach(new youLose(app));
//            stateManager.detach(stateManager.getState(playState.class));
//        }
        //Action Phase
            case DRAW:
                System.out.println("in action phase");
                //If Action GUI isn't attached
                if(guiNode.getChildIndex(actionGUI)==-1){
                    guiNode.detachChild(betGUI);
                    guiNode.attachChild(actionGUI);
                }   localRootNode.attachChild(pHand.DrawCard(assetManager));
                localRootNode.attachChild(dHand.DrawCard(assetManager));
                localRootNode.attachChild(pHand.DrawCard(assetManager));
                localRootNode.attachChild(dHand.DrawCard(assetManager));
                getCountingGUI();
                //
                if(dHand.isSplittable()){
                    //create splittable button
                    //add split phase
                }   phase=PHASES.ACTION;
                break;
        //Dealer Phase
            case ACTION:
                if(isHit==true){
                    localRootNode.attachChild(pHand.DrawCard(assetManager));
                    getCountingGUI();
                    //pHand.hasCondition
                    isHit=false;
                }
                else if(isStay==true){
                    phase=PHASES.DEALER;
                    isStay=false;
                    
                }   if(pHand.isSplittable()){
                    //attach split button
                    //phase = PHASES.SPLIT;
                    //phase = PHASES.DEALER;
                }   break;
            case DEALER:
                while(dHand.getTotal()<17)
                    localRootNode.attachChild(dHand.DrawCard(assetManager));
                getCountingGUI();
                phase=PHASES.REVEAL;
                break;
            case REVEAL:
                if(pHand.getTotal()>dHand.getTotal() && pHand.getTotal()<=21){
                    if(bet>0){
                        user.addWallet((bet/2)*3);
                        bet=0;
                    }
                    if(guiNode.getChildIndex(goGUI)==-1){
                        getCountingGUI();
                        guiNode.attachChild(getGOmenu("Player Won!"));
                    }
                }
                else if(dHand.getTotal()<=21){
                    if(guiNode.getChildIndex(goGUI)==-1){
                        getCountingGUI();
                        guiNode.attachChild(getGOmenu("House Won!"));
                    }
                } else {
                    if(bet>0){
                        user.addWallet((bet/2));
                        bet=0;
                    }
                    if(guiNode.getChildIndex(goGUI)==-1){
                        getCountingGUI();
                        guiNode.attachChild(getGOmenu("TIE"));
                        
                    }
                }  
                break;
            default:
                break;
        
        }
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
    
    //Light needed for spatials to be illuminated
    public void createLight(){ 
        DirectionalLight sun = new DirectionalLight(); 
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f)); 
        rootNode.addLight(sun); 
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
                        System.out.println("Why isn't this work right?");
                        guiNode.attachChild((Spatial) savedGUI.remove(i));}
                }
                
            }
        }
    };
    
    
    
    
    
    
    
    void initGame(){
        deck = new Deck();        
        pHand = new Hand("player", deck);
        dHand = new Hand("dealer", deck);
        getCountingGUI();
        phase=PHASES.BET;
        isHit = false; isSplit = false; isStay = false;//Supposed to set false every update?

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