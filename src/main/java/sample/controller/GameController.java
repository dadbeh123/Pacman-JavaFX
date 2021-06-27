package sample.controller;

import sample.model.Board;
import sample.view.GameMenu;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class GameController {
    private Board board;
    private int score, lifePoint, eatenGhosts;
    private boolean isScoreBallConsumed;

    public GameController() {
        eatenGhosts = 0;
        isScoreBallConsumed = false;
        lifePoint = 3;
        score = 0;
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public int getLifePoint() {
        return lifePoint;
    }

    public int getScore() {
        return score;
    }

    //Keyboard key handle
    public void handleKey(char key) {
        if (key == 'd' &&  (board.getPacmanYIndex() + 1) != 31 && !board.isBlockWall(getBoard().getPacmanXIndex(), board.getPacmanYIndex() + 1)) {
            if (board.getBoard()[getBoard().getPacmanYIndex() + 1][getBoard().getPacmanXIndex()] == '*') {
                score += 5;
                GameMenu.getInstance().playSound("Chomp");
            }
            if (board.getBoard()[getBoard().getPacmanYIndex() + 1][getBoard().getPacmanXIndex()] == '#')
                GameMenu.getInstance().handleEscapingGhosts();
            board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex()] = '0';
            board.setPacmanYIndex(board.getPacmanYIndex() + 1, 's');
        } else if (key == 'u' && (board.getPacmanYIndex() - 1) != -1 && !board.isBlockWall(getBoard().getPacmanXIndex(), board.getPacmanYIndex() - 1)) {
            if (board.getBoard()[getBoard().getPacmanYIndex() - 1][getBoard().getPacmanXIndex()] == '*') {
                score += 5;
                GameMenu.getInstance().playSound("Chomp");
            }
            if (board.getBoard()[getBoard().getPacmanYIndex() - 1][getBoard().getPacmanXIndex()] == '#')
                GameMenu.getInstance().handleEscapingGhosts();
            board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex()] = '0';
            board.setPacmanYIndex(board.getPacmanYIndex() - 1, 'n');
        } else if (key == 'r' && (board.getPacmanXIndex() + 1) != 31 && !board.isBlockWall(getBoard().getPacmanXIndex() + 1, board.getPacmanYIndex())) {
            if (board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex() + 1] == '*') {
                score += 5;
                GameMenu.getInstance().playSound("Chomp");
            }
            if (board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex() + 1] == '#')
                GameMenu.getInstance().handleEscapingGhosts();
            board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex()] = '0';
            board.setPacmanXIndex(board.getPacmanXIndex() + 1, 'e');
        } else if (key == 'l' && (board.getPacmanXIndex() - 1) != -1 && !board.isBlockWall(getBoard().getPacmanXIndex() - 1, board.getPacmanYIndex())) {
            if (board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex() - 1] == '*') {
                score += 5;
                GameMenu.getInstance().playSound("Chomp");
            }
            if (board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex() - 1] == '#')
                GameMenu.getInstance().handleEscapingGhosts();
            board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex()] = '0';
            board.setPacmanXIndex(board.getPacmanXIndex() - 1, 'w');
        }
        try {
            checkGhostCollision();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isGameWin()) {
            board.resetBoard();
        }
        GameMenu.getInstance().renderImages(GameMenu.getInstance().gridPane);
    }

    //Moving ghosts in random directions
    public void moveGhosts() throws IOException {
        int seed = LocalDateTime.now().getSecond();
        ArrayList<Character> directions = new ArrayList<>();
        Random rand = new Random(seed);
        for (Map.Entry<String, Integer[]> set : board.getGhostsIndexes().entrySet()) {
            directions.clear();
            Integer[] indexes = set.getValue();
            if (indexes[1] > 0 && !board.isBlockWall(indexes[0], indexes[1] - 1))
                directions.add('u');
            if (indexes[1] < 30 && !board.isBlockWall(indexes[0], indexes[1] + 1))
                directions.add('d');
            if (indexes[0] < 30 && !board.isBlockWall(indexes[0] + 1, indexes[1]))
                directions.add('r');
            if (indexes[0] > 0 && !board.isBlockWall(indexes[0] - 1, indexes[1]))
                directions.add('l');
            int movement = rand.nextInt() % directions.size();
            movement = movement >= 0 ? movement : -1 * movement;
            board.moveGhost(set.getKey(), directions.get(movement));
        }
        checkGhostCollision();
        GameMenu.getInstance().renderImages(GameMenu.getInstance().gridPane);
    }

    public void checkGhostCollision() throws IOException {
        for (Map.Entry<String, Integer[]> set : board.getGhostsIndexes().entrySet()) {
            Integer[] indexes = set.getValue();
            if (board.getPacmanXIndex() == indexes[0] && board.getPacmanYIndex() == indexes[1]) {
                GameMenu.getInstance().renderImages(GameMenu.getInstance().gridPane);
                if (isScoreBallConsumed) {
                    eatenGhosts++;
                    score += 200 * eatenGhosts;
                    board.getGhostsIndexes().put(set.getKey(), new Integer[]{1, 0});
                } else {
                    GameMenu.getInstance().playSound("Death");
                    lifePoint--;
                    if (isGameLost()) {
                        if (RegisterController.currentUser != null) RegisterController.getInstance().updateScore(score);
                        GameMenu.getInstance().setAfterGameScene();
                    } else {
                        board.getBoard()[getBoard().getPacmanYIndex()][getBoard().getPacmanXIndex()] = '0';
                        board.resetPacmanPosition();
                        GameMenu.getInstance().renderImages(GameMenu.getInstance().gridPane);
                    }
                }
            }
        }
    }

    public boolean isGameLost() {
        return lifePoint == 0;
    }

    public  boolean isGameWin() {
        return score / 5 == board.getScoreBallCounter();
    }

    public void setScoreBallConsumed(boolean scoreBallConsumed) {
        isScoreBallConsumed = scoreBallConsumed;
    }

    public boolean isScoreBallConsumed() {
        return isScoreBallConsumed;
    }
}
