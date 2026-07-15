package senior.borrowGiveBackSystemPlus.librarySystemPlusII;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import senior.borrowGiveBackSystemPlus.models.Book;
import senior.borrowGiveBackSystemPlus.models.Category;
import senior.borrowGiveBackSystemPlus.models.Item;
import senior.borrowGiveBackSystemPlus.models.Member;

// LibrarySystemFrame 是 librarySystemPlusII 的主要 Swing 視窗。
// 這個版本的重點是「畫面展示與假資料互動」：
// 1. 使用 CardLayout 控制登入頁與主畫面切換。
// 2. 使用 JTabbedPane 建立借還書、會員管理、書類管理分頁。
// 3. 使用 ArrayList 當作暫時資料來源，示範未連資料庫前的 CRUD 操作流程。
public class LibrarySystemFrame extends JFrame {
    // CardLayout 需要用字串當作每一張卡片的名稱。
    // LOGIN_CARD 表示登入畫面，MAIN_CARD 表示登入後主畫面。
    private static final String LOGIN_CARD = "login";
    private static final String MAIN_CARD = "main";

    // 畫面共用設定，集中成變數後，後續要換字型或版本號只需要改這裡。
    private final String fontName = "SansSerif";
    private final String projectTitle = "librarySystemPlus";
    private final String projectVersion = "2.0";

    // CardLayout 與 rootPanel 組合成「整個視窗的頁面切換容器」。
    private CardLayout cardLayout;
    private JPanel rootPanel;

    // 登入後主畫面上方會用到的元件。
    private JLabel currentUserLabel;
    private JTabbedPane mainTabs;

    // 登入頁用到的輸入欄位與提示訊息。
    private JTextField accountField;
    private JPasswordField passwordField;
    private JLabel loginMessageLabel;

    // 借還書頁面會用到的書籍表格，借書/還書按鈕會讀取目前選取列。
    private JTable bookTable;

    // 這四個 ArrayList 是本版本的假資料來源。
    // 後續接資料庫時，這些資料會改成從 Repository 或 Service 取得。
    private ArrayList<Member> members;
    private ArrayList<Category> categories;
    private ArrayList<Item> items;
    private ArrayList<Book> books;

    // currentMember 記錄目前登入者，用來判斷是否為 admin 與顯示操作訊息。
    private Member currentMember;

    // 建構子負責建立視窗、初始化假資料、組裝登入頁與主畫面。
    public LibrarySystemFrame() {
        //========================Frame 設定===========================
        // 設定視窗標題，標題文字由 projectTitle + projectVersion 組合而成。
        setTitle(getProjectName());

        // 設定使用者按下視窗右上角關閉按鈕時，整個 Java 程式一起結束。
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 設定視窗初始大小，寬 1000、高 680。
        setSize(1000, 680);

        // 讓視窗出現在螢幕中央；null 代表相對於整個螢幕置中。
        setLocationRelativeTo(null);
        //========================Frame 設定===========================


        //========================資料初始化============================
        // 建立會員假資料清單，後面 initMembers() 會把 admin、aa 加進來。
        members = new ArrayList<>();

        // 建立分類假資料清單，後面 initCategories() 會加入小說類、程式類。
        categories = new ArrayList<>();

        // 建立子分類假資料清單，後面 initItems() 會加入 Java、Python、奇幻、歷史。
        items = new ArrayList<>();

        // 建立書籍假資料清單，後面 initBooks() 會加入四本書。
        books = new ArrayList<>();

        // 初始化會員資料，必須先做，因為 initBooks() 會用到會員資料當借閱人。
        initMembers();

        // 初始化分類資料。
        initCategories();

        // 初始化子分類資料。
        initItems();

        // 初始化書籍資料。
        initBooks();
        //========================資料初始化============================


        //========================登入畫面&主畫面========================
        // 建立 CardLayout，讓同一個 rootPanel 可以切換「登入頁」與「主畫面」。
        cardLayout = new CardLayout();

        // 建立最外層容器 rootPanel，並指定它使用 CardLayout 管理子畫面。
        rootPanel = new JPanel(cardLayout);

        // 把登入頁加入 rootPanel，並用 LOGIN_CARD 這個名稱註冊。
        rootPanel.add(createLoginPanel(), LOGIN_CARD);

        // 把主畫面加入 rootPanel，並用 MAIN_CARD 這個名稱註冊。
        rootPanel.add(createMainPanel(), MAIN_CARD);

        // 把 rootPanel 放進 JFrame，這樣視窗內容就會顯示 rootPanel 管理的畫面。
        add(rootPanel);
        //========================登入畫面&主畫面========================

        // 顯示視窗；Swing 元件都建立完後才呼叫。
        setVisible(true);
    }

    // ============================================================
    // 畫面生成區
    // ============================================================

    // 建立登入頁。
    //
    // createLoginPanel() 佈局圖：
    //
    // JPanel page = new JPanel(new BorderLayout())
    //
    // +------------------------------------------------+
    // | NORTH  ----------------------------------------|---> titleLabel：librarySystemPlusII
    // +------------------------------------------------+
    // | CENTER ----------------------------------------|---> formPanel = BoxLayout.Y_AXIS
    // |                                                |
    // |   loginBox ------------------------------------|---> 登入資訊區塊，有邊框
    // |     createFieldPanel("account", accountField) -|---> 帳號輸入列
    // |     createFieldPanel("password", passwordField)|---> 密碼輸入列
    // |     loginMessageLabel -------------------------|---> 登入提示 / 錯誤訊息
    // |     buttonPanel -------------------------------|---> FlowLayout.RIGHT
    // |       loginButton -----------------------------|---> 登入按鈕
    // |                                                |
    // |   hintBox  ------------------------------------|---> 假資料帳密區塊，有邊框
    // |     createLoginHintPanel() --------------------|---> 假帳密提示表格
    // +------------------------------------------------+
    private JPanel createLoginPanel() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBorder(BorderFactory.createEmptyBorder(48, 180, 48, 180));

        JLabel titleLabel = new JLabel(getProjectName(), JLabel.CENTER);
        titleLabel.setFont(new Font(fontName, Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        page.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 18));
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBorder(BorderFactory.createTitledBorder("登入資訊"));
        JPanel hintBox = new JPanel(new BorderLayout());
        hintBox.setBorder(BorderFactory.createTitledBorder("假資料帳密"));

        accountField = new JTextField(18);
        passwordField = new JPasswordField(18);
        accountField.setText(members.get(0).getAccount());
        passwordField.setText(members.get(0).getPasswordHash());
        accountField.setFont(new Font(fontName, Font.PLAIN, 18));
        passwordField.setFont(new Font(fontName, Font.PLAIN, 18));
        loginMessageLabel = new JLabel("請輸入 members 帳號密碼登入");
        loginMessageLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        loginBox.add(createFieldPanel("帳號:", accountField));
        loginBox.add(createFieldPanel("密碼:", passwordField));
        loginBox.add(loginMessageLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton loginButton = new JButton("登入");
        buttonPanel.add(loginButton);
        loginBox.add(buttonPanel);

        hintBox.add(createLoginHintPanel(), BorderLayout.CENTER);
        contentPanel.add(loginBox, BorderLayout.NORTH);
        contentPanel.add(hintBox, BorderLayout.CENTER);
        loginButton.addActionListener(e -> login());

        page.add(contentPanel, BorderLayout.CENTER);
        return page;
    }

    // 建立一列「標籤 + 輸入欄位」。
    // 例如：帳號: [ JTextField ]、密碼: [ JPasswordField ]。
    private JPanel createFieldPanel(String labelText, java.awt.Component field) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(fontName, Font.PLAIN, 18));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        panel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 48));
        return panel;
    }

    // 建立登入頁下方的假帳號提示表格。
    // GridLayout 讓每個欄位寬度平均，初學者較容易看懂資料是如何排成表格。
    private JPanel createLoginHintPanel() {
        JPanel hintPanel = new JPanel(new GridLayout(members.size() + 1, 4, 12, 4));
        hintPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        hintPanel.add(createHintLabel("身分"));
        hintPanel.add(createHintLabel("account"));
        hintPanel.add(createHintLabel("password"));
        hintPanel.add(createHintLabel("登入後權限"));

        for (Member member : members) {
            hintPanel.add(createHintLabel(getMemberRoleName(member)));
            hintPanel.add(createHintValueLabel(member.getAccount()));
            hintPanel.add(createHintValueLabel(member.getPasswordHash()));
            hintPanel.add(createHintLabel(member.isAdmin() ? "借還書、會員管理、書類管理" : "借還書"));
        }

        return hintPanel;
    }

    // 一般提示文字的 JLabel。
    private JLabel createHintLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(fontName, Font.PLAIN, 14));
        return label;
    }

    // 重要提示值的 JLabel，例如帳號與密碼。
    private JLabel createHintValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(fontName, Font.BOLD, 14));
        return label;
    }


    // 建立登入後主畫面。
    //
    // createMainPanel() 佈局圖：
    //
    // JPanel page = new JPanel(new BorderLayout(10, 10))
    //
    // +--------------------------------------------------------------+
    // | page = BorderLayout(10, 10)                                  |
    // |                                                              |
    // |   +------------------------- NORTH -----------------------+  |
    // |   | createHeaderPanel()                                   |--|--------> page.add(createHeaderPanel(), BorderLayout.NORTH)
    // |   |                                                       |  |
    // |   |   headerPanel = BorderLayout(10, 0)                   |--|--------> headerPanel 內部也用 BorderLayout
    // |   |                                                       |  |
    // |   |   +----------------+        +---------------------+   |  |
    // |   |   | WEST           |        | EAST                |   |  |
    // |   |   | titleLabel     |        | currentUserLabel    |   |  |
    // |   |   |                |        | logoutButton        |   |  |
    // |   |   +----------------+        +---------------------+   |  |
    // |   |        |                            |                 |  |
    // |   |        |                            +-----------------|--|--------> 目前登入身分 + 登出按鈕
    // |   |        +----------------------------------------------|--|--------> 系統標題 librarySystemPlus2.0
    // |   +-------------------------------------------------------+  |
    // |                                                              |
    // |   +------------------------ CENTER -----------------------+  |
    // |   | mainTabs = JTabbedPane                                |--|--------> page.add(mainTabs, BorderLayout.CENTER)
    // |   |                                                       |  |
    // |   |   +-------------+ +-------------+ +-------------+     |  |
    // |   |   | TAB 1       | | TAB 2       | | TAB 3       |     |  |
    // |   |   | borrow      | | member      | | bookType    |     |  |
    // |   |   +-------------+ +-------------+ +-------------+     |  |
    // |   |        |               |               |              |  |
    // |   |        |               |               +--------------|--|--------> admin 才加入：書類管理 createBookTypeManageTab()
    // |   |        |               +------------------------------|--|--------> admin 才加入：會員管理 createMemberManageTab()
    // |   |        +----------------------------------------------|--|--------> 預設一定加入：借還書 createBorrowTab()
    // |   +-------------------------------------------------------+  |
    // +--------------------------------------------------------------+
    private JPanel createMainPanel() {
        JPanel page = new JPanel(new BorderLayout(10, 10));
        page.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        page.add(createHeaderPanel(), BorderLayout.NORTH);

        mainTabs = new JTabbedPane();
        mainTabs.addTab("借還書", createBorrowTab());
        page.add(mainTabs, BorderLayout.CENTER);

        return page;
    }

    // 建立主畫面上方 header。
    // 左側是系統標題，右側是目前登入身分與登出按鈕。
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));

        JLabel titleLabel = new JLabel(getProjectName());
        titleLabel.setFont(new Font(fontName, Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        currentUserLabel = new JLabel("目前身分：未登入");
        JButton logoutButton = new JButton("登出");
        logoutButton.addActionListener(e -> {
            logout();
            resetMainTabs(false);
        });
        userPanel.add(currentUserLabel);
        userPanel.add(logoutButton);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    // Tab 1：借還書 createBorrowTab() 佈局圖：
    //
    // JPanel tab = new JPanel(new BorderLayout(0, 10))
    //
    // +--------------------------------------------------------------+
    // | NORTH -------------------------------------------------------|--------> tab.add(createSearchPanel(), BorderLayout.NORTH)
    // |                                                              |
    // |   searchPanel = FlowLayout.LEFT -----------------------------|--------> 搜尋列由左往右排列
    // |                                                              |
    // |   +----------+ +--------------+ +----------+ +-------------+ |
    // |   | keyword  | | categoryBox  | | search   | | refresh     | |
    // |   +----------+ +--------------+ +----------+ +-------------+ |
    // |       |              |              |              |         |
    // |       |              |              |              +---------|--------> 重新整理按鈕
    // |       |              |              +------------------------|--------> 搜尋按鈕
    // |       |              +---------------------------------------|--------> 分類下拉選單
    // |       +------------------------------------------------------|--------> 關鍵字輸入欄位
    // +--------------------------------------------------------------+
    // | CENTER ------------------------------------------------------|--------> tab.add(createBookTable(), BorderLayout.CENTER)
    // |                                                              |
    // |   +------------------------------------------------------+   |
    // |   | JScrollPane                                          | --|--------> 表格外層滾動容器
    // |   |   +-----------------------------------------------+  |   |
    // |   |   | JTable                                        | -|------------> 書籍列表
    // |   |   |-----------------------------------------------|  |   |
    // |   |   | id | title | author | category | item | status|  | --|--------> 欄位：編號 / 書名 / 作者 / 分類 / 子分類 / 狀態
    // |   |   |-----------------------------------------------|  |   |
    // |   |   | 1  | Java  | xx     | xxx      | xx   | xxx   |  |   |
    // |   |   | 2  | Python| xx     | xxx      | xx   | xxx   |  |   |
    // |   |   +-----------------------------------------------+  |   |
    // |   +------------------------------------------------------+   |
    // |                                                              |
    // +--------------------------------------------------------------+
    // | SOUTH -------------------------------------------------------|--------> tab.add(createBorrowOperatePanel(), BorderLayout.SOUTH)
    // |                                                              |
    // |   createBorrowOperatePanel() = BorderLayout -----------------|--------> 借還書操作區
    // |                                                              |
    // |   +--------------------- NORTH ---------------------------+  |
    // |   |                         [ borrowButton ] [ giveBack ] |  |
    // |   +-------------------------------------------------------+  |
    // |                                   |              |           |
    // |                                   |              +-----------|--------> 還書按鈕
    // |                                   +--------------------------|--------> 借書按鈕
    // |                                                              |
    // |   +--------------------- SOUTH ---------------------------+  |
    // |   | summaryLabel                                          |--|--------> 目前共有 / 可借閱 / 已借出 統計
    // |   +-------------------------------------------------------+  |
    // +--------------------------------------------------------------+
    private JPanel createBorrowTab() {
        JPanel tab = createTabContentPanel();
        tab.add(createSearchPanel(), BorderLayout.NORTH);
        tab.add(createBookTable(), BorderLayout.CENTER);
        tab.add(createBorrowOperatePanel(), BorderLayout.SOUTH);
        return tab;
    }

    private JPanel createTabContentPanel() {
        JPanel tab = new JPanel(new BorderLayout(0, 10));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return tab;
    }

    // 建立搜尋列。
    // 目前搜尋與重新整理先用 JOptionPane 顯示監聽事件已觸發，尚未真正過濾資料。
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        JTextField keywordField = new JTextField(18);
        JComboBox<String> categoryComboBox = new JComboBox<String>(buildCategoryOptions());
        JButton searchButton = new JButton("搜尋");
        JButton refreshButton = new JButton("重新整理");

        searchButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "搜尋關鍵字: " + keywordField.getText().trim()
                        + ", 分類: " + categoryComboBox.getSelectedItem()));
        refreshButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "重新整理執行成功"));

        searchPanel.add(new JLabel("關鍵字:"));
        searchPanel.add(keywordField);
        searchPanel.add(new JLabel("分類:"));
        searchPanel.add(categoryComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        return searchPanel;
    }

    // 建立借還書用的書籍表格。
    // createReadonlyTableModel 會讓表格只能選取，不能直接編輯儲存格。
    private JScrollPane createBookTable() {
        String[] columns = { "編號", "書名", "作者", "分類", "子分類", "狀態", "借閱人" };
        bookTable = new JTable(createReadonlyTableModel(buildBookTableData(), columns));
        bookTable.setRowHeight(28);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(bookTable);
    }

    // 建立借書/還書操作區。
    // 使用者先在表格選一本書，再按借書或還書。
    private JPanel createBorrowOperatePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton borrowButton = new JButton("借書");
        JButton giveBackButton = new JButton("還書");

        borrowButton.addActionListener(e -> showBorrowActionMessage("借入"));
        giveBackButton.addActionListener(e -> showBorrowActionMessage("歸還"));

        buttonPanel.add(borrowButton);
        buttonPanel.add(giveBackButton);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JLabel(buildBookSummaryText()), BorderLayout.SOUTH);
        return panel;
    }

    // Tab 2：admin 才加入 會員管理 createMemberManageTab() 佈局圖：
    //
    // JPanel tab = new JPanel(new BorderLayout(0, 10))
    //
    // +--------------------------------------------------------------+
    // | CENTER ------------------------------------------------------|--------> tab.add(new JScrollPane(memberTable), BorderLayout.CENTER)
    // |                                                              |
    // |   +------------------------------------------------------+   |
    // |   | JTable                                               | --|--------> 會員列表
    // |   |------------------------------------------------------|   |
    // |   | memberId | account | displayName | isAdmin           | --|--------> 欄位：會員ID / 帳號 / 顯示姓名 / 是否管理員
    // |   |------------------------------------------------------|   |
    // |   |   1      | admin   | xxxxx1      | Yes               |   |
    // |   |   2      | aa      | xxxxx2      | No                |   |
    // |   +------------------------------------------------------+   |
    // |                                                              |
    // +--------------------------------------------------------------+
    // | SOUTH  ------------------------------------------------------|--------> tab.add(buttonPanel, BorderLayout.SOUTH)
    // |                                                              |
    // |   buttonPanel = FlowLayout.LEFT -----------------------------|--------> 按鈕由左往右排列
    // |                                                              |
    // |   +------------+           +------------------+              |
    // |   | addButton  |           | updateInfoButton |              |
    // |   +------------+           +------------------+              |
    // |       |    +----------------------+    |    +--------------+ |
    // |       |    | updatePasswordButton |    |    | deleteButton | |
    // |       |    +----------------------+    |    +--------------+ |
    // |       |              |                 |              |      |
    // |       |              |                 |              +------|--------> 刪除會員
    // |       |              |                 +---------------------|--------> 修改會員資料
    // |       |              +---------------------------------------|--------> 修改會員密碼
    // |       +------------------------------------------------------|--------> 新增會員
    // +--------------------------------------------------------------+
    private JPanel createMemberManageTab() {
        JPanel tab = createTabContentPanel();
        String[] columns = { "會員ID", "帳號", "顯示姓名", "是否管理員" };
        DefaultTableModel memberTableModel = createReadonlyTableModel(buildMemberTableData(), columns);
        JTable memberTable = new JTable(memberTableModel);
        memberTable.setRowHeight(28);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tab.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton addButton = new JButton("新增會員");
        JButton updateInfoButton = new JButton("修改會員資料");
        JButton updatePasswordButton = new JButton("修改會員密碼");
        JButton deleteButton = new JButton("刪除會員");

        addButton.addActionListener(e -> addMember(memberTableModel, columns));
        updateInfoButton.addActionListener(e -> updateMemberInfo(memberTable, memberTableModel, columns));
        updatePasswordButton.addActionListener(e -> updateMemberPassword(memberTable));
        deleteButton.addActionListener(e -> deleteMember(memberTable, memberTableModel, columns));

        buttonPanel.add(addButton);
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(updatePasswordButton);
        buttonPanel.add(deleteButton);
        tab.add(buttonPanel, BorderLayout.SOUTH);
        return tab;
    }

    // Tab 3：admin 才加入 書類管理 createBookTypeManageTab() 佈局圖：
    //
    // JPanel tab = new JPanel(new BorderLayout(0, 10))
    //
    // +------------------------------------------------+
    // | CENTER ----------------------------------------|--------> tab.add(splitPane, BorderLayout.CENTER)
    // |                                                |
    // |   JSplitPane.VERTICAL_SPLIT -------------------|--------> 上下切割
    // |                                                |
    // |   +----------------------------------------+   |
    // |   | TOP                                    | --|--------> createBookManagePanel()
    // |   |   CENTER: JTable                       | --|--------> 書籍列表
    // |   |   SOUTH : buttons                      | --|--------> 新增 / 修改 / 刪除書籍
    // |   +----------------------------------------+   |
    // |                                                |
    // |   bottomPanel = GridLayout(1, 2)  -------------|--------> 下半部左右各一格
    // |   +-------------------+--------------------+   |
    // |   | LEFT              | RIGHT              |   |
    // |   | JTable + buttons  | JTable + buttons   |   |
    // |   +-------------------+--------------------+   |
    // |     ^                   ^                      |
    // |     |                   |                      |
    // |     |                   +----------------------|--------> createItemManagePanel()：子分類管理
    // |     +------------------------------------------|--------> createCategoryManagePanel()：分類管理
    // +------------------------------------------------+
    private JPanel createBookTypeManageTab() {
        JPanel tab = createTabContentPanel();
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createBookManagePanel(), bottomWrapper);

        bottomPanel.add(createCategoryManagePanel());
        bottomPanel.add(createItemManagePanel());
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomWrapper.add(bottomPanel, BorderLayout.CENTER);

        splitPane.setResizeWeight(0.55);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        tab.add(splitPane, BorderLayout.CENTER);
        return tab;
    }

    // 建立書籍管理區塊。
    // JTable 顯示書籍資料；下方按鈕示範書籍 CRUD 事件。
    private JPanel createBookManagePanel() {
        String[] columns = { "編號", "書名", "作者", "分類", "子分類" };
        DefaultTableModel tableModel = createReadonlyTableModel(buildBookManageTableData(), columns);
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("書籍管理"));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        JButton addButton = new JButton("新增書籍");
        JButton updateButton = new JButton("修改書籍");
        JButton deleteButton = new JButton("刪除書籍");
        addButton.addActionListener(e -> addBook(tableModel, columns));
        updateButton.addActionListener(e -> updateBook(table, tableModel, columns));
        deleteButton.addActionListener(e -> deleteBook(table, tableModel, columns));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JLabel(""));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 建立分類管理區塊。
    // categories 目前只有 id/name，分類不再包含 code。
    private JPanel createCategoryManagePanel() {
        String[] columns = { "分類ID", "名稱" };
        DefaultTableModel tableModel = createReadonlyTableModel(buildCategoryTableData(), columns);
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("分類管理"));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        JButton addButton = new JButton("新增分類");
        JButton updateButton = new JButton("修改分類");
        JButton deleteButton = new JButton("刪除分類");
        addButton.addActionListener(e -> addCategory(tableModel, columns));
        updateButton.addActionListener(e -> updateCategory(table, tableModel, columns));
        deleteButton.addActionListener(e -> deleteCategory(table, tableModel, columns));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JLabel(""));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 建立子分類管理區塊。
    // 子分類資料會保存 categoryId，用來表示它屬於哪一個分類。
    private JPanel createItemManagePanel() {
        String[] columns = { "子分類ID", "分類ID", "名稱" };
        DefaultTableModel tableModel = createReadonlyTableModel(buildItemTableData(), columns);
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("子分類管理"));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        JButton addButton = new JButton("新增子分類");
        JButton updateButton = new JButton("修改子分類");
        JButton deleteButton = new JButton("刪除子分類");
        addButton.addActionListener(e -> addItem(tableModel, columns));
        updateButton.addActionListener(e -> updateItem(table, tableModel, columns));
        deleteButton.addActionListener(e -> deleteItem(table, tableModel, columns));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JLabel(""));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 建立書籍新增/修改共用表單。
    // 同一個表單方法可同時支援新增與修改，差別只在傳入欄位是否已先填值。
    private JPanel createBookFormPanel(JTextField numberField, JTextField titleField, JTextField authorField,
            JComboBox<String> categoryComboBox, JComboBox<String> itemComboBox) {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.add(new JLabel("編號："));
        formPanel.add(numberField);
        formPanel.add(new JLabel("書名："));
        formPanel.add(titleField);
        formPanel.add(new JLabel("作者："));
        formPanel.add(authorField);
        formPanel.add(new JLabel("分類："));
        formPanel.add(categoryComboBox);
        formPanel.add(new JLabel("子分類："));
        formPanel.add(itemComboBox);
        return formPanel;
    }

    // 建立分類表單，目前只有分類名稱。
    private JPanel createCategoryFormPanel(JTextField nameField) {
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        formPanel.add(new JLabel("名稱："));
        formPanel.add(nameField);
        return formPanel;
    }

    // 建立子分類表單，需要選所屬分類並填入名稱。
    private JPanel createItemFormPanel(JComboBox<String> categoryComboBox, JTextField nameField) {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.add(new JLabel("分類："));
        formPanel.add(categoryComboBox);
        formPanel.add(new JLabel("名稱："));
        formPanel.add(nameField);
        return formPanel;
    }

    // ============================================================
    // 動作處理區
    // ============================================================


    // 借書/還書的暫時事件處理。
    // 目前只顯示「會員 xxx 借入/歸還 xxx 書」，尚未真的改變 books 狀態。
    private void showBorrowActionMessage(String action) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "請先選擇一本書");
            return;
        }

        int modelRow = bookTable.convertRowIndexToModel(selectedRow);
        String bookName = bookTable.getModel().getValueAt(modelRow, 1).toString();
        JOptionPane.showMessageDialog(this, "會員 " + currentMember.getDisplayName() + " " + action + "「" + bookName + "」書");
    }

    // 新增書籍。
    // 使用 JOptionPane.showConfirmDialog 顯示表單，按 OK 後才把資料加入 books。
    private void addBook(DefaultTableModel tableModel, String[] columns) {
        JTextField numberField = new JTextField(String.valueOf(getNextBookNumber()), 12);
        JTextField titleField = new JTextField(12);
        JTextField authorField = new JTextField(12);
        JComboBox<String> categoryComboBox = new JComboBox<String>(buildCategoryIdOptions());
        JComboBox<String> itemComboBox = new JComboBox<String>(buildItemIdOptions());

        JPanel formPanel = createBookFormPanel(numberField, titleField, authorField, categoryComboBox, itemComboBox);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "新增書籍", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String number = numberField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        if (number.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "編號、書名、作者都必填");
            return;
        }

        if (findBookByNumber(number) != null) {
            JOptionPane.showMessageDialog(this, "書籍編號已存在");
            return;
        }

        int categoryId = parseId(categoryComboBox.getSelectedItem());
        int itemId = parseId(itemComboBox.getSelectedItem());
        Category category = findCategoryById(categoryId);
        books.add(new Book(number, title, author, true, "", 0, categoryId, itemId,
                category == null ? "" : category.getName(),
                findItemName(itemId)));
        reloadBookManageTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "新增書籍執行成功");
    }

    // 修改書籍。
    // 先從表格取得選取的 Book，再把原資料帶入表單，按 OK 後替換 books 內的舊資料。
    private void updateBook(JTable table, DefaultTableModel tableModel, String[] columns) {
        Book book = getSelectedBook(table);
        if (book == null) {
            return;
        }

        JTextField numberField = new JTextField(book.getNumber(), 12);
        JTextField titleField = new JTextField(book.getTitle(), 12);
        JTextField authorField = new JTextField(book.getAuthor(), 12);
        JComboBox<String> categoryComboBox = new JComboBox<String>(buildCategoryIdOptions());
        JComboBox<String> itemComboBox = new JComboBox<String>(buildItemIdOptions());
        selectId(categoryComboBox, book.getCategoryId());
        selectId(itemComboBox, book.getItemId());

        JPanel formPanel = createBookFormPanel(numberField, titleField, authorField, categoryComboBox, itemComboBox);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "修改書籍", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String number = numberField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        if (number.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "編號、書名、作者都必填");
            return;
        }

        Book sameNumberBook = findBookByNumber(number);
        if (sameNumberBook != null && !sameNumberBook.getNumber().equals(book.getNumber())) {
            JOptionPane.showMessageDialog(this, "書籍編號已存在");
            return;
        }

        int categoryId = parseId(categoryComboBox.getSelectedItem());
        int itemId = parseId(itemComboBox.getSelectedItem());
        Category category = findCategoryById(categoryId);
        replaceBook(new Book(number, title, author, book.isAvailable(), book.getBorrowUser(),
                book.getBorrowMemberId(), categoryId, itemId,
                category == null ? "" : category.getName(),
                findItemName(itemId)), book.getNumber());
        reloadBookManageTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "修改書籍執行成功");
    }

    // 刪除書籍。
    // 刪除前先跳確認視窗，避免使用者誤刪。
    private void deleteBook(JTable table, DefaultTableModel tableModel, String[] columns) {
        Book book = getSelectedBook(table);
        if (book == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "確認刪除書籍「" + book.getTitle() + "」？",
                "刪除書籍",
                JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        books.remove(book);
        reloadBookManageTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "刪除書籍執行成功");
    }

    // 新增分類。
    // 分類目前只需要輸入名稱。
    private void addCategory(DefaultTableModel tableModel, String[] columns) {
        JTextField nameField = new JTextField(12);
        JPanel formPanel = createCategoryFormPanel(nameField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "新增分類", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "名稱必填");
            return;
        }

        categories.add(new Category(getNextCategoryId(), name));
        reloadCategoryTable(tableModel, columns);
        JOptionPane.showMessageDialog(this, "新增分類執行成功");
    }

    // 修改分類。
    // 選取分類後，表單會帶入原本名稱；確認後替換 categories 內的資料。
    private void updateCategory(JTable table, DefaultTableModel tableModel, String[] columns) {
        Category category = getSelectedCategory(table);
        if (category == null) {
            return;
        }

        JTextField nameField = new JTextField(category.getName(), 12);
        JPanel formPanel = createCategoryFormPanel(nameField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "修改分類", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "名稱必填");
            return;
        }

        replaceCategory(new Category(category.getId(), name));
        reloadCategoryTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "修改分類執行成功");
    }

    // 刪除分類。
    // 目前是畫面版示範，所以只從 categories 移除，未處理資料庫外鍵限制。
    private void deleteCategory(JTable table, DefaultTableModel tableModel, String[] columns) {
        Category category = getSelectedCategory(table);
        if (category == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "確認刪除分類「" + category.getName() + "」？",
                "刪除分類",
                JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        categories.remove(category);
        reloadCategoryTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "刪除分類執行成功");
    }

    // 新增子分類。
    // 表單需要選擇所屬分類，再輸入子分類名稱。
    private void addItem(DefaultTableModel tableModel, String[] columns) {
        JComboBox<String> categoryComboBox = new JComboBox<String>(buildCategoryIdOptions());
        JTextField nameField = new JTextField(12);
        JPanel formPanel = createItemFormPanel(categoryComboBox, nameField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "新增子分類", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "名稱必填");
            return;
        }

        items.add(new Item(getNextItemId(), parseId(categoryComboBox.getSelectedItem()), name));
        reloadItemTable(tableModel, columns);
        JOptionPane.showMessageDialog(this, "新增子分類執行成功");
    }

    // 修改子分類。
    // 選取子分類後，可以調整所屬分類與名稱。
    private void updateItem(JTable table, DefaultTableModel tableModel, String[] columns) {
        Item item = getSelectedItem(table);
        if (item == null) {
            return;
        }

        JComboBox<String> categoryComboBox = new JComboBox<String>(buildCategoryIdOptions());
        selectId(categoryComboBox, item.getCategoryId());
        JTextField nameField = new JTextField(item.getName(), 12);
        JPanel formPanel = createItemFormPanel(categoryComboBox, nameField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "修改子分類", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "名稱必填");
            return;
        }

        replaceItem(new Item(item.getId(), parseId(categoryComboBox.getSelectedItem()), name));
        reloadItemTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "修改子分類執行成功");
    }

    // 刪除子分類。
    // 刪除前先確認，確認後才從 items 移除。
    private void deleteItem(JTable table, DefaultTableModel tableModel, String[] columns) {
        Item item = getSelectedItem(table);
        if (item == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "確認刪除子分類「" + item.getName() + "」？",
                "刪除子分類",
                JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        items.remove(item);
        reloadItemTable(tableModel, columns);
        reloadBorrowBookTable();
        JOptionPane.showMessageDialog(this, "刪除子分類執行成功");
    }

    // 建立書籍新增/修改共用表單。
    // 同一個表單方法可同時支援新增與修改，差別只在傳入欄位是否已先填值。


    // 新增會員。
    // 表單包含帳號、顯示姓名、密碼、是否管理員。
    private void addMember(DefaultTableModel memberTableModel, String[] columns) {
        JTextField accountTextField = new JTextField(12);
        JTextField displayNameTextField = new JTextField(12);
        JPasswordField passwordTextField = new JPasswordField(12);
        JCheckBox adminCheckBox = new JCheckBox("管理員");

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.add(new JLabel("帳號："));
        formPanel.add(accountTextField);
        formPanel.add(new JLabel("顯示姓名："));
        formPanel.add(displayNameTextField);
        formPanel.add(new JLabel("密碼："));
        formPanel.add(passwordTextField);
        formPanel.add(new JLabel("是否管理員："));
        formPanel.add(adminCheckBox);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "新增會員", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String account = accountTextField.getText().trim();
        String displayName = displayNameTextField.getText().trim();
        String password = new String(passwordTextField.getPassword());
        if (account.isEmpty() || displayName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "帳號、顯示姓名、密碼都必填");
            return;
        }

        if (findMemberByAccount(account) != null) {
            JOptionPane.showMessageDialog(this, "帳號已存在");
            return;
        }

        members.add(new Member(getNextMemberId(), account, displayName, password, adminCheckBox.isSelected()));
        reloadMemberTable(memberTableModel, columns);
        JOptionPane.showMessageDialog(this, "新增會員執行成功");
    }

    // 修改會員基本資料。
    // 密碼不在這裡修改，密碼由 updateMemberPassword 專門負責。
    private void updateMemberInfo(JTable memberTable, DefaultTableModel memberTableModel, String[] columns) {
        Member member = getSelectedMember(memberTable);
        if (member == null) {
            return;
        }

        JTextField accountTextField = new JTextField(member.getAccount(), 12);
        JTextField displayNameTextField = new JTextField(member.getDisplayName(), 12);
        JCheckBox adminCheckBox = new JCheckBox("管理員", member.isAdmin());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.add(new JLabel("帳號："));
        formPanel.add(accountTextField);
        formPanel.add(new JLabel("顯示姓名："));
        formPanel.add(displayNameTextField);
        formPanel.add(new JLabel("是否管理員："));
        formPanel.add(adminCheckBox);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "修改會員資料", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String account = accountTextField.getText().trim();
        String displayName = displayNameTextField.getText().trim();
        if (account.isEmpty() || displayName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "帳號、顯示姓名都必填");
            return;
        }

        Member sameAccountMember = findMemberByAccount(account);
        if (sameAccountMember != null && sameAccountMember.getId() != member.getId()) {
            JOptionPane.showMessageDialog(this, "帳號已存在");
            return;
        }

        replaceMember(new Member(member.getId(), account, displayName, member.getPasswordHash(), adminCheckBox.isSelected()));
        reloadMemberTable(memberTableModel, columns);
        JOptionPane.showMessageDialog(this, "修改會員資料執行成功");
    }

    // 修改會員密碼。
    // 這裡只更新 passwordHash 欄位，其它會員資料維持不變。
    private void updateMemberPassword(JTable memberTable) {
        Member member = getSelectedMember(memberTable);
        if (member == null) {
            return;
        }

        JPasswordField passwordTextField = new JPasswordField(12);
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        formPanel.add(new JLabel("新密碼:"));
        formPanel.add(passwordTextField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "修改會員密碼", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordTextField.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "新密碼必填");
            return;
        }

        replaceMember(new Member(member.getId(), member.getAccount(), member.getDisplayName(), password, member.isAdmin()));
        JOptionPane.showMessageDialog(this, "修改會員密碼執行成功");
    }

    // 刪除會員。
    // 刪除前先確認，確認後從 members 清單移除。
    private void deleteMember(JTable memberTable, DefaultTableModel memberTableModel, String[] columns) {
        Member member = getSelectedMember(memberTable);
        if (member == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "確認刪除會員「" + member.getDisplayName() + "」？",
                "刪除會員",
                JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        members.remove(member);
        reloadMemberTable(memberTableModel, columns);
        JOptionPane.showMessageDialog(this, "刪除會員執行成功");
    }

    // 登入按鈕事件。
    // 先檢查帳密是否空白，再用 members 清單比對帳號密碼。
    private void login() {
        String account = accountField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (account.isEmpty()) {
            loginMessageLabel.setText("請輸入帳號");
            return;
        }

        if (password.isEmpty()) {
            loginMessageLabel.setText("請輸入密碼");
            return;
        }

        Member member = findMember(account, password);
        if (member != null) {
            showMainPage(member);
            return;
        }

        loginMessageLabel.setText("帳號或密碼錯誤，請使用假資料登入");
    }

    // 登入成功後切換到主畫面。
    // admin 會看到會員管理與書類管理，一般會員只看到借還書。
    private void showMainPage(Member member) {
        currentMember = member;
        currentUserLabel.setText("目前身分：" + member.getAccount() + getMemberRoleDisplayText(member));
        resetMainTabs(member.isAdmin());
        mainTabs.setSelectedIndex(0);
        cardLayout.show(rootPanel, MAIN_CARD);
    }

    // 登出事件。
    // 清空登入欄位、重設目前會員，並切回登入頁。
    private void logout() {
        accountField.setText("");
        passwordField.setText("");
        loginMessageLabel.setText("已登出，請重新登入");
        currentUserLabel.setText("目前身分：未登入");
        currentMember = null;
        cardLayout.show(rootPanel, LOGIN_CARD);
    }

    // 根據是否為 admin 重新建立可見分頁。
    // 先保留第一個借還書分頁，再依權限決定是否加入管理分頁。
    private void resetMainTabs(boolean admin) {
        while (mainTabs.getTabCount() > 1) {
            mainTabs.removeTabAt(1);
        }

        if (admin) {
            mainTabs.addTab("會員管理", createMemberManageTab());
            mainTabs.addTab("書類管理", createBookTypeManageTab());
        }
    }

    // ============================================================
    // 資料與工具方法區
    // ============================================================


    // 初始化假會員資料。
    // passwordHash 在這個 II 畫面版先放明碼，主要用來示範登入流程。
    private void initMembers() {
        members.add(new Member(1, "admin", "管理員", "admin123", true));
        members.add(new Member(2, "aa", "AA會員", "aa123", false));
    }

    // 初始化分類資料。現在分類只保留 id 與 name，不再使用 code。
    private void initCategories() {
        categories.add(new Category(1, "小說類"));
        categories.add(new Category(2, "程式類"));
    }

    // 初始化子分類資料。
    // categoryId 用來表示這個子分類屬於哪個分類。
    private void initItems() {
        items.add(new Item(1, 2, "Java"));
        items.add(new Item(2, 2, "Python"));
        items.add(new Item(3, 1, "奇幻"));
        items.add(new Item(4, 1, "歷史"));
    }

    // 初始化書籍資料。
    // Book 內同時放 categoryId/itemId 與分類名稱/子分類名稱，方便畫面表格直接顯示。
    private void initBooks() {
        books.add(new Book("1", "Java入門", "張三", false, members.get(0).getDisplayName(), 2, 2, 1, "程式類", "Java"));
        books.add(new Book("2", "Python程式設計", "王五", true, "", 0, 2, 2, "程式類", "Python"));
        books.add(new Book("3", "哈利波特", "J.K.Rowling", true, "", 0, 1, 3, "小說類", "奇幻"));
        books.add(new Book("4", "達文西密碼", "丹布朗", true, "", 0, 1, 4, "小說類", "歷史"));
    }

    // 專案名稱 = 專案主名稱 + 版本號。
    // 例如 librarySystemPlus + II = librarySystemPlusII。
    private String getProjectName() {
        return projectTitle + projectVersion;
    }

    // 將 categories 轉成 JComboBox 選項。
    // 第一個固定放「全部」，後面才是每個分類名稱。
    private String[] buildCategoryOptions() {
        String[] options = new String[categories.size() + 1];
        options[0] = "全部";
        for (int i = 0; i < categories.size(); i++) {
            options[i + 1] = categories.get(i).getName();
        }
        return options;
    }

    // 將 books 轉成借還書表格使用的二維陣列。
    // JTable 的 DefaultTableModel 需要 Object[][] 當作資料列。
    private Object[][] buildBookTableData() {
        Object[][] data = new Object[books.size()][7];
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getNumber();
            data[i][1] = book.getTitle();
            data[i][2] = book.getAuthor();
            data[i][3] = findCategoryName(book.getCategoryId());
            data[i][4] = findItemName(book.getItemId());
            data[i][5] = book.isAvailable() ? "可借閱" : "已借出";
            data[i][6] = book.getBorrowUser();
        }
        return data;
    }

    // 將 books 轉成書籍管理表格資料。
    // 管理頁不需要顯示狀態與借閱人，所以欄位比借還書頁少。
    private Object[][] buildBookManageTableData() {
        Object[][] data = new Object[books.size()][5];
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getNumber();
            data[i][1] = book.getTitle();
            data[i][2] = book.getAuthor();
            data[i][3] = findCategoryName(book.getCategoryId());
            data[i][4] = findItemName(book.getItemId());
        }
        return data;
    }

    // 將 categories 轉成分類管理表格資料。
    private Object[][] buildCategoryTableData() {
        Object[][] data = new Object[categories.size()][2];
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            data[i][0] = category.getId();
            data[i][1] = category.getName();
        }
        return data;
    }

    // 將 members 轉成會員管理表格資料。
    private Object[][] buildMemberTableData() {
        Object[][] data = new Object[members.size()][4];
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            data[i][0] = member.getId();
            data[i][1] = member.getAccount();
            data[i][2] = member.getDisplayName();
            data[i][3] = member.isAdmin() ? "是" : "否";
        }
        return data;
    }

    // 將 items 轉成子分類管理表格資料。
    private Object[][] buildItemTableData() {
        Object[][] data = new Object[items.size()][3];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            data[i][0] = item.getId();
            data[i][1] = item.getCategoryId();
            data[i][2] = item.getName();
        }
        return data;
    }

    // 計算借還書頁下方統計文字。
    // 因為資料都在 books 裡，所以統計也直接從 books 計算。
    private String buildBookSummaryText() {
        int availableCount = 0;
        for (Book book : books) {
            if (book.isAvailable()) {
                availableCount++;
            }
        }

        return "目前共有：" + books.size() + " 本書 可借閱：" + availableCount + " 本 已借出："
                + (books.size() - availableCount) + " 本";
    }

    // 用 categoryId 找分類名稱，讓表格可以顯示中文分類。
    private String findCategoryName(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return category.getName();
            }
        }

        return "";
    }

    // 用 itemId 找子分類名稱，讓表格可以顯示子分類。
    private String findItemName(int itemId) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item.getName();
            }
        }

        return "";
    }

    // 新增書籍。
    // 使用 JOptionPane.showConfirmDialog 顯示表單，按 OK 後才把資料加入 books。

    // 建立分類下拉選單的顯示文字。
    // 格式是「id - name」，後續 parseId 可以把 id 取出來。
    private String[] buildCategoryIdOptions() {
        String[] options = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            options[i] = category.getId() + " - " + category.getName();
        }
        return options;
    }

    // 建立子分類下拉選單的顯示文字。
    private String[] buildItemIdOptions() {
        String[] options = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            options[i] = item.getId() + " - " + item.getName();
        }
        return options;
    }

    // 從「id - name」格式中取出 id。
    // 例如「2 - 程式類」會回傳 2。
    private int parseId(Object selectedItem) {
        if (selectedItem == null) {
            return 0;
        }

        String text = selectedItem.toString();
        int dashIndex = text.indexOf(" - ");
        if (dashIndex < 0) {
            return Integer.parseInt(text);
        }

        return Integer.parseInt(text.substring(0, dashIndex));
    }

    // 讓 JComboBox 選到指定 id 的項目。
    // 修改資料時會用這個方法把原本分類或子分類選回來。
    private void selectId(JComboBox<String> comboBox, int id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (parseId(comboBox.getItemAt(i)) == id) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    // 從書籍管理表格取得目前選取的 Book。
    // 表格只放字串資料，真正的物件仍然從 books 清單查回來。
    private Book getSelectedBook(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "請先選擇一本書");
            return null;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        String number = table.getModel().getValueAt(modelRow, 0).toString();
        Book book = findBookByNumber(number);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "找不到選取的書籍");
        }
        return book;
    }

    // 從分類管理表格取得目前選取的 Category。
    private Category getSelectedCategory(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "請先選擇一個分類");
            return null;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int categoryId = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
        Category category = findCategoryById(categoryId);
        if (category == null) {
            JOptionPane.showMessageDialog(this, "找不到選取的分類");
        }
        return category;
    }

    // 從子分類管理表格取得目前選取的 Item。
    private Item getSelectedItem(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "請先選擇一個子分類");
            return null;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int itemId = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
        Item item = findItemById(itemId);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "找不到選取的子分類");
        }
        return item;
    }

    // 用書籍編號尋找 Book。
    private Book findBookByNumber(String number) {
        for (Book book : books) {
            if (book.getNumber().equals(number)) {
                return book;
            }
        }
        return null;
    }

    // 用分類 ID 尋找 Category。
    private Category findCategoryById(int categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    // 用子分類 ID 尋找 Item。
    private Item findItemById(int itemId) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    // 用新 Book 取代 books 清單內指定舊編號的資料。
    // oldNumber 是為了支援修改時連書籍編號也一起改。
    private void replaceBook(Book newBook, String oldNumber) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getNumber().equals(oldNumber)) {
                books.set(i, newBook);
                return;
            }
        }
    }

    // 用新 Category 取代 categories 清單內相同 id 的資料。
    private void replaceCategory(Category newCategory) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == newCategory.getId()) {
                categories.set(i, newCategory);
                return;
            }
        }
    }

    // 用新 Item 取代 items 清單內相同 id 的資料。
    private void replaceItem(Item newItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == newItem.getId()) {
                items.set(i, newItem);
                return;
            }
        }
    }

    // 取得下一個書籍編號。
    // 這裡假設書籍編號都是數字字串。
    private int getNextBookNumber() {
        int maxNumber = 0;
        for (Book book : books) {
            int number = Integer.parseInt(book.getNumber());
            if (number > maxNumber) {
                maxNumber = number;
            }
        }
        return maxNumber + 1;
    }

    // 取得下一個分類 ID。
    private int getNextCategoryId() {
        int maxId = 0;
        for (Category category : categories) {
            if (category.getId() > maxId) {
                maxId = category.getId();
            }
        }
        return maxId + 1;
    }

    // 取得下一個子分類 ID。
    private int getNextItemId() {
        int maxId = 0;
        for (Item item : items) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        return maxId + 1;
    }

    // 重新載入書籍管理表格。
    private void reloadBookManageTable(DefaultTableModel tableModel, String[] columns) {
        tableModel.setDataVector(buildBookManageTableData(), columns);
    }

    // 重新載入分類管理表格。
    private void reloadCategoryTable(DefaultTableModel tableModel, String[] columns) {
        tableModel.setDataVector(buildCategoryTableData(), columns);
    }

    // 重新載入子分類管理表格。
    private void reloadItemTable(DefaultTableModel tableModel, String[] columns) {
        tableModel.setDataVector(buildItemTableData(), columns);
    }

    // 重新載入借還書頁面的書籍表格。
    // 當書籍、分類、子分類有變動時，借還書頁也要同步更新顯示。
    private void reloadBorrowBookTable() {
        if (bookTable != null) {
            bookTable.setModel(createReadonlyTableModel(buildBookTableData(),
                    new String[] { "編號", "書名", "作者", "分類", "子分類", "狀態", "借閱人" }));
        }
    }

    // 建立唯讀 TableModel。
    // 預設 JTable 可以雙擊儲存格直接編輯，這裡覆寫 isCellEditable 讓它不能編輯。
    private DefaultTableModel createReadonlyTableModel(Object[][] data, String[] columns) {
        return new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // 新增會員。
    // 表單包含帳號、顯示姓名、密碼、是否管理員。

    // 從會員表格取得目前選取的 Member。
    private Member getSelectedMember(JTable memberTable) {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "請先選擇一位會員");
            return null;
        }

        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        int memberId = Integer.parseInt(memberTable.getModel().getValueAt(modelRow, 0).toString());
        for (Member member : members) {
            if (member.getId() == memberId) {
                return member;
            }
        }

        JOptionPane.showMessageDialog(this, "找不到選取的會員");
        return null;
    }

    // 用 account 尋找會員。
    // 新增/修改會員時用來檢查帳號是否重複。
    private Member findMemberByAccount(String account) {
        for (Member member : members) {
            if (member.getAccount().equals(account)) {
                return member;
            }
        }

        return null;
    }

    // 用新的 Member 物件替換 members 清單內相同 id 的會員。
    // 如果被修改的是目前登入者，也要同步更新 currentMember 與畫面右上角文字。
    private void replaceMember(Member newMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == newMember.getId()) {
                members.set(i, newMember);
                if (currentMember != null && currentMember.getId() == newMember.getId()) {
                    currentMember = newMember;
                    currentUserLabel.setText("目前身分：" + newMember.getAccount()
                            + getMemberRoleDisplayText(newMember));
                }
                return;
            }
        }
    }

    // 取得下一個會員 ID。
    private int getNextMemberId() {
        int maxId = 0;
        for (Member member : members) {
            if (member.getId() > maxId) {
                maxId = member.getId();
            }
        }
        return maxId + 1;
    }

    // 重新載入會員管理表格。
    private void reloadMemberTable(DefaultTableModel memberTableModel, String[] columns) {
        memberTableModel.setDataVector(buildMemberTableData(), columns);
    }

    // 登入按鈕事件。
    // 先檢查帳密是否空白，再用 members 清單比對帳號密碼。

    // 從 members 清單尋找符合帳號密碼的會員。
    private Member findMember(String account, String password) {
        for (Member member : members) {
            if (member.getAccount().equals(account) && member.getPasswordHash().equals(password)) {
                return member;
            }
        }

        return null;
    }

    // 回傳角色名稱，不包含括號。
    // 例如：管理員、一般會員。
    private String getMemberRoleName(Member member) {
        return member.isAdmin() ? "管理員" : "一般會員";
    }

    // 回傳畫面右上角要顯示的角色文字，包含括號。
    // 例如：（管理員）、（一般會員）。
    private String getMemberRoleDisplayText(Member member) {
        return "(" + getMemberRoleName(member) + ")";
    }

}
