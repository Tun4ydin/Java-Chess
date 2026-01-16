public class Pawn extends Piece {
    private int lastMoveDistance = 0;

    public Pawn(int row, char col, boolean color) {
        super(row, col, color);
    }

    @Override
    public void set_pos(int row, char col) {
        lastMoveDistance = Math.abs(row - this.row);
        super.set_pos(row, col);
    }

    public boolean movedTwoSquaresLastTurn() {
        return lastMoveDistance == 2;
    }

    @Override
    public boolean move_check(int new_row, char new_col, Piece[][] board) {
        int dr = new_row - row;
        int dc = new_col - col;
        int colIdx = new_col - 'a';

        // White moves -1 (up), Black moves +1 (down)
        int direction = color_check ? -1 : 1;

        // Normal forward move
        if (dc == 0) {
            if (dr == direction) {
                return board[new_row][colIdx] == null;
            }
            // First double move
            if (!moved && dr == 2 * direction) {
                int intermediateRow = row + direction;
                return board[intermediateRow][colIdx] == null && board[new_row][colIdx] == null;
            }
        }

        // Capture move
        if (Math.abs(dc) == 1 && dr == direction) {
            Piece target = board[new_row][colIdx];
            if (target != null && target.isColor_check() != color_check) {
                return true;
            }
            // En Passant
            Piece adjacent = board[row][colIdx];
            if (target == null && adjacent instanceof Pawn &&
                    adjacent.isColor_check() != color_check &&
                    ((Pawn) adjacent).movedTwoSquaresLastTurn()) {
                return true;
            }
        }
        return false;
    }

    // Inside Pawn.java
    public boolean canPromote() {
        if (color_check) {
            return row == 0; // White reaches top
        } else {
            return row == 7; // Black reaches bottom
        }
    }
}