package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import mygame.gameState.playState;
import mygane.menuState.mainMenu;

public class Main extends SimpleApplication {
    private static AppSettings settings;
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        //flyCam.setMoveSpeed(50);
        settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("Blackjack");
        guiNode.scale(4.0f);
        flyCam.setEnabled(paused);
        setDisplayFps(false);
        setDisplayStatView(false);
        stateManager.attach(new mainMenu(this));
    }
}