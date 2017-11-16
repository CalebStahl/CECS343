package mygame;
/**
 * @Author - Caleb
 * @Edited - Adam
 */
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;



public class Hand{
    private String owner;
    private int cards;
    Deck deck;
    List<Spatial> sparray;
    public enum cardsLoc{CARD1, CARD2, CARD3, CARD4}
    
    //Constructor
    public Hand(String participant, Deck deckInPlay){
        owner = participant;
        deck = deckInPlay;  //Might have to be moved to drawCard?
        sparray = new ArrayList<>();
    }
    
    
    List<Card> cardsInHand = new ArrayList<>(); //change name
    
    
    //Place card
    public void PlaceCard(cardsLoc CARD, Spatial cardImage){
        if("player".equals(owner)){
            if(null!=CARD)
                switch (CARD) {
                case CARD1:
                     cardImage.setLocalTranslation(-2.5f, 0.5f, 0.0f);
                    break;
                case CARD2:
                    cardImage.setLocalTranslation(0.5f, 0.5f, 0.0f);
                    break;
                case CARD3:
                    //Cards 1 and 2 need to start being moved over so update
                    //translations for them as well
                    cardImage.setLocalTranslation(1.5f, 0.5f, 0.0f);
                    break;
                case CARD4:
                    //same as above
                    cardImage.setLocalTranslation(3.5f, 0.5f, 0.0f);
                    break;
                default:
                    break;
            }
        }
        if("dealer".equals(owner)){
            if(null!=CARD)
                switch (CARD) {
                case CARD1:
                    cardImage.setLocalTranslation(-2.5f, 3.5f, 0.0f);
                    break;
                case CARD2:
                    cardImage.setLocalTranslation(0.5f, 3.5f, 0.0f);
                    break;
                case CARD3:
                    //Cards 1 and 2 need to start being moved over so update
                    //translations for them as well
                    cardImage.setLocalTranslation(1.5f, 2.5f, 0.0f);
                    break;
                case CARD4:
                    //same as above
                    cardImage.setLocalTranslation(3.5f, 2.5f, 0.0f);
                    break;
                default:
                    break;
            }
        }
    }
    
    
    //Adds a card from the deck to the hand and places it
    public Spatial DrawCard(AssetManager assetManager){       
        cardsInHand.add(deck.dealCard());
        sparray.add(cardsInHand.get(cardsInHand.size()-1).create(assetManager));
        switch (cardsInHand.size()) {
            case 1:
                PlaceCard(cardsLoc.CARD1, sparray.get((sparray.size()-1)));
                break;
            case 2:
                PlaceCard(cardsLoc.CARD2, sparray.get((sparray.size()-1)));
                break;
            case 3:
                PlaceCard(cardsLoc.CARD3, sparray.get((sparray.size()-1)));
                break;
            case 4:
                PlaceCard(cardsLoc.CARD4, sparray.get((sparray.size()-1)));
                break;
            default:
                break;
            }
        return sparray.get((sparray.size()-1));
    }
    
    //Calculate total points in hand
    public int getTotal(){
        int total = 0, aceCount = 0;
        for(Card card : cardsInHand){
            if("ace".equals(card.getFace())){
                aceCount += 1;
            }
            total = total + card.getValue();
        }
        
        //The default value for getValue is 1
        //After the total has been calculated for aces at the lower value,
        //if there's a large enough difference, one of the aces is effectively 
        //changed to 11.
        if((21 - total) >= 10 && aceCount >= 1)
            total = total + 10;
        
        return total;    
    }
    
    //Checks to see if the hand has two of the same face
    public boolean isSplittable(){
        for(int i=0; i<cardsInHand.size(); i++){
           for(int j=i+1; j<cardsInHand.size();j++){
               if(cardsInHand.get(i)==cardsInHand.get(j))
                   return true;
            }
        }
        return false;
    }
}
