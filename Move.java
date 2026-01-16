public class Move
{
    private Piece piece;
    private char fromCol;
    private int fromRow;
    private char toCol;
    private int toRow;
    private boolean isCapture;
    private boolean isCastling;
    private boolean isCheck;
    private boolean isCheckmate;

    public Move(Piece piece, char fromCol, int fromRow,
                char toCol, int toRow, boolean isCapture)
    {
        this.piece = piece;
        this.fromCol = fromCol;
        this.fromRow = fromRow;
        this.toCol = toCol;
        this.toRow = toRow;
        this.isCapture = isCapture;
        this.isCastling = false;
        this.isCheck = false;
        this.isCheckmate = false;
    }

    public void setCastling(boolean kingSide)
    {
        isCastling = true;
        toCol = kingSide ? 'g' : 'c';
    }

    public void setCheck(boolean check)
    {
        isCheck = check;
    }

    public void setCheckmate(boolean checkmate)
    {
        isCheckmate = checkmate;
    }

    public String toString()
    {
        if (isCastling)
        {
            return toCol == 'g' ? "O-O" : "O-O-O";
        }

        StringBuilder sb = new StringBuilder();

        if (!(piece instanceof Pawn))
        {
            sb.append(piece.getClass().getSimpleName().charAt(0));
        }

        if (isCapture) sb.append("x");

        sb.append(toCol).append(8 - toRow); // chess rows 8→1

        if (isCheckmate) sb.append("#");
        else if (isCheck) sb.append("+");

        return sb.toString();
    }
}
