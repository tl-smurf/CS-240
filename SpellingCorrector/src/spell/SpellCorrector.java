package spell;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.*;


public class SpellCorrector implements ISpellCorrector{
    //@Override
    //TODO: Figure out where to put the dictionary and set... Having it here you can't override
    Trie myDictionary = new Trie();
    public void useDictionary(String dictionaryFileName) throws IOException {
        //Creates Dictionary: this is where the code on line 10 used to be
        //Makes File stream to read in dictionary
        FileInputStream fis = new FileInputStream(dictionaryFileName);
        Scanner scanner = new Scanner(fis);

        //Reads in file line by line
        //DONE: Make this handle multiple words on one line
        while (scanner.hasNextLine()) {
            String[] currLine = scanner.nextLine().split("\\s+");
            //If key is in map, increment its value
            for (String currWord : currLine) {
                    myDictionary.add(currWord);
            }

        }
        scanner.close();
        //outputs content of dictionary
        //System.out.println("Dictionary:" + myDictionary.toString());
    }

    //Creates ED of 1 based on input word
    public Set<String> makeWordSet(String wordIn) {
        Set<String> wordSet = new HashSet<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        //Makes alter 1 letter error algorithm
        for (int i = 0; i < wordIn.length(); i++) {
            char[] singleLetterAlteration = wordIn.toCharArray();
            for (char replacement : alphabet) {
                singleLetterAlteration[i] = replacement;
                wordSet.add(new String(singleLetterAlteration));
            }
        }
        //System.out.println("Set contents after checking single letter mistake:");
        //System.out.println(wordSet.toString());

        //Makes delete 1 letter error algorithm
        char[] inputWordChars = wordIn.toCharArray();
        for (int i = 0; i < wordIn.length(); i++) {
            char[] beginning = Arrays.copyOfRange(inputWordChars, 0, i);
            char[] end = Arrays.copyOfRange(inputWordChars, i + 1, wordIn.length());
            String newBeginning = new String(beginning);
            String newEnd = new String(end);
            String deletionTestWord = newBeginning + newEnd;
            wordSet.add(deletionTestWord);
        }
        //Makes insert 1 letter error algorithm
        for (int i = 0; i < wordIn.length() + 1; i++) {
            char[] singleLetterAddition = wordIn.toCharArray();
            for (char addition : alphabet) {
                char[] beginning = Arrays.copyOfRange(singleLetterAddition, 0, i);
                char[] end = Arrays.copyOfRange(singleLetterAddition, i, wordIn.length());
                String newBeginning = new String(beginning);
                String newEnd = new String(end);
                String addditionTestWord = (newBeginning + addition + newEnd);
                wordSet.add(addditionTestWord);
            }
        }
        //Makes transpose 1 letter error algorithm
        for (int i = 0; i < wordIn.length() - 1; i++) {
            char firstLetter = inputWordChars[i];
            char secondLetter = inputWordChars[i + 1];
            char[] transposeArray = wordIn.toCharArray();
            transposeArray[i] = secondLetter;
            transposeArray[i + 1] = firstLetter;
            String newTransposeString = new String(transposeArray);
            wordSet.add(newTransposeString);
        }
        return wordSet;
    }

    //checks to see if word is in dictionary
    public boolean checkDictionary(String wordIn) {
        if (myDictionary.find(wordIn) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public String chooseWinner(String wordToCheck, String currentWinner) {
        if (currentWinner == "abcdefghijklmnopqrstuvwxyz") {
            return wordToCheck;
        }
        else if (myDictionary.find(wordToCheck).getValue() > myDictionary.find(currentWinner).getValue()) {
            return wordToCheck;
        }
        else if (myDictionary.find(wordToCheck).getValue() < myDictionary.find(currentWinner).getValue()) {
            return currentWinner;
        }
        else {
            if (currentWinner.compareTo(wordToCheck) < 0) {
                return currentWinner;
            }
            else {
                return wordToCheck;
            }
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        //Make the alphabet into an iterable list of char
        //Lowercases the input word
        String winningWord = "abcdefghijklmnopqrstuvwxyz";
        inputWord = inputWord.toLowerCase(Locale.ROOT);
        if (checkDictionary(inputWord)) {
            return inputWord;
        }
        else {
            Set<String> ED1 = makeWordSet(inputWord);
            for (String currWord : ED1) {
                if (checkDictionary(currWord)) {
                    winningWord = chooseWinner(currWord, winningWord);
                }
            }
            if (winningWord == "abcdefghijklmnopqrstuvwxyz") {
                //Make ED2
                Set<String> ED2 = new HashSet<String>();
                for (String currWord : ED1) {
                    ED2.addAll(makeWordSet(currWord));
                }
                for (String currWord : ED2) {
                    if (checkDictionary(currWord)) {
                        winningWord = chooseWinner(currWord, winningWord);
                    }
                }
                if (winningWord == "abcdefghijklmnopqrstuvwxyz") {
                    return null;
                }
                else {
                    return winningWord;
                }
            }
            else {
                return winningWord;
            }
        }
    }
        //Check word set list against dictionary
}
