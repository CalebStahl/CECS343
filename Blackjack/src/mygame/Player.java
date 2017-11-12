package mygame;

public class Player{
    private String pName;
    private double wallet;
    private int win;
    private int loss;
    private double lrgRtn;
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
    
    public double getWallet(){
        return wallet;
    }
    
    public int getWins(){
        return win;
    }
    
    public int getLosses(){
        return loss;
    }
    
    public void setLrgRtn(double amtRtn){
        if(amtRtn>lrgRtn)
            lrgRtn=amtRtn;
        else
            System.out.println("Current value is larger");
    }
    
    public double getLrgRtn(){
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