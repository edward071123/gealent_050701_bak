package basic;

public class Condition {
    public static void main(String[] args) {
        // 條件式
        // if(條件成立) {
        //      條件成立時候跑進來這裡面
        // } else {
        //      條件不成立時候跑進來這裡面
        // }

        // int a11 = 15;
        // int b11 = 15;

        // if((a11 < b11)) {
        //     System.out.println("a11 小於 b11"); 
        // } else {
        //     System.out.println("a11 非小於 b11");
        // }

        // // 課堂練習 把 && 寫進 if(條件)
        // int a12 = 15;
        // int b12 = 13;
        // int c12 = 12;
        // int d12 = 14;

        // if((b12 > c12) && (a12 > b12)) {
        //     System.out.println("and1 為 true");
        // } else {
        //     System.out.println("and1 為 false");
        // }

        // if((b12 > c12) && (a12 > b12) && (c12 > d12)) {
        //     System.out.println("and2 為 true");
        // } else {
        //     System.out.println("and2 為 false");
        // }

        // else if (優先程度由上而下)
        // if(條件1) {
        //     // 條件1達成 跑來這裡
        // } else if(條件2) {
        //     // 條件2達成 跑來這裡
        // } else if(條件3) {
        //     // 條件3達成 跑來這裡
        // } else {
        //     // 上面條件都不符合 才跑來這裡
        // }

        // int a = 6;

        // if(a == 0) {
        //     System.out.println("a 是 0 ");
        // } else if(a == 1) {
        //     System.out.println("a 是 1");
        // } else if(a == 2) {
        //     System.out.println("a 是 2");
        // } else {
        //     System.out.println("a 是 0, 1, 2 以外的數值");
        // }

        // 課堂練習 if, else if, else
        // 判斷學生分數
        // 如果分數超過 90分 印出: 超棒
        // 如果分數超過80 低於90分 印出: 很好
        // 如果分數超過60 低於80分 印出: 好
        // 如果分數未滿 60分 印出: 請加油

        // int score = 60;
        // if(score >= 90) {
        //     System.out.println("超棒");
        // } else if((score >= 80) && (score < 90)) {
        //     System.out.println("很好");
        // } else if(score >= 60 && score < 80 ) {
        //     System.out.println("好");
        // } else {
        //     System.out.println("請加油");
        // }

        // 驗證: score 0 ~ 100;
        // 迴圈 (loop) 
        // for loop
        //  起始值           可以進來的條件  每次先加1
        // for(int index = 0;  index <= 100;  index++) {
        //     // System.out.println(index);
        //     int score1 = index;

        //     if(score1 >= 90) {
        //         System.out.println("分數:" + score1 + "--超棒");
        //     } else if((score1 > 80) && (score1 < 90)) {
        //         System.out.println("分數:" + score + "--很好");
        //     } else if(score1 > 60 && score1 < 80 ) {
        //         System.out.println("分數:" + score1 + "--好");
        //     } else {
        //         System.out.println("分數:" + score1 + "--請加油");
        //     }

        // }

        // 外面i 表示天數
        // 裡面k　表示遊玩次數
        // 想像 你有10天 每1天可以玩此遊樂設施10次

        // for (int i = 1; i <= 10; i++) {
        //     for (int k = 1; k <= 10; k++) {
        //         System.out.print("第:" + i + "天,玩設施" + k + "次");
        //         System.out.print(";");
        //     }
        //     System.out.println("");
        // }

        // for(int i = 1; i <= 9; i++) {
        //     for (int k = 1; k <= 9; k++) {
        //         int result = i * k; // int * int 
        //         System.out.print(i + "x" + k + "=" + result + "\t");
        //         // System.out.println(i + "x" + k + "=" + (i * k) + "\t");
        //         System.out.print(" ");
        //     }
            
        //     System.out.println("");
        // }

        for (int i = 1; i <= 9; i++) {
            for (int k = 1; k <= 9; k++) {
                int result = i * k; // int * int 
                // format (轉換樣式) \t: 幫我們排版整齊
                System.out.print(i + "x" + k + "=" + result + "\t");
            }

            System.out.println("");

        }
    }
}
