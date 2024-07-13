public class sleepUtil {
    // Utility method to add a delay
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
        }
    }
}
