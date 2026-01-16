import javax.swing.*;
import java.awt.BorderLayout;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            Board board = new Board();
            Game game = new Game(board, true);
            BoardPanel boardPanel = new BoardPanel(game);
            JFrame frame = new JFrame("Chess Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(boardPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
