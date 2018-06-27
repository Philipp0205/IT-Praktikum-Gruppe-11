package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;


public class DBConnection {


    private static Connection con = null;

    private static String user = "root";
    private static String password = "ThieskesOberesDrittel!";
    private static String url;
    private static String googleUrl = "jdbc:google:mysql://it-projekt-jabics:europe-west3:jabics/jabics?user=root&password=ThieskesOberesDrittel!";
    private static String localUrl = "jdbc:mysql://35.198.159.112:3306/jabics?verifyServerCertificate=false&useSSL=true";

    
    public static Connection connection() {


            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    url = googleUrl;
                } else {

                    Class.forName("com.mysql.jdbc.Driver");
                    url = localUrl;
                }

                con = DriverManager.getConnection(url,user,password);
            } catch (Exception e) {
                con = null;
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        

        return con;
    }

}