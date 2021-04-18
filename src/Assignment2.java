
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Assignment2 {
    public static ArrayList<Character> charArray = new ArrayList<>();
    public static ArrayList<Double> charfreq = new ArrayList<>();
    private static double codelength = (double) 0;
    private double prob;
    private char symbol;

    Assignment2 left;
    Assignment2 right;
    private String nameofnode;

    public Assignment2(double prob, char symbol) {
        this.prob = prob;
        this.symbol = symbol;
    }

    public Assignment2() {

    }

    public String getNameofnode() {
        return nameofnode;
    }

    public void setNameofnode(String nameofnode) {
        this.nameofnode = nameofnode;
    }

    static class MyComparator implements Comparator<Assignment2> {
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
                Assignment2 assignment2 = new Assignment2(probability, strr.charAt(i));
                listofprobs.add(assignment2);
            }
        }
        Collections.sort(listofprobs, Comparator.comparingDouble(Assignment2::getProb));
        for (int m = 0; m < listofprobs.size(); m++) {
            if (listofprobs.get(m).getSymbol() == ' ') {
                System.out.print("space" + " - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                charArray.add(listofprobs.get(m).getSymbol());
                charfreq.add(listofprobs.get(m).getProb());
                System.out.println();
            } else {
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
        content = content.replace("\n", "").replace("\r", "");
        getProbability(content);

        Scanner s = new Scanner(System.in);

        int n = charArray.size();

        PriorityQueue<Assignment2> qu
                = new PriorityQueue<Assignment2>(n, new MyComparator());

        for (int i = 0; i < n; i++) {

            Assignment2 hnn = new Assignment2();

            hnn.symbol = charArray.get(i);
            hnn.prob = charfreq.get(i);

            hnn.left = null;
            hnn.right = null;

            qu.add(hnn);
        }

        Assignment2 root = null;

        while (qu.size() > 1) {

            Assignment2 x = qu.peek();
            qu.poll();

            Assignment2 y = qu.peek();
            qu.poll();

            Assignment2 temp = new Assignment2();

            temp.prob = x.prob + y.prob;
            temp.symbol = '-';


            temp.left = x;

            temp.right = y;

            root = temp;

            qu.add(temp);

        }
        System.out.println();
        printCode(root,"");

        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root,"",huffmanCode);
        StringBuilder sb = new StringBuilder();
        for (char c: content.toCharArray()) {
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

        double compratio = (double) (content.length()* 8) / sb.toString().length();

        System.out.println("Number of bits in the original text: "+ content.length()* 8 + " bits");

        System.out.println("Number of bits in the compressed text: "+sb.toString().length()+ " bits");

        System.out.print("Compression ratio = ");
        System.out.format("%.2f", compratio);
        System.out.println();

        System.out.print("Average code length = ");
        printCodeLength(root,"");
        System.out.format("%.2f", codelength);
        System.out.println(" bits/symbol");
        System.out.println();


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

    public static void printCode(Assignment2 root, String s)
    {

        if (root.left == null && root.right == null && Character.isDefined(root.symbol))
        {
            if(root.symbol == ' '){
                System.out.print("SPACE"+" - ");
                System.out.format("%.3f", root.getProb());
                System.out.println(" - "+ s);

                return;
            }
            else{
                System.out.print(root.symbol+" - ");
                System.out.format("%.3f", root.getProb());
                System.out.println(" - "+ s);

                return;}
        }


        printCode(root.left, s + "0");
        printCode(root.right, s + "1");
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

