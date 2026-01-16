public class King extends Piece
{

    public King(int row, char col, boolean isWhite)
    {
        super(row, col, isWhite);
    }

    @Override
    public boolean move_check(int new_row, char new_col, Piece[][] board)
    {

        int dr = Math.abs(new_row - row);
        int dc = Math.abs(new_col - col);

        if (dr <= 1 && dc <= 1)
        {
            Piece target = board[new_row][new_col-'a'];
            return target == null || target.isColor_check() != this.isColor_check();
        }

        return dr == 0 && dc == 2 && !has_moved();
    }
}

