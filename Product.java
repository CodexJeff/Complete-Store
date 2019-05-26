//Base class for all products the store will sell
public class Product{
    private double price;
    private int quantity;

    public Product(double initPrice, int initQuantity){
        price = initPrice;
        quantity = initQuantity;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getPrice(){
        return price;
    }

    //Returns the total revenue (price * amount) if there are at least amount items in stock
    //Return 0 otherwise (i.e., there is no sale completed)
    public double sellUnits(int amount){
        if(amount > 0 && quantity >= amount){
            quantity -= amount;
            return price * amount;
        }
        return 0.0;
    }

    public void sellUnits2(int amount){
        if(amount > 0 && !(quantity > 10)){
            quantity += amount;
        }
    }
}