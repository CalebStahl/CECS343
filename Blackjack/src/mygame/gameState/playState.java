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
import mygane.menuState.mainMenu;
import mygame.Player;
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
    private Container betGUI;
    private Container actionGUI;
    private Container cntGUI;
    private Container goGUI;
    private Container escGUI;
    private Container wallet;
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
    public enum PLAYERS{P1, P2, P3,PN}
    public enum PHASES{BET, DRAW, ACTION, SPLIT,DEALER, REVEAL, BROKE};
     
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
                         
        getCountingGUI(); 
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
    
    //GUI for the bet menu phase
    public Container getBetMenu(){ 
        Container betWinMain =  new Container(new BorderLayout());
        betWinMain.setLocalTranslation(50,50,0);
        Container betWindow = new Container(new BoxLayout(Axis.X, FillMode.Even)); 
        betWinMain.addChild(betWindow, BorderLayout.Position.Center); 
        betWinMain.addChild(new Label("Bet"), BorderLayout.Position.North); 
        Button oneK = betWindow.addChild(new Button("$ 1,000")); 
        Button twoK = betWindow.addChild(new Button("$ 2,000"));
        Button fiveK = betWindow.addChild(new Button("$ 5,000")); 
        Button tenK = betWindow.addChild(new Button("$ 10,000")); 
        oneK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
                int betAmt = 1000;                 
                if(user.getWallet()>=betAmt){
                    bet=betAmt;
                    user.deductWallet(betAmt);
                    phase=PHASES.DRAW;                    
                    wallet.detachChild(walNum);
                    walNum = wallet.addChild(new Label(labelWallet()));
                }
               else{
                   System.out.println("Not Enough Money.");
               }
        }});
        twoK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               int betAmt = 2000;
                if(user.getWallet()>=betAmt){
                    bet=betAmt;
                    user.deductWallet(betAmt);
                    phase=PHASES.DRAW;
                    wallet.detachChild(walNum);
                    walNum = wallet.addChild(new Label(labelWallet()));
               }
               else{
                   System.out.println("Not Enough Money.");
               }
        }             
        });
        fiveK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               int betAmt = 5000;
                if(user.getWallet()>=betAmt){
                    bet=betAmt;
                    user.deductWallet(betAmt);
                    phase=PHASES.DRAW;
                    wallet.detachChild(walNum);
                    walNum=wallet.addChild(new Label(labelWallet()));
               }
               else{
                   System.out.println("Not Enough Money.");
               }
        }             
        }); 
        tenK.addClickCommands(new Command<Button>(){ 
           @Override 
           public void execute(Button source){
               int betAmt = 10000;
                if(user.getWallet()>=betAmt){
                    bet=betAmt;
                    user.deductWallet(betAmt);
                    phase=PHASES.DRAW;
                    wallet.detachChild(walNum);
                    walNum=wallet.addChild(new Label(labelWallet()));
               }
               else{
                   System.out.println("Not Enough Money.");
               }
        }             
        }); 
        return betWinMain;
    }
    
    //GUI for the game over menu
    public Container getGOmenu(String gameStatus){
        goGUI = new Container(new BorderLayout());
        goGUI.setLocalTranslation(50,250,0);
        goGUI.addChild(new Label(gameStatus + " Actions\n Play Again?"), BorderLayout.Position.North); 
        Container guiButtons = new Container(new BoxLayout(Axis.X, FillMode.Even));
        goGUI.addChild(guiButtons, BorderLayout.Position.Center);
        Button yes = guiButtons.addChild(new Button("Yes")); 
        Button no = guiButtons.addChild(new Button("No")); 
        Button ExitGame = guiButtons.addChild(new Button("Exit Game"));
        yes.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                //user.saveGame();
                if(guiNode.getChildIndex(actionGUI)!=-1)
                    guiNode.detachChild(actionGUI);
                if(guiNode.getChildIndex(goGUI)!=-1)
                    guiNode.detachChild(goGUI);
                localRootNode.detachAllChildren();
                initGame(); 
            } 
        });
        no.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){
                SimpleApplication app =(SimpleApplication) stateManager.getApplication();
                stateManager.attach(new mainMenu(app));
                stateManager.detach(stateManager.getState(playState.class));
            } 
        });
        ExitGame.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                isHit=true; 
            } 
        });
        return goGUI;
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
        hit.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                isHit=true; 
            } 
        });
        stand.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                isStay=true;
            } 
        });
        split.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                isSplit=true; 
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
                        System.out.println("Why isn't this work right?");
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
                SimpleApplication app =(SimpleApplication) stateManager.getApplication();
                guiNode.detachAllChildren();
                stateManager.detach(stateManager.getState(playState.class));
                stateManager.attach(new mainMenu(app));
            }
        });
        _Settings.addClickCommands(new Command<Button>(){
            @Override public void execute(Button source){
                SimpleApplication app =(SimpleApplication) stateManager.getApplication();
                guiNode.detachAllChildren();
                stateManager.detach(stateManager.getState(playState.class));
                stateManager.attach(new settingsState(app));
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
                    for(int i=0; i<savedGUI.size(); i++){   //Need to Fix this too...
                        System.out.println("I assume that this is not working?");
                        guiNode.attachChild(savedGUI.remove(i));
                    }
                }
            } 
        });
        return escWindow;
    }
    
    private void getCountingGUI(){
        if(guiNode.hasChild(cntGUI)){
            guiNode.detachChild(cntGUI);
        }
        cntGUI = new Container(new BoxLayout(Axis.Y, FillMode.Even));
        cntGUI.setLocalTranslation(0, 200, 0);
        cntGUI.addChild(new Label("Dealer: "+ dHand.getTotal()));
        cntGUI.addChild(new Label("Player: "+ pHand.getTotal()));
        guiNode.attachChild(cntGUI);
    }
    
    
    
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