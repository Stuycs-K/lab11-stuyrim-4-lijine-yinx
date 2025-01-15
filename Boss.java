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
