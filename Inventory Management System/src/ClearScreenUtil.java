// ClearScreenUtil.java
public class ClearScreenUtil {

    public static void clearScreen() {
       
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void clearPreviousLine(){
        System.out.print("\033[F");  // Moves the cursor up one line
        System.out.print("\033[K");
    }
    public static void moveCursorUp(){
        System.out.print("\033[F");
    }
}