public class Queen extends Piece
{

    public Queen(int row, char col, boolean isWhite)
    {
        super(row, col, isWhite);
    }

    @Override
    public boolean move_check(int new_row, char new_col, Piece[][] board)
    {

        boolean straight = (row == new_row || col == new_col);
        boolean diagonal = Math.abs(new_row - row) == Math.abs(new_col - col);

        if (!straight && !diagonal)
            return false;

        if (!is_path_clear(new_row, new_col, board))
            return false;

        Piece target = board[new_row][new_col-'a'];
        return target == null || target.isColor_check() != this.isColor_check();
    }
}

