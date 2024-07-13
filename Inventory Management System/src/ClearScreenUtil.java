// ClearScreenUtil.java
public class ClearScreenUtil {

    public static void clearScreen() {
        // Using ANSI escape sequences to clear the screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}