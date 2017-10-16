/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygane.menuState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
    
    protected Spatial pokerChip1;
    protected Spatial pokerChip2;
    protected Spatial pokerChip3;
    protected Spatial pokerChip4;
    
    public mainMenu(SimpleApplication app){
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
        assetManager = app.getAssetManager();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        rootNode.attachChild(localRootNode);  //Add main menu to screen        
        localRootNode.attachChild(pivot);     //Attaching Cards to main menu
        createLight();       
        
        
        //Generating Cards for Main Menu Scene
        pokerChip1 = createCard("Textures/green.jpg");
        pokerChip1.setLocalTranslation(-3.5f, 0.0f, 0.0f);
        pokerChip1.rotate(-5.0f,0.0f,1.0f);
        pivot.attachChild(pokerChip1);
        
        pokerChip2 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat2.setTexture("ColorMap", assetManager.loadTexture("Textures/black.jpg"));
        pokerChip2.setMaterial(pokerMat2);
        pokerChip2.setLocalTranslation(-0.5f, 0.0f, 0.0f);
        pokerChip2.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip2);
            
        pokerChip3 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat3.setTexture("ColorMap", assetManager.loadTexture("Textures/red.jpg"));
        pokerChip3.setMaterial(pokerMat3);
        pokerChip3.setLocalTranslation(1.5f, 0.0f, 0.0f);
        pokerChip3.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip3);
        
        pokerChip4 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat4.setTexture("ColorMap", assetManager.loadTexture("Textures/blue.jpg"));
        pokerChip4.setMaterial(pokerMat4);
        pokerChip4.setLocalTranslation(3.5f, 0.0f, 0.0f);
        pokerChip4.rotate(-5.0f,0.0f,0.0f);
        pivot.attachChild(pokerChip4);
        
        
    }
    
    public void createLight(){ 
         DirectionalLight sun = new DirectionalLight(); 
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f)); 
        rootNode.addLight(sun); 
    }
    
    @Override
    public void update(float tpf){
        pokerChip1.rotate(0,0,(2*tpf));
        pokerChip2.rotate(0,0,(2*tpf));
        pokerChip3.rotate(0,0,(2*tpf));
        pokerChip4.rotate(0,0,(2*tpf));
        
    }
    
    public Spatial createCard(String cardFace){
        pokerChip1 = assetManager.loadModel("Models/PokerChip.j3o");
        Material pokerMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pokerMat1.setTexture("ColorMap", assetManager.loadTexture(cardFace));
        pokerChip1.setMaterial(pokerMat1);
        
    }
}
