import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Assignment2 {

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static void getProbability(String strr)
    {

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

                if(strr.charAt(i)==' '){
                    System.out.print("space" + " - ");
                    System.out.format("%.3f", probability);
                    System.out.println();
                }
                else if(strr.charAt(i)=='\n'){
                    System.out.print("newline" + " - ");
                    System.out.format("%.3f", probability);
                    System.out.println();
                }
                else {
                    System.out.print(strr.charAt(i) + " - ");
                    System.out.format("%.3f", probability);
                    System.out.println();
                }
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
}

