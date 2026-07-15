package senior.uploadImage.swingPlusIV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadService {

    // 上傳目標資料夾，也就是圖片最後會被複製到哪裡。
    private File uploadDir;

    public UploadService(File uploadDir) {
        // 建構子接收資料夾，讓 Frame 可以決定上傳位置。
        this.uploadDir = uploadDir;

        // 如果 uploads 資料夾不存在，就自動建立。
        if (!this.uploadDir.exists()) {
            this.uploadDir.mkdirs();
        }
    }

    // 判斷檔案是否為系統允許的圖片格式。
    public boolean isImageFile(File file) {
        // 先把檔名轉成小寫，這樣 .JPG / .jpg 都能判斷成功。
        String name = file.getName().toLowerCase();

        // 第一步：確認它是真的「檔案」，不是資料夾。
        boolean isFile = file.isFile();

        // 第二步：分別判斷副檔名。
        boolean isJpg = name.endsWith(".jpg");
        boolean isJpeg = name.endsWith(".jpeg");
        boolean isPng = name.endsWith(".png");
        boolean isGif = name.endsWith(".gif");

        // 第三步：只要符合其中一種圖片副檔名，就算是圖片格式。
        boolean isImageType = isJpg || isJpeg || isPng || isGif;

        // 第四步：必須同時滿足：
        // 1. 它是檔案
        // 2. 它是允許的圖片格式
        return isFile && isImageType;
    }

    // 把使用者選擇的圖片複製到 uploads 資料夾。
    public File upload(
            File sourceFile,
            int index,
            int total,
            IUploadTaskListener listener
    ) throws Exception {
        /*
         * 上傳邏輯放在這個類別，而不是放在 Frame：
         *
         * Frame         ：負責畫面與使用者操作。
         * UploadService ：負責檔案複製。
         *
         */

        // targetFile 是複製後的新位置。
        // 例如 sourceFile 是 /Users/xxx/a.png，
        // targetFile 就會是 src/senior/uploadImage/uploads/a.png。
        File targetFile = new File(uploadDir, sourceFile.getName());

        // 每次讀取 4096 bytes，不要一次把整張圖片全部讀進記憶體。
        byte[] buffer = new byte[4096];
        long totalSize = sourceFile.length();
        long copiedSize = 0;

        // try-with-resources 會在區塊結束時自動關閉 FileInputStream 和 FileOutputStream。
        try (
                // FileInputStream：從原始圖片讀取資料。
                FileInputStream fis = new FileInputStream(sourceFile);

                // FileOutputStream：把資料寫到 uploads 裡的新檔案。
                FileOutputStream fos = new FileOutputStream(targetFile)
        ) {
            int len;

            // fis.read(buffer) 會把資料讀進 buffer。
            // 回傳值 len 表示這次實際讀到幾個 bytes。
            // 如果回傳 -1，代表檔案讀完了。
            while ((len = fis.read(buffer)) != -1) {
                // 只寫入本次實際讀到的長度 len。
                fos.write(buffer, 0, len);

                copiedSize += len;

                // 計算目前上傳百分比，交給 Frame 更新進度條。
                int progress = (int) ((copiedSize * 100) / totalSize);
                listener.onProgress(sourceFile, index, total, progress);

                // 教學用：稍微放慢一點，方便看到 loading 過程。
                Thread.sleep(10);
            }

        } catch (Exception e) {
            throw new Exception("圖片複製失敗：" + e.getMessage());
        }

        // 回傳複製完成後的檔案，讓 Frame 可以顯示它。
        return targetFile;
    }

    // 多檔上傳：每一張圖片建立一個 Thread，同時上傳。
    public void uploadMultiple(File[] sourceFiles, IUploadTaskListener listener) {
        /*
         * 這個方法是 swingPlusIV 的重點：
         *
         * Frame 不直接 new Thread。
         * Frame 只呼叫：
         *
         * uploadService.uploadMultiple(sourceFiles, listener);
         *
         * UploadService 負責：
         * - 建立多個 Thread
         * - 每個 Thread 呼叫單檔 upload()
         * - 回報每張進度
         * - 回報單張成功
         * - 回報單張失敗
         * - 回報全部完成
         *
         * 但是 UploadService 不碰 Swing 元件。
         * 它只透過 listener 把事件傳出去。
         */

        final int total = sourceFiles.length;

        /*
         * AtomicInteger 是多執行緒安全的整數。
         *
         * 多張圖片同時上傳時，
         * 可能有多個 Thread 幾乎同時完成。
         *
         * 如果用普通 int，很容易發生數字更新互相覆蓋。
         * AtomicInteger 可以安全地統計完成數量。
         */
        final AtomicInteger finishedCount = new AtomicInteger(0);

        for (int i = 0; i < sourceFiles.length; i++) {
            final File sourceFile = sourceFiles[i];
            final int index = i + 1;

            Thread uploadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadOneFile(sourceFile, index, total, listener, finishedCount);
                }
            });

            uploadThread.start();
        }
    }

    // 單一 Thread 實際執行的上傳工作。
    private void uploadOneFile(
            final File sourceFile,
            final int index,
            final int total,
            final IUploadTaskListener listener,
            final AtomicInteger finishedCount
    ) {
        try {
            File targetFile = upload(sourceFile, index, total, listener);
            listener.onSuccess(sourceFile, targetFile, index, total);

        } catch (Exception ex) {
            listener.onError(sourceFile, ex, index, total);

        } finally {
            int finished = finishedCount.incrementAndGet();

            // 不管成功或失敗，只要這個 Thread 結束，就算完成一個。
            if (finished == total) {
                listener.onAllFinished(total);
            }
        }
    }
}
