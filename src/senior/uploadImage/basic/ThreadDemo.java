package senior.uploadImage.basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        // 執行緒介紹
        // 情景: 生產線, 搶票, 影音串流, 上傳圖片, 多個重複的事情....

        // |---------|---------主執行緒(main)-----------------------
        //           |
        //           |---------執行緒1(Thread1)---------------------
        //           |
        //           |---------執行緒2(Thread2)---------------------

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("執行緒 1 正在執行: " + i);
                    try {
                        // 1000毫秒 = 1秒
                        // 睡一秒鐘
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("執行緒 2 正在執行: " + i);
                try {
                    // 睡兩秒鐘
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 排隊執行 thread1 全部跑完後 換 thread2
        thread1.run();
        thread2.run();

        // 同步執行 thread1 跟 thread2 同步起跑
        thread1.start();
        thread2.start();

        // ========================main 執行緒查看================================
        System.out.println("main-thread start...");

        Thread thread3 = new Thread(() -> {
            System.out.println("thread3 run...");
            try {
                // 睡兩秒鐘
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("thread3 end.");
        });

        thread3.start();

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}

        System.out.println("main-thread end...");

        // ========================中斷執行緒================================

        // System.out.println("main執行緒 開始");
        Thread thread4 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("執行緒4執行中...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("sleep 被 interrupt 中斷了");

                    // 重要：InterruptedException 會清掉 interrupt 狀態
                    // 所以這裡手動補回去，讓 while 條件可以結束
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("執行緒4結束");
        });

        // // 啟動執行緒4
        thread4.start();

        // main執行緒 停留3秒不動
        Thread.sleep(3000);
        System.out.println("main執行緒 讓呼叫 interrupt()");
        // 執行緒4開始中斷
        thread4.interrupt();

        thread4.join(); // main執行緒等待執行緒4程式結束
        System.out.println("main執行緒 結束");

        // ==========================執行緒池==============================
        // ExecutorService：執行緒池
        // 情境：使用者一次選了 8 張圖片，但我們只開 3 條執行緒同時上傳。
        // 好處：不用每張圖片都 new Thread，可以控制同時工作的數量。

        // 假設有8張圖片
        String[] imageNames = {
            "cat.jpg",
            "dog.png",
            "family.jpeg",
            "travel01.jpg",
            "travel02.jpg",
            "avatar.gif",
            "food.png",
            "receipt.jpg"
        };

        // 執行緒池開三條執行緒的線來處理上傳圖片
        ExecutorService excutor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < imageNames.length; i++) {
            int index = i + 1;
            String imageName = imageNames[i];

            excutor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + "開始上傳第" + index + "張" + imageName);
                try {
                    // 模擬每張圖片大小不同, 所以上傳時間不同
                    Thread.sleep(700 + index * 150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(threadName + " 上傳中斷：" + imageName);
                    return;
                }

                System.out.println(threadName + "完成上傳第" + index + "張" + imageName);
            });

        }

        // 不再接收新任務, 但是已送出的任務會繼續做完
        excutor.shutdown();

        // main執行緒 等待執行緒池任務完成, 最多等10秒
        // TimeUnit.SECONDS: 單位是秒
        boolean finished = excutor.awaitTermination(10, TimeUnit.SECONDS);

        if (finished) {
            System.out.println("全部上傳任務已完成");
        } else {
            System.out.println("等待逾時, 強制停止未完成任務");
            excutor.shutdownNow();
        }
    }
}
