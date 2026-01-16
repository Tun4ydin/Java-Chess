public class Rook extends Piece
{
    public Rook(int row, char col, boolean isWhite)
    {
        super(row, col, isWhite);
    }

    @Override
    public boolean move_check(int new_row, char new_col, Piece[][] board)
    {
        if (row != new_row && col != new_col)
            return false;
        if (!is_path_clear(new_row, new_col, board))
            return false;

        Piece target = board[new_row][new_col - 'a'];
        return target == null || target.isColor_check() != this.isColor_check();
    }
}