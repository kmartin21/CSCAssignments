/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;



/**
 *
 * @author keithmartin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, FileNotFoundException, Exception {
        
        RecommendationWindow window = new RecommendationWindow();
        window.run();
        
    }
}
