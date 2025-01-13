public class Healer extends Adventurer{
  private int mana, manaMax;

  public Healer(String name, int hp){
    super(name, hp);
    manaMax = 20;
    mana = manaMax/2;
  }

  public Healer(String name){
    this(name, 18);
  }

  public Healer(){
    this("Priest");
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
  public abstract void setSpecial(int n){
      mana = n;
  }

  //concrete method written using abstract methods.
  //refill special resource by amount, but only up to at most getSpecialMax()
  public int restoreSpecial(int n){
    if( n > getSpecialMax() - getSpecial()){
      n = getSpecialMax() - getSpecial();
    }
    setSpecial(getSpecial()+n);
    return n;
  }

  /*
  all adventurers must have a way to attack enemies and
  support their allys
  */
  //hurt or hinder the target adventurer
  public String attack(Adventurer other){
    int damage = (int)(Math.random() * 5) + 1;
    other.applyDamage(damage);
    return this + "smacks" + other + "with the staff for" + damage + " damage.";
  }

  /*This is an example of an improvement that you can make to allow
   * for more flexible targetting.
   */
  //heal or buff the party
  //public abstract String support(ArrayList<Adventurer> others);

  //heal or buff the target adventurer
  public abstract String support(Adventurer other);

  //heal or buff self
  public abstract String support();

  //hurt or hinder the target adventurer, consume some special resource
  public abstract String specialAttack(Adventurer other);

}
