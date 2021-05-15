import java.util.Random;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Assignment2 {
    public static ArrayList<Character> charArray = new ArrayList<>();
    public static ArrayList<Double> charfreq = new ArrayList<>();
    public static StringBuilder decodedText = new StringBuilder();
    private static double codelength = (double) 0;
    public static List <String> DataBlocks4 = new ArrayList<String>();
    public static List <String> DataBlocks8 = new ArrayList<String>();
    public static List <String> DataBlocks16 = new ArrayList<String>();
    public static List <String> ErrorDataBlocks8 = new ArrayList<String>();
    public static List <String> DataBlocks11 = new ArrayList<String>();
    public static List <String> ErrorDataBlocks16 = new ArrayList<String>();
    public static String Encodedbitstring74 = "";
    public static String Encodedbitstring1511 = "";
    public static String Decodedbitstring8 = "";
    public static String Checkingbitstring4 = "";
    public static String Decodedbitstring16 = "";
    private static Random rand = new Random();
    private double prob;
    private char symbol;
    private int frequency;

    Assignment2 left;
    Assignment2 right;
    private String nameofnode;
    public Assignment2(double prob, char symbol, int frequency) {
        this.prob = prob;
        this.symbol = symbol;
        this.frequency = frequency;
    }

    public Assignment2() {

    }

    public String getNameofnode() {
        return nameofnode;
    }

    public void setNameofnode(String nameofnode) {
        this.nameofnode = nameofnode;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    static class Comp implements Comparator<Assignment2> {
        @Override
        public int compare(Assignment2 o1, Assignment2 o2) {
            if(o2.getProb()-o1.getProb()>0){
                return -1;
            }
           return 1;
        }
    }


    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public static void getProbability(String strr) {
        ArrayList<Assignment2> listofprobs = new ArrayList<>();
        double count[] = new double[128]; //ASCII size

        for (int i = 0; i < strr.length(); i++) //to counting how much each letter in string by ASCII code
            count[strr.charAt(i)]++;

        char chh[] = new char[strr.length()]; //new array for comparing
        int i;
        for (i = 0; i < strr.length(); i++) {
            chh[i] = strr.charAt(i);
            int howmuch = 0;                //counter for symbols
            int j;
            for (j = 0; j <= i; j++) {

                if (strr.charAt(i) == chh[j])   //if that symbol exists one more time
                    howmuch++;
            }

            if (howmuch == 1) {
                double probability = count[strr.charAt(i)] / strr.length();

                Assignment2 assignment2 = new Assignment2(probability, strr.charAt(i), (int) count[strr.charAt(i)]);
                listofprobs.add(assignment2);
            }

        }
        Collections.sort(listofprobs, Comparator.comparingDouble(Assignment2::getProb).reversed());
        for (int m = 0; m < listofprobs.size(); m++) {
            if (listofprobs.get(m).getSymbol() == ' ') {
                System.out.print("space" + " - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                charArray.add(listofprobs.get(m).getSymbol());
                charfreq.add(listofprobs.get(m).getProb());
                System.out.println();
            }
            else if (listofprobs.get(m).getSymbol() == '\n') {
                System.out.print("new line" + " - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                charArray.add(listofprobs.get(m).getSymbol());
                charfreq.add(listofprobs.get(m).getProb());
                System.out.println();
            }
            else if (listofprobs.get(m).getSymbol() == '\r') {
                System.out.print("/r" + " - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                charArray.add(listofprobs.get(m).getSymbol());
                charfreq.add(listofprobs.get(m).getProb());
                System.out.println();
            }
            else {
                System.out.print(listofprobs.get(m).getSymbol() + " - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                charArray.add(listofprobs.get(m).getSymbol());
                charfreq.add(listofprobs.get(m).getProb());
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\kuany\\Desktop\\test.txt");

        BufferedReader text = new BufferedReader(new FileReader(file));

        String content = Files.readString(Path.of("C:\\Users\\kuany\\Desktop\\test.txt"), StandardCharsets.US_ASCII);
        getProbability(content);

        Scanner s = new Scanner(System.in);

        int size = charArray.size();

        PriorityQueue<Assignment2> HuffmanTree = new PriorityQueue<Assignment2>(size, new Comp());

        for (int i = 0; i < size; i++) {

            Assignment2 temp = new Assignment2();

            temp.symbol = charArray.get(i);
            temp.prob = charfreq.get(i);

            temp.left = null;
            temp.right = null;

            HuffmanTree.add(temp);
        }

        Assignment2 root = null;

        while (HuffmanTree.size() > 1) {

            Assignment2 first = HuffmanTree.peek();
            HuffmanTree.poll();

            Assignment2 second = HuffmanTree.peek();
            HuffmanTree.poll();

            Assignment2 temp = new Assignment2();

            temp.prob = first.prob + second.prob;
            temp.symbol = '-';


            temp.left = first;

            temp.right = second;

            root = temp;

            HuffmanTree.add(temp);

        }
        System.out.println();
        HuffmanCode(root, "");


        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);
        StringBuilder sb = new StringBuilder();
        for (char c : content.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }
        System.out.println();
        System.out.println("The encoded string is: " + sb);


        File file1 = new File("C:\\Users\\kuany\\Desktop\\encoded.txt");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file1));
            writer.write(sb.toString());
        } finally {
            if (writer != null) writer.close();
        }

        System.out.println();

        double compratio = (double) (content.length() * 8) / sb.toString().length();

        System.out.println("Number of bits in the original text: " + content.length() * 8 + " bits");

        System.out.println("Number of bits in the compressed text: " + sb.toString().length() + " bits");

        System.out.print("Compression ratio = ");
        System.out.format("%.2f", compratio);
        System.out.println();

        System.out.print("Average code length = ");
        printCodeLength(root, "");
        System.out.format("%.2f", codelength);
        System.out.println(" bits/symbol");
        System.out.println();
        String decoding = Files.readString(Path.of("C:\\Users\\kuany\\Desktop\\encoded.txt"), StandardCharsets.US_ASCII);
        System.out.println("Decoded text:");
        System.out.println();
        if (root.left == null && root.right == null) {
            while (root.getFrequency() == 0) {
                System.out.print(root.symbol);
            }
        } else {
            int index = -1;
            while (index < sb.length() - 1) {
                index = decode(root, index, sb);
            }
        }
        System.out.println();
        System.out.println();
        System.out.println("Original text:");
        System.out.println();
        System.out.println(content);
        System.out.println();
        if (content.equals(decodedText.toString())) {
            System.out.println("The original text and decoded text match.");
        } else {
            System.out.println("The original text and decoded doesn't match.");
        }

        System.out.println();
        System.out.println("ASSIGNMENT 6");
        System.out.println();

        String content2 = Files.readString(Path.of("C:\\Users\\kuany\\Desktop\\encoded.txt"), StandardCharsets.US_ASCII);
        System.out.println(content2);

        int lenofcontent2 = content2.length();
        System.out.println();

        divideto4(content2);
        System.out.println();
        divideto11(content2);

        System.out.println();

        HammingEncode();
        HammingEncode2();

        System.out.println();
        System.out.println("Encoded sequence Hamming code (7,4):");
        System.out.println(Encodedbitstring74);
        System.out.println();
        System.out.println("Encoded sequence Hamming code (15,11):");
        System.out.println(Encodedbitstring1511);

        File file2 = new File("C:\\Users\\kuany\\Desktop\\encoded74.txt");
        BufferedWriter writer2 = null;
        try {
            writer2 = new BufferedWriter(new FileWriter(file2));
            writer2.write(Encodedbitstring74);
        } finally {
            if (writer2 != null) writer2.close();
        }

        System.out.println();

        File file3 = new File("C:\\Users\\kuany\\Desktop\\encoded1511.txt");
        BufferedWriter writer3 = null;
        try {
            writer3 = new BufferedWriter(new FileWriter(file3));
            writer3.write(Encodedbitstring1511);
        } finally {
            if (writer3 != null) writer3.close();
        }



        System.out.println();
        System.out.println("Assignment 7");
        System.out.println();
        String content74 = Files.readString(Path.of("C:\\Users\\kuany\\Desktop\\encoded74.txt"), StandardCharsets.US_ASCII);
        System.out.println("Content of encoded74.txt:");
        System.out.println(content74);
        System.out.println();

        divideto8(content74);

        System.out.println();
        String content1511 = Files.readString(Path.of("C:\\Users\\kuany\\Desktop\\encoded1511.txt"), StandardCharsets.US_ASCII);
        System.out.println("Content of encoded1511.txt:");
        System.out.println();
        System.out.println(content1511);
        System.out.println();

        divideto16(content1511);

        System.out.println();
        ErrorGen(30,DataBlocks8);
        System.out.println();
        ErrorGen(50, DataBlocks16);
        System.out.println();
        HammingDecode();
        System.out.println();
        System.out.println("Decoded sequence:");
        System.out.println(Decodedbitstring8);
        System.out.println();
        System.out.println();
        StringBuffer sb2 = new StringBuffer();
        System.out.println("Sequence from assignment 3:");
        for (String s2 : DataBlocks4) {
            sb2.append(s2);
            sb2.append(" ");
        }
        String str = sb2.toString();
        System.out.println(str.replaceAll(" ", ""));

        if(Decodedbitstring8.equals(str.replaceAll(" ", ""))){
            System.out.println();
            System.out.println("They match");
        }
        else{
            System.out.println();
            System.out.println("They doesn't match");
        }
        File file4 = new File("C:\\Users\\kuany\\Desktop\\DecodedSequence.txt");
        BufferedWriter writer4 = null;
        try {
            writer4 = new BufferedWriter(new FileWriter(file4));
            writer4.write(Decodedbitstring8);
        } finally {
            if (writer4 != null) writer4.close();
        }
        System.out.println();

        HammingDecode2();
        System.out.println();
        System.out.println("Decoded sequence16:");
        System.out.println(Decodedbitstring16);
        System.out.println();
        System.out.println();
        StringBuffer sb3 = new StringBuffer();
        System.out.println("Sequence from assignment 3:");
        for (String s3 : DataBlocks11) {
            sb3.append(s3);
            sb3.append(" ");
        }
        String str3 = sb3.toString();
        System.out.println(str3.replaceAll(" ", ""));

        if(Decodedbitstring16.equals(str3.replaceAll(" ", ""))){
            System.out.println();
            System.out.println("They match");
        }
        else{
            System.out.println();
            System.out.println("They doesn't match");
        }
        File file5 = new File("C:\\Users\\kuany\\Desktop\\DecodedSequence16.txt");
        BufferedWriter writer5 = null;
        try {
            writer5 = new BufferedWriter(new FileWriter(file5));
            writer5.write(Decodedbitstring16);
        } finally {
            if (writer5 != null) writer5.close();
        }
        System.out.println();
        File file6 = new File("C:\\Users\\kuany\\Desktop\\SequenceFromAssignment3_11.txt");
        BufferedWriter writer6 = null;
        try {
            writer6 = new BufferedWriter(new FileWriter(file6));
            writer6.write(str3.replaceAll(" ", ""));
        } finally {
            if (writer6 != null) writer6.close();
        }
        System.out.println();
        File file7 = new File("C:\\Users\\kuany\\Desktop\\SequenceFromAssignment3_4.txt");
        BufferedWriter writer7 = null;
        try {
            writer7 = new BufferedWriter(new FileWriter(file7));
            writer7.write(str.replaceAll(" ", ""));
        } finally {
            if (writer7 != null) writer7.close();
        }
        System.out.println();
    }

    public static void HammingDecode(){
        //                  0   0   1   1   0   1   1   1
        //                  p0  p1  p2  b3  p3  b5  b6  b7
        //
        //
        //                p1: b3+b5+b7 = 1+1+1 = 1 incorrect.             p1 + p3 +p0 = 5    p1 + p2 +p0= b3   p2 + p3 + p0 = b6  p1 + p2 + p3 + p0 = b7
        //                  p2: b3+b6+b7 = 1+1+1 = 1 correct.
        //                  p3: b5+b6+b7 = 1+1+1 = 1 incorrect.
        //                  p0: b1+b2+b3+b4+b5+b6+b7 = 0+1+1+0+0+1+1 = 1 incorrect.
        //
        //
        //                  Error in position: 5
        //                  Corrected bitstring: 0 0 1 1 0 0 1 1.
        //                  Decoded bitstring: 1 0 1 1.
        //
        //
        for(int i = 0; i < DataBlocks8.size(); i++){
            System.out.println("Checking parity bits: ");
            char p0 = DataBlocks8.get(i).charAt(0);
            char p1 = DataBlocks8.get(i).charAt(1);
            char p2 = DataBlocks8.get(i).charAt(2);
            char b3 = DataBlocks8.get(i).charAt(3);
            char p3 = DataBlocks8.get(i).charAt(4);
            char b5 = DataBlocks8.get(i).charAt(5);
            char b6 = DataBlocks8.get(i).charAt(6);
            char b7 = DataBlocks8.get(i).charAt(7);
            System.out.println(p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7 + ":");
            char p1result;
            if((b3+b5+b7)%2==0){
                p1result = '0';
            }
            else {
                p1result = '1';
            }

            char p2result;
            if((b3+b6+b7)%2==0){
                p2result = '0';
            }
            else {
                p2result = '1';
            }

            char p3result;
            if((b5+b6+b7)%2==0){
                p3result = '0';
            }
            else {
                p3result = '1';
            }

            char p0result;
            if((p1result+p2result+b3+p3result+b5+b6+b7)%2==0){
                p0result = '0';
            }
            else {
                p0result = '1';
            }

            System.out.print("p1: " + " b3 + b5 + b7 = " + b3 + " + " + b5 + " + " + b7 + " = " + p1result);
            boolean p1check;
            if(p1 == p1result){
                System.out.println(" correct");
                p1check = true;
            }
            else{
                System.out.println(" incorrect");
                p1check = false;
            }
            System.out.print("p2: " + " b3 + b6 + b7 = " + b3 + " + " + b6 + " + " + b7 + " = " + p2result);
            boolean p2check;
            if(p2 == p2result){
                System.out.println(" correct");
                p2check = true;
            }
            else{
                System.out.println(" incorrect");
                p2check = false;
            }
            System.out.print("p3: " + " b5 + b6 + b7 = " + b5 + " + " + b6 + " + " + b7 + " = " + p3result);
            boolean p3check;
            if(p3 == p3result){
                System.out.println(" correct");
                p3check = true;

            }
            else{
                System.out.println(" incorrect");
                p3check = false;
            }
            System.out.print("p0: " + " p1 + p2 + b3 + p3 + b5 + b6 + b7 = " + p1 + " + " + p2 + " + " + b3 + " + " + p3 + " + " + b5 + " + " + b6 + " + " + b7 + " = " + p0result);
            boolean p0check;
            if(p0==p0result){
                System.out.println(" correct");
                p0check = true;
            }
            else{
                System.out.println(" incorrect");
                p0check = false;
            }
            if(p1check == false && p3check == false && p0check == false && p2check == true){
                System.out.println("Error in position: 5");
                if(b5 == '0'){
                    b5 = '1';
                }
                else{
                    b5 = '0';
                }

                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p1check == false && p3check == false && p0check == true && p2check == true){
                System.out.println("Error in position: 5");
                if(b5 == '0'){
                    b5 = '1';
                }
                else{
                    b5 = '0';
                }

                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p1check == false && p2check == false && p0check == false && p3check == true){
                System.out.println("Error in position: 3");
                if(b3 == '0'){
                    b3 = '1';
                }
                else{
                    b3 = '0';
                }

                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == false && p3check == false && p0check == false && p1check == true){
                System.out.println("Error in position: 6");
                if(b6 == '0'){
                    b6 = '1';
                }
                else{
                    b6 = '0';
                }

                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == false && p3check == false && p0check == true && p1check == true){
                System.out.println("Error in position: 6");
                if(b6 == '0'){
                    b6 = '1';
                }
                else{
                    b6 = '0';
                }

                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == false && p3check == false && p0check == false && p1check == false){
                System.out.println("Error in position: 7");
                if(b7 == '0'){
                    b7 = '1';
                }
                else{
                    b7 = '0';
                }

                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == true && p3check == true && p0check == true && p1check == true){
                System.out.println("No error");
                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);

            }
            else if(p2check == true && p3check == true && p0check == false && p1check == true){
                System.out.println("Error in position: 0");

                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == true && p3check == false && p0check == false && p1check == true){
                System.out.println("Error in position: 4");


                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == false && p3check == true && p0check == false && p1check == true){
                System.out.println("Error in position: 2");
                if(p2result == '0'){
                    p2result = '1';
                }
                else{
                    p2result = '0';
                }
                if(p0result == '0'){
                    p0result = '1';
                }
                else{
                    p0result = '0';
                }
                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == true && p3check == true && p0check == false && p1check == false){
                System.out.println("Error in position: 1");

                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == true && p3check == false && p0check == true && p1check == true){
                System.out.println("Error in position: 4");

                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == true && p3check == true && p0check == true && p1check == false){
                System.out.println("Error in position: 1");

                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == false && p3check == true && p0check == true && p1check == true){
                System.out.println("Error in position: 2");

                System.out.println("Corrected bitstring: "+ p0result + " " + p1result + " " + p2result + " " + b3 + " " + p3result + " " + b5 + " " + b6 + " " + b7);
            }
            else if(p2check == false && p3check == false && p0check == true && p1check == false){
                System.out.println("Error in position: 7");
                if(b7 == '0'){
                    b7 = '1';
                }
                else{
                    b7 = '0';
                }
                System.out.println("Corrected bitstring: "+ p0 + " " + p1 + " " + p2 + " " + b3 + " " + p3 + " " + b5 + " " + b6 + " " + b7);
            }
            else{
                System.out.println("Something went wrong");
            }
            System.out.println("Decoded bitstring: " + b3 + " " + b5 + " " + b6 + " " + b7);
            String Decodedbitstring = String.valueOf(b3) +  String.valueOf(b5) + String.valueOf(b6) + String.valueOf(b7);
            Decodedbitstring8 += Decodedbitstring;
            System.out.println();
        }
    }

    public static void HammingDecode2(){
        //                  00001111010:
        //                  Expand the block to 16 bits: _ _ _ 0 _ 0 0 0 _ 1 1 1 1 0 1 0
        //                  p1 = d3 + d5 + d7 + d9 + d11 + d13 + d15 = 0 + 0 + 0 + 1 + 1 + 0 + 0 = 0
        //                  p2 = d3 + d6 + d7 + d10 + d11 + d14 + d15 = 0 + 0 + 0 + 1 + 1 + 1 + 0 = 1
        //                  p4 = d5 + d6 + d7 + d12 + d13 + d14 + d15 = 0 + 0 + 0 + 1 + 0 + 1 + 0 = 0
        //                  p8 = d9 + d10 + d11 + d12 + d13 + d14 + d15 = 1 + 1 + 1 + 1 + 0 + 1 + 0 = 1
        //                  p0 = p1 + p2+ d3 + p4 + d5 + d6 + d7 + p8 + d9 + d10 + d11 + d12 + d13 + d14 + d15 = 0 + 1 + 0 + 0 + 0 + 0 + 0 + 1 + 1 + 1 + 1 + 1 + 0 + 1 + 0 = 1
        //                  Encoded bitstring: 1010000011111010
        //
        //
        for(int i = 0; i < DataBlocks16.size(); i++){

            char p0 = DataBlocks16.get(i).charAt(0);
            char p1 = DataBlocks16.get(i).charAt(1);
            char p2 = DataBlocks16.get(i).charAt(2);
            char d3 = DataBlocks16.get(i).charAt(3);
            char p4 = DataBlocks16.get(i).charAt(4);
            char d5 = DataBlocks16.get(i).charAt(5);
            char d6 = DataBlocks16.get(i).charAt(6);
            char d7 = DataBlocks16.get(i).charAt(7);
            char p8 = DataBlocks16.get(i).charAt(8);
            char d9 = DataBlocks16.get(i).charAt(9);
            char d10 = DataBlocks16.get(i).charAt(10);
            char d11 = DataBlocks16.get(i).charAt(11);
            char d12 = DataBlocks16.get(i).charAt(12);
            char d13 = DataBlocks16.get(i).charAt(13);
            char d14 = DataBlocks16.get(i).charAt(14);
            char d15 = DataBlocks16.get(i).charAt(15);

            char p1result;
            if((d3+d5+d7+d9+d11+d13+d15)%2==0){
                p1result = '0';
            }
            else {
                p1result = '1';
            }
            char p2result;
            if((d3+d6+d7+d10+d11+d14+d15)%2==0){
                p2result = '0';
            }
            else {
                p2result = '1';
            }
            char p4result;
            if((d5+d6+d7+d12+d13+d14+d15)%2==0){
                p4result = '0';
            }
            else {
                p4result = '1';
            }
            char p8result;
            if((d9+d10+d11+d12+d13+d14+d15)%2==0){
                p8result = '0';
            }
            else {
                p8result = '1';
            }

            char p0result;
            if((p1+p2+d3+p4+d5+d6+d7+p8+d9+d10+d11+d12+d13+d14+d15)%2==0){
                p0result = '0';
            }
            else {
                p0result = '1';
            }

            System.out.println(p0 + " " + p1 + " " + p2 + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            System.out.println("Checking parity bits:");
            System.out.println();
            System.out.print("p1 = d3 + d5 + d7 + d9 + d11 + d13 + d15 = " + d3 + " + " + d5 + " + " + d7 + " + " + d9 + " + " + d11 + " + " + d13 + " + " + d15 + " = " + p1result);
            boolean p1check;
            if(p1 == p1result){
                System.out.println(" correct");
                p1check = true;
            }
            else{
                System.out.println(" incorrect");
                p1check = false;
            }
            System.out.print("p2 = d3 + d6 + d7 + d10 + d11 + d14 + d15 = " + d3 + " + " + d6 + " + " + d7 + " + " + d10 + " + " + d11 + " + " + d14 + " + " + d15 + " = " + p2result);
            boolean p2check;
            if(p2 == p2result){
                System.out.println(" correct");
                p2check = true;
            }
            else{
                System.out.println(" incorrect");
                p2check = false;
            }
            System.out.print("p4 = d5 + d6 + d7 + d12 + d13 + d14 + d15 = " + d5 + " + " + d6 + " + " + d7 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p4result);
            boolean p4check;
            if(p4 == p4result){
                System.out.println(" correct");
                p4check = true;
            }
            else{
                System.out.println(" incorrect");
                p4check = false;
            }
            System.out.print("p8 = d9 + d10 + d11 + d12 + d13 + d14 + d15 = " + d9 + " + " + d10 + " + " + d11 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p8result);
            boolean p8check;
            if(p8 == p8result){
                System.out.println(" correct");
                p8check = true;
            }
            else{
                System.out.println(" incorrect");
                p8check = false;
            }
            System.out.print("p0 = p1 + p2+ d3 + p4 + d5 + d6 + d7 + p8 + d9 + d10 + d11 + d12 + d13 + d14 + d15 = "+ p1 + " + " + p2 + " + " + d3 + " + " + p4 + " + " + d5 + " + " + d6 + " + " + d7 + " + " + p8 + " + " + d9 + " + " + d10 + " + " + d11 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p0result);
            boolean p0check;
            if(p0 == p0result){
                System.out.println(" correct");
                p0check = true;
            }
            else{
                System.out.println(" incorrect");
                p0check = false;
            }
            System.out.println();
            if(p1check == false && p2check == false && p0check == false && p4check == true && p8check == true){
                System.out.println("Error in position: 3");
                if(d3 == '0'){
                    d3 = '1';
                }
                else{
                    d3 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2 + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == false && p0check == false && p4check == true && p8check == true){
                System.out.println("Error in position: 2");

                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2result + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == true && p0check == false && p4check == false && p8check == false){
                System.out.println("Error in position: 13");
                if(d13 == '0'){
                    d13 = '1';
                }
                else{
                    d13 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == false && p0check == false && p4check == false && p8check == true){
                System.out.println("Error in position: 6");
                if(d6 == '0'){
                    d6 = '1';
                }
                else{
                    d6 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == false && p0check == false && p4check == true && p8check == false){
                System.out.println("Error in position: 10");
                if(d10 == '0'){
                    d10 = '1';
                }
                else{
                    d10 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == false && p0check == false && p4check == false && p8check == false){
                System.out.println("Error in position: 15");
                if(d15 == '0'){
                    d15 = '1';
                }
                else{
                    d15 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == true && p0check == false && p4check == true && p8check == true){
                System.out.println("Error in position: 0");

                System.out.println("Corrected bitstring: " + p0result + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == false && p0check == false && p4check == false && p8check == false){
                System.out.println("Error in position: 14");
                if(d14 == '0'){
                    d14 = '1';
                }
                else{
                    d14 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == false && p0check == false && p4check == true && p8check == false){
                System.out.println("Error in position: 11");
                if(d11 == '0'){
                    d11 = '1';
                }
                else{
                    d11 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == true && p0check == false && p4check == false && p8check == false){
                System.out.println("Error in position: 12");
                if(d12 == '0'){
                    d12 = '1';
                }
                else{
                    d12 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == false && p0check == false && p4check == false && p8check == true){
                System.out.println("Error in position: 7");
                if(d7 == '0'){
                    d7 = '1';
                }
                else{
                    d7 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == true && p0check == false && p4check == true && p8check == false){
                System.out.println("Error in position: 8");

                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2 + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8result + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == true && p0check == false && p4check == false && p8check == true){
                System.out.println("Error in position: 5");
                if(d5 == '0'){
                    d5 = '1';
                }
                else{
                    d5 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == true && p0check == false && p4check == true && p8check == false){
                System.out.println("Error in position: 9");
                if(d9 == '0'){
                    d9 = '1';
                }
                else{
                    d9 = '0';
                }
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2+ " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == true && p0check == false && p4check == false && p8check == true){
                System.out.println("Error in position: 4");

                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2 + " " + d3 + " " + p4result + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == false && p2check == true && p0check == false && p4check == true && p8check == true){
                System.out.println("Error in position: 1");

                System.out.println("Corrected bitstring: " + p0 + " " + p1result + " " + p2 + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else if(p1check == true && p2check == true && p0check == true && p4check == true && p8check == true){
                System.out.println("No error");

                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2 + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            }
            else{
                System.out.println("Something went wrong");
                System.out.println("Corrected bitstring: " + p0 + " " + p1 + " " + p2 + " " + d3 + " " + p4 + " " + d5 + " " + d6 + " " + d7 + " " + p8 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);

            }
            System.out.println("Decoded bitstring: " + d3 + " " + d5 + " " + d6 + " " + d7 + " " + d9 + " " + d10 + " " + d11 + " " + d12 + " " + d13 +" " +d14 + " "+d15);
            String Decodedbitstring = String.valueOf(d3) + String.valueOf(d5) + String.valueOf(d6) + String.valueOf(d7) + String.valueOf(d9) + String.valueOf(d10) + String.valueOf(d11) + String.valueOf(d12) + String.valueOf(d13) + String.valueOf(d14)  + String.valueOf(d15);
            Decodedbitstring16 += Decodedbitstring;
            System.out.println();

        }
    }

    public static void ErrorGen(int percent,List content){
        System.out.println("Error generation...");
        System.out.println();
        int random_int = (int)Math.floor(percent);
        int sizeError = (content.size()*random_int)/100;
        System.out.println("Consists of " + content.size() + " data blocks");

        System.out.println("Taking off "+random_int+"%");
        System.out.println("Total size of corrupted data blocks is " + sizeError);
        for (int i = 0; i < sizeError; i++) {
            int randomIndex = rand.nextInt(content.size());
            String randomElement = (String) content.get(randomIndex);
            int randomintchar = rand.nextInt(randomElement.length());
            char randomChar = randomElement.charAt(randomintchar);
            if(randomChar == '0'){
                char[] myNameChars = randomElement.toCharArray();
                myNameChars[randomintchar] = '/';
                randomElement = String.valueOf(myNameChars);
                content.set(randomIndex,randomElement);


            }
            else if(randomChar == '1'){
                char[] myNameChars = randomElement.toCharArray();
                myNameChars[randomintchar] = '*';
                randomElement = String.valueOf(myNameChars);
                content.set(randomIndex,randomElement);


            }
            if(randomElement.contains("/")){
                char[] myNameChars = randomElement.toCharArray();
                myNameChars[randomintchar] = '1';
                randomElement = String.valueOf(myNameChars);
                content.set(randomIndex,randomElement);

            }
            if(randomElement.contains("*")){
                char[] myNameChars = randomElement.toCharArray();
                myNameChars[randomintchar] = '0';
                randomElement = String.valueOf(myNameChars);
                content.set(randomIndex,randomElement);

            }
            ErrorDataBlocks8 = content;
        }
        System.out.print("Blocks with errors: ");
        for(int i = 0; i < content.size(); i++){
            System.out.print("b"+(i+1)+": "+content.get(i)+" ");
        }
        System.out.println();


    }

    public static void divideto11(String content){

        int size2 = 11;
        System.out.println("Data blocks dividing into 11 bits");
        String[] tokens = content.split("(?<=\\G.{" + size2 + "})");
        Collections.addAll(DataBlocks11, tokens);
        for(int i = 0; i < DataBlocks11.size(); i++){
            System.out.print("b"+(i+1)+": ");
            if(DataBlocks11.get(i).length()==3){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"00000000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else if(DataBlocks11.get(i).length()==1){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"0000000000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else if(DataBlocks11.get(i).length()==2){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"000000000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else if(DataBlocks11.get(i).length()==4){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"0000000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else if(DataBlocks11.get(i).length()==5){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"000000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }else if(DataBlocks11.get(i).length()==6){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"00000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }else if(DataBlocks11.get(i).length()==7){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"0000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else if(DataBlocks11.get(i).length()==8){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"000"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }else if(DataBlocks11.get(i).length()==9){
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)),"00"+DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else if(DataBlocks11.get(i).length()==10) {
                DataBlocks11.set(DataBlocks11.indexOf(DataBlocks11.get(i)), "0" + DataBlocks11.get(i));
                System.out.println(DataBlocks11.get(i));
            }
            else{
                System.out.println(DataBlocks11.get(i));
            }
        }
    }
    public static void divideto4(String content){
        int size2 = 4;
        System.out.println("Data blocks dividing into 4 bits");

        String[] tokens = content.split("(?<=\\G.{" + size2 + "})");
        Collections.addAll(DataBlocks4, tokens);
        for(int i = 0; i < DataBlocks4.size(); i++){
            System.out.print("b"+(i+1)+": ");

            if(DataBlocks4.get(i).length()==1){
                DataBlocks4.set(DataBlocks4.indexOf(DataBlocks4.get(i)),"000"+DataBlocks4.get(i));
                System.out.println(DataBlocks4.get(i));
            }
            else if(DataBlocks4.get(i).length()==2){
                DataBlocks4.set(DataBlocks4.indexOf(DataBlocks4.get(i)),"00"+DataBlocks4.get(i));
                System.out.println(DataBlocks4.get(i));
            }
            else if(DataBlocks4.get(i).length()==3){
                DataBlocks4.set(DataBlocks4.indexOf(DataBlocks4.get(i)),"0"+DataBlocks4.get(i));
                System.out.println(DataBlocks4.get(i));
            }
            else{
                System.out.println(DataBlocks4.get(i));
            }
        }
    }

    public static void divideto8(String content){
        int size2 = 8;
        System.out.println("Data blocks dividing into 8 bits");

        String[] tokens = content.split("(?<=\\G.{" + size2 + "})");
        Collections.addAll(DataBlocks8, tokens);
        for(int i = 0; i < DataBlocks8.size(); i++){
            System.out.print("b"+(i+1)+": ");
            System.out.println(DataBlocks8.get(i));

        }
    }

    public static void divideto16(String content){
        int size2 = 16;
        System.out.println("Data blocks dividing into 16 bits");

        String[] tokens = content.split("(?<=\\G.{" + size2 + "})");
        Collections.addAll(DataBlocks16, tokens);
        for(int i = 0; i < DataBlocks16.size(); i++){
            System.out.print("b"+(i+1)+": ");
            System.out.println(DataBlocks16.get(i));

        }
    }

    public static void HammingEncode(){//1011
        for(int i = 0; i < DataBlocks4.size(); i++){
            System.out.println(DataBlocks4.get(i)+":");
            char b3 = DataBlocks4.get(i).charAt(0);
            char b5 = DataBlocks4.get(i).charAt(1);
            char b6 = DataBlocks4.get(i).charAt(2);
            char b7 = DataBlocks4.get(i).charAt(3);

            int p1 = 0;
            if((b3+b5+b7)%2==0){
                p1 = 0;
            }
            else {
                p1 = 1;
            }

            int p2 = 0;
            if((b3+b6+b7)%2==0){
                p2 = 0;
            }
            else {
                p2 = 1;
            }

            int p3 = 0;
            if((b5+b6+b7)%2==0){
                p3 = 0;
            }
            else {
                p3 = 1;
            }

            int p0 = 0;
            if((p1+p2+b3+p3+b5+b6+b7)%2==0){
                p0 = 0;
            }
            else {
                p0 = 1;
            }

            System.out.println("Expand the block to 8 bits: _ _ _ "+b3+" _ "+b5+" "+b6 + " " + b7);
            System.out.println("p1: " + " b3 + b5 + b7 = " + b3 + " + " + b5 + " + " + b7 + " = " + p1);
            System.out.println("p2: " + " b3 + b6 + b7 = " + b3 + " + " + b6 + " + " + b7 + " = " + p2);
            System.out.println("p3: " + " b5 + b6 + b7 = " + b5 + " + " + b6 + " + " + b7 + " = " + p3);
            System.out.println("p0: " + " b1 + b2 + b3 + b4 + b5 + b6 + b7 = " + p1 + " + " + p2 + " + " + b3 + " + " + p3 + " + " + b5 + " + " + b6 + " + " + b7 + " = " + p0);

            String Encodedbitstring2 = String.valueOf(p0)+ String.valueOf(p1)+ String.valueOf(p2) + String.valueOf(b3) + String.valueOf(p3) + String.valueOf(b5) + String.valueOf(b6) + String.valueOf(b7);
            System.out.println("Encoded bitstring: " + Encodedbitstring2);
            Encodedbitstring74 += Encodedbitstring2;

            System.out.println();



        }
    }

    public static void HammingEncode2(){
        for(int i = 0; i < DataBlocks11.size(); i++){
            System.out.println(DataBlocks11.get(i)+":");
            char d3 = DataBlocks11.get(i).charAt(0);
            char d5 = DataBlocks11.get(i).charAt(1);
            char d6 = DataBlocks11.get(i).charAt(2);
            char d7 = DataBlocks11.get(i).charAt(3);
            char d9 = DataBlocks11.get(i).charAt(4);
            char d10 = DataBlocks11.get(i).charAt(5);
            char d11 = DataBlocks11.get(i).charAt(6);
            char d12 = DataBlocks11.get(i).charAt(7);
            char d13 = DataBlocks11.get(i).charAt(8);
            char d14 = DataBlocks11.get(i).charAt(9);
            char d15 = DataBlocks11.get(i).charAt(10);

            int p1 = 0;
            if((d3+d5+d7+d9+d11+d13+d15)%2==0){
                p1 = 0;
            }
            else {
                p1 = 1;
            }
            int p2 = 0;
            if((d3+d6+d7+d10+d11+d14+d15)%2==0){
                p2 = 0;
            }
            else {
                p2 = 1;
            }
            int p4 = 0;
            if((d5+d6+d7+d12+d13+d14+d15)%2==0){
                p4 = 0;
            }
            else {
                p4 = 1;
            }
            int p8 = 0;
            if((d9+d10+d11+d12+d13+d14+d15)%2==0){
                p8 = 0;
            }
            else {
                p8 = 1;
            }

            int p0 = 0;
            if((p1+p2+d3+p4+d5+d6+d7+p8+d9+d10+d11+d12+d13+d14+d15)%2==0){
                p0 = 0;
            }
            else {
                p0 = 1;
            }

            System.out.println("Expand the block to 16 bits: _ _ _ " + d3 + " _ " + d5 + " " + d6 + " "+ d7 + " _ " + d9 + " "+ d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            System.out.println("p1 = d3 + d5 + d7 + d9 + d11 + d13 + d15 = " + d3 + " + " + d5 + " + " + d7 + " + " + d9 + " + " + d11 + " + " + d13 + " + " + d15 + " = " + p1);
            System.out.println("p2 = d3 + d6 + d7 + d10 + d11 + d14 + d15 = " + d3 + " + " + d6 + " + " + d7 + " + " + d10 + " + " + d11 + " + " + d14 + " + " + d15 + " = " + p2);
            System.out.println("p4 = d5 + d6 + d7 + d12 + d13 + d14 + d15 = " + d5 + " + " + d6 + " + " + d7 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p4);
            System.out.println("p8 = d9 + d10 + d11 + d12 + d13 + d14 + d15 = " + d9 + " + " + d10 + " + " + d11 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p8);
            System.out.println("p0 = p1 + p2+ d3 + p4 + d5 + d6 + d7 + p8 + d9 + d10 + d11 + d12 + d13 + d14 + d15 = "+ p1 + " + " + p2 + " + " + d3 + " + " + p4 + " + " + d5 + " + " + d6 + " + " + d7 + " + " + p8 + " + " + d9 + " + " + d10 + " + " + d11 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p0);

            String Encodedbitstring = String.valueOf(p0)+String.valueOf(p1)+ String.valueOf(p2) + String.valueOf(d3) + String.valueOf(p4) + String.valueOf(d5) + String.valueOf(d6) + String.valueOf(d7) + String.valueOf(p8) + String.valueOf(d9) + String.valueOf(d10) + String.valueOf(d11) + String.valueOf(d12) + String.valueOf(d13) + String.valueOf(d14)  + String.valueOf(d15);
            System.out.println("Encoded bitstring: " + Encodedbitstring);
            Encodedbitstring1511 += Encodedbitstring;
            System.out.println();



        }
    }

    public static int decode(Assignment2 root, int index, StringBuilder sb)
    {
        if (root == null) {
            return index;
        }

        if (root.left == null && root.right == null)
        {
            decodedText.append(root.symbol);
            System.out.print(root.symbol);
            return index;
        }

        index++;

        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decode(root, index, sb);
        return index;
    }

    public static void encode(Assignment2 root, String str,
                              Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }

        huffmanCode.put(root.symbol, str.length() > 0 ? str : "1");


        encode(root.left, str + '0', huffmanCode);
        encode(root.right, str + '1', huffmanCode);
    }

    public static void printCodeLength(Assignment2 root, String s)
    {

        if (root.left == null && root.right == null && Character.isDefined(root.symbol))
        {
                for(int i = 0; i < charArray.size(); i++){
                    codelength+=root.getProb()*s.length();

                return;
            }
        }
        printCodeLength(root.left, s + "0");
        printCodeLength(root.right, s + "1");
    }

    public static void HuffmanCode(Assignment2 root, String code)
    {

        if (root.left == null && root.right == null && Character.isDefined(root.symbol))
        {
            if(root.symbol == ' '){
                System.out.print("SPACE"+" - ");
                System.out.format("%.3f", root.getProb());
                System.out.println(" - "+ code);

                return;
            }
            else if(root.symbol == '\n'){
                System.out.print("New line"+" - ");
                System.out.format("%.3f", root.getProb());
                System.out.println(" - "+ code);

                return;
            }
            else if(root.symbol == '\r'){
                System.out.print("/r"+" - ");
                System.out.format("%.3f", root.getProb());
                System.out.println(" - "+ code);

                return;
            }
            else{
                System.out.print(root.symbol+" - ");
                System.out.format("%.3f", root.getProb());
                System.out.println(" - "+ code);

                return;}
        }


        HuffmanCode(root.left, code + "0");
        HuffmanCode(root.right, code + "1");
    }


    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

}

