/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author keithmartin
 */
public class ProcessWebsite {

    private static final int URL_BLOCK = 100;
    BTree bTree;
    int j = 0;
    String numOneWebsite;
    String numTwoWebsite;
    String numThreeWebsite;
    boolean hasCacheBeenLoadedCalled;
    boolean putSitesIntoFileCalled;

    public void writeExternalLinksToFile(String file) throws FileNotFoundException, IOException {
        String s = "";

        FileWriter fWriter = new FileWriter(file, true);
        File f = new File(file);
        Scanner input = new Scanner(f);
        while (input.hasNext()) {
            int count = 0;
            String website = input.nextLine();
            Document doc = Jsoup.connect(website).get();
            org.jsoup.select.Elements links = doc.select("a[href]");
            for (Element link : links) {
                if (link.attr("abs:href").length() <= 100) {
                    if (count != 20 && link.attr("abs:href").length() >= website.length() && link.attr("abs:href").substring(0, website.length()).equals(website)) {
                        s += link.attr("abs:href") + "\n";
                        count++;
                    }
                }
            }
        }
        fWriter.write(s);
        this.stripDuplicatesFromFile(file);
    }

    public int numOfLinesInFile() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("web.txt");
        LineNumberReader lnr = new LineNumberReader(fr);

        int linenumber = 0;

        while (lnr.readLine() != null) {
            linenumber++;
        }

        return linenumber;
    }

    public void stripDuplicatesFromFile(String filename) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Set<String> lines = new HashSet<>(10000);
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }

    //read from seekPos and return the string
    public String stringHashCheck(long seekPos, RandomAccessFile file) throws IOException {
        String s = "";
        file.seek(seekPos);
        FileChannel inChannel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(URL_BLOCK);
        inChannel.read(buffer);
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            s += (char) buffer.get();
        }
        return s;
    }

    //uses linear probing to check if slot is there to insert url to cache
    public long findSlot(String website, RandomAccessFile file, long seekPos, long urlHashCode, long urlSeekPosition) throws IOException {
        String s = "";
        long url_seekPosition = urlSeekPosition;
        int i = 2;
        // to check if string is null or not
        s = this.stringHashCheck(seekPos, file);
        while (!s.trim().isEmpty() && !s.trim().equals(website)) {
            // if spot isnt empty and it isn't equal to website passed in then move to next pos
            long seekPosition = seekPos * i;
            url_seekPosition = seekPosition;
            s = this.stringHashCheck(url_seekPosition, file);
            i++;
        }
        //pos is good to use
        return url_seekPosition;
    }

    public void retrieveUrlToHashAndWrite() throws FileNotFoundException, IOException {
        Scanner webFile = new Scanner(new File("web.txt"));
        RandomAccessFile file = new RandomAccessFile("/Users/keithmartin/Desktop/BtreeCache/index.dat", "rw");
        while (webFile.hasNext()) {
            String website = webFile.nextLine();
            //create hashcode
            long urlHashCode = Math.abs(website.hashCode() % URL_BLOCK) + 1;
            //create seek pos
            long urlSeekPosition = urlHashCode * URL_BLOCK;
            //get final seek pos
            long finalUrlSeekPosition = this.findSlot(website, file, urlSeekPosition, urlHashCode, urlSeekPosition);
            // create url block for index cache file
            String finalIndex = Raf.createDataBlock(website + "", URL_BLOCK);
            // go to that spot
            file.seek(finalUrlSeekPosition);
            // write in url to index cache
            file.write(finalIndex.getBytes());
        }
    }

    public String checkUrlCache(RandomAccessFile raf, String website) throws IOException {
        String s = "";
        int urlHashCode = Math.abs(website.hashCode() % URL_BLOCK);
        long urlSeekPosition = urlHashCode * URL_BLOCK;
        raf.seek(urlSeekPosition);
        FileChannel inChannel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(URL_BLOCK);
        inChannel.read(buffer);
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            s += (char) buffer.get();
        }
        String url = s.replaceAll("\\p{Z}", "");
        return url;
    }

    public void checkIfSiteModified(String cacheWebsite, String raf) throws MalformedURLException, IOException, ParseException, FileNotFoundException, Exception {

        File f = new File("/Users/keithmartin/Desktop/BtreeCache/index.dat");
        File rafFile = new File(raf);
        URL url = new URL(cacheWebsite);
        URLConnection connection = url.openConnection();
        String date = connection.getHeaderField("Date");
        DateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        Date websiteDate = format.parse(date);
        Date fileDate = new Date(f.lastModified());
        if (fileDate.before(websiteDate)) {
            rafFile.delete();
            this.createNewestSiteBTree(cacheWebsite);
        }
    }

    public void createNewestSiteBTree(String cacheWebsite) throws FileNotFoundException, Exception {

        try {


            URL oracle = new URL(cacheWebsite);

            String fileName = Raf.stripUrlToFileName(cacheWebsite);


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

            // Intialize cache bTree
            BTree newCacheBTree = new BTree(fileName);
            // Set url string to website name to access the name of best website
            newCacheBTree.setUrlString(cacheWebsite);

            // For each word, get bTree from current position in ArrayList
            // and put word every word from current site into bTree starting with a count of 1
            for (String word : w) {
                newCacheBTree.put(word, cacheWebsite);
            }

            BTree.Node root = newCacheBTree.getRootNode();
            int treeHeight = newCacheBTree.getHeight();

            newCacheBTree.writeIntoFile(root, treeHeight);

        } catch (MalformedURLException ex) {
            throw ex;
        }

    }

    // Method used to process websites from "web.txt" fileList
    public void processFileWebsites() throws MalformedURLException, IOException, Exception {
        java.io.File file = new java.io.File("web.txt");

        try {

            if (this.numOfLinesInFile() == 20) {
                this.writeExternalLinksToFile("web.txt");
                Scanner input = new Scanner(file);
                while (input.hasNext()) {

                    String website = input.nextLine();

                    String fileName = Raf.stripUrlToFileName(website);
                    File f = new File(fileName);

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

                    // Intialize cache bTree
                    BTree newCacheBTree = new BTree(fileName);
                    // Set url string to website name to access the name of best website
                    newCacheBTree.setUrlString(website);

                    // For each word, get bTree from current position in ArrayList
                    // and put word every word from current site into bTree starting with a count of 1
                    for (String word : w) {
                        newCacheBTree.put(word, website);
                    }

                    BTree.Node root = newCacheBTree.getRootNode();
                    int treeHeight = newCacheBTree.getHeight();

                    newCacheBTree.writeIntoFile(root, treeHeight);
                    in.close();

                }
                this.retrieveUrlToHashAndWrite();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist\n");
        }

    }

    //Method used to process website user types in
    public void processUserWebsite(String website) throws IOException, FileNotFoundException, Exception {
        String fileName = Raf.stripUrlToFileName(website);
        RandomAccessFile file = new RandomAccessFile("/Users/keithmartin/Desktop/BtreeCache/index.dat", "rw");
        String cacheWebsite = this.checkUrlCache(file, website);
        if (cacheWebsite.equals(website)) {
            this.checkIfSiteModified(cacheWebsite, fileName);
        }

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

            // Intialize cache bTree
            bTree = new BTree(fileName);
            // Set url string to website name to access the name of best website
            bTree.setUrlString(website);

            // For each word, get bTree from current position in ArrayList
            // and put word every word from current site into bTree starting with a count of 1
            for (String word : w) {
                bTree.put(word, website);
            }
        } catch (MalformedURLException ex) {
            throw ex;
        }

    }

    /*
     * reads the index cache file returning a map of the btreefileName and fileName
     */
    public Map<String, String> readIndexCacheFile() throws IOException {
        String fileName = "";
        ArrayList<String> FileNameList = new ArrayList<>();
        Map<String, String> fileNameList = new HashMap();
        StringBuilder builder = new StringBuilder();
        RandomAccessFile raf = new RandomAccessFile("/Users/keithmartin/Desktop/BtreeCache/index.dat", "r");
        while (raf.getFilePointer() < raf.length()) {
            String s = "";
            FileChannel inChannel = raf.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(URL_BLOCK);
            inChannel.read(buffer);
            buffer.flip();
            for (int i = 0; i < buffer.limit(); i++) {
                s += (char) buffer.get();
            }

            fileName = Raf.stripUrlToFileName(s);
            File f = new File(fileName);
            if (f.exists()) {
                fileNameList.put(fileName, s.trim());
            }
        }
        return fileNameList;
    }


// Similarity metric
    public void sim() throws UnsupportedEncodingException, IOException {

        //Similar words between user website and fileList website
        int simFileCount = 0;
        //Total count of words in fileList website
        int totalFileCount = 0;
        //Temporary website to compare to current bestWebsite
        float tmpWebsite = 0;
        //Current bestWebsite
        float bestWebsite = 0;
        float secondBestWebsite = 0;
        float thirdBestWebsite = 0;

        if (bTree == null) {
            return;
        }

        BTree.Node node = bTree.getRootNode();
        int height = bTree.getHeight();
        int count = 0;
        ArrayList<String> userWords = bTree.getKeys(node, height);
        BTree bt = new BTree();
        Map<String, String> fileNames = this.readIndexCacheFile();
        for (Map.Entry<String, String> fileList : fileNames.entrySet()) {
            ArrayList<String> fileWords = bt.readBTreeCacheFile(fileList.getKey());
            for (String word : fileWords) {
                String strippedWord = word.replaceAll("[^A-Za-z]", "");
                String stringFreq = word.replaceAll("[^0-9]", "");
                if (!stringFreq.isEmpty()) {
                    int freq = Integer.parseInt(stringFreq);
                    totalFileCount += freq;
                    if (bTree.get(strippedWord) != 0) {
                        simFileCount += freq;
                    }
                }

            }
            //Divide count of similar words in fileList site by total count of words in fileList site
            //Multiply by 100 to get percentage
            tmpWebsite = (simFileCount * 100 / totalFileCount);
            //Set both equal to zero to use when the loop starts again
            totalFileCount = 0;
            simFileCount = 0;
            //If tmpWebsite % is greater than bestWebsite %
            //Set bestWebsite equal to tmpWebsite
            if (tmpWebsite > bestWebsite) {
                bestWebsite = tmpWebsite;
                numOneWebsite = fileList.getValue();
            }
            if (tmpWebsite < bestWebsite && tmpWebsite > secondBestWebsite) {
                secondBestWebsite = tmpWebsite;
                numTwoWebsite = fileList.getValue();
            }
            if (tmpWebsite < bestWebsite && tmpWebsite < secondBestWebsite && tmpWebsite > thirdBestWebsite) {
                thirdBestWebsite = tmpWebsite;
                numThreeWebsite = fileList.getValue();
            }
        }
    }

    public String getNumOneURL() {
        return numOneWebsite;
    }
    
    public String getNumTwoURL() {
        return numTwoWebsite;
    }
    
    public String getNumThreeURL() {
        return numThreeWebsite;
    }
}
