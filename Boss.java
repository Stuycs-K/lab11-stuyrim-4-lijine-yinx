public class Boss extends Adventurer{
  private int rage, rageMax;

  public Boss(String name, int hp){
    super(name, hp);
    rageMax = 20;
    rage = rageMax/2;
  }

  public Boss(String name){
    this(name, 120);
  }

  public Boss(){
    this("Dragon");
  }

  public String getSpecialName(){
    return "rage";
  }

  //accessor methods
  public int getSpecial(){
    return rage;
  }

  public int getSpecialMax(){
    return rageMax;
  }

  public void setSpecial(int n){
    rage = n;
  }

  public int restoreSpecial(int n){
    if(n > getSpecialMax() - getSpecial()){
      n = getSpecialMax() - getSpecial();
    }
    setSpecial(getSpecial() - n);
    return n;
  }

  public String attack(Adventurer other){
    int damage = 20;
    other.applyDamage(damage);
    return this + "shoots fire from his mouth and burns" + other + "for" + damage + "HP.";
  }

  public String support(){
    int hp = 10;
    if(getSpecial() > 10){
      
    }
  }
