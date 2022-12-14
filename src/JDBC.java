import com.mysql.cj.result.Row;

import javax.swing.text.TabExpander;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JDBC {
    private  static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private  static final String DATABASEURL="jdbc:mysql://localhost:3306/Products";
    private static final String USERNAME="root1";
    private static final String PASSWORD="Pa$$w0rd";

    private static final String TABLENAME="PRODUCTS";

    private static int ID=0;

public static void createTable()
{
    try
    {
        Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        String dropIfExistsSqlString="DROP TABLE IF EXISTS "+ TABLENAME;
        statement.execute(dropIfExistsSqlString);

        String createSqlString = "CREATE TABLE " + TABLENAME + " " +
                "(id INTEGER not NULL," +
                " prodName VARCHAR(255)," +
                " price FLOAT,"+
                " productCount INTEGER,"+
                " weight FLOAT,"+
                " PRIMARY KEY ( id ))";

        statement.executeUpdate(createSqlString);
        System.out.println("Created Table");
    }
     catch (SQLException e)
     {
         System.out.println("couldn't connect to database");
         e.printStackTrace();
    }
}

public static void InsertProductRows(Product product)
{
    try
    {
        ++ID;
        Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        String sqlString = String.format("INSERT INTO %s(id, prodName,price,productCount,weight)"
                + " values (?, ?, ?, ?, ?)",TABLENAME);

        PreparedStatement preparedStmt = connection.prepareStatement(sqlString);

        preparedStmt.setInt (1, ID);
        preparedStmt.setString (2, product.getName());
        preparedStmt.setFloat(3,product.getPrice());
        preparedStmt.setInt(4,product.getCount());
        preparedStmt.setFloat(5,product.getWeight());

        preparedStmt.executeUpdate();

        System.out.println("inserted into table: " + product.getName());

    } catch (SQLException e) {
        System.out.println("couldn't insert");
        e.printStackTrace();
    }
}

    public static void GetSimilarProductsCount()
    {
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            statement.execute("SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''))");
            String sql = "SELECT prodName as productName,COUNT(prodName) as count_name,SUM(productCount) as prodCount from " + TABLENAME +" GROUP BY prodName HAVING ( COUNT(prodName) > 0 )";
            ResultSet  rs = statement.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("productName");
                System.out.println("tviton produqtis raodenobis gatvaliswinebis gareshe : " + name + " " + rs.getInt("count_name"));
                int sumValue = rs.getInt("prodCount");
                System.out.println("produqtis raodenobis gatvaliswinebit : " + name+ " "  + " " + sumValue);
            }

        } catch (SQLException e) {
            System.out.println("Couldnt retrieve data");
            e.printStackTrace();
        }
    }

//  updates product count based on ID
    public static void UpdateProductWithID(int ID, int count)
    {
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            String sqlString = "UPDATE " + TABLENAME +
                    " SET productCount = " + count + " WHERE id = "  + ID;
            statement.executeUpdate(sqlString);
        } catch (SQLException e) {
            System.out.println("Couldnt Update data");
            e.printStackTrace();
        }
    }

    //deletes product with corresponding id
    public static void DeleteProductWithID(int ID)
    {
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();

            String checkIfExists = "DELETE FROM " + TABLENAME+ " WHERE id = " + ID;
            statement.executeUpdate(checkIfExists);
        } catch (SQLException e) {
            System.out.println("Couldnt Delete data");
            e.printStackTrace();
        }
    }

    public static  void PrintAllData()
    {
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM " + TABLENAME;

            ResultSet  rs = statement.executeQuery(sql);

            while (rs.next()) {
                System.out.println("-------------------------------------------------------");
                System.out.println("| "+ rs.getInt("id") +" | "  + rs.getString("prodName") + " | " + rs.getFloat("price")+ " | " + rs.getInt("productCount")+ " | " + rs.getFloat("weight") + "|");
            }
            System.out.println("-------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Couldnt retrieve data");
            e.printStackTrace();
        }
    }

    public static Map<String,Integer> GetMap()
    {
        List<Product> productList = new ArrayList<>();
        List<Product> UniqueproductList = new ArrayList<>();
        String input="";
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM " + TABLENAME;

            ResultSet  rs = statement.executeQuery(sql);

            while (rs.next()) {
                Product newProduct = new Product(rs.getString("prodName") ,rs.getFloat("price"),rs.getInt("productCount"),rs.getFloat("weight") );
                productList.add(newProduct);
            }
            UniqueproductList.add(productList.get(0));

            for(int i =1; i < productList.size();i++)
            {
                if(UniqueproductList.contains(productList.get(i)))
                {
                    int productIndex = i;
                   int index = IntStream.range(0, UniqueproductList.size())
                            .filter(k -> productList.get(productIndex).equals(UniqueproductList.get(k)))
                            .findFirst()
                            .orElse(-1);
                    UniqueproductList.get(index).AddCount(productList.get(productIndex).getCount());
                    UniqueproductList.get(index).IncreaseAmount();
                }
                else
                {
                    UniqueproductList.add(productList.get(i));
                }
            }
            //itvaliswinebs produqtebis jamur raodenobas
            //jamuri raodenobis gareshe tviton ertnairi row ebis raodenobis dasabruneblad getCount() is magivrad IncreaseAmount() unda eweros;
            Map<String, Integer> lengthMap
                    = UniqueproductList.stream()
                    .collect(Collectors.toMap(
                            value -> value.getName(),
                            value -> value.getCount()));
            return  lengthMap;
        } catch (SQLException e) {
            System.out.println("Couldnt retrieve data");
            e.printStackTrace();
        }
        return null;
    }
}
