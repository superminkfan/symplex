



public class MainCalc {
    private static int ROW;

    private static int COL;


    private static double[] calctemp(double[] temp, double[][] constLeft,
                                     double[] targetFunc, int[] basic) {
        double[] calcTemp = new double[temp.length];
        for (int i = 0; i < COL; i++) {
            calcTemp[i] = 0;
            for (int j = 0; j < ROW; j++) {
                calcTemp[i] += targetFunc[basic[j]] * constLeft[j][i];
            }
           calcTemp[i] -= targetFunc[i];
        }




//        for (int i = 0 ; i<calcTemp.length ; i++)
//        {
//            calcTemp[i] = BigDecimal.valueOf(calcTemp[i]).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
//        }



        return calcTemp;
    }

    private static int minimum(double[] arr) {
        double arrmin = arr[0];
        int minPos = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < arrmin) {
                arrmin = arr[i];
                minPos = i;
            }
        }
        return minPos;

    }

    private static void printFrame(double[] targetFunc) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cj\t\t\t\t\t\t");
        for (int i = 0; i < targetFunc.length; i++) {
            sb.append(String.format(" %15f |" ,targetFunc[i] ));
        }
       // sb.append("\ncB\txB\tb\t\t\t\t\t");
        sb.append(String.format("%5s  | %-2s  | %9s ||" , "\ncB" ,"xB" , "b" ));
        for (int i = 0; i < targetFunc.length; i++) {
            sb.append(String.format(" %14s  |" ,"x" + (i + 1) ));
        }
        System.out.print(sb);
    }

    private static void printAll(double[] targetFunc, double[] constraintRight,
                                 double[][] constraintLeft, int[] basic) {
        printFrame(targetFunc);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROW; i++) {
//            sb.append("\n" + targetFunc[basic[i]] + "\tx" + (basic[i] + 1)
//                    + "\t" + constraintRight[i] + "\t");
//
            sb.append(String.format("%s | x%-2d | %9f ||" , "\n" + targetFunc[basic[i]] ,(basic[i] + 1) , constraintRight[i] ));

            for (int j = 0; j < COL+1; j++) {
                sb.append(String.format(" %15f |" ,constraintLeft[i][j] ));
            }
            sb.append("\n");


        }
        System.out.println(sb);




    }

    public static void main(String[] args) {
        double[] targetFunc = { 1, 2, 3, 0, 0, 0};
        ROW = 3;
        COL = 2 + ROW;
        double[][] constraintsLeft = { { 1, 2, 3, 1 ,0 ,0},
                                       { 2, 1, 4, 0, 1, 0 },
                                       { 4, 3, 1, 0, 0, 1}};

        double[] constraintsRight =    { 13, 9, 32 };
//        double[] targetFunc = { 5, 3, 1, 0, 0};
//        ROW = 2;
//        COL = 2 + ROW;
//        double[][] constraintsLeft = { { 2, -1, 3, 1 ,0},
//                                       { 2, 0, -1, 0, 1 }};
//
//        double[] constraintsRight =    { 13, 9};

        double[] temp = new double[COL+1];

        int tempMinPos;
        double[] miniRatio = new double[ROW];
        int miniRatioMinPos = 0;
        double key;
        int goOutCol = 0;
        double z;
        double[] x = new double[COL+1];
        int[] basic = new int[ROW];
        int[] nonBasic = new int[ROW];
        boolean flag = false;

        for (int i = 0; i < ROW; i++) {
            basic[i] = (i + ROW);
            nonBasic[i] = i;
        }
        System.out.println("------------Calculating------------");
        while (!flag) {
            z = 0;
            temp = calctemp(temp, constraintsLeft, targetFunc, basic);

            tempMinPos = minimum(temp);
            printAll(targetFunc, constraintsRight, constraintsLeft, basic);
            System.out.print("Zj-Cj\t\t\t\t\t");
            for (int i = 0; i < COL+1; i++) {
                System.out.print(String.format(" %15f |" ,temp[i] ));
            }

            System.out.println("\n--------------------------------------------------");
            System.out.println("Basic variables : ");
            for (int i = 0; i < ROW; i++) {


                x[basic[i]] = constraintsRight[i];
                x[nonBasic[i]] = 0;
                System.out.println("x" + (basic[i] + 1) + " = "
                        + constraintsRight[i]);

            }
            for (int i = 0; i < ROW; i++) {
                z = z + targetFunc[i] * x[i];
            }
            System.out.println("Max(z) = " + z);

            for (int i = 0; i < ROW; i++) {
                if (constraintsLeft[i][tempMinPos] <= 0) {
                    miniRatio[i] = 999;
                    continue;
                }
                miniRatio[i] = constraintsRight[i]
                        / constraintsLeft[i][tempMinPos];
            }
            miniRatioMinPos = minimum(miniRatio);

            for (int i = 0; i < ROW; i++) {
                if (miniRatioMinPos == i) {
                    goOutCol = basic[i];
                }
            }
            System.out.println("Outgoing variable : x" + (goOutCol + 1));
            System.out.println("Incoming variable : x" + (tempMinPos + 1));

            basic[miniRatioMinPos] = tempMinPos;
            nonBasic[tempMinPos] = goOutCol;

            key = constraintsLeft[miniRatioMinPos][tempMinPos];
            constraintsRight[miniRatioMinPos] /= key;
            for (int i = 0; i < COL; i++) {
                constraintsLeft[miniRatioMinPos][i] /= key;
            }
            for (int i = 0; i < ROW; i++) {
                if (miniRatioMinPos == i) {
                    continue;
                }
                key = constraintsLeft[i][tempMinPos];
                for (int j = 0; j < COL; j++) {
                    constraintsLeft[i][j] -= constraintsLeft[miniRatioMinPos][j]
                            * key;
                }
                constraintsRight[i] -= constraintsRight[miniRatioMinPos] * key;
            }

            for (int i = 0; i < COL+1; i++) {
                flag = true;
                if (temp[i] < 0) {
                    flag = false;
                    break;
                }
            }
        }
    }
}
