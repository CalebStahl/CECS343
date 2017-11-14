package mygame;


import com.jme3.app.SimpleApplication;
import mygame.gameState.playState;
import mygane.menuState.mainMenu;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        //flyCam.setMoveSpeed(50);
        guiNode.scale(4.0f);
        flyCam.setEnabled(paused);
        setDisplayFps(false);
        setDisplayStatView(false);
        stateManager.attach(new playState(this));
    }
}