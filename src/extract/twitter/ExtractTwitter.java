/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extract.twitter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author aqeel
 */
public class ExtractTwitter {

    /**
     * @param args the command line arguments
     */
    
    
    /*
    public static Statement createDBConnection()
    {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/twitterwali","root","1234");
            stm = con.createStatement();
            
        } catch (Exception ex) {
            Logger.getLogger(ExtractTwitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stm;
    }
    public static void close()
    {
        
        try {
            if(con != null)
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ExtractTwitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    public static void main(String[] args)  {
       	
        TwitterLocal t9 = new TwitterLocal("rH1mfylmobXJ55LHA6gRZOVki", 
                "lwv6OdDneQOJVQuJm3glx8JsNZ3JOalT5eFqbtBOQUjFLtrEmo", 	
                "784025170793099264-ub3kwtMpjcE7cOmAGVDxne5KSt2ezkG", 
                "Q8wIYtx0fHvZBEMoGMdb8NHX0D1m0EobOd9DW5fWw9jF0");
        
        t9.table = "paris";
         
        t9.setX(48.861322);
        t9.setY(2.340002);
        t9.setR(5);
        
       // t1.getLocationBasedTweetsAndInsertIntoDB();
        t9.start();
        /*
        TwitterLocal t1 = new TwitterLocal("rTE4CBpi4NDP0fCNgIyb0ES3l", 
                "FlaQXfanpc1ZUguNbGHVJUKCfjrXc9ImH8DvmM7sIQ4i42MZJS", 	
                "784025170793099264-pWDIHt6Vofolp8BKMDMU3Y7PzJaBQ0z", 
                "Z0d63CzJm7eAceRTwQPD6N51A4Qy5WdfyDZeMKFk0GmCO");
        t1.table = "lahore";
        t1.setX(31.550989);
        t1.setY(74.358467);
        t1.setR(20);
        
        t1.start();
       
        
        TwitterLocal t3 = new TwitterLocal("1dlftOTJT0Xsx2oLf11xfVlnn", 
                "PL5VFRNPjtCwWhDP1GMEm7iZX7uiaVevKPbZGqHrgXztmG2ut2", 	
                "784025170793099264-R86tpWrxXBdP2PgjKlvB2709LYhMoYr", 
                "rmiL5BbMEwSeqOp2nvyDD9vUSKf3O0OXlniJZorUw5zfz");
        t3.table = "newdelhi";
        t3.setX(28.674393);
        t3.setY(77.105576);
        t3.setR(26);
        
        t3.start();
        
        TwitterLocal t2 = new TwitterLocal("U8NoGWHEF4ZUTR5bftCLxEONx", 
                "OWmdOxo2C3N3ycNSpCJWQ0iPxb8jr54E33bhU27vLYMDjrd9Vv", 	
                "3311899827-3NdcZZIXtA13CHk5cgecPm8DU6dd5wJh9WTw86W", 
                "ZcXWBXccWkC0n4cG1an7l7PONXcQ2izJNZSswZ8YUTcJz");
        t2.table = "berlin";
        t2.setX(52.518076);
        t2.setY(13.390407);
        t2.setR(23);
        t2.start();
        
        
        TwitterLocal t4 = new TwitterLocal("uXFtGbz4OHLKrtLxfgqDHIjZS", 
                "J1JjtwVdjmuoqD99E0eF3WosC7HCFr6SZdnCzzYsAbJzHGHTs0", 	
                "3311899827-CBcc2LrySMnrRldR3oLMF570Ko53mxeq5RenU0U", 
                "5xDp0t8nBkORUFjNnLU8j4bFyDmnguVaGt5RaaXPlE6iI");
        t4.table = "hawaii";
        t4.setX(21.317400);
        t4.setY(-157.844556);
        t4.setR(13);
        
        t4.start();
        
        TwitterLocal t5 = new TwitterLocal("JJV71fauceNNWkYNBhzZOyFQo", 
                "OauUZkqu3uI4ua8JTvqPdGujx5jfCUrflEEoJfGdYMRYKBky0U", 	
                "3311899827-gDKRkBA09pks3x0FFEFWA14TyCsSx6Y83JZinxp", 
                "O6jJEH3jl3hGNINA3kicGhgfksp0iplQUexWQB2mvHCys");
        t5.table = "helsinki";
        t5.setX(60.229307);
        t5.setY(24.940884);
        t5.setR(13);
        
        t5.start();
        
        TwitterLocal t6 = new TwitterLocal("4wSUWkV6wsSzDsLXc5F5tOfz6", 
                "pVI30COcFQ9tPBOBVnil6o79a7rsf2gOCclUSkp6TyRsXEvWPR", 	
                "784025170793099264-t3Jiqn37HIQvdV3q7ssfcR4sv3cRCQl", 
                "RmBjYLGDeDcNEB73qYDABYYdHFjthCFysSAJNONqInh9F");
        t6.table = "islamabad";
        t6.setX(33.685055);
        t6.setY(73.028225);
        t6.setR(17);
        
        t6.start();
        */
    }
    
    public static void copyListofPanamaPeople()
    {
         try {
            // TODO code application logic here
            BufferedReader br = new BufferedReader(new FileReader("D://listan.txt"));
            PrintWriter fs  = new PrintWriter("D://list with only names.txt");
            
            String line = null;
            
            do{
                line = br.readLine();
                //System.out.println(line);
                String [] parts = line.split(",");
                System.out.println(parts[0]);
                fs.println(parts[0].trim());
            
            }while(line != null);
            br.close();
            fs.close();
        } catch (Exception ex) {
            Logger.getLogger(ExtractTwitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
