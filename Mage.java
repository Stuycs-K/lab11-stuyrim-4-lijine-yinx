public class Mage extends Adventurer{
    private int mana, manaMax;

    public Mage(String name, int hp){
        super(name,hp);
        manaMax = 30;
        mana = manaMax/2;
    }

    public Mage(String name){
      this(name, 50);
    }
  
    public Mage(){
      this("wizard");
    }

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
    if (getSpecialMax() - getSpecial() > n){
        n = getSpecial() - getSpecial();
    }
    setSpecial(getSpecial() + n);
    return n;
  }

  /*
  all adventurers must have a way to attack enemies and
  support their allys
  */
  //hurt or hinder the target adventurer
    public String attack(Adventurer other){
        int damage = 15;
        other.applyDamage(damage);
        return other.getName() + " has been pulverized until a crisp. They lose 15 hp and now have " + other.getHP();
    }

  /*This is an example of an improvement that you can make to allow
   * for more flexible targetting.
   */
  //heal or buff the party
  //public abstract String support(ArrayList<Adventurer> others);

  //heal or buff the target adventurer
    public String support(Adventurer other){
        return "uses spiritual powers on "+other+" and restores "
        + other.restoreSpecial(5)+" "+other.getSpecialName();
      }
      /*Restores 6 special and 1 hp to self.*/
    
    public String support(){
        int hp = 5;
        if (getSpecial() > 10){
            setHP(getHP()+hp);
            setSpecial(getSpecial()- 5);
            return this+"consumes 10 mana to restore 5 hp";
        }
        else {
            return this + "did not have enough mana";
        }
      }


  //hurt or hinder the target adventurer, consume some special resource
    public String specialAttack(Adventurer other){
        if(getSpecial() >= 10){
            setSpecial(getSpecial()-10);
            int damage = 20;
            other.applyDamage(damage);
            return this + " used their wand and struck the head of the opponent";
          }else{
            return "Not enough caffeine to use the ultimate code. Instead "+attack(other);
          }
    }


}
