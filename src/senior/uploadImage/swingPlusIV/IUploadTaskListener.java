package senior.uploadImage.swingPlusIV;

import java.io.File;

/*
 * 多檔上傳事件回報介面。
 *
 * 為什麼需要這個介面？
 *
 * UploadService 負責：
 * - 建立 Thread
 * - 複製檔案
 * - 判斷哪一張成功或失敗
 * - 判斷全部是否完成
 *
 * Frame 負責：
 * - 建立進度條
 * - 更新進度條
 * - 更新圖片清單
 * - 顯示錯誤訊息
 *
 * UploadService 不應該直接操作 JProgressBar、JList、JLabel。
 * 否則 Service 就會混到畫面邏輯。
 *
 * 所以 UploadService 透過這個介面通知 Frame：
 * 「某張圖片進度改變了」
 * 「某張圖片成功了」
 * 「某張圖片失敗了」
 * 「全部圖片都完成了」
 */
public interface IUploadTaskListener {

    // 某一張圖片上傳進度改變。
    void onProgress(File file, int index, int total, int progress);

    // 某一張圖片上傳成功。
    void onSuccess(File sourceFile, File targetFile, int index, int total);

    // 某一張圖片上傳失敗。
    void onError(File sourceFile, Exception ex, int index, int total);

    // 全部圖片都完成，不管成功或失敗都算完成。
    void onAllFinished(int total);
}
