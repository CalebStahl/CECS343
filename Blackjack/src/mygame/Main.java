package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
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
        flyCam.setEnabled(paused);
        setDisplayFps(false);
        setDisplayStatView(false);
        stateManager.attach(new mainMenu(this)); 
    }
}