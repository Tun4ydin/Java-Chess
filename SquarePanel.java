import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SquarePanel extends JPanel
{

    private int row, col;
    private Piece piece;
    private JLabel pieceLabel;
    private Game game;
    private BoardPanel boardPanel;

    private static int dragOffsetX, dragOffsetY;
    private static JLabel draggedLabel;
    private static Piece draggedPiece;
    private static int originRow, originCol;

    public SquarePanel(int row, int col, Game game, BoardPanel boardPanel)
    {
        this.row = row;
        this.col = col;
        this.game = game;
        this.boardPanel = boardPanel;
        setLayout(null); // absolute for drag

        pieceLabel = new JLabel();
        pieceLabel.setHorizontalAlignment(JLabel.CENTER);
        pieceLabel.setVerticalAlignment(JLabel.CENTER);
        pieceLabel.setBounds(0, 0, 70, 70);
        add(pieceLabel);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (piece != null)
                {
                    draggedPiece = piece;
                    draggedLabel = new JLabel(PieceIcon.getIcon(piece));
                    draggedLabel.setSize(70, 70);
                    boardPanel.add(draggedLabel, JLayeredPane.DRAG_LAYER);
                    Point p = SwingUtilities.convertPoint(SquarePanel.this, e.getPoint(), boardPanel);
                    dragOffsetX = e.getX();
                    dragOffsetY = e.getY();
                    draggedLabel.setLocation(p.x - dragOffsetX, p.y - dragOffsetY);
                    originRow = row;
                    originCol = col;
                    pieceLabel.setIcon(null);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (draggedPiece != null)
                {
                    Point p = SwingUtilities.convertPoint(SquarePanel.this, e.getPoint(), boardPanel);

                    // Use double to avoid rounding down too early, then clamp the values
                    int targetCol = Math.min(7, Math.max(0, p.x / (boardPanel.getWidth() / 8)));
                    int targetRow = Math.min(7, Math.max(0, p.y / (boardPanel.getHeight() / 8)));

                    // Attempt move - IMPORTANT: Note the char conversion logic
                    boolean success = game.move(originRow, (char) ('a' + originCol),
                            targetRow, (char) ('a' + targetCol));

                    draggedLabel.setVisible(false);
                    boardPanel.remove(draggedLabel); // Remove the floating icon from the drag layer
                    draggedPiece = null;
                    draggedLabel = null;

                    boardPanel.refreshBoard(); // Re-draw everything in the correct spots
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedLabel != null)
                {
                    Point p = SwingUtilities.convertPoint(SquarePanel.this, e.getPoint(), boardPanel);
                    draggedLabel.setLocation(p.x - dragOffsetX, p.y - dragOffsetY);
                }
            }
        });
    }

    public void setPiece(Piece p)
    {
        this.piece = p;
        if (p != null)
        {
            ImageIcon icon = PieceIcon.getIcon(p);
            if (icon != null) {
                pieceLabel.setIcon(icon);
                // This ensures the image actually takes up the square space
                pieceLabel.setBounds(0, 0, 70, 70);
            } else {
                // If image fails, show text so the game is still playable
                pieceLabel.setText(p.isColor_check() ? "W" : "B");
                pieceLabel.setBounds(0, 0, 70, 70);
            }

        }


    }
}
