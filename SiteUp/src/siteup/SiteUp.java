/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siteup;

import java.io.*;
import java.net.*;
//import java.util.*;
import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * 
 * @author perry
 */
public class SiteUp {
    final String JSONFileLocation = "./sites.json";
    /**
    * @expects json file to be at the 
    * @description opens a JSON file that's an array of simple arrays
    * ex format [["http://site.com","some string"],["http://site2.com","some other string"]]
    */
    public JSONArray getSiteList(){
        JSONParser parser = new JSONParser();
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        try {
            Object obj = parser.parse(new FileReader(JSONFileLocation));
            JSONArray array = (JSONArray)obj;
            //System.out.println(array.size());
            return array;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
    * @params theURL: the URL to check 
    *         phrase: the string to look for at the given URL
    * @returns boolean: is string found in the given URL?
    * @description checks the markup at the given url for the phrase
    */
    public boolean verifySite(String theUrl, String phrase) throws IOException{
        try {
        // do nothing yet
            URL gotoUrl = new URL(theUrl);
            System.out.println("\nChecking URL: "+theUrl);
            System.out.println("For an instance of the word: "+phrase);
            
            InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
            BufferedReader in = new BufferedReader(isr);

            StringBuffer sb = new StringBuffer();
            String inputLine;
            boolean isFirst = true;
            
            //grab the contents of the URL
            while ((inputLine = in.readLine()) != null){
                sb.append(inputLine+"\r\n");
            }
            //System.out.print(sb);
            boolean stringFound;
            String html = sb.toString();
            stringFound = html.toLowerCase().contains(phrase.toLowerCase());
            if(stringFound){
               System.out.println("Hey, I found it!");
               return true;
            }else{
               return false;
            }
        }
        catch (MalformedURLException mue) {
            return false;
            //mue.printStackTrace();
        }
        catch (IOException ioe) {
            return false;
            //throw ioe;
        }
    }
    /**
    * @params siteName: name of the site that's down
    * @description sends an alert that the site is down
    */
    public static void siteDown(String siteName){
        System.out.println("Ruh Roh, something's wrong, sending email alert");
        String[] message = {"hey there, "+siteName+" is down"};
            SendEmail emailer = new SendEmail();
            emailer.main(message);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            SiteUp httpGetter = new SiteUp();
            JSONArray array = httpGetter.getSiteList();
            for(int i = 0; i < array.size(); i = i+1){
                JSONArray line = (JSONArray) array.get(i);
                //System.out.println("Checking "+line.get(0)+" for the word "+line.get(1));
                if(!httpGetter.verifySite((String)line.get(0), (String)line.get(1))){
                    siteDown((String)line.get(0));
                };
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


