package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
import mygame.gameState.playState; 

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    protected Spatial card;
    protected Spatial pokerChip1;
    protected Spatial pokerChip2;
    protected Spatial pokerChip3;
    protected Spatial pokerChip4;
    
    @Override
    public void simpleInitApp() {
        //flyCam.setMoveSpeed(50);
        flyCam.setEnabled(paused);
        setDisplayFps(false);
        setDisplayStatView(false);
        stateManager.attach(new playState(this)); 
    }
}