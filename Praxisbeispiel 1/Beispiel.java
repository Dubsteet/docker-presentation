import java.util.Random;

public class Beispiel {
    public static void main(String[] args) {

        Random rng = new Random();
        int zahl;

        while(true) {
            zahl = rng.nextInt(20) + 1;
            System.out.println("Die Zahl ist " + zahl);
            try {
                Thread.sleep(5000);
            } catch(InterruptedException ex) {
                System.out.println("Fehler beim Sleep");
            }
        }
    }
}