
import javax.xml.crypto.Data;
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
    public static List <String> DataBlocks11 = new ArrayList<String>();
    public static String Encodedbitstring74 = "";
    public static String Encodedbitstring1511 = "";

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
            else if(DataBlocks11.get(i).length()==1) {
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

            System.out.println("Expand the block to 15 bits: _ _ " + d3 + " _ " + d5 + " " + d6 + " "+ d7 + " _ " + d9 + " "+ d10 + " " + d11 + " " + d12 + " " + d13 + " " + d14 + " " + d15);
            System.out.println("p1 = d3 + d5 + d7 + d9 + d11 + d13 + d15 = " + d3 + " + " + d5 + " + " + d7 + " + " + d9 + " + " + d11 + " + " + d13 + " + " + d15 + " = " + p1);
            System.out.println("p2 = d3 + d6 + d7 + d10 + d11 + d14 + d15 = " + d3 + " + " + d6 + " + " + d7 + " + " + d10 + " + " + d11 + " + " + d14 + " + " + d15 + " = " + p2);
            System.out.println("p4 = d5 + d6 + d7 + d12 + d13 + d14 + d15 = " + d5 + " + " + d6 + " + " + d7 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p4);
            System.out.println("p8 = d9 + d10 + d11 + d12 + d13 + d14 + d15 = " + d9 + " + " + d10 + " + " + d11 + " + " + d12 + " + " + d13 + " + " + d14 + " + " + d15 + " = " + p8);

            String Encodedbitstring = String.valueOf(p1)+ String.valueOf(p2) + String.valueOf(d3) + String.valueOf(p4) + String.valueOf(d5) + String.valueOf(d6) + String.valueOf(d7) + String.valueOf(p8) + String.valueOf(d9) + String.valueOf(d10) + String.valueOf(d11) + String.valueOf(d12) + String.valueOf(d13) + String.valueOf(d14)  + String.valueOf(d15);
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

