package com.photoncat.aiproj2.heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Heuristic3 {

    private int numToWin; // the number of continuous chess to win the game
    private int player; // 1 if play first, otherwise -1

    private static final double GAMMA = 1.5; // weight on enemy's chess, increase this value the ai will tend to block enemy
    private static final double CC = 1.0; // coefficient of continuous chess, when CC->0 continuous chess will not matter
    public static final int SCORE_WHEN_WIN = 999999;

    public Heuristic3(int numToWin, int player) {
        this.numToWin = numToWin;
        this.player = player;
    }

    /**
     * place chess on the board and evaluate the score of whole board based on the difference between previous score
     *
     * @param board     current board with chess last play
     * @param prevScore score of the board when player did not place the chess
     * @param chess     chess last placed on the board
     * @param x         row of the chess last placed on the board
     * @param y         column of the chess last placed on the board
     * @return
     */
    public double evaluateBoard(int[][] board, double prevScore, int chess, int x, int y) {
        // the stretch length on west side of (x, y)
        int w = y - numToWin + 1 < 0 ? y : numToWin - 1;
        // the stretch length on east side of (x, y)
        int e = y + numToWin - 1 < board[0].length - 1 ? numToWin - 1 : board[0].length - 1 - y;
        // the stretch length on north side of (x, y)
        int n = x - numToWin + 1 < 0 ? x : numToWin - 1;
        // the stretch length on south side of (x, y)
        int s = x + numToWin - 1 < board.length - 1 ? numToWin - 1 : board.length - 1 - x;
        // the stretch length on northwest side of (x, y)
        int nw = Math.min(n, w);
        // the stretch length on southeast side of (x, y)
        int se = Math.min(s, e);
        // the stretch length on southwest side of (x, y)
        int sw = Math.min(s, w);
        // the stretch length on northeast side of (x, y)
        int ne = Math.min(n, e);
     
        // evaluate the nearby partial score of (x, y) from different directions if not place chess
        board[x][y] = 0;
        double scoreNotPlace = partialEvaluate(board, x, y, w, e, n, s, nw, se, sw, ne);
        if (Math.abs(scoreNotPlace) == SCORE_WHEN_WIN) {
            return scoreNotPlace;
        }
        // evaluate the nearby partial score of (x, y) from different directions if place chess
        board[x][y] = chess;
        double scorePlaced = partialEvaluate(board, x, y, w, e, n, s, nw, se, sw, ne);
        if (Math.abs(scorePlaced) == SCORE_WHEN_WIN) {
            return scorePlaced;
        }
        return prevScore + (scorePlaced - scoreNotPlace);
    }

    private double partialEvaluate(int[][] board, int x, int y, int w, int e, int n, int s, int nw, int se, int sw, int ne) {
        double[] myFeature = new double[numToWin];
        double[] enemyFeature = new double[numToWin];
        // evaluate from west point to east point
        for (int i = -w; i < e + 1 - numToWin + 1; i++) {
            int sum = 0;
            int count = 0; // number of continuous chess in the window
            int countMax = 0;
            for (int k = 0; k < numToWin; k++) {
                if (board[x][y + i + k] == 0) {
                    countMax = Math.max(countMax, count);
                    count = 0;
                } else if (sum * board[x][y + i + k] >= 0) {
                    sum += board[x][y + i + k];
                    count++;
                } else {
                    sum = 0;
                    i += k - 1;
                    break;
                }
            }
            countMax = Math.max(countMax, count);
            if (Math.abs(sum) == numToWin) {
                return player * Integer.signum(sum) * SCORE_WHEN_WIN;
            }
            if (sum * player > 0) {
                myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            } else if (sum * player < 0) {
                enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            }
        }
        // evaluate from north point to south point
        for (int i = -n; i < s + 1 - numToWin + 1; i++) {
            int sum = 0;
            int count = 0; // number of continuous chess in the window
            int countMax = 0;
            for (int k = 0; k < numToWin; k++) {
                if (board[x + i + k][y] == 0) {
                    countMax = Math.max(countMax, count);
                    count = 0;
                } else if (sum * board[x + i + k][y] >= 0) {
                    sum += board[x + i + k][y];
                    count++;
                } else {
                    sum = 0;
                    i += k - 1;
                    break;
                }
            }
            countMax = Math.max(countMax, count);
            if (Math.abs(sum) == numToWin) {
                return player * Integer.signum(sum) * SCORE_WHEN_WIN;
            }
            if (sum * player > 0) {
                myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            } else if (sum * player < 0) {
                enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            }
        }
        // evaluate from northwest point to southeast point
        for (int i = -nw; i < se + 1 - numToWin + 1; i++) {
            int sum = 0;
            int count = 0; // number of continuous chess in the window
            int countMax = 0;
            for (int k = 0; k < numToWin; k++) {
                if (board[x + i + k][y + i + k] == 0) {
                    countMax = Math.max(countMax, count);
                    count = 0;
                } else if (sum * board[x + i + k][y + i + k] >= 0) {
                    sum += board[x + i + k][y + i + k];
                    count++;
                } else {
                    sum = 0;
                    i += k - 1;
                    break;
                }
            }
            countMax = Math.max(countMax, count);
            if (Math.abs(sum) == numToWin) {
                return player * Integer.signum(sum) * SCORE_WHEN_WIN;
            }
            if (sum * player > 0) {
                myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            } else if (sum * player < 0) {
                enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            }
        }
        // evaluate from southwest point to northeast point
        for (int i = -sw; i < ne + 1 - numToWin + 1; i++) {
            int sum = 0;
            int count = 0; // number of continuous chess in the window
            int countMax = 0;
            for (int k = 0; k < numToWin; k++) {
                if (board[x - i - k][y + i + k] == 0) {
                    countMax = Math.max(countMax, count);
                    count = 0;
                } else if (sum * board[x - i - k][y + i + k] >= 0) {
                    sum += board[x - i - k][y + i + k];
                    count++;
                } else {
                    sum = 0;
                    i += k - 1;
                    break;
                }
            }
            countMax = Math.max(countMax, count);
            if (Math.abs(sum) == numToWin) {
                return player * Integer.signum(sum) * SCORE_WHEN_WIN;
            }
            if (sum * player > 0) {
                myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            } else if (sum * player < 0) {
                enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
            }
        }
        double score = 0;
        for (int i = 1; i < myFeature.length; i++) {
            score += i * (myFeature[i] - GAMMA * enemyFeature[i]);
        }
        return score;
    }

    /**
     * evaluate the board and return the score of possible places sorted
     *
     * @param board
     * @param currScore
     * @param chess
     * @return
     */
    public List<PointScore> getValuablePlaces(int[][] board, double currScore, int chess) {
        List<PointScore> pointScoreList = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = chess;
                    double score = this.evaluateBoard(board, currScore, chess, i, j);
                    pointScoreList.add(new PointScore(i, j, score));
                    board[i][j] = 0;
                }
            }
        }
        if (chess * player > 0) {
            Collections.sort(pointScoreList, Comparator.comparingDouble(PointScore::getScore).reversed());
        } else if (chess * player < 0) {
            Collections.sort(pointScoreList, Comparator.comparingDouble(PointScore::getScore));
        }
        return pointScoreList;
    }

    /**
     * evaluate the score of given board from four directions(row, column, diagonal and anti-diagonal), based on slide windows
     *
     * @param board
     * @return
     */
    public double evaluateWholeBoard(int[][] board) {
        double[] myFeature = new double[numToWin];
        double[] enemyFeature = new double[numToWin];
        // evaluate row
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length - numToWin + 1; j++) {
                int sum = 0;
                int count = 0; // number of continuous chess in the window
                int countMax = 0;
                for (int k = 0; k < numToWin; k++) {
                    if (board[i][j + k] == 0) {
                        countMax = Math.max(countMax, count);
                        count = 0;
                    } else if (sum * board[i][j + k] >= 0) {
                        sum += board[i][j + k];
                        count++;
                    } else { // encounter a different chess, jump window
                        sum = 0;
                        j += k - 1;
                        break;
                    }
                }
                countMax = Math.max(countMax, count);
                if (Math.abs(sum) == numToWin) {
                    return player * Integer.signum(sum) * SCORE_WHEN_WIN;
                }
                if (sum * player > 0) {
                    myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                } else if (sum * player < 0) {
                    enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                }
            }
        }
        // evaluate column
        for (int j = 0; j < board[0].length; j++) {
            for (int i = 0; i < board.length - numToWin + 1; i++) {
                int sum = 0;
                int count = 0; // number of continuous chess in the window
                int countMax = 0;
                for (int k = 0; k < numToWin; k++) {
                    if (board[i + k][j] == 0) {
                        countMax = Math.max(countMax, count);
                        count = 0;
                    } else if (sum * board[i + k][j] >= 0) {
                        sum += board[i + k][j];
                        count++;
                    } else {
                        sum = 0;
                        i += k - 1;
                        break;
                    }
                }
                countMax = Math.max(countMax, count);
                if (Math.abs(sum) == numToWin) {
                    return player * Integer.signum(sum) * SCORE_WHEN_WIN;
                }
                if (sum * player > 0) {
                    myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                } else if (sum * player < 0) {
                    enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                }
            }
        }
        int diagLength = 2 * board.length - 1;
        // evaluate diagonal (/)
        int row = numToWin - 1;
        int col = 0;
        for (int d = numToWin - 1; d < diagLength - numToWin + 1; d++) {
            int i = row;
            int j = col;
            while (i >= numToWin - 1 && j < board[0].length - numToWin + 1) {
                int sum = 0;
                int count = 0; // number of continuous chess in the window
                int countMax = 0;
                for (int k = 0; k < numToWin; k++) {
                    if (board[i - k][j + k] == 0) {
                        countMax = Math.max(countMax, count);
                        count = 0;
                    } else if (sum * board[i - k][j + k] >= 0) {
                        sum += board[i - k][j + k];
                        count++;
                    } else {
                        sum = 0;
                        i -= k - 1;
                        j += k - 1;
                        break;
                    }
                }
                countMax = Math.max(countMax, count);
                if (Math.abs(sum) == numToWin) {
                    return player * Integer.signum(sum) * SCORE_WHEN_WIN;
                }
                if (sum * player > 0) {
                    myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                } else if (sum * player < 0) {
                    enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                }
                i--;
                j++;
            }
            if (row < board.length - 1) {
                row++;
            } else if (col < board[0].length - numToWin) {
                col++;
            }
        }
        // evaluate anti-diagonal (\)
        row = board.length - numToWin;
        col = 0;
        for (int d = numToWin - 1; d < diagLength - numToWin + 1; d++) {
            int i = row;
            int j = col;
            while (i < board.length - numToWin + 1 && j < board[0].length - numToWin + 1) {
                int sum = 0;
                int count = 0; // number of continuous chess in the window
                int countMax = 0;
                for (int k = 0; k < numToWin; k++) {
                    if (board[i + k][j + k] == 0) {
                        countMax = Math.max(countMax, count);
                        count = 0;
                    } else if (sum * board[i + k][j + k] >= 0) {
                        sum += board[i + k][j + k];
                        count++;
                    } else {
                        sum = 0;
                        i += k - 1;
                        j += k - 1;
                        break;
                    }
                }
                countMax = Math.max(countMax, count);
                if (Math.abs(sum) == numToWin) {
                    return player * Integer.signum(sum) * SCORE_WHEN_WIN;
                }
                if (sum * player > 0) {
                    myFeature[sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                } else if (sum * player < 0) {
                    enemyFeature[-sum * player] += Math.pow(numToWin / (numToWin - CC * countMax), 2);
                }
                i++;
                j++;
            }
            if (row > 0) {
                row--;
            } else if (col < board[0].length - numToWin) {
                col++;
            }
        }
        double score = 0;
        for (int i = 1; i < myFeature.length; i++) {
            score += i * (myFeature[i] - GAMMA * enemyFeature[i]);
        }
        return score;
    }
}
