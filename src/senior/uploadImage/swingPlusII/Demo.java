package senior.uploadImage.swingPlusII;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Enumeration;

public class Demo {
    public static void main(String[] args) {
        // 在建立任何 Swing 元件之前，先啟用 FlatLaf 外觀。
        FlatLightLaf.setup();

        // 在建立 Frame 之前，先統一設定 Swing 的預設字型。
        setDefaultFont();

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

    private static void setDefaultFont() {
        /*
         * 為什麼要統一設定字型？
         *
         * 在 macOS 上，Swing 有時候會把中文字和阿拉伯數字
         * 分配到不同字型去顯示。
         *
         * 結果可能會看到：
         * 「一次最多只能上傳   張圖片」
         *
         * 也就是中文字有出現，但中間的數字不見了。
         *
         * 這裡把 Swing 所有預設字型統一改成 Dialog。
         * Dialog 是 Java 的邏輯字型，Java 會依照作業系統
         * 自動對應到可以顯示中文和數字的實際字型。
         */
        FontUIResource font = new FontUIResource("Dialog", Font.PLAIN, 16);

        /*
         * UIManager.getDefaults() 可以取得 Swing 預設設定表。
         *
         * 裡面有很多設定，例如：
         * Button.font      按鈕字型
         * Label.font       標籤字型
         * OptionPane.font  訊息視窗字型
         * List.font        清單字型
         *
         * 我們把所有 FontUIResource 都換成同一個 font，
         * 讓整個程式的字型一致。
         */
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = defaults.get(key);

            if (value instanceof FontUIResource) {
                defaults.put(key, font);
            }
        }
    }
}
