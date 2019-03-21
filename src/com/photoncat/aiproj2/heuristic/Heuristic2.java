package com.photoncat.aiproj2.heuristic;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Board.PieceType;
import com.photoncat.aiproj2.interfaces.Heuristics;

public class Heuristic2 implements Heuristics {
    private enum Direction {
        EAST{
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row, col - len);
            }
        },
        NORTH{
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row + len, col);
            }
        },
        NORTH_EAST {
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row + len, col - len);
            }
        },
        NORTH_WEST {
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row + len, col + len);
            }
        },
        SOUTH {
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row - len, col);
            }
        },
        SOUTH_EAST {
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row - len, col - len);
            }
        },
        SOUTH_WEST {
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row - len, col + len);
            }
        },
        WEST {
            @Override
            public int getByDirLen(Board board, int row, int col, int len) {
                return Direction.getByDirLen(board, row, col + len);
            }
        },
        ;
        //get the piece by the direction and length to current piece
        //len typically is negative.
        public abstract int getByDirLen(Board board, int row, int col, int len);
        private static int getByDirLen(Board board, int row, int col) {
            PieceType piece = board.getPiece(row, col);
            if (piece == null) {
                return -1;          //Return -1 if outta bounds
            }
            switch(piece) {
                case CROSS:
                    return 2;       //Cross as our opponent is indicated as 2
                case CIRCLE:
                    return 1;       //Circle as our computer is indicated as 1
                case NONE:
                default:
                    return 0;       //Empty Cell
            }
        }
    }

    @Override
    public int heuristic(Board board){

        //first check if there is win or draw
        if(board.gameover()){
            if (board.wins()==PieceType.CIRCLE){
                return Integer.MAX_VALUE;
            }
            else if(board.wins()==PieceType.CROSS){
                return Integer.MIN_VALUE;
            }
            return 0;
        }

        int n=board.getSize();
        int m=board.getM();

        if (n==3&&m==3){
            return ThreeByThree(board);
        }
        if(n==3){
            return NbyThree(board);
        }
        if (m==5){
            return NByFive(board);
        }
        if (m==4){
            return NByFour(board);
        }
        else{
            return NbyM(board);
        }
    }
    //evaluate the board when M>5
    private int NbyM(Board board) {
        int sum=0;
        int N=board.getSize();
        for(int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                if (board.getPiece(i,j)==PieceType.NONE){
                    sum=sum+evaluateNByM(board,i,j,PieceType.CIRCLE);
                    sum=sum-evaluateNByM(board,i,j,PieceType.CROSS);
                }
            }
        }
        return sum;

    }

    //evaluate the board when M==3 && N>3
    private int NbyThree(Board board) {
        int sum=0;
        int N=board.getSize();
        for(int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                if (board.getPiece(i,j)==PieceType.NONE){
                    sum=sum+evaluateNByThree(board,i,j,PieceType.CIRCLE);
                    sum=sum-evaluateNByThree(board,i,j,PieceType.CROSS);
                }
            }
        }
        return sum;

    }

    //evaluate the board when M==5
    private int NByFive(Board board) {
        int sum=0;
        int N=board.getSize();
        for(int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                if (board.getPiece(i,j)==PieceType.NONE){
                    sum=sum+evaluateNByFive(board,i,j,PieceType.CIRCLE);
                    sum=sum-evaluateNByFive(board,i,j,PieceType.CROSS);
                }
            }
        }
        return sum;

    }

    //evaluate the board when M=4
    private int NByFour(Board board) {
        int sum=0;
        int N=board.getSize();
        for(int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                if (board.getPiece(i,j)==PieceType.NONE){
                    sum=sum+evaluateNByFour(board,i,j,PieceType.CIRCLE);
                    sum=sum-evaluateNByFour(board,i,j,PieceType.CROSS);
                }
            }
        }
        return sum;
    }


    private int evaluateNByM(Board board, int row, int col, PieceType player) {
        int M=board.getM();
        int score=0;
        int cur=0;
        int opp=0;
        int numOfTwo=0;         // count for third level
        boolean computerFlag;  //if current player is our computer
        if (player==PieceType.CIRCLE){
            cur=1;
            opp=2;
            computerFlag=true;
        }
        else {
            cur=2;
            opp=1;
            computerFlag=false;
        }

        for (Direction d: Direction.values()){
            //top level--------------------------------------
            //01111* or 21111*
            int idx=-1;
            boolean flagFirst=true;    //flag to indicate whether the pattern is valid
            for (;idx>-M;idx--){
                if (d.getByDirLen(board,row,col,idx)!=cur){
                    flagFirst=false;
                    break;
                }
            }
            if (flagFirst){
                if (d.getByDirLen(board,row,col,idx)==0){
                    score+=3000000;     //01111*
                }
                else {
                    score+=25000;       //21111*
                }
                if (computerFlag){
                    score+=500;
                }
                continue;
            }
            //111*1 or 11*11 etc
            boolean tierFlag=true;      //flag to indicate whether we find a valid pattern in this level
            int loopTime=(M+1)/2-1;     // all the position * can be inserted at
            int leftEnd=-M+1;
            int rightEnd=2;
            for (int l=0;l<loopTime;l++){
                int left=-1;
                int right=1;
                boolean flag=true;      //flag to indicate whether the pattern is valid
                for (;left>leftEnd;left--){
                    if (d.getByDirLen(board,row,col,left)!=cur){
                        flag=false;
                        break;
                    }
                }
                if (!flag){             //pattern on the left side does not exist, go to next * position
                    leftEnd++;
                    rightEnd++;
                    continue;
                }
                for (;right<rightEnd;right++){
                    if (d.getByDirLen(board,row,col,right)!=cur){
                        flag=false;
                        break;
                    }
                }
                if (!flag){             //pattern on the right side does not exist, go to next * position
                    leftEnd++;
                    rightEnd++;
                    continue;
                }
                score+=24000;
                if (computerFlag){
                    score+=500;
                }
                tierFlag=false;         //we find a valid pattern
                break;
            }

            if (!tierFlag){             //we find a valid pattern, go the next direciton on the outerloop
                continue;
            }

            //second level--------------------------------------
            tierFlag=true;
            //update leftEnd and rightEnd
            leftEnd=-(M-2);
            rightEnd=2;
            for (int l=0;l<loopTime;l++){           //loopTime is the same, don't need to consider 211*12

                //when l==0, we only need to consider one side like 2111*
                if (l==0){
                    int leftend=-(M-1);             //different leftend used specifically for l==0
                    boolean secondflag=true;
                    for (int j=-1;j>leftend;j--){
                        if (d.getByDirLen(board,row,col,j)!=cur){
                            secondflag=false;
                            break;
                        }
                    }
                    if (secondflag){
                        if (d.getByDirLen(board,row,col,1)==0){
                            score+=750;
                            if (d.getByDirLen(board,row,col,leftend)==0){
                                score+=10250;           //0111*0
                                if (computerFlag){
                                    score+=500;
                                }
                            }
                        }
                        else {
                            if (d.getByDirLen(board,row,col,leftend)==0){
                                score+=500;             //0111*1
                            }
                        }
                        tierFlag=false;
                        break;
                    }
                    if (d.getByDirLen(board,row,col,-1)==0){
                        secondflag=true;
                        for (int j=-2;j>=leftend;j--){
                            if (d.getByDirLen(board,row,col,j)!=cur){
                                secondflag=false;
                                break;
                            }
                        }
                        if (secondflag){
                            score+=350;                //1110*
                            tierFlag=false;
                            break;
                        }
                    }
                }

                //11*1 etc, when l!=0
                else{
                    int left=-1;
                    int right=1;
                    boolean flag=true;
                    for (;left>leftEnd;left--){
                        if (d.getByDirLen(board,row,col,left)!=cur){
                            flag=false;
                            break;
                        }
                    }
                    if (!flag){
                        leftEnd++;
                        rightEnd++;
                        continue;
                    }
                    for (;right<rightEnd;right++){
                        if (d.getByDirLen(board,row,col,right)!=cur){
                            flag=false;
                            break;
                        }
                    }
                    if (!flag){
                        leftEnd++;
                        rightEnd++;
                        continue;
                    }
                    score+=600;
                    if (d.getByDirLen(board,row,col,leftEnd)==0&&d.getByDirLen(board,row,col,rightEnd)==0){
                        score+=9600;                //011*10
                        if (computerFlag){
                            score+=500;
                        }
                        tierFlag=false;
                        break;
                    }
                    if (d.getByDirLen(board,row,col,leftEnd)!=cur&&d.getByDirLen(board,row,col,rightEnd)!=cur){
                        tierFlag=false;             //211*12
                        break;
                    }
                    else {
                        score+=700;                 //211*10 or 011*12
                        tierFlag=false;
                        break;
                    }
                }
            }
            if (!tierFlag){
                continue;
            }

            //third level-----------------------------------
            leftEnd=-(M-2);     //update the leftEnd
            for (int j=-1;j>leftEnd;j--){
                if (d.getByDirLen(board,row,col,leftEnd)!=cur){
                    break;
                }
                if (d.getByDirLen(board,row,col,leftEnd)==0){
                    numOfTwo++;
                }
            }

            //fourth level-----------------------------------
            int lonewolf=0;
            for(int k=-(M-1);k<=0;k++){
                int temp=0;
                for (int l = 0; l <= (M-1); l++) {
                    if (d.getByDirLen(board,row,col,k+l)==cur){
                        temp++;
                    }
                    else {
                        break;
                    }
                }
                lonewolf+=temp;
            }
            score+=lonewolf*15;
        }

        if (numOfTwo>=2){
            score+=12000;
            if (computerFlag){
                score+=200;
            }
        }

        return score;
    }
    private int evaluateNByFive(Board board, int row, int col, PieceType player) {
        int score=0;
        int two=0;      //used to count two live 2
        int cur=0;
        int opp=0;
        boolean computerFlag;  //if current player is our computer
        if (player==PieceType.CIRCLE){
            cur=1;
            opp=2;
            computerFlag=true;
        }
        else {
            cur=2;
            opp=1;
            computerFlag=false;
        }
        // iterate through eight directions
        for (Direction d: Direction.values()){
            //live4 01111*
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur&&
                    d.getByDirLen(board,row,col,-4)==cur&&
                    d.getByDirLen(board,row,col,-5)==0){
                        score+=3000000;
                        if (computerFlag){
                            score+=5000;
                        }
                        continue;
            }

            //dead4 21111*
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur&&
                    d.getByDirLen(board,row,col,-4)==cur&&
                    (d.getByDirLen(board,row,col,-5)==opp||d.getByDirLen(board,row,col,-5)==-1)){
                        score+=25000;
                        if (computerFlag){
                            score+=500;
                        }
                         continue;
            }
            //dead4 111*1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur&&
                    d.getByDirLen(board,row,col,1)==cur){
                        score+=24000;
                        if (computerFlag){
                            score+=500;
                        }
                        continue;
            }
            //dead4 11*11
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,1)==cur&&
                    d.getByDirLen(board,row,col,2)==cur){
                    score+=23000;
                    if (computerFlag){
                        score+=500;
                    }
                    continue;
            }

            //live3 111*0
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur){

                        if (d.getByDirLen(board,row,col,1)==0){
                            score+=750;         //2111*0
                            if (d.getByDirLen(board,row,col,-4)==0){
                                score+=10250;   //0111*0
                                if (computerFlag){
                                    score+=500;
                                }
                            }
                        }
                        if ((   d.getByDirLen(board,row,col,1)==opp||
                                d.getByDirLen(board,row,col,1)==-1)&&
                                d.getByDirLen(board,row,col,-4)==0){
                                score+=500;      //0111*2
                        }

                        continue;
            }
            //dead3 1110*
            if (    d.getByDirLen(board,row,col,-1)==0&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur&&
                    d.getByDirLen(board,row,col,-4)==cur){
                        score+=350;
                        continue;
            }


            //dead 3 11*1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,1)==cur){

                        score+=600;

                        //011*10
                        if (    d.getByDirLen(board,row,col,-3)==0&&
                                d.getByDirLen(board,row,col,2)==0){
                                    score+=9600;
                                    if (computerFlag){
                                        score+=500;
                                    }
                                    continue;
                        }
                        //211*12
                        if ((   d.getByDirLen(board,row,col,-3)==opp|| d.getByDirLen(board,row,col,-3)==-1)&&(
                                d.getByDirLen(board,row,col,2)==opp || d.getByDirLen(board,row,col,2)==-1)){
                                    continue;
                        }
                        //011*12 or 211*10
                        else {
                            score+=700;
                            continue;
                        }

            }

            // two live 2
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==0){
                        two++;
            }

            int lonewolf=0;
            //check those single ones in each direction
            for(int k=-4;k<=0;k++){
                int temp=0;
                for (int l = 0; l <= 4; l++) {
                    if (d.getByDirLen(board,row,col,k+l)==cur){
                        temp++;
                    }
                    else {
                        break;
                    }
                }
                lonewolf+=temp;
            }
            score+=lonewolf*15;
        }
        // two live 2 is powerful, its score is equivalent to live3
        if (two>=2){
            score+=12000;
            if (computerFlag){
                score+=200;
            }
        }
        return score;
    }
    private int evaluateNByFour(Board board, int row, int col, PieceType player) {
        int score=0;
        int cur=0;
        int opp=0;
        int one=0;             //used to count two live 1
        boolean computerFlag;  //if current player is our computer
        if (player==PieceType.CIRCLE){
            cur=1;
            opp=2;
            computerFlag=true;
        }
        else {
            cur=2;
            opp=1;
            computerFlag=false;
        }
        for (Direction d: Direction.values()){
            //live 3 0111*
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur&&
                    d.getByDirLen(board,row,col,-4)==0){
                        score+=3000000;
                        if (computerFlag){
                            score+=5000;
                        }
                        continue;
            }
            //dead 3 2111*
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==cur&&
                    (d.getByDirLen(board,row,col,-4)==opp||d.getByDirLen(board,row,col,-4)==-1)){
                        score+=25000;
                        if (computerFlag){
                            score+=500;
                        }
                        continue;
            }
            //dead 3  11*1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,1)==cur){
                        score+=25000;
                        if (computerFlag){
                            score+=500;
                        }
                        continue;
            }
            //live2 11*0
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur){

                if (d.getByDirLen(board,row,col,1)==0){
                    score+=750;         //211*0
                    if (d.getByDirLen(board,row,col,-3)==0){
                        score+=39250;   //011*0
                        if (computerFlag){
                            score+=50000;
                        }
                    }
                }
                if ((   d.getByDirLen(board,row,col,1)==opp||
                        d.getByDirLen(board,row,col,1)==-1)&&
                        d.getByDirLen(board,row,col,-3)==0){
                            score+=500;      //011*2
                }
                continue;
            }
            //dead 2 1*1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,1)==cur) {
                        score += 600;
                        if (d.getByDirLen(board, row, col, -2) == 0 &&
                            d.getByDirLen(board, row, col, 2) == 0) {
                                score += 25000;     //01*10
                                if (computerFlag){
                                    score+=50000;
                                }
                                continue;
                }
                if ((   d.getByDirLen(board, row, col, -2) == opp || d.getByDirLen(board, row, col, -2) == -1) && (
                        d.getByDirLen(board, row, col, 2) == opp  || d.getByDirLen(board, row, col, 2) == -1)) {
                            continue;           //21*12
                } else {
                    score += 700;               // 01*12 or 21*10
                    continue;
                }
            }

            //two ones
            if (    d.getByDirLen(board, row, col, -1) == cur &&
                    d.getByDirLen(board, row, col, 1) == 0) {
                        one++;
            }

            //
            int lonewolf=0;
            for(int k=-4;k<=0;k++){
                int temp=0;
                for (int l = 0; l <= 4; l++) {
                    if (d.getByDirLen(board,row,col,k+l)==cur){
                        temp++;
                    }
                    else {
                        break;
                    }
                }
                lonewolf+=temp;
            }
            score+=lonewolf*15;
        }

        if (one>=2){
            score+=20000;
            if (computerFlag){
                score+=500;
            }
        }
        return score;
    }
    private int evaluateNByThree(Board board, int row, int col, PieceType player) {
        int score=0;
        int cur=0;
        int opp=0;
        boolean computerFlag;  //if current player is our computer
        if (player==PieceType.CIRCLE){
            cur=1;
            opp=2;
            computerFlag=true;
        }
        else {
            cur=2;
            opp=1;
            computerFlag=false;
        }
        for (Direction d: Direction.values()){
            //live 2 011*
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    d.getByDirLen(board,row,col,-3)==0){
                score+=3000000;
                if (computerFlag){
                    score+=5000;
                }
                continue;
            }
            //dead 2 211*
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==cur&&
                    (d.getByDirLen(board,row,col,-3)==opp||d.getByDirLen(board,row,col,-5)==-1)){
                score+=25000;
                if (computerFlag){
                    score+=500;
                }
                continue;
            }
            //dead 2 1*1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,1)==cur){
                score+=24000;
                if (computerFlag){
                    score+=500;
                }
                continue;
            }
            //live 1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)==0){
                score+=10250;
                if (computerFlag){
                    score+=500;
                }
                continue;
            }
            //dead 1
            if (    d.getByDirLen(board,row,col,-1)==cur&&
                    d.getByDirLen(board,row,col,-2)!=cur){
                score+=750;
                if (computerFlag){
                    score+=500;
                }
                continue;
            }

        }
        return score;
    }

    //evaluate the board when N==3 AND M==3
    private int ThreeByThree(Board board) {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLineThreeByThree(0, 0, 0, 1, 0, 2,board);  // row 0
        score += evaluateLineThreeByThree(1, 0, 1, 1, 1, 2,board);  // row 1
        score += evaluateLineThreeByThree(2, 0, 2, 1, 2, 2,board);  // row 2
        score += evaluateLineThreeByThree(0, 0, 1, 0, 2, 0,board);  // col 0
        score += evaluateLineThreeByThree(0, 1, 1, 1, 2, 1,board);  // col 1
        score += evaluateLineThreeByThree(0, 2, 1, 2, 2, 2,board);  // col 2
        score += evaluateLineThreeByThree(0, 0, 1, 1, 2, 2,board);  // diagonal
        score += evaluateLineThreeByThree(0, 2, 1, 1, 2, 0,board);  // alternate diagonal
        return score;
    }
    private int evaluateLineThreeByThree(int row1, int col1, int row2, int col2, int row3, int col3,Board board) {
        int score = 0;

        // First cell
        PieceType cur=PieceType.CIRCLE;
        PieceType opp=PieceType.CROSS;

        if (board.getPiece(row1,col1) == cur) {
            score = 1;
        } else if (board.getPiece(row1,col1) == opp) {
            score = -1;
        }

        // Second cell
        if (board.getPiece(row2,col2) == cur) {
            if (score == 1) {           // cell1 is 1
                score = 10;
            } else if (score == -1) {   // cell1 is 2
                return 0;
            } else {                    // cell1 is empty
                score = 1;
            }
        } else if (board.getPiece(row2,col2) == opp) {
            if (score == -1) {          // cell1 is 2
                score = -10;
            } else if (score == 1) {    // cell1 is 1
                return 0;
            } else {                    // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board.getPiece(row3,col3) == cur) {
            if (score > 0) {            // cell1 and/or cell2 is 1
                score *= 10;
            } else if (score < 0) {     // cell1 and/or cell2 is 2
                return 0;
            } else {                    // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board.getPiece(row3,col3) == opp) {
            if (score < 0) {            // cell1 and/or cell2 is 2
                score *= 10;
            } else if (score > 1) {     // cell1 and/or cell2 is 1
                return 0;
            } else {                    // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }
}
