package com.photoncat.aiproj2.heuristic;

import com.photoncat.aiproj2.interfaces.Board;
import com.photoncat.aiproj2.interfaces.Board.PieceType;
import com.photoncat.aiproj2.interfaces.Heuristics;

public class Heuristic2 implements Heuristics {
    @Override
    public int heuristic(Board board){
        int n=board.getSize();
        int m=board.getM();

        if (n==3&&m==3){
            return ThreeByThree(board);
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
//        if ()
//        return 0;
    }

    private int NbyM(Board board) {
        if (checkWin(board,PieceType.CROSS)){
            return 50000000;
        }
        else if (checkWin(board,PieceType.CIRCLE)){
            return -50000000;
        }
        else {
            int sum=0;
            int N=board.getSize();
            for(int i=0;i<N;i++){
                for (int j=0;j<N;j++){
                    if (board.getPiece(i,j)==PieceType.NONE){
                        sum=sum+evaluateNByM(board,i,j,PieceType.CROSS);
                        sum=sum-evaluateNByM(board,i,j,PieceType.CIRCLE);
                    }

                }
            }
            return sum;
        }
    }
    private int NByFive(Board board) {
        if (checkWin(board,PieceType.CROSS)){
            return 10000000;
        }
        else if (checkWin(board,PieceType.CIRCLE)){
            return -10000000;
        }
        else {
            int sum=0;
            int N=board.getSize();
            for(int i=0;i<N;i++){
                for (int j=0;j<N;j++){
                    if (board.getPiece(i,j)==PieceType.NONE){
//                         sum=sum+evaluateNByFive(board,i,j,PieceType.CROSS);
//                         sum=sum-evaluateNByFive(board,i,j,PieceType.CIRCLE);
//                         sum=sum+evaluateNByFive2(board,i,j,PieceType.CROSS);
//                         sum=sum-evaluateNByFive2(board,i,j,PieceType.CIRCLE);
//                         sum=sum+evaluateNByM(board,i,j,PieceType.CROSS);
//                         sum=sum-evaluateNByM(board,i,j,PieceType.CIRCLE);
                        sum=sum+evaluateNByFive3(board,i,j,PieceType.CROSS);
                        sum=sum-evaluateNByFive3(board,i,j,PieceType.CIRCLE);
                    }

                }
            }
            return sum;
        }
    }
    private int NByFour(Board board) {
        if (checkWin(board,PieceType.CROSS)){
            return 50000000;
        }
        else if (checkWin(board,PieceType.CIRCLE)){
            return -50000000;
        }
        else {
            int sum=0;
            int N=board.getSize();
            for(int i=0;i<N;i++){
                for (int j=0;j<N;j++){
                    if (board.getPiece(i,j)==PieceType.NONE){
                        sum=sum+evaluateNByFour(board,i,j,PieceType.CROSS);
                        sum=sum-evaluateNByFour(board,i,j,PieceType.CIRCLE);
                    }

                }
            }
            return sum;
        }
    }

    private int evaluateNByM(Board board, int row, int col, PieceType player) {
        int M=board.getM();
        int score=0;
        int cur=0;
        int opp=0;
        if (player==PieceType.CROSS){
            cur=1;
            opp=2;
        }
        else {
            cur=2;
            opp=1;
        }
        int numOfTwo=0;
        for (int i=1;i<=8;i++){
            //top level--------------------------------------
            //01111* or 21111*
            int idx=-1;
            boolean flagFirst=true;
            for (;idx>-M;idx--){
                if (getByDirLen(board,row,col,i,idx)!=cur){
                    flagFirst=false;
                    break;
                }
            }
            if (flagFirst){
                if (getByDirLen(board,row,col,i,idx)==0){
                    score+=3000000;
                }
                else {
                    score+=25000;
                }
                if (player==PieceType.CROSS){
                    score+=500;
                }
                continue;
            }
            //111*1 or 11*11 etc
            boolean tierFlag=true;
            int loopTime=(M+1)/2-1;
            int leftEnd=-M+1;
            int rightEnd=2;
            for (int l=0;l<loopTime;l++){
                int left=-1;
                int right=1;
                boolean flag=true;
                for (;left>leftEnd;left--){
                    if (getByDirLen(board,row,col,i,left)!=cur){
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
                    if (getByDirLen(board,row,col,i,right)!=cur){
                        flag=false;
                        break;
                    }
                }
                if (!flag){
                    leftEnd++;
                    rightEnd++;
                    continue;
                }
                score+=24000;
                if (player==PieceType.CROSS){
                    score+=500;
                }
                tierFlag=false;
                break;
            }

            if (!tierFlag){
                continue;
            }

            //second level--------------------------------------
            tierFlag=true;
            leftEnd=-(M-2);
            rightEnd=2;
            for (int l=0;l<loopTime;l++){
                // dont need to consider 211*12
                if (l==0){
                    int leftend=-(M-1);
                    boolean secondflag=true;
                    for (int j=-1;j>leftend;j--){
                        if (getByDirLen(board,row,col,i,j)!=cur){
                            secondflag=false;
                            break;
                        }
                    }
                    if (secondflag){
                        if (getByDirLen(board,row,col,i,1)==0){
                            score+=750;
                            if (getByDirLen(board,row,col,i,leftend)==0){
                                score+=10250;
                                if (player==PieceType.CROSS){
                                    score+=500;
                                }
                            }
                        }
                        else {
                            if (getByDirLen(board,row,col,i,leftend)==0){
                                score+=500;
                            }
                        }
                        tierFlag=false;
                        break;
                    }
                    if (getByDirLen(board,row,col,i,-1)==0){
                        secondflag=true;
                        for (int j=-2;j>=leftend;j--){
                            if (getByDirLen(board,row,col,i,j)!=cur){
                                secondflag=false;
                                break;
                            }
                        }
                        if (secondflag){
                            score+=350;
                            tierFlag=false;
                            break;
                        }
                    }
                    continue;
                }

                //11*1 etc
                else{
                    int left=-1;
                    int right=1;
                    boolean flag=true;
                    for (;left>leftEnd;left--){
                        if (getByDirLen(board,row,col,i,left)!=cur){
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
                        if (getByDirLen(board,row,col,i,right)!=cur){
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
                    if (getByDirLen(board,row,col,i,leftEnd)==0&&getByDirLen(board,row,col,i,rightEnd)==0){
                        score+=9600;
                        if (player==PieceType.CROSS){
                            score+=500;
                        }
                        tierFlag=false;
                        break;
                    }
                    if (getByDirLen(board,row,col,i,leftEnd)!=cur&&getByDirLen(board,row,col,i,rightEnd)!=cur){
                        tierFlag=false;
                        break;
                    }
                    else {
                        score+=700;
                        tierFlag=false;
                        break;
                    }
                }
            }
            if (!tierFlag){
                continue;
            }
            //third level-----------------------------------
            leftEnd=-(M-2);
            boolean flag=true;
            for (int j=-1;j>leftEnd;j--){
                if (getByDirLen(board,row,col,i,leftEnd)!=cur){
                    break;
                }
//                if (getByDirLen(board,row,col,i,leftEnd)!=opp&&getByDirLen(board,row,col,i,1)!=opp){
//                    numOfTwo++;
//                }
                if (getByDirLen(board,row,col,i,leftEnd)==0){
                    numOfTwo++;
                }
            }
            //fourth level-----------------------------------
            int lonewolf=0;
            for(int k=-(M-1);k<=0;k++){
                int temp=0;
                for (int l = 0; l <= (M-1); l++) {
                    if (getByDirLen(board,row,col,i,k+l)==cur){
                        temp++;
                    }
                    else {
//                        if (    getByDirLen(board,row,col,i,k+l)==opp||
//                                getByDirLen(board,row,col,i,k+l)==-1){
//                            temp=0;
//                            break;
//                        }
//                        if (    getByDirLen(board,row,col,i,k+l)!=cur){
//                            temp=0;
//                            break;
//                        }
                        break;
                    }
                }
                lonewolf+=temp;
            }
            score+=lonewolf*15;
        }
        if (numOfTwo>=2){
            score+=12000;
            if (player==PieceType.CROSS){
                score+=200;
            }
        }

        return score;
    }
    private int evaluateNByFive3(Board board, int row, int col, PieceType player) {
        int score=0;
        int two=0;
        int three=0;
        int cur=0;
        int opp=0;
        if (player==PieceType.CROSS){
            cur=1;
            opp=2;
        }
        else {
            cur=2;
            opp=1;
        }

        for (int i=1;i<=8;i++){
            //live 4 01111*
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur&&
                    getByDirLen(board,row,col,i,-4)==cur&&
                    getByDirLen(board,row,col,i,-5)==0){
                score+=3000000;
//                       10000000
//                System.out.println("1111");
                if (player==PieceType.CROSS){
                    score+=5000;
                }
                continue;
            }
//            System.out.println("11111");

            //dead4 21111*
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur&&
                    getByDirLen(board,row,col,i,-4)==cur&&
                    (getByDirLen(board,row,col,i,-5)==opp||getByDirLen(board,row,col,i,-5)==-1)){
                score+=25000;
                if (player==PieceType.CROSS){
                    score+=500;
                }
//                score+=10600;
                continue;
            }
            //dead4 111*1
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur&&
                    getByDirLen(board,row,col,i,1)==cur){
                score+=24000;
//                if ( getByDirLen(board,row,col,i,-4)==0&& getByDirLen(board,row,col,i,2)==0){
//                    score+=3000000;
//                }
//                score+=10600;
                if (player==PieceType.CROSS){
                    score+=500;
                }
                continue;
            }
            //dead4 11*11
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,1)==cur&&
                    getByDirLen(board,row,col,i,2)==cur){
                score+=23000;
//                if ( getByDirLen(board,row,col,i,-3)==0&& getByDirLen(board,row,col,i,3)==0){
//                    score+=3000000;
//                }
//                score+=10600;
                if (player==PieceType.CROSS){
                    score+=500;
                }
                continue;
            }
//            System.out.println("11111");

            //live3 111*0
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur){

                if (getByDirLen(board,row,col,i,1)==0){
                    score+=750;     //2111*0
                    if (getByDirLen(board,row,col,i,-4)==0){
                        score+=10250;  ////0111*0
//                        three++;
                        if (player==PieceType.CROSS){
                            score+=500;
                        }
                    }
                }
                if ((   getByDirLen(board,row,col,i,1)==opp||
                        getByDirLen(board,row,col,i,1)==-1)&&
                        getByDirLen(board,row,col,i,-4)==0){
                    score+=500;      //0111*2
                }

                continue;
            }
            //dead3 1110*
            if (    getByDirLen(board,row,col,i,-1)==0&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur&&
                    getByDirLen(board,row,col,i,-4)==cur){
                score+=350;
                continue;
            }


            //dead 3 11*1
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,1)==cur){

                score+=600;
                if (getByDirLen(board,row,col,i,-3)==0&&
                        getByDirLen(board,row,col,i,2)==0){
                    score+=9600;
                    if (player==PieceType.CROSS){
                        score+=500;
                    }
                    continue;
                }
                if (( getByDirLen(board,row,col,i,-3)==opp||
                        getByDirLen(board,row,col,i,-3)==-1)&&(
                        getByDirLen(board,row,col,i,2)==opp||
                                getByDirLen(board,row,col,i,2)==-1)){
                    continue;
                }
                else {
                    score+=700;
                    continue;
                }

            }


            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur
                    && getByDirLen(board,row,col,i,-3)==0
                    ){
                two++;
            }






            int lonewolf=0;
            for(int k=-4;k<=0;k++){
                int temp=0;
                for (int l = 0; l <= 4; l++) {
                    if (getByDirLen(board,row,col,i,k+l)==cur){
                        temp++;
                    }
                    else {
//                        if (    getByDirLen(board,row,col,i,k+l)==opp||
//                                getByDirLen(board,row,col,i,k+l)==-1)
//                            temp=0;
                        break;
//                        if (    getByDirLen(board,row,col,i,k+l)!=cur){
//                            temp=0;
//                            break;
//                        }
                    }
                }
                lonewolf+=temp;
            }
            score+=lonewolf*15;
        }
        if (two>=2){
            score+=12000;
            if (player==PieceType.CROSS){
                score+=200;
            }
        }
        return score;
    }
    private int evaluateNByFour(Board board, int row, int col, PieceType player) {
        int score=0;
        int cur=0;
        int opp=0;
        int one=0;
        if (player==PieceType.CROSS){
            cur=1;
            opp=2;
        }
        else {
            cur=2;
            opp=1;
        }
        for (int i=1;i<=8;i++){
            //live 4 0111*
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur&&
                    getByDirLen(board,row,col,i,-4)==0){
                score+=3000000;
                if (player==PieceType.CIRCLE){
                    score+=5000;
//                    score+=500;
                }
                continue;
            }
            //dead 3 2111*
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,-3)==cur&&
                    (getByDirLen(board,row,col,i,-4)==opp||getByDirLen(board,row,col,i,-4)==-1)){
                score+=25000;
//                System.out.println("111");
                if (player==PieceType.CIRCLE){
//                    System.out.println("111");
                    score+=500;
                }
                continue;
            }
            //dead 3  11*1
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur&&
                    getByDirLen(board,row,col,i,1)==cur){
//                score+=240000;
                score+=25000;
                if (player==PieceType.CIRCLE){
                    score+=500;
                }
                continue;
            }
            //live2 11*0
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,-2)==cur){

                if (getByDirLen(board,row,col,i,1)==0){
                    score+=750;     //2111*0
                    if (getByDirLen(board,row,col,i,-3)==0){
                        score+=39250;  ////0111*0
                        if (player==PieceType.CIRCLE){
                            score+=50000;
                        }
                    }
                }
                if ((   getByDirLen(board,row,col,i,1)==opp||
                        getByDirLen(board,row,col,i,1)==-1)&&
                        getByDirLen(board,row,col,i,-3)==0){
                    score+=500;      //0111*2
                }

                continue;
            }
            //dead 2 1*1
            if (    getByDirLen(board,row,col,i,-1)==cur&&
                    getByDirLen(board,row,col,i,1)==cur) {

                score += 600;
                if (getByDirLen(board, row, col, i, -2) == 0 &&
                        getByDirLen(board, row, col, i, 2) == 0) {
                    score += 25000;
                    if (player==PieceType.CIRCLE){
                        score+=50000;
                    }
                    continue;
                }
                if ((getByDirLen(board, row, col, i, -2) == opp ||
                        getByDirLen(board, row, col, i, -2) == -1) && (
                        getByDirLen(board, row, col, i, 2) == opp ||
                                getByDirLen(board, row, col, i, 2) == -1)) {
                    continue;
                } else {
                    score += 700;
                    continue;
                }
            }
            if (getByDirLen(board, row, col, i, -1) == cur &&
                    getByDirLen(board, row, col, i, -2) == 0
                    && getByDirLen(board, row, col, i, 1) == 0
                    ) {
                one++;
            }
            int lonewolf=0;
            for(int k=-4;k<=0;k++){
                int temp=0;
                for (int l = 0; l <= 4; l++) {
                    if (getByDirLen(board,row,col,i,k+l)==cur){
                        temp++;
                    }
                    else {
                        if (    getByDirLen(board,row,col,i,k+l)==opp||
                                getByDirLen(board,row,col,i,k+l)==-1)
                            temp=0;
                        break;
                    }
                }
                lonewolf+=temp;
            }
            score+=lonewolf*15;


        }
        if (one>=2){
            score+=20000;
            if (player==PieceType.CIRCLE){
                score+=500;
            }
        }
        return score;
    }

    private int getByDirLen(Board board, int row, int col, int dir, int len) {
        //len typically is negative.
        //up
        if (dir==1){
            row+=len;
        }
        //Northwest
        else if (dir==2){
            row+=len;
            col+=len;
        }
        //left
        else if (dir==3){
            col+=len;
        }
        //Southwest
        else if (dir==4){
            row-=len;
            col+=len;
        }
        //down
        else if (dir==5){
            row-=len;
        }
        //Southeast
        else if (dir==6){
            row-=len;
            col-=len;
        }
        //right
        else if (dir==7){
            col-=len;
        }
        //Northeast
        else if (dir==8){
            row+=len;
            col-=len;
        }
        int N=board.getSize();
        if (row<0||col<0||row>=N||col>=N){
            return -1;
        }
        if (board.getPiece(row,col)==PieceType.CROSS){
            return 1;
        }
        else if (board.getPiece(row,col)==PieceType.CIRCLE){
            return 2;
        }
        else {
            return 0;
        }
    }
    private boolean checkWin(Board board,PieceType player) {
        int N=board.getSize();
        int M=board.getM();
        int[] dia=new int[2*N-1];
        int ind=0;
        for (int i=1;i<=N;i++){
            dia[ind]=i;
            ind++;
        }
        for (int i=N-1;i>=1;i--){
            dia[ind]=i;
            ind++;
        }
        // computer's pieceType
        PieceType cur=player;
        //horizontal
        for(int i=0;i<N;i++){
            int acu=0;
            int j=0;
            while(j<N){
                if (board.getPiece(i,j)==cur){
                    acu++;
                    if (acu==M){
                        return true;
                    }
                }
                else{
                    acu=0;
                }
                j++;
            }
        }
        //vertical
        for(int j=0;j<N;j++){
            int acu=0;
            int i=0;
            while(i<N){
                if (board.getPiece(i,j)==cur){

                    acu++;
                    if (acu==M){
                        return true;
                    }
                }
                else{
                    acu=0;
                }
                i++;
            }
        }
        //counter diagonal
        int t=0;
        for (int start=N-1;start>=0;start--){
            int acu=0;
            int i=start;
            int j=0;
            int len=dia[t];
            int time=0;
            while (time<len){
                if (board.getPiece(i,j)==cur){

                    acu++;
                    if (acu==M){
                        return true;
                    }
                }
                else{
                    acu=0;
                }
                i++;
                j++;
                time++;
            }
            t++;
        }
        for (int start=1;start<N;start++){
            int acu=0;
            int j=start;
            int i=0;
            int len=dia[t];
            int time=0;
            while (time<len){
                if (board.getPiece(i,j)==cur){
                    acu++;
                    if (acu==M){
                        return true;
                    }
                }
                else{
                    acu=0;
                }
                i++;
                j++;
                time++;
            }
            t++;
        }


        //diagonal
        t=0;
        for (int start=0;start<N;start++){
            int acu=0;
            int i=start;
            int j=0;
            int len=dia[t];
            int time=0;
            while (time<len){
                if (board.getPiece(i,j)==cur){
                    acu++;
                    if (acu==M){
                        return true;
                    }
                }
                else{
                    acu=0;
                }
                i--;
                j++;
                time++;
            }
            t++;
        }
        for (int start=1;start<N;start++){
            int acu=0;
            int j=start;
            int i=N-1;
            int len=dia[t];
            int time=0;
            while (time<len){
                if (board.getPiece(i,j)==cur){
                    acu++;
                    if (acu==M){
                        return true;
                    }
                }
                else{
                    acu=0;
                }
                i--;
                j++;
                time++;
            }
            t++;
        }
        return false;
    }

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

        if (board.getPiece(row1,col1) == PieceType.CROSS) {
            score = 1;
        } else if (board.getPiece(row1,col1) == PieceType.CIRCLE) {
            score = -1;
        }

        // Second cell
        if (board.getPiece(row2,col2) == PieceType.CROSS) {
            if (score == 1) {   // cell1 is 1
                score = 10;
            } else if (score == -1) {  // cell1 is 2
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (board.getPiece(row2,col2) == PieceType.CIRCLE) {
            if (score == -1) { // cell1 is 2
                score = -10;
            } else if (score == 1) { // cell1 is 1
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board.getPiece(row3,col3) == PieceType.CROSS) {
            if (score > 0) {  // cell1 and/or cell2 is 1
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is 2
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board.getPiece(row3,col3) == PieceType.CIRCLE) {
            if (score < 0) {  // cell1 and/or cell2 is 2
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is 1
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }
}
