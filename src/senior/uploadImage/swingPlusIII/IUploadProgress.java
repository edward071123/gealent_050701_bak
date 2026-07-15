package senior.uploadImage.swingPlusIII;

/*
 * 上傳進度回報介面。
 *
 * 為什麼要有這個介面？
 *
 * UploadService 只負責複製檔案，它不知道畫面上有沒有 progressBar。
 * Frame 負責畫面，所以 Frame 才知道要怎麼更新 progressBar。
 *
 * 因此 UploadService 不直接操作 progressBar，
 * 而是透過 IUploadProgress.onProgress(progress) 把百分比傳出去。
 *
 * 簡單理解：
 * - UploadService：我算出目前進度是幾 %
 * - IUploadProgress
 * - Frame：我收到進度後更新 progressBar
 */
public interface IUploadProgress {
    void onProgress(int progress);
}
