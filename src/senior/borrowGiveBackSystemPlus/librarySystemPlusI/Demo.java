package senior.borrowGiveBackSystemPlus.librarySystemPlusI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.FontUIResource;

import com.formdev.flatlaf.FlatLightLaf;

public class Demo {
    /*
    * 畫面佈局簡圖：
    *
    * BorderLayout 會把畫面分成 5 個區域：
    *
    * +---------------------------+
    * |          NORTH            |
    * +-------+-----------+-------+
    * | WEST  |  CENTER   | EAST  |
    * |       |           |       |
    * +-------+-----------+-------+
    * |          SOUTH            |
    * +---------------------------+
    *
    * 本程式實際放置的元件：
    *
    * JFrame
    * +-----------------------------------------------------------------------+
    * | titlePanel                                                            |
    * |                    圖書館借還書系統                                      |
    * +-----------------------------------------------------------------------+
    * | searchPanel                                                           |
    * | 關鍵字：[___________]  分類：[全部▼]  [搜尋]  [重新整理]                   |
    * +-----------------------------------------------------------------------+
    * | tableScrollPane                                                       |
    * | +----------------------------------------------------------------------+
    * | | 編號 | 書名 | 作者 | 分類 | 狀態 | 借閱人                             | |
    * | |---------------------------------------------------------------|   | |
    * | | 1    | Java入門 | 王小明 | 程式 | 可借閱 |                           | |
    * | | 2    | Python   | 李小華 | 程式 | 已借出 | Edward                    | |
    * | | 3    | 三國演義 | 羅貫中 | 小說 | 可借閱 |                            | |
    * | +----------------------------------------------------------------------+
    * +-----------------------------------------------------------------------+
    * | operatePanel                                                          |
    * | 書籍編號：[____]   借閱人：[__________]                                  |
    * |                                                                       |
    * | [借書]   [還書]   [新增書籍]   [修改書籍]   [刪除書籍]                     |
    * +-----------------------------------------------------------------------+
    * | statusPanel                                                           |
    * | 目前共有：20 本書　　可借閱：15 本　　已借出：5 本                           |
    * +-----------------------------------------------------------------------+
    *
    * BorderLayout.NORTH  ：titlePanel + searchPanel
    * BorderLayout.CENTER ：tableScrollPane (JTable)
    * BorderLayout.SOUTH  ：operatePanel + statusPanel
    *
    * titlePanel        ：系統標題
    * searchPanel       ：搜尋、分類、重新整理
    * tableScrollPane   ：JTable 顯示所有書籍
    * operatePanel      ：借書、還書、新增、修改、刪除
    * statusPanel       ：顯示書籍統計資訊
    */

    private static final String UI_FONT_NAME = chooseUiFontName();

    public static void main(String[] args) {
        // 在建立任何 Swing 元件之前，先啟用 FlatLaf 外觀。
        FlatLightLaf.setup();

        // 統一預設字型，避免 macOS 上中文字旁邊的數字顯示不出來。
        setDefaultFont();

        SwingUtilities.invokeLater(() -> createFrame());
    }

    private static void setDefaultFont() {
        FontUIResource font = new FontUIResource(UI_FONT_NAME, Font.PLAIN, 16);
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = defaults.get(key);

            if (value instanceof Font) {
                defaults.put(key, font);
            }
        }
    }

    private static String chooseUiFontName() {
        String[] candidates = {
                "PingFang TC",
                "PingFang SC",
                "Heiti TC",
                "Microsoft JhengHei",
                "Noto Sans CJK TC",
                "Dialog"
        };

        String[] availableFonts = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        for (String candidate : candidates) {
            for (String fontName : availableFonts) {
                if (fontName.equals(candidate)) {
                    return candidate;
                }
            }
        }

        return "Dialog";
    }

    private static void createFrame() {
        JFrame frame = new JFrame("圖書館借還書系統");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 620);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(createTablePanel(), BorderLayout.CENTER);
        frame.add(createBottomPanel(), BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JLabel titleLabel = new JLabel("圖書館借還書系統");
        titleLabel.setFont(new Font(UI_FONT_NAME, Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.add(new JLabel("關鍵字："));
        searchPanel.add(new JTextField(18));
        searchPanel.add(new JLabel("分類："));
        searchPanel.add(new JComboBox<String>(new String[] { "全部", "小說類", "程式類" }));
        searchPanel.add(new JButton("搜尋"));
        searchPanel.add(new JButton("重新整理"));
        headerPanel.add(searchPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private static JScrollPane createTablePanel() {
        String[] columns = { "編號", "書名", "作者", "分類", "子分類", "狀態", "借閱人" };
        Object[][] data = {
                { "1", "Java入門", "張三", "程式類", "Java", "已借出", "aa" },
                { "2", "Python程式設計", "王五", "程式類", "Python", "可借閱", "" },
                { "3", "哈利波特", "J.K.Rowling", "小說類", "奇幻", "可借閱", "" },
                { "4", "達文西密碼", "丹布朗", "小說類", "歷史", "可借閱", "" }
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(28);
        return new JScrollPane(table);
    }

    private static JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        bottomPanel.add(createOperatePanel(), BorderLayout.CENTER);
        bottomPanel.add(createStatusPanel(), BorderLayout.SOUTH);
        return bottomPanel;
    }

    private static JPanel createOperatePanel() {
        JPanel operatePanel = new JPanel(new GridLayout(2, 1, 0, 8));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputPanel.add(new JLabel("書籍編號："));
        inputPanel.add(new JTextField(8));
        inputPanel.add(new JLabel("借閱人："));
        inputPanel.add(new JTextField(12));
        operatePanel.add(inputPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.add(new JButton("借書"));
        buttonPanel.add(new JButton("還書"));
        buttonPanel.add(new JButton("新增書籍"));
        buttonPanel.add(new JButton("修改書籍"));
        buttonPanel.add(new JButton("刪除書籍"));
        operatePanel.add(buttonPanel);

        return operatePanel;
    }

    private static JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        statusPanel.add(new JLabel("目前共有：4 本書　　可借閱：3 本　　已借出：1 本"));
        return statusPanel;
    }
}
