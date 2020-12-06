import java.time.*;
import java.time.format.*;
import java.util.ArrayList;
abstract  class Drinks{
    protected String name;//名字
    protected double cost;//成本
    protected LocalDate producedate;//生产日期
    protected int exp;//保质期
    public Drinks(){}
    public Drinks(String name, double cost, LocalDate producedate, int exp) {
        this.name = name;
        this.cost = cost;
        this.producedate = producedate;
        this.exp = exp;
    }
    protected boolean isoverdue(){
        LocalDate d=LocalDate.now();
        Period x=Period.between(this.producedate,d);
        if(d.compareTo(this.producedate.plusDays(exp))>0) return false;
        else return true;
    }
    public abstract String toString();
}
class Beer extends Drinks{
    private float alcohol;
    public Beer(String name, double cost, LocalDate producedate, int exp ,float alcohol){
        super(name,cost,producedate,exp=30);
        this.alcohol=alcohol;
    }
    @Override
    public String toString() {
        return "Beer{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", producedate=" + producedate +
                ", exp=" + exp +
                ", alcohol=" + alcohol +
                '}';
    }
}
class Juice extends Drinks{
    public Juice(String name, double cost, LocalDate producedate, int exp){
        super(name,cost,producedate,exp=2);
    }
    @Override
    public String toString() {
        return "Juice{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", producedate=" + producedate +
                ", exp=" + exp +
                '}';
    }
}
class SetMeal{
    private String tname;//套餐名
    private double price;//套餐价格
    private String zname;//炸鸡名
    private Drinks d;
    public SetMeal(String tname, double price, String zname, Drinks d) {
        this.tname = tname;
        this.price = price;
        this.zname = zname;
        this.d = d;
    }
    public String getTname() {
        return tname;
    }
    public double getPrice() {
        return price;
    }
    public String getZname() {
        return zname;
    }
    public Drinks getD() {
        return d;
    }
    @Override
    public String toString() {
        return "SetMeal{" +
                "tname='" + tname + '\'' +
                ", price=" + price +
                ", zname='" + zname + '\'' +
                ", " + d +
                '}';
    }
}
interface FriedChickenRestaurant{
    void SellMeal(SetMeal meal);
    void inMeal(SetMeal meal);
}
abstract class West2FriedChickenRestauran implements FriedChickenRestaurant{
    double account;
    ArrayList<Beer>beerlist=new ArrayList<Beer>();
    ArrayList<Juice>juicelist=new ArrayList<Juice>();
    static ArrayList<SetMeal>setmeallist=new ArrayList<SetMeal>() ;{
        LocalDate produce = LocalDate.of(2020, 10, 10);//指定日期
        Drinks d1 = new Beer("beer1", 6.5, produce, 30, 5);
        Drinks d2 = new Beer("beer2", 5.5, produce, 30, 4);
        Drinks d3 = new Juice("orange juice", 7, produce, 2);
        SetMeal meal1 = new SetMeal("meal1", 28, "fc1", d1);
        SetMeal meal2 = new SetMeal("meal2", 25, "fc2", d2);
        SetMeal meal3 = new SetMeal("meal3", 26, "fc3", d3);
        setmeallist.add(meal1);
        setmeallist.add(meal2);
        setmeallist.add(meal3);
    }
    public West2FriedChickenRestauran(int account, ArrayList<Beer> beerlist, ArrayList<Juice> juicelist) {
        this.account = account;
        this.beerlist = beerlist;
        this.juicelist = juicelist;
    }
    private boolean use(Beer beer){
        if(beerlist.contains(beer)){
            beerlist.remove(beerlist.indexOf(beer));
            return true;
        }
        else{
            throw new IngredientSortOutException("啤酒已售完");
        }
    }
    private boolean use(Juice juice){
        if(juicelist.contains(juice)){
            juicelist.remove(juicelist.indexOf(juice));
            return true;
        }
        else{
            throw new IngredientSortOutException("果汁已售完");
        }
    }
    @Override
    public void SellMeal(SetMeal meal) {
        if(meal.getD() instanceof Beer){
            Beer b=(Beer) meal.getD();
            if(use(b)) account+=meal.getPrice();
        }
        if(meal.getD() instanceof Juice){
            Juice j=(Juice) meal.getD();
            if(use(j)) account+=meal.getPrice();
        }
    }
    @Override
    public void inMeal(SetMeal meal) {
        double sum=meal.getPrice();
        double x=account-sum;
        if(x<0) throw new OverdraftBalanceException("进货差 "+(sum-account)+" 元");
        else account-=sum;
        if(meal.getD() instanceof Beer){
            Beer b=(Beer) meal.getD();
            beerlist.add(b);
        }
        if(meal.getD() instanceof Juice){
            Juice j=(Juice) meal.getD();
            juicelist.add(j);
        }
    }
}
//自定义异常
class IngredientSortOutException extends RuntimeException{//果汁或啤酒售完
    public IngredientSortOutException(){
        super();
    }
    public IngredientSortOutException(String message,Throwable cause){
        super(message,cause);
    }
    public IngredientSortOutException(String message){
        super(message);
    }
    public IngredientSortOutException(Throwable cause){
        super(cause);
    }
}
class OverdraftBalanceException extends RuntimeException{//进货费用超出拥有余额
    public OverdraftBalanceException(){
        super();
    }
    public OverdraftBalanceException(String message,Throwable cause){
        super(message,cause);
    }
    public OverdraftBalanceException(String message){
        super(message);
    }
    public OverdraftBalanceException(Throwable cause){
        super(cause);
    }
}
