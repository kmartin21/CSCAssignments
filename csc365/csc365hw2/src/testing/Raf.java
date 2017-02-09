/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 *
 * @author keithmartin
 */
public class Raf {
    
    public static String createDataBlock(String data, int blockSize) {

        String dataBlock = data;
        int originalLength = dataBlock.length();
        int blockLengthNeededDifference = blockSize - originalLength;
        if (blockLengthNeededDifference > 0) {
            for (int i = 0; i < blockLengthNeededDifference; i++) {
                dataBlock += " ";
            }
        }
        return dataBlock;
    }

    public static String stripUrlToFileName(String url) {
        if (url.substring(0, 7).equals("http://")) {
            url = url.replaceAll("http://", "");
        } else if (url.substring(0, 8).equals("https://")) {
            url = url.replaceAll("https://", "");
        }
        String fileName = url.replaceAll("[^a-zA-Z0-9]", "");
        return "/Users/keithmartin/Desktop/BtreeCache/" + fileName + ".dat";
    }
    
    public static String ensureFixedNode(String data, long bytesLeft) {
        String dataBlock = data;
        for(int i = 0; i < bytesLeft; i++) {
            dataBlock += " ";
        }
        return dataBlock;
    }
    
    public static RandomAccessFile createRaf() throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile("/Users/keithmartin/Desktop/BtreeCache/index.dat","rw");
        return raf;
    }
    
}
