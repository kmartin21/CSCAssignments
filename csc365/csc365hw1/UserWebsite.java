/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webpagecategorize;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;

/**
 *
 * @author keithmartin
 */
public class UserWebsite {

    //Create user hashTable
    HashTable hashTable;
    //Create ArrayList of file hashTables
    ArrayList<HashTable> hashList = new ArrayList<>();
    //Variable to iterate through
    int j = 0;
    //String for official recommended site
    String recommendedWebsite;
    //Flag to load ArrayList of hashtables once and never again
    boolean called;

    //Method used to process website user types in
    public void processUserWebsite(String website) throws IOException {

        try {

            URL oracle = new URL(website);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            String inputText = null;
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                inputText += inputLine;
            }

            //Strip html tags
            String strippedText = Jsoup.parse(inputText).text();
            //Create regex pattern to get words a-z
            Pattern regex = Pattern.compile("([A-Za-z])+");
            //Find matched words
            Matcher regexMatcher = regex.matcher(strippedText);

            //Create string to store words
            String words = "";

            // Go through all of matched words and separate by commas
            while (regexMatcher.find()) {
                // Start position of text
                int start = regexMatcher.start(0);
                // End position of text
                int end = regexMatcher.end(0);
                // Add words to string 'words' and put comma before word
                words += "," + strippedText.substring(start, end).toLowerCase();
            }

            // Take out initial comma
            words = words.substring(1);
            //Split by comma
            String[] w = words.split(",");

            //Intialize user hashTable
            hashTable = new HashTable();

            //Put every word into hashTable starting with a count of 1
            for (String word : w) {
                hashTable.put(word, 1);
            }
        } catch (MalformedURLException ex) {
            throw ex;
        }

    }

    // Method used to process websites from "web.txt" file
    public void processFileWebsites() throws MalformedURLException, IOException {

        java.io.File file = new java.io.File("web.txt");

        try {
            Scanner input = new Scanner(file);
            while (input.hasNext() && hashList.size() >= j) {


                String website = input.nextLine();

                URL oracle = new URL(website);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String inputText = null;
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    inputText += inputLine;
                }

                //Strip html tags
                String strippedText = Jsoup.parse(inputText).text();
                //Create regex pattern to get words a-z
                Pattern regex = Pattern.compile("([A-Za-z])+");
                //Find matched words
                Matcher regexMatcher = regex.matcher(strippedText);

                //Create string to store words
                String words = "";

                // Go through all of matched words and separate by commas
                while (regexMatcher.find()) {
                    // Start position of text
                    int start = regexMatcher.start(0);
                    // End position of text
                    int end = regexMatcher.end(0);
                    // Add words to string 'words' and put comma before word
                    words += "," + strippedText.substring(start, end).toLowerCase();
                }

                // Take out intial comma
                words = words.substring(1);
                // Split by comma
                String[] w = words.split(",");

                // Intialize file hashTable
                HashTable newHashTable = new HashTable();
                // Set url string to website name to access the name of best website
                newHashTable.setUrlString(website);
                // Add hashTable to ArrayList
                hashList.add(newHashTable);

                // For each word, get hashTable from current position in ArrayList
                // and put word every word from current site into hashTable starting with a count of 1
                for (String word : w) {
                    hashList.get(j).put(word, 1);
                }


                //Increment j to continue along loop
                j++;


                in.close();

            }

        } catch (FileNotFoundException e) {
            System.out.println("File does not exist\n");
        }

    }

    // Used to load ArrayList once in beginning and then never again
    public void loadArrayList() throws MalformedURLException, IOException {
        if (called) {
            return;
        }
        called = true;
        this.processFileWebsites();
    }

// Similarity metric
    public void sim() {

        //Similar words between user website and file website
        int simFileCount = 0;
        //Total count of words in file website
        int totalFileCount = 0;
        //Temporary website to compare to current bestWebsite
        float tmpWebsite = 0;
        //Current bestWebsite
        float bestWebsite = 0;



        if (hashTable == null) {
            return;
        }

        //Store keys in array
        String[] userSiteWordsArray = hashTable.getKeys();
        //Iterate through hashTables in ArrayList
        for (HashTable ht : hashList) {
            //Iterate through keys in user HashTable
            for (String key : userSiteWordsArray) {
                //if keys are equal in both sites then increment simFileCount by count of the word
                if (ht.get(key) != 0) {
                    simFileCount += ht.get(key);
                }
            }
            //Loop used to get total count of every word in file site
            for (String k : ht.getKeys()) {
                totalFileCount += ht.get(k);

            }

            //Divide count of similar words in file site by total count of words in file site
            //Multiply by 100 to get percentage
            tmpWebsite = (simFileCount * 100 / totalFileCount);
            //Set both equal to zero to use when the loop starts again
            totalFileCount = 0;
            simFileCount = 0;
            //If tmpWebsite % is greater than bestWebsite %
            //Set bestWebsite equal to tmpWebsite
            if (tmpWebsite > bestWebsite) {
                bestWebsite = tmpWebsite;
                //Change recommendedWebsite to current urlString in file hashTable
                recommendedWebsite = ht.urlString;
            }
        }
    }

    //Retrieve the url from recommendedWebsite variable
    public String getURL() {
        return recommendedWebsite;
    }
}
