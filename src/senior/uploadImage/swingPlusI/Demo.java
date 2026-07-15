package senior.uploadImage.swingPlusI;

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
        FontUIResource font = new FontUIResource("Dialog", Font.PLAIN, 16);
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
