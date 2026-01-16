import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JLayeredPane
{

    private SquarePanel[][] squares = new SquarePanel[8][8];
    private Game game;

    public BoardPanel(Game game)
    {
        this.game = game;
        setLayout(null);

        int tileSize = 70;

        boolean whiteSquare = true;
        for (int row = 0; row < 8; row++)
        {
            whiteSquare = !whiteSquare;
            for (int col = 0; col < 8; col++)
            {
                SquarePanel square = new SquarePanel(row, col, game, this);
                square.setBounds(col * tileSize, row * tileSize, tileSize, tileSize);
                square.setBackground(whiteSquare ? Color.WHITE : Color.GRAY);
                square.setOpaque(true);

                squares[row][col] = square;
                add(square, JLayeredPane.DEFAULT_LAYER);

                whiteSquare = !whiteSquare;
            }
        }

        setPreferredSize(new Dimension(tileSize * 8, tileSize * 8));
        refreshBoard();
    }


    public void refreshBoard()
    {
        Piece[][] boardArray = game.getBoard().getBoard();
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                squares[row][col].setPiece(boardArray[row][col]);
            }
        }
        repaint();
    }
}

