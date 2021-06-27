package sample.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private char[][] board;
    private char[][] cloneBoard = new char[31][31];
    private int pacmanXIndex = 1, pacmanYIndex = 0, scoreBallCounter;
    private final HashMap<String, Integer[]> ghostsIndexes = new HashMap<>();

    public Board() {
        makeBoard();
        cloneBoard();
    }

    public char[][] getBoard() {
        return board;
    }

    public HashMap<String, Integer[]> getGhostsIndexes() {
        return ghostsIndexes;
    }

    public int getPacmanXIndex() {
        return pacmanXIndex;
    }

    public void setPacmanXIndex(int pacmanXIndex, char direction) {
        this.pacmanXIndex = pacmanXIndex;
        board[pacmanYIndex][pacmanXIndex] = direction;
    }

    public int getPacmanYIndex() {
        return pacmanYIndex;
    }

    public void setPacmanYIndex(int pacmanYIndex, char direction) {
        this.pacmanYIndex = pacmanYIndex;
        board[pacmanYIndex][pacmanXIndex] = direction;
    }

    public int getScoreBallCounter() {
        return scoreBallCounter;
    }

    public void resetPacmanPosition() {
        pacmanXIndex = 1;
        pacmanYIndex = 0;
    }

    public void moveGhost(String ghostColor, char direction) {
        Integer[] indexes = ghostsIndexes.get(ghostColor);
        if (direction == 'u')
            ghostsIndexes.replace(ghostColor, new Integer[]{indexes[0], indexes[1] - 1});
        else if (direction == 'd')
            ghostsIndexes.replace(ghostColor, new Integer[]{indexes[0], indexes[1] + 1});
        else if (direction == 'r')
            ghostsIndexes.replace(ghostColor, new Integer[]{indexes[0] + 1, indexes[1]});
        else if (direction == 'l')
            ghostsIndexes.replace(ghostColor, new Integer[]{indexes[0] - 1, indexes[1]});
    }

    public boolean isBlockWall(int x, int y) {
        return board[y][x] == '1';
    }

    public void makeBoard() {
        board = arrayInitializer(15, 15);
        int seed = LocalDateTime.now().getSecond();
        Random rand = new Random(seed);
        for (int i = 1; i < 2 * 15; i += 2) {
            rand.setSeed(seed + i);
            for (int j = 1; j < 2 * 15; j += 2) {
                if (j != 2 * 15 - 1 && i != 2 * 15 - 1) {
                    if (rand.nextInt(100) % 2 == 0) {
                        scoreBallCounter++;
                        board[i][j + 1] = '*';
                        board[i + 1][j] = board[i + 1][j + 1] = '1';
                    } else {
                        scoreBallCounter++;
                        board[i + 1][j] = '*';
                        board[i][j + 1] = board[i + 1][j + 1] = '1';
                    }
                } else if (j == 2 * 15 - 1 && i != 2 * 15 - 1) {
                    scoreBallCounter++;
                    board[i + 1][j] = '*';
                } else if (j != 2 * 15 - 1 && i == 2 * 15 - 1) {
                    scoreBallCounter++;
                    board[i][j + 1] = '*';
                }
            }
        }
        randomRemove();
    }

    public char[][] arrayInitializer(int row, int column) {
        char[][] array = new char[2 * row + 1][2 * column + 1];
        array[2 * row][2 * column] = array[0][0] = '1';
        array[0][1] = 's';
        ghostsIndexes.put("red", new Integer[]{29,3});
        ghostsIndexes.put("blue", new Integer[]{29,15});
        ghostsIndexes.put("pink", new Integer[]{3,29});
        ghostsIndexes.put("yellow", new Integer[]{15,29});
        //initializing first and last rows
        for (int i = 2; i <= 2 * column; i++) {
            array[0][i] = '1';
            array[2 * row][i - 2] = '1';
        }
        //initializing first and last columns
        for (int i = 1; i < 2 * row; i++) {
            array[i][0] = '1';
            array[i][2 * column] = '1';
        }
        //initializing rest of the maze
        for (int i = 1; i < 2 * row; i++) {
            for (int j = 1; j < 2 * column; j++) {
                if (j % 2 == 1) {
                    scoreBallCounter++;
                    array[i][j] = '*';
                } else
                    array[i][j] = '#';
            }
        }
        return array;
    }

    public void display() {
        for (char[] sub_arr : board) {
            StringBuilder temp_str = new StringBuilder();
            for (char character : sub_arr) {
                temp_str.append(character);
            }
            System.out.println(temp_str);
        }
        System.out.println();
    }

    public void randomRemove() {
        int seed = LocalDateTime.now().getSecond(), row, column;
        Random rand = new Random(seed);
        int counter = 0;
        for (int i = 1; i <= 300; i += 2) {
            row = rand.nextInt() % 30;
            column = rand.nextInt() % 30;
            row = row >=0 ? row : -1 * row;
            column = column >=0 ? column : -1 * column;
            row++;
            column++;
            if (board[row][column] == '1')
                if (counter < 4) {
                    board[row][column] = '#';
                    counter++;
                } else
                    board[row][column] = '*';
        }
    }

    public void cloneBoard() {
        for(int i = 0; i < board.length; i++)
            cloneBoard[i] = board[i].clone();
    }


    public void resetBoard() {
        pacmanXIndex = 1;
        pacmanYIndex = 0;
        ghostsIndexes.put("red", new Integer[]{29,3});
        ghostsIndexes.put("blue", new Integer[]{29,15});
        ghostsIndexes.put("pink", new Integer[]{3,29});
        ghostsIndexes.put("yellow", new Integer[]{15,29});
        for(int i = 0; i < board.length; i++)
            board[i] = cloneBoard[i].clone();
    }
}
