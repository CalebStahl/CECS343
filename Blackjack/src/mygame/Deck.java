package mygame;
/**
 * @Author - Jason
 * @Edited - Adam
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Deck{
    private final int DECK_SIZE = 52;
    private List<Card> cardDeck = new ArrayList<>();
    private int cards;
    
    public Deck(){
        initDeck();
    }
    
    //returns the next card from the deck
    public Card dealCard(){
        if (cardDeck.isEmpty()){
		System.out.println("No cards left in the deck to print");
                return null;
        }
        else{
		return cardDeck.remove(cardDeck.size()-1);
        }
    }
    
    //Returns the amount of cards left
    public int cardsLeft(){
        return cardDeck.size();
    }
    
    //Shuffle the deck
    public void shuffle(){
//        if (cards < 51)
//		System.out.println("Deck not full. Can't shuffle unless the deck is full.\nReset the deck and try again.\n");
//	else {
//		for (int firstCard = 0; firstCard < DECK_SIZE; firstCard++) {
//			int secondCard = getRandomInt(0, 51);
//			Card temp = myDeck[firstCard];
//			myDeck[firstCard] = myDeck[secondCard];
//			myDeck[secondCard] = temp;
//		}
//		System.out.println("The deck has been shuffled.\n");
//	}
        Collections.shuffle(cardDeck);
    }
    
    //Resets the deck to have all 52 cards in order
    public void initDeck(){
        if(!cardDeck.isEmpty()){
            cardDeck.clear();
        }
        String faces[] = { "ace","2","3","4","5","6","7","8","9","10","jack","queen","king" };
	String suits[] = { "hearts","diamonds","clubs","spades"};
	for (int index = 0; index < 4; index++) {
		for (int c = 0; c < 13; c++) {
			cardDeck.add(new Card(faces[c], suits[index]));
		}
        }
        shuffle();
    }
    
    public static int getRandomInt(int min, int max){
		boolean inRange = false;
		int random = min;
		while(!inRange){
			random = (int)(Math.random() * max + min);
			if(random >= min && random <= max)
				inRange = true;
		}
		return random;
	}
}