package senior.uploadImage.swing;

import javax.swing.SwingUtilities;

public class Demo {
    public static void main(String[] args) {
        /*
         * SwingUtilities.invokeLater 的用途：
         *
         * Swing 是「事件驅動」的 GUI 框架。
         * 按鈕點擊、清單選取、畫面更新，都是由 Swing 的事件執行緒處理。
         *
         * invokeLater 代表：
         * 「請 Swing 稍後在事件執行緒中建立畫面」
         *
         * 這樣寫比直接 new Frame() 更標準，
         * 可以避免 Swing 畫面在錯誤的執行緒中建立或更新。
         */
        SwingUtilities.invokeLater(() -> {
            // 建立主視窗。
            new Frame();
        });
    }
}
