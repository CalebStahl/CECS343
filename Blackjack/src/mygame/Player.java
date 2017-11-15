package mygame;
/**
 * @Author - Adam
 * 
 */
public class Player{
    private String pName;
    private int wallet;
    private int win;
    private int loss;
    private int lrgRtn;
    private Hand currHand;
  
    public Player(){
        pName = " ";
        wallet = 0;
        win = 0;
        loss = 0;
    }
    public Player(String name){
        pName = name;
        wallet = 10000;
        win = 0;
        loss = 0;
    }
    
    public String getPName(){
        return pName;
    }
    
    public void deductWallet(int d){
        wallet-=d;
    }
    
    public void addWallet(int d){
        wallet+=d;
    }
    
    public int getWallet(){
        return wallet;
    }
    
    public int getWins(){
        return win;
    }
    
    public int getLosses(){
        return loss;
    }
    
    public void setLrgRtn(int amtRtn){
        if(amtRtn>lrgRtn)
            lrgRtn=amtRtn;
        else
            System.out.println("Current value is larger");
    }
    
    public int getLrgRtn(){
        return lrgRtn;
    }
    
    public void setCurrHand(Hand hand){
        currHand = hand;
    }
    
    public void saveStats(){
        
    }
    
    public Player loadStats(){
        return this;
    }
}