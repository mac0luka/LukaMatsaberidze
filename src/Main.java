import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        JDBC.createTable();
        for(int i =0;i<15;i++)
        {
            Product product;
            if(i%2==0)
            {
                product=new Product("laptop",4000.96f,i+4,2013.3f);
            }
            else if(i>10)
            {
                product=new Product("phone",1000.96f,i-2,209.5f);
            }
            else if(i%3==0)
            {
                product=new Product("tv",1400.0f,i,8012.3f);
            }
            else {
                product=new Product("couch",510.13f,i,9042.5f);
            }
            JDBC.InsertProductRows(product);
        }

        //washlis gamoyeneba shlis ID is mititebit
        JDBC.GetSimilarProductsCount();
        System.out.println(" before deleting 1st ID");
        JDBC.PrintAllData();
        JDBC.DeleteProductWithID(1);
        System.out.println(" after deleting 1st ID");
        JDBC.PrintAllData();

        //update is gamoyeneba, cvlis counts ID is mititebit
        System.out.println(" before Updating 3rd ID");
        JDBC.PrintAllData();
        JDBC.UpdateProductWithID(3,50);
        System.out.println(" after Updating 3rd ID");
        JDBC.PrintAllData();


        Map<String,Integer> hashMapList = JDBC.GetMap();
        try {
            for (Map.Entry entry : hashMapList.entrySet()) {
                System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
            }
        }
        catch (NullPointerException exc)
        {
            System.out.println("empty set");
            exc.printStackTrace();
        }
    }

}