public class Board {
    private final Piece[][] board = new Piece[8][8];

    public Board() {
        setupPieces();
    }

    public void setupPieces()
    {
        // Setup Black Pieces (Rows 0 and 1)
        board[0][0] = new Rook(0, 'a', false);
        board[0][1] = new Knight(0, 'b', false);
        board[0][2] = new Bishop(0, 'c', false);
        board[0][3] = new Queen(0, 'd', false);
        board[0][4] = new King(0, 'e', false);
        board[0][5] = new Bishop(0, 'f', false);
        board[0][6] = new Knight(0, 'g', false);
        board[0][7] = new Rook(0, 'h', false);
        for (int i = 0; i < 8; i++) board[1][i] = new Pawn(1, (char)('a' + i), false);

        // Setup White Pieces (Rows 6 and 7)
        for (int i = 0; i < 8; i++) board[6][i] = new Pawn(6, (char)('a' + i), true);
        board[7][0] = new Rook(7, 'a', true);
        board[7][1] = new Knight(7, 'b', true);
        board[7][2] = new Bishop(7, 'c', true);
        board[7][3] = new Queen(7, 'd', true);
        board[7][4] = new King(7, 'e', true);
        board[7][5] = new Bishop(7, 'f', true);
        board[7][6] = new Knight(7, 'g', true);
        board[7][7] = new Rook(7, 'h', true);
    }

    public Piece getPiece(int row, char col) {
        return board[row][col - 'a'];
    }

    public Piece movePiece(Piece piece, int newRow, char newCol)
    {
        int oldRow = piece.getRow();
        int oldColIdx = piece.getCol() - 'a';
        int newColIdx = newCol - 'a';

        // Identify the captured piece
        Piece captured = board[newRow][newColIdx];

        // Clear the old square
        board[oldRow][oldColIdx] = null;

        //  Update the piece's internal coordinates
        piece.set_pos(newRow, newCol);

        //  Place the piece in the new square in the array
        board[newRow][newColIdx] = piece;

        return captured;
    }

    public void undoMove(Piece piece, int oldRow, char oldCol, Piece captured)
    {
        int currentRow = piece.getRow();
        int currentColIdx = piece.getCol() - 'a';
        int oldColIdx = oldCol - 'a';

        board[currentRow][currentColIdx] = null; // Clear the square it moved to
        piece.set_pos(oldRow, oldCol);           // Reset piece internal state
        board[oldRow][oldColIdx] = piece;        // Put piece back

        // Restore captured piece if there was one
        if (captured != null)
        {
            board[captured.getRow()][captured.getCol() - 'a'] = captured;
        }
    }

    public Piece findKing(boolean color)
    {
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                Piece p = board[r][c];
                if (p instanceof King && p.isColor_check() == color)
                {
                    return p;
                }
            }
        }
        return null;
    }

    public boolean isSquareAttacked(int row, char col, boolean byWhite)
    {
        if (isAdjacentToEnemyKing(row, col, !byWhite))
        {
            return true;
        }

        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                Piece p = board[r][c];
                if (p != null && p.isColor_check() == byWhite)
                {
                    if (p.move_check(row, col, board))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isKingInCheck(boolean isWhite)
    {
        Piece king = findKing(isWhite);
        if (king == null) return false;

        return isSquareAttacked(king.getRow(), king.getCol(), !isWhite);
    }

    public Piece simulateMove(Piece piece, int newRow, char newCol)
    {
        int oldRow = piece.getRow();
        int oldCol = piece.getCol() - 'a';
        int newColIndex = newCol - 'a';

        Piece captured = board[newRow][newColIndex];

        board[oldRow][oldCol] = null;
        piece.set_pos(newRow, newCol);
        board[newRow][newColIndex] = piece;

        return captured;
    }

    public boolean canCastle(King king, char targetCol)
    {

        if (king.has_moved()) return false;
        if (isKingInCheck(king.isColor_check())) return false;

        int row = king.getRow();
        int kingCol = king.getCol() - 'a';
        int target = targetCol - 'a';

        boolean kingSide = target > kingCol;
        int rookCol = kingSide ? 7 : 0;

        Piece rook = board[row][rookCol];
        if (!(rook instanceof Rook) || rook.has_moved()) return false;

        int step = kingSide ? 1 : -1;

        // Squares between king and rook must be empty
        for (int c = kingCol + step; c != rookCol; c += step)
        {
            if (board[row][c] != null) return false;
        }

        // King must not pass through check
        for (int c = kingCol + step; c != target + step; c += step)
        {
            if (isSquareAttacked(row, (char) ('a' + c), !king.isColor_check()))
            {
                return false;
            }
        }

        return true;
    }

    public void castle(King king, char targetCol)
    {

        int row = king.getRow();
        int kingCol = king.getCol() - 'a';
        int target = targetCol - 'a';

        boolean kingSide = target > kingCol;

        int rookFromCol = kingSide ? 7 : 0;
        int rookToCol = kingSide ? target - 1 : target + 1;

        Rook rook = (Rook) board[row][rookFromCol];

        // Move king
        board[row][kingCol] = null;
        king.set_pos(row, targetCol);
        board[row][target] = king;

        // Move rook
        board[row][rookFromCol] = null;
        rook.set_pos(row, (char) ('a' + rookToCol));
        board[row][rookToCol] = rook;
    }

    public Piece[][] getBoard()
    {
        return board;
    }

    private boolean areAdjacent(int r1, char c1, int r2, char c2)
    {
        return Math.abs(r1 - r2) <= 1 && Math.abs(c1 - c2) <= 1;
    }

    private boolean isAdjacentToEnemyKing(int row, char col, boolean isWhite)
    {

        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                Piece p = board[r][c];
                if (p instanceof King && p.isColor_check() != isWhite)
                {
                    return areAdjacent(row, col, r, p.getCol());
                }
            }
        }
        return false;
    }

}
