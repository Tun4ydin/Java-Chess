public abstract class Piece
{
    protected int row;
    protected char col;
    protected boolean color_check;
    protected boolean moved = false;

    public Piece(int row, char col, boolean color_check)
    {
        this.row = row;
        this.col = col;
        this.color_check = color_check;
    }

    public int getRow()
    {
        return row;
    }

    public char getCol()
    {
        return col;
    }

    public void set_pos(int row, char col)
    {
        this.row = row;
        this.col = col;
        this.moved = true;
    }

    public boolean has_moved()
    {
        return moved;
    }

    public boolean isColor_check()
    {
        return color_check;
    }

    public abstract boolean move_check(int target_row, char target_column, Piece[][] board);

    protected boolean is_path_clear(int new_row, char new_col, Piece[][] board)
    {
        int new_col_idx = new_col - 'a';
        int row_diff = new_row - row;
        int col_diff = new_col_idx - (col - 'a');

        int row_step = Integer.signum(row_diff);
        int col_step = Integer.signum(col_diff);

        // Only allow straight or diagonal moves
        if (row_step != 0 && col_step != 0 && Math.abs(row_diff) != Math.abs(col_diff)) return false;
        if (row_step == 0 && col_step == 0) return false; // same square

        int r = row + row_step;
        int c = (col - 'a') + col_step;

        while (r != new_row || c != new_col_idx)
        {
            if (board[r][c] != null) return false;
            r += row_step;
            c += col_step;
        }

        return true;
    }
}

