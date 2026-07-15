package senior.uploadImage.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
    public File upload(File sourceFile) throws Exception {
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

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            // FileInputStream：從原始圖片讀取資料。
            fis = new FileInputStream(sourceFile);

            // FileOutputStream：把資料寫到 uploads 裡的新檔案。
            fos = new FileOutputStream(targetFile);

            int len;

            // fis.read(buffer) 會把資料讀進 buffer。
            // 回傳值 len 表示這次實際讀到幾個 bytes。
            // 如果回傳 -1，代表檔案讀完了。
            while ((len = fis.read(buffer)) != -1) {
                // 只寫入本次實際讀到的長度 len。
                fos.write(buffer, 0, len);
            }

        } catch (Exception e) {
            throw new Exception("圖片複製失敗：" + e.getMessage());

        } finally {
            // finally 不管成功或失敗都會執行，適合拿來關閉檔案資源。
            if (fis != null) {
                fis.close();
            }

            if (fos != null) {
                fos.close();
            }
        }

        // 回傳複製完成後的檔案，讓 Frame 可以顯示它。
        return targetFile;
    }
}
