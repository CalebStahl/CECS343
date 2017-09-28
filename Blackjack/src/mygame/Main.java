package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
        flyCam.setMoveSpeed(40);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
        
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot);
        
        pokerChip1 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat1.setTexture("ColorMap", assetManager.loadTexture("Textures/green.jpg"));
        pokerChip1.setMaterial(pokerMat1);
        pokerChip1.setLocalTranslation(-5.0f, 0.0f, 0.0f);
        pokerChip1.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip1);
        
        pokerChip2 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat2.setTexture("ColorMap", assetManager.loadTexture("Textures/black.jpg"));
        pokerChip2.setMaterial(pokerMat2);
        pokerChip2.setLocalTranslation(-2.5f, 0.0f, 0.0f);
        pokerChip2.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip2);
        
        pokerChip3 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat3.setTexture("ColorMap", assetManager.loadTexture("Textures/red.jpg"));
        pokerChip3.setMaterial(pokerMat3);
        pokerChip3.setLocalTranslation(0.0f, 0.0f, 0.0f);
        pokerChip3.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip3);
        
        pokerChip4 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat4.setTexture("ColorMap", assetManager.loadTexture("Textures/blue.jpg"));
        pokerChip4.setMaterial(pokerMat4);
        pokerChip4.setLocalTranslation(2.5f, 0.0f, 0.0f);
        pokerChip4.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip4);
        
        card = assetManager.loadModel("Models/basicCard.j3o");
        Material cardMat = assetManager.loadMaterial("Materials/CardMat.j3m");
        cardMat.setTexture("ColorMap2", assetManager.loadTexture("Textures/kingofhearts.jpg"));
        card.setMaterial(cardMat);
        card.setLocalTranslation(-1.0f, 2.5f, 0.0f);
        pivot.attachChild(card);

    }
    
    @Override
    public void simpleUpdate(float tpf) {
        card.rotate(0, 2*tpf, 0);
        pokerChip1.rotate(0, 0, 2*tpf);
        pokerChip2.rotate(0, 0, 2*tpf);
        pokerChip3.rotate(0, 0, 2*tpf);
        pokerChip4.rotate(0, 0, 2*tpf);
    }
}