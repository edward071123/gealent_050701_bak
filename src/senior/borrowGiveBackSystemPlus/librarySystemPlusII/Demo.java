package senior.borrowGiveBackSystemPlus.librarySystemPlusII;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;

public class Demo {
    public static void main(String[] args) {
        // 在建立任何 Swing 元件之前，先啟用 FlatLaf 外觀。
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> new LibrarySystemFrame());
    }
}
