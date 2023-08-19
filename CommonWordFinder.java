import java.io.*;
import java.util.Iterator;

/**
 * A program that finds the n most common words in a document, using an
 * implemented BSTMap, AVLTreeMap, or MyHashMap.
 *
 * @author Linda Tang lt2899
 * @version 1.0.0 December 14, 2022
 */

public class CommonWordFinder {
    /**
     * Reads through the given text file and parses the words with a Buffered Reader.
     * Subsequently, places each word into the map, tracking the frequency in which the word
     * is inserted.
     * SOURCE: https://www.geeksforgeeks.org/java-io-bufferedreader-class-java/
     * @param map stores the words in the text file
     * @param txtFile given text file
     */
    public static void parseFile (MyMap<String,Integer> map, File txtFile) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(txtFile));
            StringBuilder str = new StringBuilder();
            while (reader.ready()) {
                int asciiCh = reader.read();

                //Checks if reader is at end of word
                if ((asciiCh == 32 || asciiCh == 10) && !str.isEmpty()) {
                    if (map.get(str.toString()) == null) {
                        map.put(str.toString(), 1);
                    } else {
                        map.put(str.toString(), map.get(str.toString()) + 1);
                    }
                    str.setLength(0); //reset the StringBuilder
                }
                //Appends any lowercase letter or single quotes
                else if (asciiCh == 39 || (asciiCh >= 97 && asciiCh <= 122)) {
                    str.append((char) asciiCh);
                }
                //Changes uppercase letter to lowercase and appends to string
                else if (asciiCh >= 65 && asciiCh <= 90) {
                    asciiCh = asciiCh + 32;
                    str.append((char) asciiCh);
                }
                //Appends hyphen if it is not the first character of the word
                else if (asciiCh == 45 && !str.isEmpty()) {
                    str.append((char) asciiCh);
                }
                //Skips any other character
                else {
                    continue;
                }
            }
            //Adds the last word into the map
            if(!str.isEmpty()){
                if (map.get(str.toString()) == null) {
                    map.put(str.toString(), 1);
                } else {
                    map.put(str.toString(), map.get(str.toString()) + 1);
                }
                str.setLength(0);
            }

        } catch (IOException e) {
            System.out.println("Error: An I/O error occurred reading '" + txtFile.getName() + "'.");
            System.exit(1);
        }
    }

    /**
     * Converts the given map of words into a sorted array, which orders the
     * words of the text file alphabetically by frequency.
     * @param map stores the words in the text file
     * @return the sorted array
     */
    public static Entry<String,Integer>[] sortMap(MyMap<String,Integer> map){
        Iterator<Entry<String,Integer>> iter = map.iterator();

        //Copy map entries to an array
        Entry<String,Integer>[] entryArr = new Entry[map.size()];
        int index = 0;
        while(iter.hasNext()){
            entryArr[index] = iter.next();
            index++;
        }

        //First sort array alphabetically by key
        mergesortKey(entryArr);

        //Then sort by value
        mergesortValue(entryArr);

        return entryArr;
    }

    /**
     * Displays the output of the sorted words.
     * SOURCE: https://www.baeldung.com/java-printstream-printf
     * @param entryArr the sorted array of words from the text file
     * @param limit the number of words that should be displayed
     */
    public static void displayOutput(Entry<String,Integer>[] entryArr, int limit) {
        System.out.println("Total unique words: " + entryArr.length);
        limit = Math.min(entryArr.length, limit); //Changes limit to the smaller value
        int rightAlign = String.valueOf(limit).length();
        int leftAlign = 0;
        //Find longest word count
        for (int i = 0; i<limit; i++){
            if(entryArr[i].key.length() > leftAlign) {
                leftAlign = entryArr[i].key.length();
            }
        }
        //Print out formatting
        for(int i = 1; i <= limit; i++){
            System.out.printf("%"+rightAlign+"s. %-"+ leftAlign +"s %d%n", i, entryArr[i-1].key, entryArr[i-1].value);
        }
    }

    /**
     * Aids in sorting the array of entries alphabetically.
     */
    private static void mergesortHelperKey(Entry<String,Integer>[] array, Entry<String,Integer>[] scratch, int low, int
            high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergesortHelperKey(array, scratch, low, mid);
            mergesortHelperKey(array, scratch, mid + 1, high);
            int i = low, j = mid + 1;
            for (int k = low; k <= high; k++) {
                if (i <= mid && (j > high || array[i].key.compareTo(array[j].key) <= 0)) {
                    scratch[k] = array[i++];
                } else {
                    scratch[k] = array[j++];
                }
            }
            for (int k = low; k <= high; k++) {
                array[k] = scratch[k];
            }
        }
    }

    /**
     * Sorts the array of entries alphabetically.
     * @param array holds the entries in the map
     */
    public static void mergesortKey(Entry<String,Integer>[] array) {
        Entry<String,Integer>[] scratch = new Entry[array.length];
        mergesortHelperKey(array, scratch, 0, array.length - 1);
    }

    /**
     * Aids in sorting the array of entries by frequency.
     */
    private static void mergesortHelperValue(Entry<String,Integer>[] array, Entry<String,Integer>[] scratch, int low, int
            high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergesortHelperValue(array, scratch, low, mid);
            mergesortHelperValue(array, scratch, mid + 1, high);
            int i = low, j = mid + 1;
            for (int k = low; k <= high; k++) {
                if (i <= mid && (j > high || array[i].value >= array[j].value)) {
                    scratch[k] = array[i++];
                } else {
                    scratch[k] = array[j++];
                }
            }
            for (int k = low; k <= high; k++) {
                array[k] = scratch[k];
            }
        }
    }

    /**
     * Sorts the array of entries by frequency.
     * @param array holds the entries in the map
     */
    public static void mergesortValue(Entry<String,Integer>[] array) {
        Entry<String,Integer>[] scratch = new Entry[array.length];
        mergesortHelperValue(array, scratch, 0, array.length - 1);
    }

    /**
     * Main method that handles command line formatting.
     */
    public static void main(String[] args) {
        //Checks if there are either two or three command line arguments
        if (args.length != 2 && args.length != 3){
            System.out.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }
        //Checks if input file exists
        File txtFile = new File(args[0]);
        if (!txtFile.exists()){
            System.out.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        }
        //Checks if given data structure is valid
        if (!(args[1].equals("bst") || args[1].equals("avl") || args[1].equals("hash"))){
            System.out.println("Error: Invalid data structure '" + args[1] + "' received.");
            System.exit(1);
        }
        //Checks if optional given limit is valid
        int limit = 10;
        if (args.length == 3) {
            try{
                if (Integer.parseInt(args[2]) <= 0) {
                    System.out.println("Error: Invalid limit '" + args[2] + "' received.");
                    System.exit(1);
                } else {
                    limit = Integer.parseInt(args[2]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid limit '" + args[2] + "' received.");
                System.exit(1);
            }
        }

        //Instantiate the map
        MyMap<String,Integer> map;
        if(args[1].equals("bst")){
            map = new BSTMap<>();
        } else if (args[1].equals("avl")){
            map = new AVLTreeMap<>();
        } else {
            map = new MyHashMap<>();
        }
        //Parse the input file
        parseFile(map,txtFile);

        //Sort map alphabetically and by word frequency
        Entry<String,Integer>[] entryArr = sortMap(map);

        //Display the output
        displayOutput(entryArr, limit);
    }
}
