package search;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static Scanner scFile;
    private static List<String> bookArrayList = new ArrayList<>();

    public static void main(String[] args) {
        //String[] bookArray = getInput();

        Map<String, Set<Integer>> bookArrayInvertedIndex = new HashMap<>();
        bookArrayInvertedIndex = getInputFromFileStage5(new File(args[1]));
/*
        int l = bookArrayList.size();

        String[] bookArray = new String[l];

        for(int i=0; i<l; i++){
            bookArray[i] = bookArrayList.get(i);
        }
*/
        String choice ="";

        while(!choice.equals("0")){
            showMenu();
            choice = sc.nextLine().trim();
            switch(choice){
                case "1" :
                    findStuff(bookArrayInvertedIndex);
                    break;
                case "2" :
                    printAll();
                    break;
                case "0" :
                    System.out.println("Bye!");
                    break;
                default :
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
        //generateOutput(bookArray);//STAGE 2
        /* STAGE 1
        String line = sc.nextLine();
        String word = sc.nextLine();

        String[] lineArr = line.split("\\s+");
        int l = lineArr.length;
        int wordNum = -1;
        for(int i=0; i<l; i++){
            if(lineArr[i].equals(word)){
               wordNum = i+1;
               break;
            }
        }
        System.out.println( wordNum>-1 ? wordNum : "Not found");*/
    }

    private static String[] getInput(){
        /*
        "ISBN": "0195153448",
        "Book-Title": "Classical Mythology",
        "Book-Author": "Mark P. O. Morford",
        "Year-Of-Publication": "2002",
        "Publisher": "Oxford University Press",
        */
        System.out.println("Enter the number of people:");
        int n = sc.nextInt();
        sc.nextLine();
        String[] books = new String[n];
        System.out.println("Enter all people:");
        int count=0;
        for(int i=0; i<n; i++){
            books[i] = sc.nextLine().trim();
        }
        return books;
    }
    private static void generateOutput(String[] dataArray){
        System.out.println("Enter the number of search queries:");
        int l = dataArray.length;
        int n = sc.nextInt();
        sc.nextLine();
        for(int i=0; i<n; i++){
            System.out.println("Enter data to search people:");
            String currentSearch = sc.nextLine().trim();
            int foundCount = 0;
            for(int j=0; j<l; j++){
                if(dataArray[j].toLowerCase(Locale.ENGLISH).contains(currentSearch.toLowerCase(Locale.ENGLISH))){
                    foundCount++;
                    if(foundCount == 1){
                        System.out.println("Found people:");
                    }
                    System.out.println(dataArray[j]);
                }
            }
            if(foundCount==0){
                System.out.println("No matching people found.");
            }
        }
    }

    private static void showMenu(){
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }

    private static void findStuff(String[] dataArray){
        System.out.println("Enter a name or email to search all suitable people.");
        int l = dataArray.length;
        int n = 1;
        //sc.nextLine();
        for(int i=0; i<n; i++){
            //System.out.println("Enter data to search people:");
            String currentSearch = sc.nextLine().trim();
            int foundCount = 0;
            for(int j=0; j<l; j++){
                if(dataArray[j].toLowerCase(Locale.ENGLISH).contains(currentSearch.toLowerCase(Locale.ENGLISH))){
                    foundCount++;
                    /*if(foundCount == 1){
                       System.out.println("Found people:");
                    }*/
                    System.out.println(dataArray[j]);
                }
            }
            if(foundCount==0){
                System.out.println("No matching people found.");
            }
        }
    }
    private static void findStuff(Map<String, Set<Integer>> dataMap){
        System.out.println("Enter a name or email to search all suitable people.");
        //sc.nextLine();
            String currentSearch = sc.nextLine().trim();
            Set<Integer> res = dataMap.get(currentSearch);
            int foundCount = null != res ? res.size() : 0;
            if(foundCount==0){
                System.out.println("No matching people found.");
            }else{
                System.out.println(foundCount+" persons found:");
                for(Integer i : res){
                    System.out.println(bookArrayList.get(i));
                }
            }
    }
    private static void printAll(String[] dataArray){
        System.out.println("=== List of people ===");
        int l = dataArray.length;
        for(int j=0; j<l; j++){
            System.out.println(dataArray[j]);
        }
    }
    private static void printAll(){
        System.out.println("=== List of people ===");
        int l = bookArrayList.size();
        for(int j=0; j<l; j++){
            System.out.println(bookArrayList.get(j));
        }
    }
/*
    private static String[] getCommandLineArgumentsFourthStage(String [] argsInput){
        String[] results = new String[2];
        results = argsInput;
         return results;
    }
*/
private static List<String> getInputFromFile(File inputFile) {
    scanFile(inputFile);
    List<String> inputArrayList = new ArrayList<>();
    while(scFile.hasNextLine()){
        inputArrayList.add(scFile.nextLine().trim());
    }
    scFile.close();
    return inputArrayList;
}

    private static Map<String, Set<Integer>> getInputFromFileStage5(File inputFile) {
        scanFile(inputFile);
        //List<String> inputArrayList = new ArrayList<>();
        Map<String, Set<Integer>> invertedIndex = new HashMap<>();
        while(scFile.hasNextLine()){
            bookArrayList.add(scFile.nextLine().trim());
        }
        scFile.close();
        int l = bookArrayList.size();
        Set<String> wordSet = new HashSet<>();

        for(int i=0; i<l; i++){
            for(String word : bookArrayList.get(i).split("\\s+")){

                if(!wordSet.contains(word)){
                    wordSet.add(word);
                    Set<Integer> currentLine = new HashSet<>();
                    currentLine.add(i);
                    invertedIndex.put(word, currentLine);
                }else{
                    Set<Integer> currentLine = invertedIndex.get(word);
                    currentLine.add(i);
                    invertedIndex.put(word, currentLine);
                }

            }
        }
        return invertedIndex;
    }

    private static void scanFile(File f) {
        try {
            scFile = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
