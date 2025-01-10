public class Mage extends Adventurer{
    private int mana, manaMax;

    public String getSpecialName(){
        return "mana";
    }
  //accessor methods
    public int getSpecial(){
        return mana;
    }
    public int getSpecialMax(){
        return manaMax;
    }
    public void setSpecial(int n){
        mana = n;
    }

  //concrete method written using abstract methods.
  //refill special resource by amount, but only up to at most getSpecialMax()
    public int restoreSpecial(int n){
    if (n < getSpecialMax()){

    }
  }

  /*
  all adventurers must have a way to attack enemies and
  support their allys
  */
  //hurt or hinder the target adventurer
    public String attack(Adventurer other);

  /*This is an example of an improvement that you can make to allow
   * for more flexible targetting.
   */
  //heal or buff the party
  //public abstract String support(ArrayList<Adventurer> others);

  //heal or buff the target adventurer
    public String support(Adventurer other);

  //heal or buff self
    public String support(){

  }

  //hurt or hinder the target adventurer, consume some special resource
    public String specialAttack(Adventurer other){
        if (getSpecial() > 0){

        }
    }


}
