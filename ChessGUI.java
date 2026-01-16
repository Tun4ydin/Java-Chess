import javax.swing.*;
import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame
{

    private BoardPanel boardPanel;
    private JLayeredPane layeredPane;

    public ChessGUI(Game game)
    {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 800));
        add(layeredPane); // add to frame
        setTitle("Java Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(game);
        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void declareWinner(String message)
    {
        // Create a semi-transparent overlay
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(new GridBagLayout());

        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        label.setForeground(Color.WHITE);
        overlay.add(label);

        // Add overlay to the top layer
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);
        overlay.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

        overlay.addMouseListener(new java.awt.event.MouseAdapter() {});
    }
}

