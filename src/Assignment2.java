import java.io.*;

public class Assignment2 {

    static void getProbability(String strr)
    {

        double count[] = new double[256]; //ASCII size

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
                System.out.print(strr.charAt(i) + " - ");
                System.out.format("%.4f", probability);
                System.out.println();

            }
        }
    }
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\kuany\\Desktop\\test.txt");

        BufferedReader text = new BufferedReader(new FileReader(file));

        String strr;
        strr = text.readLine();
        getProbability(strr);

    }
}

