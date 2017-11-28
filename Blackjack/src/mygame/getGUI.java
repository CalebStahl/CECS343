package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.BoxLayout;
import java.util.ArrayList;
import java.util.List;
import mygame.gameState.playState;
import mygame.settingState.settingsState;
import mygane.menuState.mainMenu;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adam
 */
public class getGUI {
    //Nodes for GUI Containers
    private Node rootNode;
    private Node localRootNode;
    private Node guiNode;
    //GUI Containers
    private Container betGUI;
    private Container actionGUI;
    private Container cntGUI;
    private Container goGUI;
    private Container escGUI;
    private Container wallet;
    private Container aaa;
    private Container aaas;
    private Container asdf;
    
    private List<Spatial> spList;
    
    PHASES phase;
    
    public getGUI(Node rootNode, Node localRootNode, Node guiNode, PHASES PHASE){
        this.rootNode = rootNode;
        this.localRootNode = localRootNode;
        this.phase=PHASE;
        this.guiNode=guiNode;
        spList = new ArrayList<>();
    }
    
    private void counter(Node guiNode, Hand dHand, Hand pHand){
        if(guiNode.hasChild(cntGUI)){
            guiNode.detachChild(cntGUI);
        }
        cntGUI = new Container(new BoxLayout(Axis.Y, FillMode.Even));
        cntGUI.setLocalTranslation(0, 200, 0);
        cntGUI.addChild(new Label("Dealer: "+ dHand.getTotal()));
        cntGUI.addChild(new Label("Player: "+ pHand.getTotal()));
        guiNode.attachChild(cntGUI);
    }
    
    
    private Container escape(AppStateManager stateManager, int bet){
        final AppStateManager tsManager = stateManager;
        final int tBet = bet;
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
                SimpleApplication app =(SimpleApplication) tsManager.getApplication();
                guiNode.detachAllChildren();
                tsManager.detach(tsManager.getState(playState.class));
                tsManager.attach(new mainMenu(app));
            }
        });
        _Settings.addClickCommands(new Command<Button>(){
            @Override public void execute(Button source){
                SimpleApplication app =(SimpleApplication) tsManager.getApplication();
                guiNode.detachAllChildren();
                tsManager.detach(tsManager.getState(playState.class));
                tsManager.attach(new settingsState(app));
            }
        });
        _Cancel.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                if(tBet>0){
                    guiNode.detachChild(escGUI);
                    guiNode.attachChild(betGUI);
                }
                 if(tBet>0){
                    guiNode.detachChild(escGUI);
                    for(int i=0; i<spList.size(); i++){   //Need to Fix this too...
                        System.out.println("I assume that this is not working?");
                        guiNode.attachChild(spList.remove(i));
                    }
                }
            } 
        });
        return escWindow;
    }
    
    public Container getGOmenu(String gameStatus, AppStateManager stateManager, boolean isHit, boolean isSplit, boolean isStay){
        final AppStateManager tsManager = stateManager;
        final boolean tHit;
        if(isHit==true)
            tHit= true;
        else if(isHit==false)
            tHit=false;
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
                SimpleApplication app =(SimpleApplication) tsManager.getApplication();
                tsManager.attach(new mainMenu(app));
                tsManager.detach(tsManager.getState(playState.class));
            } 
        });
        ExitGame.addClickCommands(new Command<Button>(){ 
            @Override 
            public void execute(Button source){ 
                tHit=true; 
            } 
        });
        return goGUI;
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
                    phase=playState.PHASES.DRAW;                    
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
                    phase=playState.PHASES.DRAW;
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
                    phase=playState.PHASES.DRAW;
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
                    phase=playState.PHASES.DRAW;
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
    
    public Container action(){
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
}
