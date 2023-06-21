import java.io.File;    // import libry files 
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections; 
import java.util.Scanner;
import java.util.Stack;

public class Dataminiprojct {

    public static void main(String[] args) {
        // keymaping hold list using arrry
        ArrayList<Keymaping> listOfKeys = new ArrayList<Keymaping>();

        long start = System.currentTimeMillis();

        // open keyword file ,file location scaning
        Scanner keyWordFile = openTextFile("C:\\Users\\Dumith Sachinthaka\\OneDrive\\Desktop\\Miniproject\\src\\Keywords.txt");

        // reading keyword file
        readKeys(keyWordFile, listOfKeys);

       
        int[] occurencesOfLetters = new int[26];  // from this array will be used to count frequency of letters.

        
        populateOccurencesOfLetters(listOfKeys, occurencesOfLetters);   // this method counts the frequency of letters 

       
        
        setValueOfAllKeys(listOfKeys, occurencesOfLetters);      // sets the value of all keywords based off the frequency of each word's first and last letter.

        
        Collections.sort(listOfKeys);   // This method sorts the list of keys by value in descending order.

        
        ArrayList<Character> uniqueLetters = new ArrayList<Character>();
        populateUniqueLetters(uniqueLetters, occurencesOfLetters);

       
        int[][] g = new int[uniqueLetters.size()][3];
        populateGArray(uniqueLetters, g);

        // create  hashtable and it  will have keywords mapped to it using a hash function while the cichelli's algorithm is running.
       
        Keymaping[] hashTable = new Keymaping[listOfKeys.size()];

            
            
        
        int maxValue = listOfKeys.size() / 2;   // max value is utilized in cichelli's algorithm
        int modValue = listOfKeys.size();       // mod value is utilized in the hash function

        
        Stack<Keymaping> keyWordStack = new Stack<Keymaping>(); //creating a stack and puting  it with the keywords from my list of key
        populateStack(listOfKeys, keyWordStack);

        
        cichelli(keyWordStack, hashTable, g, modValue, maxValue);   // Cichelli's algorithm is called to map key words to the hashTable

        
        int[] keyWordCounter = new int[hashTable.length];   //array is used to count the frequency each keyword
       
        initializeCounter(keyWordCounter);

        // opens text file then returns a scanner of the file.
        Scanner textFile = openTextFile("C:\\Users\\Dumith Sachinthaka\\OneDrive\\Desktop\\Miniproject\\src\\Txt.txt");

        // reding the text file 
        readOtherFile(textFile, g, hashTable, keyWordCounter, modValue, start);

    }

    
    
    // method opens text file then returns a scanner of the file.
    public static Scanner openTextFile(String fileName) {
        Scanner data;

        try {
            data = new Scanner(new File(fileName));
            return data;
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " did not read correctly");
        }
        data = null;
        return data;
    }

    // method reads the key word file and populates a list of keys.
    public static void readKeys(Scanner data, ArrayList<Keymaping> listOfKeys) {
        String currentToken;
        while (data.hasNext()) {
            currentToken = data.next();
            currentToken = currentToken.toLowerCase();
            Keymaping currentKey = new Keymaping(currentToken);
            listOfKeys.add(currentKey);
        }
    }

    // this method counts the frequency of letters and stores in occurenceOfLetters
    // array.
    public static void populateOccurencesOfLetters(ArrayList<Keymaping> listOfKeys, int[] occurencesOfLetters) {
        // using an array of 26 to keep track of occurences of letters
        // initialize all array values to zero.
        for (int i = 0; i < 26; i += 1) {
            occurencesOfLetters[i] = 0;
        }
        // count the frequency that each letter appears as a first or last letter
        for (Keymaping e : listOfKeys) {
            for (int i = 97; i < 123; i += 1) {
                if (e.getFirstLetter() == (char) i) {
                    occurencesOfLetters[i - 97] += 1;
                }
                if (e.getLastLetter() == (char) i) {
                    occurencesOfLetters[i - 97] += 1;
                }
            } 
        } 
    }

    //this method sets the value of all keywords based off the frequency of each word
     
    public static void setValueOfAllKeys(ArrayList<Keymaping> listOfKeys, int[] occurencesOfLetters) {
        // set values of keys
        for (Keymaping e : listOfKeys) {
            int firstLetterValue = occurencesOfLetters[((int) e.getFirstLetter() - 97)];
            e.setFirstLetterValue(firstLetterValue);
            int lastLetterValue = occurencesOfLetters[((int) e.getLastLetter() - 97)];
            e.setLastLetterValue(lastLetterValue);
            int totalValue = firstLetterValue + lastLetterValue;
            e.setTotalValue(totalValue);
        }
    }

    // the uniqueLetters arrayList is populated with letters from the
    // occurencesOfLetters array.
    public static void populateUniqueLetters(ArrayList<Character> uniqueLetters, int[] occurencesOfLetters) {
        for (int i = 0; i < occurencesOfLetters.length; i += 1) {
            if (occurencesOfLetters[i] != 0) {
                uniqueLetters.add((char) (i + 97));
            }
        }
    }

    // this method populates 2 dimension g array
    public static void populateGArray(ArrayList<Character> uniqueLetters, int[][] g) {
        for (int i = 0; i < uniqueLetters.size(); i += 1) {
            // collumn 0 holds g values. all initialized to zero
            g[i][0] = 0;
            // collumn 1 holds ascci values of letters.
            g[i][1] = (int) uniqueLetters.get(i);
            // collumn 2 holds 0 if g-value is not set and 1 if g-value is set. all
            // initialized to zero.
            g[i][2] = 0;
        }
    }

    // this method populates the stack with keys from the listOfKeys.
    public static void populateStack(ArrayList<Keymaping> listOfKeys, Stack<Keymaping> keyWordStack) {
        for (int i = (listOfKeys.size() - 1); i >= 0; i -= 1) {
            keyWordStack.push(listOfKeys.get(i));
        }
    }

    // Cichelli's algorithm is called to map key words to the hashTable and is a
    // perfect hashing algorithm.
    public static boolean cichelli(Stack<Keymaping> keyWordStack, Keymaping[] hashTable, int[][] g, int modValue, int maxValue) {

        while (keyWordStack.isEmpty() != true) {

            Keymaping word = keyWordStack.pop();

            // finding first and last g values by comparing 2d gArray to first and last
            // letters of word.
            // setPositionInG refers to the row of the array where the letters was found.
            for (int i = 0; i < g.length; i += 1) {
                if (g[i][1] == ((int) word.getFirstLetter())) {
                    word.setPositionInGFirst(i);
                }
                if (g[i][1] == ((int) word.getLastLetter())) {
                    word.setpositionInGLast(i);
                }
            }

            // if values are already assigned for both letters try to place key in table
            if (g[word.getPositionInGFirst()][2] == 1 && g[word.getPositionInGLast()][2] == 1) {
                // check if hash value for word is valid.
                if (hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                        modValue)] == null) {
                    // assign hash value to word
                    hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                            modValue)] = word;
                    // recursively call cichelli method
                    if (cichelli(keyWordStack, hashTable, g, modValue, maxValue) == true) {
                        return true;
                    }
                    // detach the hash value for word
                    else {
                        hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                                modValue)] = null;
                        // set values of g array column 2 back to zero.
                    }
                }
                keyWordStack.push(word);
                return false;
            }

            // neither letter assigned g-value AND first != last letter
            else if ((g[word.getPositionInGFirst()][2] == 0) && (g[word.getPositionInGLast()][2] == 0)
                    && (word.getFirstLetter() != word.getLastLetter())) {
                
                 //increment first g value up to maxValue times
                 
                for (int m = 0; m < (maxValue + 1); m += 1) {
                    // not incrementing g-value in first iteration
                    if (m > 0) {
                        // increment g-value
                        g[word.getPositionInGFirst()][0] += 1;
                    }
                    // check if hash value is valid if not we want to keep incrementing g.
                    if (hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                            modValue)] == null) {
                        // assign hash value to word
                        hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                                modValue)] = word;
                        // set values of g array column 2 to 1.
                        g[word.getPositionInGFirst()][2] = 1;
                        g[word.getPositionInGLast()][2] = 1;
                        // recursively call cichelli method
                        if (cichelli(keyWordStack, hashTable, g, modValue, maxValue) == true) {
                            return true;
                        }
                        // detach the hash value for word
                        else {
                            hashTable[hashFunction(word, g[word.getPositionInGFirst()][0],
                                    g[word.getPositionInGLast()][0], modValue)] = null;
                            // set values of g array column 2 back to zero.
                            g[word.getPositionInGFirst()][2] = 0;
                            g[word.getPositionInGLast()][2] = 0;
                        }

                    }
                } 
               
                // reset g value
                g[word.getPositionInGFirst()][0] = 0;
                keyWordStack.push(word);
                return false;
            } 

            // else refers to case where only one letter assigned g-value OR first = last
            // letter
            else {
                // determine which letter(first or last) is assigned a g value.
                // if the following condition is true than the first letter has a g value and
                // last letter will not
                if (g[word.getPositionInGFirst()][2] == 0) {
                    word.setUnassignedLetterPosition(word.getPositionInGFirst());

                }
                // reaches (else if) if the first letter does not have a gvalue. still possible
                // neither has a g value.
                else if (g[word.getPositionInGLast()][2] == 0) {
                    word.setUnassignedLetterPosition(word.getPositionInGLast());

                }
                
                else {
                    // there is no unassigned letter first = last.
                    // set to arbitrary value of -1.
                    word.setUnassignedLetterPosition(-1);
                }

               
                for (int m = 0; m < (maxValue + 1); m += 1) {
                    // not incrementing g-value in first iteration
                    if (m > 0) {
                        // increment g-value
                        g[word.getUnassignedLetterPosition()][0] += 1;
                    }
                    // check if hash value is valid if not we want to keep incrementing g.
                    if (hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                            modValue)] == null) {
                        // assign hash value to word
                        hashTable[hashFunction(word, g[word.getPositionInGFirst()][0], g[word.getPositionInGLast()][0],
                                modValue)] = word;
                        // only need to change the unassigned letter's set field to 1.
                        g[word.getUnassignedLetterPosition()][2] = 1;
                        // recursively call cichelli's method
                        if (cichelli(keyWordStack, hashTable, g, modValue, maxValue) == true) {
                            return true;
                        }
                        // detach the hash value for word
                        else {
                            hashTable[hashFunction(word, g[word.getPositionInGFirst()][0],
                                    g[word.getPositionInGLast()][0], modValue)] = null;
                            // set unassigned letters set field back to zero.
                            g[word.getUnassignedLetterPosition()][2] = 0;
                        }
                    }
                } 
                
                
                g[word.getUnassignedLetterPosition()][0] = 0;
                keyWordStack.push(word);
                return false;
            } 

        } 
          // empty stack means we have a solution
        return true;
    }

    // overloaded hash function takes Key data type as an argument
    public static int hashFunction(Keymaping keyWord, int gFirst, int gLast, int modValue) {
        int hashValue = (keyWord.getKeyWord().length() + gFirst + gLast) % modValue;
        return hashValue;
    }

    // overloaded hash function takes String data type as an argument.
    public static int hashFunction(String word, int gFirst, int gLast, int modValue) {
        int hashValue = (word.length() + gFirst + gLast) % modValue;
        return hashValue;
    }

    // this method initializes the keyWordCounter array with zeros.
    public static void initializeCounter(int[] keyWordCounter) {
        for (int i = 0; i < keyWordCounter.length; i += 1) {
            keyWordCounter[i] = 0;
        }
    }

    public static void readOtherFile(Scanner data, int g[][], Keymaping[] hashTable, int[] keyWordCounter, int modValue,
            long start) {
        int lineCounter = 0, wordCounter = 0;

        String x;
        String[] y;
        while (data.hasNextLine()) {
            lineCounter += 1;
            x = data.nextLine();

            
            if (x.length() == 0) {
                x = data.nextLine();
            }

            x = x.toLowerCase();

           // y = x.split(" ");
            y = x.split("[\\s,.)]+");
            wordCounter += y.length;

            // method compares a token to a key word to see if they are identical.
            checkForKeyWord(y, g, hashTable, keyWordCounter, modValue);
        }
        // method prints statistical results
        printResults(lineCounter, wordCounter, hashTable, keyWordCounter, start);
    }

    // method compares a token to a key word to see if they are identical.
    public static void checkForKeyWord(String[] words, int[][] g, Keymaping[] hashTable, int[] keyWordCounter, int modValue) {
        for (String word : words) {
            char firstLetter = word.charAt(0), lastLetter = word.charAt(word.length() - 1);
            boolean firstLetterBool = false, lastLetterBool = false;
            int firstGvalue = 0, lastGvalue = 0;

            for (int i = 0; i < g.length; i += 1) {
                if (firstLetter == (char) g[i][1]) {
                    firstLetterBool = true;
                    firstGvalue = g[i][0];
                }
                if (lastLetter == (char) g[i][1]) {
                    lastLetterBool = true;
                    lastGvalue = g[i][0];
                }
            }
            if (firstLetterBool == true && lastLetterBool == true) {
                int index = hashFunction(word, firstGvalue, lastGvalue, modValue);
                if (word.equals(hashTable[index].getKeyWord())) {
                    keyWordCounter[index] += 1;
                }
            }
        }
    }

    //  prints  results
    public static void printResults(int lineCounter, int wordCounter, Keymaping[] hashTable, int[] keyWordCounter,
          
         long start) {
        long stop = System.currentTimeMillis();
        System.out.println(" - Result of Statistics - ");
System.out.println("Total Words Read From Given Text File :" + wordCounter);
 
        System.out.println("Total Lines Read From Given Text File :" + lineCounter);
         System.out.println("Break down by key word From Given Text File ");
        for (int i = 0; i < hashTable.length; i += 1) {
            System.out.println("    " + hashTable[i].getKeyWord() + ": " + keyWordCounter[i]);
        }
        int totalKeys = 0;
        for (int e : keyWordCounter) {
            totalKeys += e;
        }
        System.out.println("Total Key Words From Given Text File : " + totalKeys);

        System.out.println("Total Execution time : " + (stop - start) + " ms");
    }

}