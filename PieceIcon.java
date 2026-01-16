import javax.swing.*;
import java.net.URL;
import java.util.HashMap;

public class PieceIcon {
    private final static HashMap<String, ImageIcon> icons = new HashMap<>();

    static
    {
        String[] pieces = {"Pawn", "Rook", "Knight", "Bishop", "Queen", "King"};
        for (String type : pieces)
        {
            loadIcon("White" + type, type.toLowerCase() + "-w.png");
            loadIcon("Black" + type, type.toLowerCase() + "-b.png");
        }
    }

    private static void loadIcon(String key, String filename)
    {
        URL imgURL = PieceIcon.class.getResource("/resources/" + filename);
        if (imgURL != null)
        {
            icons.put(key, new ImageIcon(imgURL));
        }
        else
        {
            System.err.println("CRITICAL: Could not find image file: resources/" + filename);
        }
        System.out.println("Loading: /" + filename + " → " + (imgURL != null));
    }

    public static ImageIcon getIcon(Piece p)
    {
        if (p == null)
            return null;
        String key = (p.isColor_check() ? "White" : "Black") + p.getClass().getSimpleName();
        return icons.get(key);
    }
}