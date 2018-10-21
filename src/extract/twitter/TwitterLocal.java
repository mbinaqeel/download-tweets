/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extract.twitter;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.GeoLocation;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author MBA
 */
public class TwitterLocal extends Thread
{
    private String consumer = null, consumerSecret = null, access = null, accessSecret = null, fileName =null;
    
    private int c =0;
    
    //in attributes ko lazmi fill krna hai agr location based tweets laani han
    private double x=0, y=0, r =0;
    public String sinceDate = null, toDate = null, table= null;
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setC(int c) {
        this.c = c;
    }
    
    
    
    public TwitterLocal(String con, String conS, String acc, String accS)
    {
        consumer  =con;
        consumerSecret=conS;
        access=acc;
        accessSecret=accS;

    }
    
    
    public Configuration getConfigurations()
    {
        
     ConfigurationBuilder cb = new ConfigurationBuilder(); 
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumer)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(access)
                .setOAuthAccessTokenSecret(accessSecret);
        return cb.build();
    }
    
    @Override
    public void run()
    {
        getLocationBasedTweetsAndInsertIntoDB();
    }
    public void getLocationBasedTweetsAndInsertIntoFileWithCount()
    {
        //do not change this code
        
  //Connection con = null;
        //PreparedStatement stm = null;
        Twitter twitter = new TwitterFactory(getConfigurations()).getInstance();
        
        try{
        /*
            Class.forName("com.mysql.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost/twitterwali","root","1234");
        stm = con.prepareStatement("insert into nyc (obj) values(?)");
          */  
            ObjectOutputStream dout = new ObjectOutputStream(new FileOutputStream(fileName));
        //-summer -itunes -travel -orlaeans -gulf -shore -ocean -jungle -pic -photo -rio -iceland -trip -sky -beautiful -party -hat -tour -canal -food -city -beach -born -florida -visit 
            
            Query query = new Query().geoCode(new GeoLocation(x,y),r,"km");
            //Query query = new Query("place:\"پاکستان\"");
            //query.setUntil();
            query.setLang("en");
            int numberOfTweets = c;
            long lastID = Long.MAX_VALUE;
        
            ArrayList<Status> tweets = new ArrayList<Status>();
       
        
            int count = 1 ;
            do
            {
                if (numberOfTweets - tweets.size() > 100)
                    query.setCount(100);
                else 
                    query.setCount(numberOfTweets - tweets.size());
                String q = null;
                try {
                    QueryResult result = twitter.search(query);
                    ArrayList<Status> someTweets = (ArrayList) result.getTweets();
                    tweets.addAll(someTweets);
            
            
                    System.out.println("Gathered " + tweets.size()+"   " +fileName + "      tweets and count is : " + count++ );
            
                    for (Status t: someTweets) {
                        if(t.getId() < lastID)
                            lastID = t.getId();
                        //String name = t.getUser().getName();
                        
                        //object being written on file
                        dout.writeObject(t);
                        //stm.setObject(1, t);
                        //stm.executeUpdate();
                        
                        System.out.println(t.getUser().getName() +"======"+ t.getText()+"===="+t.getId()+"====");
                    }
            }
            catch (Exception te) {
                te.printStackTrace();
            System.out.println("Couldn't connect: " + te);
            System.out.println(q);
            }; 
            query.setMaxId(lastID-1);
            
            
            
            //sleeping the thread for 6 seconds
            Thread.sleep(6000);
        }while(tweets != null);
                //TwitterFactory.getSingleton();
        dout.close();
        System.out.print(tweets.size());
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
      finally{
  /*          
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TwitterLocal.class.getName()).log(Level.SEVERE, null, ex);
            }
*/

        }
          
      
    }
    public void insertInDB(PreparedStatement pstmt, Status status) throws SQLException, ClassNotFoundException, UnsupportedEncodingException
    {
        //byte[] data = status.getText().toString().getBytes("UTF-8");
        //String text = Base64.getEncoder().encodeToString(data);
        //the text is encoded
        pstmt.setString(1, status.getText());
        pstmt.setBoolean(2, status.isFavorited());
        pstmt.setInt(3, status.getFavoriteCount());
        pstmt.setString(4, status.getInReplyToScreenName());
        pstmt.setString(5, status.getCreatedAt().toString());
        pstmt.setBoolean(6, status.isTruncated());
        pstmt.setLong(7, status.getInReplyToStatusId());
        pstmt.setLong(8, status.getId());
        pstmt.setLong(9, status.getInReplyToUserId());
        pstmt.setString(10, status.getSource());
        pstmt.setString(11, status.getUser().getScreenName());
        pstmt.setDouble(12, status.getRetweetCount());
        pstmt.setBoolean(13, status.isRetweet());
        pstmt.setBoolean(14, status.isRetweeted());
        if(status.getGeoLocation() != null)
        {
            pstmt.setString(15, ""+status.getGeoLocation().getLatitude());
            pstmt.setString(16,""+status.getGeoLocation().getLatitude());
        }
        else
        {
            pstmt.setString(15, null);
            pstmt.setString(16,null);  
        }
        pstmt.addBatch();
        

    }
    
    public void getLocationBased_Accounts_InsertIntoDB()
    {
        //this function searches location based users from twitter and insert them into DB
        //do not make it work
        System.out.println("hellow lksdaj l;asjdfl;");
        String pquery = "insert into "+table+"(text,favorited,favoriteCount,replyToSN,created,truncated,replyToSID,id,replyToUID,statusSource,screenName,retweetCount,isRetweet,retweeted,longitude,latitude) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement stm = null;
        Twitter twitter = new TwitterFactory(getConfigurations()).getInstance();
        try{
        
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/twitterwali?autoReconnect=true&useSSL=false","root","1234");
            con.setAutoCommit(false);
            stm = con.prepareStatement(pquery);
          
            //ObjectOutputStream doutTweet = new ObjectOutputStream(new FileOutputStream(fileName));
            ObjectOutputStream doutUser = new ObjectOutputStream(new FileOutputStream("user_"+fileName));
        
            Query query = new Query().geoCode(new GeoLocation(x,y),r,"km");
            query.setSince("2016-11-04");
            query.setUntil("2016-11-06");
            query.setCount(100);
            query.setLang("en");
            
            long lastID = Long.MAX_VALUE;
        
            ArrayList<Status> someTweets = new ArrayList<Status>();
       
            int count = 0 ;
            do
            {
                
                try {
                    //twitter.searchUsers(query, c);
                    QueryResult result = twitter.search(query);
                    someTweets = (ArrayList) result.getTweets();
                    //tweets.addAll(someTweets);
                    count+= someTweets.size();
            
                    System.out.println("Gathered " + count+"   " +fileName + "      tweets and count is : ");
                    for (Status t: someTweets) 
                    {
                        if(t.getId() < lastID)
                            lastID = t.getId();
                        //String name = t.getUser().getName();
                        
                  //      t.get
                        //object being written on file
                        //doutTweet.writeObject(t);
                        
                        insertInDB(stm, t);
                        doutUser.writeObject(t.getUser());
                        System.out.println(t.getUser().getName() +"======"+ t.getText()+"===="+t.getId()+"====");
                    }
                    stm.executeBatch();
                    con.commit();
            }
            catch (Exception te) {
                te.printStackTrace();
            System.out.println("Couldn't connect: " + te);
            
            }; 
            query.setMaxId(lastID-1);
            
            
            
            //sleeping the thread for 6 seconds
            Thread.sleep(6000);
        }while(someTweets != null);
                //TwitterFactory.getSingleton();
        //doutTweet.close();
        doutUser.close();
        System.out.print(count);
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
      finally{
            
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TwitterLocal.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
          
    }
    private String usersFile = null;
    public void setUsersFile(String path)
    {
        usersFile = path;
    }
    public void getUsersTimeline()
    {
        //
        System.out.println("hellow lksdaj l;asjdfl;");
        String pquery = "insert into all_users(text,favorited,favoriteCount,replyToSN,created,truncated,replyToSID,id,replyToUID,statusSource,screenName,retweetCount,isRetweet,retweeted,longitude,latitude) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement stm = null;
        Twitter twitter = new TwitterFactory(getConfigurations()).getInstance();
        try{
        
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/twitterwali?autoReconnect=true&useSSL=false","root","1234");
            con.setAutoCommit(false);
            stm = con.prepareStatement(pquery);
            
            BufferedReader dinUser = new BufferedReader(new FileReader(usersFile));
            //ObjectOutputStream doutUser = new ObjectOutputStream(new FileOutputStream("user_"+fileName));
            ArrayList<Status> someTweets = null;
            String username = null;
            while(dinUser.ready()){
                username = dinUser.readLine();
                int count = 0 , i = 1;
                do
                {

                    try {

                        someTweets = (ArrayList) twitter.getUserTimeline(username, new Paging(i, 200));
                        count+= someTweets.size();

                        System.out.println("Gathered " + count+"   " +username + "      tweets and count is : ");
                        for (Status t: someTweets) 
                        {
                            insertInDB(stm, t);
                            //doutUser.writeObject(t.getUser());
                            System.out.println(t.getUser().getName() +"======"+ t.getText()+"===="+t.getId()+"====");
                        }
                        stm.executeBatch();
                        con.commit();
                    }
                    catch (Exception te) {
                        te.printStackTrace();
                    System.out.println("Couldn't connect: " + te);
                    };
                i++;
                //sleeping the thread for 6 seconds
                Thread.sleep(6000);
            }while(someTweets.size() != 0);
            //doutUser.close();
                
            System.out.print(count);
            }
            dinUser.close();
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
      finally{
            
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TwitterLocal.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }
    
    public void getLocationBasedTweetsAndInsertIntoDB()
    {
        System.out.println("hellow lksdaj l;asjdfl;");
        String pquery = "insert into "+table+"(text,favorited,favoriteCount,replyToSN,created,truncated,replyToSID,id,replyToUID,statusSource,screenName,retweetCount,isRetweet,retweeted,longitude,latitude) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement stm = null;
        Twitter twitter = new TwitterFactory(getConfigurations()).getInstance();
        try{
        
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost/twitterwali?autoReconnect=true&useSSL=false","root","1234");
            con.setAutoCommit(false);
            stm = con.prepareStatement(pquery);
          
            //ObjectOutputStream doutTweet = new ObjectOutputStream(new FileOutputStream(fileName));
            ObjectOutputStream doutUser = new ObjectOutputStream(new FileOutputStream("user_"+fileName));
        
            Query query = new Query().geoCode(new GeoLocation(x,y),r,"km");
            query.setSince("2017-05-05");
            query.setUntil("2017-05-07");
            query.setCount(100);
            query.setLang("en");
            
            long lastID = Long.MAX_VALUE;
        
            ArrayList<Status> someTweets = new ArrayList<Status>();
       
            int count = 0 ;
            do
            {
                
                try {
                    QueryResult result = twitter.search(query);
                    someTweets = (ArrayList) result.getTweets();
                    //tweets.addAll(someTweets);
                    count+= someTweets.size();
            
                    System.out.println("Gathered " + count+"   " +fileName + "      tweets and count is : ");
                    for (Status t: someTweets) 
                    {
                        if(t.getId() < lastID)
                            lastID = t.getId();
                        //String name = t.getUser().getName();
                        
                  //      t.get
                        //object being written on file
                        //doutTweet.writeObject(t);
                        
                        insertInDB(stm, t);
                        doutUser.writeObject(t.getUser());
                        System.out.println(t.getUser().getName() +"======"+ t.getText()+"===="+t.getId()+"====");
                    }
                    stm.executeBatch();
                    con.commit();
            }
            catch (Exception te) {
                te.printStackTrace();
            System.out.println("Couldn't connect: " + te);
            
            }; 
            query.setMaxId(lastID-1);
            
            
            
            //sleeping the thread for 6 seconds
            Thread.sleep(6000);
        }while(someTweets != null);
                //TwitterFactory.getSingleton();
        //doutTweet.close();
        doutUser.close();
        System.out.print(count);
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
      finally{
            
            try {
                stm.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TwitterLocal.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
          

    }
  /*  
    public void getTweetsNInsertIntoDB()
    {
        Connection con = null;
        Statement stm = null;
        Twitter twitter = new TwitterFactory(getConfigurations()).getInstance();
      try{
        
        Class.forName("com.mysql.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost/twitterwali","root","1234");
        stm = con.createStatement();
                
        
        
        String geoloc = "near:\"Hawaii, USA\" within:15mi", panamaquery = "panama -summer -itunes -travel -orlaeans -gulf -shore -ocean -jungle -pic -photo -rio -iceland -trip -sky -beautiful -party -hat -tour -canal -food -city -beach -born -florida -visit since:2016-05-10 until:2016-08-14";
        
        Query query = new Query(geoloc);
        query.setLang("en");
        int numberOfTweets = 50000;
        long lastID = Long.MAX_VALUE;
        
        ArrayList<Status> tweets = new ArrayList<Status>();
       
        while (tweets.size () < numberOfTweets)
        {
          if (numberOfTweets - tweets.size() > 100)
            query.setCount(100);
          else 
            query.setCount(numberOfTweets - tweets.size());
          String q = null;
          try {
            QueryResult result = twitter.search(query);
            ArrayList<Status> someTweets = (ArrayList) result.getTweets();
            tweets.addAll(someTweets);
            
            //here we will insert some tweets into db.
                    
            System.out.println("Gathered " + tweets.size() + " tweets");
            
            for (Status t: someTweets) {
              if(t.getId() < lastID) 
                  lastID = t.getId();
              String name = t.getUser().getName();
              name.replace("'", "`");
              q = "insert into regionalTweets (name, sname, text, uid) values('"+t.getUser().getName().replace('\'',' ').replace('?', ' ')+"', '"
                        + t.getUser().getScreenName().replace('\'',' ').replace('?', ' ')+"','"+ t.getText().replace('\'',' ').replace('\"', ' ').replaceAll("[^\\x00-\\x7F]", "")+
              "',"+t.getId()+")";
              stm.executeUpdate(q);
              
              System.out.println(t.getUser().getName() +"======"+ t.getText()+"===="+t.getId()+"====");
            }

          }

          catch (Exception te) {
            te.printStackTrace();
              System.out.println("Couldn't connect: " + te);
              System.out.println(q);
            }; 
          query.setMaxId(lastID-1);
        }        
                //TwitterFactory.getSingleton();
        
        
        System.out.print(tweets.size());
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
      finally{
       try{ stm.close();
        con.close();
       }
       catch(Exception ex){
           ex.printStackTrace();
       }
      }
    }*/
}
