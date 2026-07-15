package junior.loopTraning;

public class PyramidI {
    public static void main(String[] args) {
        // *
        // **
        // ***
        // ****
        // *****
        // int level = 10;
        // for (int i = 1; i <= level; i++) {
        //     // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
        //     for (int k = 1; k <= i; k++) {
        //         System.out.print("*");
        //     }
        //     // 換行
        //     System.out.println("");
        // }

        // *
        // ***
        // *****
        // Step1: 找出每一層(i)是否為偶數or奇數
        // int level = 10;
        // for (int i = 1; i <= level; i++) {
        //     // 排除偶數
        //     // (i % 2) => 偶數會得到餘數為0, 奇數會得到餘數為1
        //     if(i%2 == 1) {
        //         System.out.print("第" + i + "層為奇數");
        //     } else {
        //         System.out.print("第" + i + "層為偶數");
        //     }
    
        //     // // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
        //     // for (int k = 1; k <= i; k++) {
        //     //     System.out.print("*");
        //     // }
        //     // 換行
        //     System.out.println("");
        // }


        // Step2: 排除偶數
        // int level = 10;
        // for (int i = 1; i <= level; i++) {
        //     // 排除偶數
        //     // (i % 2) => 偶數會得到餘數為0, 奇數會得到餘數為1
        //     if(i%2 == 1) {
        //         System.out.print("第" + i + "層為奇數");
        //     } 

        //     // // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
        //     // for (int k = 1; k <= i; k++) {
        //     //     System.out.print("*");
        //     // }
        //     // 換行
        //     System.out.println("");
        // }


        // Step3: 只印出奇數層的星星
        // int level = 10;
        // for (int i = 1; i <= level; i++) {
        //     // 排除偶數
        //     // (i % 2) => 偶數會得到餘數為0, 奇數會得到餘數為1
        //     if(i%2 == 1) {
        //         System.out.print("第" + i + "層為奇數");
        //         // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
        //         for (int k = 1; k <= i; k++) {
        //             System.out.print("*");
        //         }
        //     } 

        //     // 換行
        //     System.out.println("");
        // }


        // Step4: 只印出奇數層的星星&換行
        // int level = 10;
        // for (int i = 1; i <= level; i++) {
        //     // 排除偶數
        //     // (i % 2) => 偶數會得到餘數為0, 奇數會得到餘數為1
        //     if (i % 2 == 1) {
        //         System.out.print("第" + i + "層為奇數");
        //         // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
        //         for (int k = 1; k <= i; k++) {
        //             System.out.print("*");
        //         }
        //         // 換行
        //         System.out.println("");
        //     }
        // }
        
        // Step5: 由左到右的前面要印出空白
        // //
        // // 最大數 金字塔  空白 星星 
        // // max:3 _*      1   1     3 - 1 ? 1  
        // // max:3 ***     0   3     3 - 3 ? 0 
        // // 
        // // max:5 __*     2   1     5 - 1 ? 2
        // // max:5 _***    1   3     5 - 3 ? 1
        // // max:5 *****   0   5     5 - 5 ? 0
        // //
        // // max:7 ___*    3   1     7 - 1 ? 3  (7 - 1) / 2  = 3
        // // max:7 __***   2   3     7 - 3 ? 2  (7 - 3) / 2  = 2
        // // max:7 _*****  1   5     7 - 5 ? 1  (7 - 5) / 2  = 1
        // // max:7 ******* 0   7     7 - 7 ? 0  (7 - 7) / 2  = 0
        // // 
        // // 根據上面的樣式 用最大數(level)跟印出星星數 要如何才能得到每層所需的空白數？
        // // (level - 當下星星數) / 2 = 空白數

        int level = 10;
        for (int i = 1; i <= level; i++) {
            // 排除偶數
            // (i % 2) => 偶數會得到餘數為0, 奇數會得到餘數為1
            if(i%2 == 1) {
                System.out.print("第" + i + "層為奇數");

                // 印出空白
                int space = (level - i) / 2;
                for (int s = 1; s <= space; s++) {
                    System.out.print("_");
                }

                // 第i天 每天只能玩i次 (第i層 只能印出i次星星)
                for (int k = 1; k <= i; k++) {
                    System.out.print("*");
                }
                // 換行
                System.out.println("");
            }
        }
    }
}
