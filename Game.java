import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game
{
    private GameListener listener;

    private Pawn enPassantTarget = null;
    private final Board board;
    private boolean white_turn;

    public void setGameListener(GameListener listener)
    {
        this.listener = listener;
    }
    public Game(Board board, boolean white_turn) {
        this.board = board;
        this.white_turn = white_turn;
        this.board.setupPieces();
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean is_white_turn()
    {
        return white_turn;
    }

    public boolean move(int f_row, char f_col, int t_row, char t_col)
    {
        Piece piece = board.getPiece(f_row, f_col);
        // Check if there is no piece on the selected square
        if(piece == null)
        {
            return false;
        }

        //Check if it is the whites turn
        if(piece.isColor_check() != white_turn)
        {
            return false;
        }

        // Check if player is trying to play into the same square
        if(f_row == t_row && f_col == t_col)
        {
            return false;
        }

        // check if the move is out of rules
        if(!piece.move_check(t_row, t_col, board.getBoard()))
        {
            return false;
        }

        // CASTLING
        if (piece instanceof King && Math.abs(t_col - f_col) == 2) {

            King king = (King) piece;

            if (!board.canCastle(king, t_col)) return false;

            board.castle(king, t_col);
            white_turn = !white_turn;
            checkGameEnd();
            return true;
        }

        int old_row = piece.getRow();
        char old_col = piece.getCol();
        // move the piece to that location
        // And capture the piece if there is any on it
        Piece captured = null;

        // Check for en passant capture
        if (piece instanceof Pawn && Math.abs(t_col - f_col) == 1 &&
                board.getPiece(t_row, t_col) == null) {

            Piece target = board.getPiece(f_row, t_col);
            if (target instanceof Pawn && target.isColor_check() != piece.isColor_check()) {
                captured = target;
                board.getBoard()[f_col][t_col - 'a'] = null; // remove captured pawn
            }
        }

        // Normal move or en passant
        if (captured == null) {
            captured = board.simulateMove(piece, t_row, t_col);
        } else {
            board.movePiece(piece, t_row, t_col);
        }

        // Track en passant opportunity
        if (piece instanceof Pawn && Math.abs(t_row - f_row) == 2) {
            enPassantTarget = (Pawn) piece;
        } else {
            enPassantTarget = null;
        }

        if(board.isKingInCheck(piece.isColor_check()))
        {
            board.undoMove(piece, old_row, old_col, captured);
        }

        if(captured != null)
        {
            System.out.println("Captured: "+ captured.getClass().getSimpleName());
        }
        // After move executed
        if (piece instanceof Pawn)
        {
            Pawn p = (Pawn) piece;
            if (p.canPromote())
            {
                char choice;
                Scanner sc = new Scanner(System.in);
                System.out.print("Please pick the promotion:\n'Q' for Queen,\n'R' for Rook,\n'B' for Bishop,\n'K' for Knight\n");
                do
                {
                    System.out.print("Choice: ");
                    choice = sc.next().charAt(0);
                    if(choice != 'Q' && choice != 'R'  && choice != 'B' && choice != 'K' && choice != 'q' && choice != 'r' && choice != 'b' && choice != 'k')
                        System.out.println("Invalid choice.\n");

                }while(choice != 'Q' && choice != 'R'  && choice != 'B' && choice != 'K' && choice != 'q' && choice != 'r' && choice != 'b' && choice != 'k');
                switch(choice)
                {
                    case 'Q': case 'q': promotePawn(p, 'Q'); // default to Queen
                                        break;
                    case 'R': case 'r': promotePawn(p, 'R');
                                        break;
                    case 'B': case 'b': promotePawn(p, 'B');
                                        break;
                    case 'K': case 'k': promotePawn(p, 'K');
                                        break;
                    default: promotePawn(p, 'Q');
                }
            }
        }

        boolean isCaptureMove = captured != null;

        // Create move object
        Move m = new Move(piece, f_col, f_row, t_col, t_row, isCaptureMove);

        // Castling
        if (piece instanceof King && Math.abs(t_col - f_col) == 2) {
            m.setCastling(t_col > f_col); // kingSide or queenSide
        }

        // Check / checkmate
        if (board.isKingInCheck(!piece.isColor_check()))
        {
            if (isCheckmate(!piece.isColor_check()))
            {
                m.setCheckmate(true);
            }
            else m.setCheck(true);
        }

        // Print move
        System.out.println(m.toString());

        // Change turns
        white_turn = !white_turn;
        checkGameEnd();
        return true;
    }

    public boolean hasAnyLegalMove(boolean isWhite)
    {

        Piece[][] boardArray = board.getBoard();

        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                Piece piece = boardArray[r][c];
                if (piece == null || piece.isColor_check() != isWhite)
                {
                    continue;
                }

                // Try every square on the board
                for (int newRow = 0; newRow < 8; newRow++)
                {
                    for (char newCol = 'a'; newCol <= 'h'; newCol++)
                    {

                        if (!piece.move_check(newRow, newCol, boardArray))
                        {
                            continue;
                        }

                        int oldRow = piece.getRow();
                        char oldCol = piece.getCol();

                        Piece captured = board.simulateMove(piece, newRow, newCol);

                        boolean stillInCheck = board.isKingInCheck(isWhite);

                        board.undoMove(piece, oldRow, oldCol, captured);

                        if (!stillInCheck)
                        {
                            return true; // Escape found
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(boolean isWhite)
    {
        if (!board.isKingInCheck(isWhite))
        {
            return false;
        }
        return !hasAnyLegalMove(isWhite);
    }

    private void checkGameEnd()
    {
        boolean currentPlayer = !white_turn;

        if (isCheckmate(!currentPlayer))
        {
            String winner = currentPlayer ? "White" : "Black";
            String message = winner + " WINS by CHECKMATE!";
            System.out.println(message);
            if (listener != null) listener.onGameEnd(message);
            System.exit(0);
        }
        else if (isStalemate(!currentPlayer))
        {
            String message = "Game is a STALEMATE!";
            System.out.println(message);
            if (listener != null) listener.onGameEnd(message);
            System.exit(1);
        }
        else if (board.isKingInCheck(!currentPlayer))
        {
            String message = (currentPlayer ? "Black" : "White") + " is in CHECK!";
            System.out.println(message);
            if (listener != null) listener.onCheck(message);
        }
    }

    public void promotePawn(Pawn pawn, char choice)
    {
        boolean isWhite = pawn.isColor_check();
        int row = pawn.getRow();
        char col = pawn.getCol();

        // Enhanced Switch
        Piece newPiece = switch (choice)
        {
            case 'Q' -> new Queen(row, col, isWhite);
            case 'R' -> new Rook(row, col, isWhite);
            case 'B' -> new Bishop(row, col, isWhite);
            case 'N' -> new Knight(row, col, isWhite);
            default -> new Queen(row, col, isWhite);
        };

        board.getBoard()[row][col - 'a'] = newPiece;
    }

    public boolean isStalemate(boolean isWhite)
    {
        return !board.isKingInCheck(isWhite) && !hasAnyLegalMove(isWhite);
    }


}
