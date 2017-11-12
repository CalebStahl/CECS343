package mygame;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import mygame.Deck;

public class Hand{
    private Card card1;
    private Card card2;
    private final int DECK_SIZE = 52;
    private Card[] myDeck = new Card[DECK_SIZE];
    private int cards;
    Spatial cardImage;
    
    
    //Constructor
    public void Hand(){
        
    }
    
    
    List<Card> cardsInHand = new ArrayList<Card>(); //change name
    
    
    //Place card
    public void PlaceCard(Card card){
        cardImage.setLocalTranslation(-1.5f, 2.5f, 0.0f); 
    }
    
    
    //Adds a card from the deck to the hand and places it
    public void DrawCard(){       
        cardsInHand.add(deck[cards]);
        PlaceCard(card1);
        //getTotal();
        
    }
    
    //Calculate total points in hand
    public int getTotal(){
        int total = 0, aceCount = 0;
        for(Card card : cardsInHand){
            if(card.getFace() == 'A')
                aceCount += 1;
            total = total + card.getValue();
        }
        
        //The default value for getValue is 1
        //After the total has been calculated for aces at the lower value,
        //if there's a large enough difference, one of the aces is effectively changed to 11.
        if((21 - total) >= 10 && aceCount >= 1)
            total = total + 10;
        
        return total;    
    }
    
    public boolean isSplittable(){
        for(int i=0; i<cardsInHand.size(); i++){
           for(int j=1; j<cardsInHand.size();j++){
               if(cardsInHnad.size())
            }
        }
        return false;
    }
}
