package mygame;

public class Deck{
    private final int DECK_SIZE = 52;
    private Card[] myDeck = new Card[DECK_SIZE];
    private int cards;
    
    public void Deck(){
        resetDeck();
    }
    
    //returns the next card from the deck
    public Card dealCard(){
        if (cards == 0){
		System.out.println("No cards left in the deck to print");
                return null;
        }
	else {
		//cards--;
		return myDeck[--cards];
        }
    }
    
    //Returns the amount of cards left
    public int cardsLeft(){
        return cards;
    }
    
    //Shuffle the deck
    public void shuffle(){
        if (cards < 52)
		System.out.println("Deck not full. Can't shuffle unless the deck is full.\nReset the deck and try again.\n");
	else {
		for (int firstCard = 0; firstCard < DECK_SIZE; firstCard++) {
			int secondCard = getRandomInt(0, 51);
			Card temp = myDeck[firstCard];
			myDeck[firstCard] = myDeck[secondCard];
			myDeck[secondCard] = temp;
		}
		System.out.println("The deck has been shuffled.\n");
	}
    }
    
    //Resets the deck to have all 52 cards in order
    public void resetDeck(){
        char faces[] = { 'A','2','3','4','5','6','7','8','9','T','J','Q','K' };
	char suits[] = { 'H','D','C','S' };
	int cardNumber = 0;
	for (int index = 0; index < 4; index++) {
		for (int c = 0; c < 13; c++) {
			myDeck[cardNumber] = new Card(faces[c], suits[index]);
			cardNumber++;
		}
	}
	cards = 52;
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