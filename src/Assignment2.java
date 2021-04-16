import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Assignment2{
    private double prob;
    private char symbol;

    public Assignment2(double prob, char symbol) {
        this.prob = prob;
        this.symbol = symbol;
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    static void getProbability(String strr)
    {
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
                double probability = count[strr.charAt(i)]/strr.length();
                Assignment2 assignment2 = new Assignment2(probability,strr.charAt(i));
                listofprobs.add(assignment2);
            }
        }
        Collections.sort(listofprobs, Comparator.comparingDouble(Assignment2::getProb).reversed());
        for(int m = 0; m<listofprobs.size();m++){
            if(listofprobs.get(m).getSymbol()==' '){
                System.out.print("space"+" - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                System.out.println();}
            else{
                System.out.print(listofprobs.get(m).getSymbol()+" - ");
                System.out.format("%.3f", listofprobs.get(m).getProb());
                System.out.println();
            }
        }
    }
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\kuany\\Desktop\\test.txt");

        BufferedReader text = new BufferedReader(new FileReader(file));

        String strr;
        String content = Files.readString(Path.of("C:\\Users\\kuany\\Desktop\\test.txt"), StandardCharsets.US_ASCII);
        content = content.replace("\n", "").replace("\r", "");
        getProbability(content);


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