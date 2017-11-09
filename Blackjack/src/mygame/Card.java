package mygame;

import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import com.jme3.asset.AssetManager;
public class Card{
    private char face;
    private char suit;
    private int value;
    
    //default constructor
    public Card(){
        face = '2';
        suit = 'S';
        value = 2;
    }
    
    //overridden constructor
    public Card(char f, char s){
        face = f;
        suit = s;
        value = calculateValue(face);
    }
    
    public Spatial create(AssetManager assetManager){
        String cardName = "Textures/Cards/"; 
        cardName = cardName.concat("2_of_spades.png"); 
        Spatial card = assetManager.loadModel("Models/basicCard.j3o"); 
        Material cardMat = assetManager.loadMaterial("Materials/CardMat.j3m"); 
        cardMat.setTexture("ColorMap2", assetManager.loadTexture(cardName)); 
        card.setMaterial(cardMat); 
        card.setLocalTranslation(-1.0f, 2.5f, 0.0f);
        return card;
    }
    
    //get the value of face
    public char getFace(){
        return face;
    }
    
    //get the value of suit
    public char getSuit(){
        return suit;
    }
    
    //get the value of the card
    public int getValue(){
        return value;
    }
    
    //set the face of the card and update the value
    public void setFace(char f){
        face = f;
        value = calculateValue(f);
    }
    
    //set the card's suit
    public void setSuit(char s){
        suit = s;
    }
    
    //set the value of the card
    public void setValue(int v){
        value = v;
    }
    
    //calculate the value of the card based on the face
    //T = 10, J = Jack, Q = Queen, K = King, A = High Ace, a = Low Ace
    private int calculateValue(char face){
        int val = 0;
        switch (face){
            case '2':
                 val = 2;
                 break;
            case '3':
                 val = 3;
                 break;
            case '4':
                 val = 4;
                 break;
            case '5':
                 val = 5;
                 break;
            case '6':
                 val = 6;
                 break;
            case '7':
                 val = 7;
                 break;
            case '8':
                 val = 8;
                 break;
            case '9':
                 val = 9;
                 break;
            case 'T':
                 val = 10;
                 break;
            case 'J':
                 val = 10;
                 break;
            case 'Q':
                 val = 10;
                 break;
            case 'K':
                 val = 10;
                 break;
            case 'A':
                 val = 11;
                 break;
            default:
                val = 1;
        }
        return val;
    }
}
