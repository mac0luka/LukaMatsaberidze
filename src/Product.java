import java.util.Objects;

public class Product {
    private String name;
    private float price;
    private int count;
    private float weight;

    private int amount=1;

    public Product(String name, float price, int count, float weight) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public float getWeight() {
        return weight;
    }

    public int GetAmount()
    {
        return amount;
    }

    public void AddCount(int count)
    {
        this.count+=count;
    }

    public void IncreaseAmount()
    {
        amount++;
    }
    @Override
    public boolean equals(Object obj) {
        Product product = (Product)obj;
        if(product==null)
        {
            return false;
        }

        return Objects.equals(this.name, product.getName());
    }
}
