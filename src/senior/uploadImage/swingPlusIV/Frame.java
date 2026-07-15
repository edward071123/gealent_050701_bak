package senior.uploadImage.swingPlusIV;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

/*
 * Frame 程式處理流程與 method 呼叫順序：
 *
 * 一、視窗啟動流程
 * 1. Demo.main()
 * 2. new Frame()
 * 3. Frame() 建立上方工具列、圖片清單、圖片預覽區、下方進度區
 * 4. Frame() 呼叫 loadUploadImages()，先載入 uploads 資料夾既有圖片
 *
 * 二、使用者按「上傳圖片」後
 * 1. uploadImage()
 * 2. chooseImageFiles()
 *    - 開啟 JFileChooser
 *    - 回傳使用者選到的 File[]
 * 3. validateSelectedFiles(sourceFiles)
 *    - 檢查一次最多 MAX_UPLOAD_COUNT 張
 *    - 檢查副檔名必須是 jpg / jpeg / png / gif
 * 4. startUpload(sourceFiles)
 *    - 更新狀態文字
 *    - 停用上傳與重新整理按鈕
 *    - 呼叫 createProgressRows(sourceFiles) 建立每張圖片的進度列
 *    - 呼叫 createUploadListener(...) 建立上傳事件監聽器
 *    - 呼叫 uploadService.uploadMultiple(sourceFiles, listener)
 *
 * 三、多執行緒上傳與畫面更新
 * 1. UploadService.uploadMultiple(...) 為每張圖片建立 Thread
 * 2. 每個 Thread 呼叫 UploadService.upload(...)
 * 3. UploadService 透過 IUploadTaskListener 回報：
 *    - onProgress(...)：更新單張進度列
 *    - onSuccess(...) ：加入左側圖片清單並顯示圖片
 *    - onError(...)   ：標示失敗並顯示錯誤訊息
 *    - onAllFinished(...)：恢復按鈕並顯示總耗時
 * 4. listener 內部用 SwingUtilities.invokeLater(...) 回到 Swing 畫面執行緒更新 UI
 */
public class Frame extends JFrame {

    // 一次最多可以選幾張圖片。
    // 之後如果要改成 10 張，只要改這裡，不用到處找數字。
    private static final int MAX_UPLOAD_COUNT = 5;
    private static final Color PROGRESS_AREA_BACKGROUND = new Color(245, 247, 250);
    private static final Color PROGRESS_ROW_BACKGROUND = Color.WHITE;
    private static final Color PROGRESS_BAR_COLOR = new Color(37, 117, 210);
    private static final Color PROGRESS_TRACK_COLOR = new Color(226, 232, 240);
    private static final Color PROGRESS_TEXT_COLOR = new Color(36, 48, 63);
    private static final Color PROGRESS_DONE_COLOR = new Color(21, 128, 61);
    private static final Color PROGRESS_ERROR_COLOR = new Color(190, 18, 60);

    // 上傳後圖片統一放在這個資料夾。
    // File 代表檔案或資料夾路徑，這裡指向 uploads 資料夾。
    private final File uploadDir = new File("src/senior/uploadImage/uploads");

    // UploadService 負責「檔案判斷」和「檔案複製」。
    // Frame 只負責畫面，不直接處理 FileInputStream / FileOutputStream。
    private final UploadService uploadService = new UploadService(uploadDir);

    // 畫面上的主要元件。
    // JLabel：單純顯示文字或圖片。
    private JLabel statusLabel = new JLabel("狀態：尚未選擇圖片");
    private JLabel fileSizeLabel = new JLabel("大小：-");
    private JLabel imageLabel = new JLabel("尚未選擇圖片", SwingConstants.CENTER);

    // JButton：使用者可以點擊的按鈕。
    private JButton uploadButton = new JButton("上傳圖片");
    private JButton refreshButton = new JButton("重新整理");

    // uploadProgressPanel：放多條進度條。
    // 多執行緒版會同時上傳多張圖片，所以不能只用一條 JProgressBar。
    private JPanel uploadProgressPanel = new JPanel();

    // DefaultListModel：清單資料來源，負責保存要顯示的 File。
    // JList：真正畫在畫面上的清單元件。
    private DefaultListModel<File> imageListModel = new DefaultListModel<>();
    private JList<File> imageList = new JList<>(imageListModel);

    public Frame() {
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
         * +-------------------------------------------------------------------+
         * | topPanel                                                          |
         * | +--------------+--------------------------------+---------------+ |
         * | | uploadButton | infoPanel                      | refreshButton | |
         * | |              | statusLabel + fileSizeLabel    |               | |
         * | +--------------+--------------------------------+---------------+ |
         * +------------------------+------------------------------------------+
         * | splitPane              |                                          |
         * | +--------------------+ | +--------------------------------------+ |
         * | | listScrollPane     | | | imageScrollPane                      | |
         * | | imageList          | | | imageLabel                           | |
         * | | - d1.jpg           | | |                                      | |
         * | | - d2.jpg           | | |                                      | |
         * | +--------------------+ | +--------------------------------------+ |
         * +------------------------+------------------------------------------+
         * | progressScrollPane                                                |
         * | uploadProgressPanel                                               |
         * | +---------------------------------------------------------------+ |
         * | |  1/3  d1.jpg 40%                                              | |
         * | |  2/3  d2.jpg 75%                                              | |
         * | |  3/3  d3.jpg 10%                                              | |
         * | +---------------------------------------------------------------+ |
         * +-------------------------------------------------------------------+
         *
         * BorderLayout.NORTH  ：上方工具列 topPanel
         * BorderLayout.CENTER ：中間左右分割 splitPane
         * BorderLayout.SOUTH  ：下方多進度條區塊 uploadProgressPanel
         * splitPane 左邊      ：圖片清單 imageList
         * splitPane 右邊      ：圖片預覽 imageLabel
         */

        // 視窗基本設定。
        // setTitle：視窗標題。
        // setSize：視窗寬高。
        // setDefaultCloseOperation：按關閉按鈕時結束程式。
        // setLocationRelativeTo(null)：讓視窗出現在螢幕中央。
        setTitle("圖片上傳與瀏覽 - swingPlusIV - 多執行緒版");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 多進度條區塊設定。
        // BoxLayout.Y_AXIS 代表元件會從上到下垂直排列。
        uploadProgressPanel.setLayout(new BoxLayout(uploadProgressPanel, BoxLayout.Y_AXIS));
        uploadProgressPanel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        uploadProgressPanel.setBackground(PROGRESS_AREA_BACKGROUND);

        // 按鈕事件。
        // addActionListener：設定按鈕被點擊後要執行什麼方法。
        uploadButton.addActionListener(e -> uploadImage());
        refreshButton.addActionListener(e -> loadUploadImages());

        // 左側圖片清單一次只能選一張。
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 清單預設會呼叫 File.toString()，可能顯示完整路徑。
        // setCellRenderer 可以自訂每一列要顯示什麼文字。
        // 這裡只顯示檔名，不顯示完整路徑。
        imageList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                // value 是清單裡的一筆資料，因為 imageListModel 放的是 File，
                // 所以這裡可以轉型成 File。
                File file = (File) value;
                setText(file.getName());

                return this;
            }
        });

        // 點選左側圖片時，在右側顯示預覽。
        // ListSelectionListener 是 JList 的選取事件。
        imageList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // 使用者點選時，事件可能會觸發兩次：
                // 一次是正在調整，一次是調整完成。
                // 這裡只處理「調整完成」的那一次。
                if (e.getValueIsAdjusting()) {
                    return;
                }

                // 取得目前被選到的圖片檔案。
                File selectedFile = imageList.getSelectedValue();

                if (selectedFile != null) {
                    showImage(selectedFile);
                }
            }
        });

        // 上方工具列：左邊上傳，中間顯示狀態/大小，右邊重新整理。
        // infoPanel 用 GridLayout(1, 2)，代表 1 列 2 欄。
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fileSizeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        infoPanel.add(statusLabel);
        infoPanel.add(fileSizeLabel);

        // topPanel 使用 BorderLayout：
        // WEST   放 uploadButton
        // CENTER 放 infoPanel
        // EAST   放 refreshButton
        // new BorderLayout(8, 0) 的 8 是左右元件間距。
        JPanel topPanel = new JPanel(new BorderLayout(8, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        topPanel.add(uploadButton, BorderLayout.WEST);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        // 左側清單固定寬度，右側顯示圖片。
        // JScrollPane 讓清單很多筆時可以捲動。
        JScrollPane listScrollPane = new JScrollPane(imageList);
        listScrollPane.setPreferredSize(new Dimension(360, 0));

        // JSplitPane：左右分割畫面。
        // HORIZONTAL_SPLIT 代表水平分割，也就是左邊一區、右邊一區。
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            listScrollPane,
            new JScrollPane(imageLabel)
        );

        // 設定左右分割線的位置。
        splitPane.setDividerLocation(360);

        // 下方進度條區塊可能有多條進度列，所以放進 JScrollPane。
        JScrollPane progressScrollPane = new JScrollPane(uploadProgressPanel);
        progressScrollPane.setPreferredSize(new Dimension(0, 150));
        progressScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        progressScrollPane.getViewport().setBackground(PROGRESS_AREA_BACKGROUND);
        progressScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // JFrame 預設就是 BorderLayout。
        // NORTH 放上方工具列，CENTER 放主要內容，SOUTH 放多進度條區塊。
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(progressScrollPane, BorderLayout.SOUTH);

        // 開啟視窗時先讀取 uploads 裡已經存在的圖片。
        loadUploadImages();

        setVisible(true);
    }

    /*
    * 多執行緒上傳流程：
    * 1. 開啟 JFileChooser 讓使用者選圖片。
    * 2. 允許使用者一次選多張圖片。
    * 3. 檢查選擇數量，最多只能 5 張。
    * 4. 逐張檢查副檔名是不是 jpg/jpeg/png/gif。
    * 5. Frame 每一張圖片建立一條 JProgressBar。
    * 6. Frame 建立 IUploadTaskListener，準備接收上傳事件。
    * 7. Frame 呼叫 uploadService.uploadMultiple(...)。
    * 8. UploadService 建立多個 Thread，同時上傳多張圖片。
    * 9. UploadService 透過 listener 回報進度、成功、失敗、全部完成。
    * 10. Frame 收到 listener 事件後更新畫面。
    */
    private void uploadImage() {
        File[] sourceFiles = chooseImageFiles();

        if (sourceFiles == null || !validateSelectedFiles(sourceFiles)) {
            return;
        }

        startUpload(sourceFiles);
    }

    private File[] chooseImageFiles() {
        // 讓使用者選擇圖片檔。
        JFileChooser chooser = new JFileChooser();

        // 允許一次選擇多個檔案。
        // 如果沒有設定這行，JFileChooser 預設只能選一個檔案。
        chooser.setMultiSelectionEnabled(true);

        chooser.setFileFilter(new FileNameExtensionFilter(
                "圖片檔案 (*.jpg, *.jpeg, *.png, *.gif)",
                "jpg",
                "jpeg",
                "png",
                "gif"
        ));

        // showOpenDialog 會打開檔案選擇視窗。
        // 回傳值用來判斷使用者是選了檔案，還是按取消。
        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        // getSelectedFiles() 會取得使用者選擇的多個檔案。
        File[] sourceFiles = chooser.getSelectedFiles();
        return sourceFiles.length == 0 ? null : sourceFiles;
    }

    private boolean validateSelectedFiles(File[] sourceFiles) {
        // 本範例限制最多一次上傳 5 張，避免初學範例一次處理太多檔案。
        if (sourceFiles.length > MAX_UPLOAD_COUNT) {
            JOptionPane.showMessageDialog(
                    this,
                    "一次最多只能上傳 " + MAX_UPLOAD_COUNT + " 張圖片"
            );
            return false;
        }

        // 先檢查所有檔案。只要其中一個不是圖片，就整批不開始上傳。
        for (File sourceFile : sourceFiles) {
            if (!uploadService.isImageFile(sourceFile)) {
                JOptionPane.showMessageDialog(
                        this,
                        "只能上傳 jpg、jpeg、png、gif 圖片"
                );
                return false;
            }
        }

        return true;
    }

    private void startUpload(File[] sourceFiles) {
        setStatusText("開始多執行緒上傳 " + sourceFiles.length + " 張圖片", null);
        uploadButton.setEnabled(false);
        refreshButton.setEnabled(false);
        /*
        * 記錄開始時間。
        *
        * System.currentTimeMillis() 會取得目前時間，
        * 單位是毫秒。
        *
        * 等全部圖片上傳完成後，
        * 再用「結束時間 - 開始時間」算出總共花幾秒。
        */
        final long startTime = System.currentTimeMillis();
        final UploadProgressRow[] progressRows = createProgressRows(sourceFiles);
        final File[] lastUploadedFile = new File[1];

        IUploadTaskListener listener = createUploadListener(
                progressRows,
                lastUploadedFile,
                startTime
        );

        // Frame 不直接 new Thread，多執行緒上傳交給 UploadService 處理。
        uploadService.uploadMultiple(sourceFiles, listener);
    }

    private UploadProgressRow[] createProgressRows(File[] sourceFiles) {
        // 清空上一批上傳留下來的進度條。
        uploadProgressPanel.removeAll();

        final int totalCount = sourceFiles.length;
        final UploadProgressRow[] progressRows = new UploadProgressRow[sourceFiles.length];

        for (int i = 0; i < sourceFiles.length; i++) {
            final File sourceFile = sourceFiles[i];
            final int currentNumber = i + 1;

            UploadProgressRow progressRow = createUploadProgressRow(
                    sourceFile,
                    currentNumber,
                    totalCount
            );

            progressRows[i] = progressRow;
            uploadProgressPanel.add(progressRow.panel);
        }

        uploadProgressPanel.revalidate();
        uploadProgressPanel.repaint();
        return progressRows;
    }

    private IUploadTaskListener createUploadListener(
            final UploadProgressRow[] progressRows,
            final File[] lastUploadedFile,
            final long startTime
    ) {
        /*
         * IUploadTaskListener 是 Frame 和 UploadService 之間的橋樑。
         * UploadService 負責上傳和 Thread；Frame 收到事件後再更新 Swing 畫面。
         */
        return new IUploadTaskListener() {
            @Override
            public void onProgress(File file, final int index, final int total, final int progress) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        UploadProgressRow progressRow = progressRows[index - 1];
                        setFileProgressText(progressRow, index, total, progress);
                    }
                });
            }

            @Override
            public void onSuccess(
                    File sourceFile,
                    final File targetFile,
                    final int index,
                    final int total
            ) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        UploadProgressRow progressRow = progressRows[index - 1];
                        setFileProgressText(progressRow, index, total, 100);

                        lastUploadedFile[0] = targetFile;
                        addUploadedFileToList(targetFile);
                        showImage(targetFile);
                        imageList.setSelectedValue(targetFile, true);
                        setStatusText("已完成第 " + index + " / " + total + " 張", targetFile);
                    }
                });
            }

            @Override
            public void onError(
                    final File sourceFile,
                    final Exception ex,
                    final int index,
                    final int total
            ) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        UploadProgressRow progressRow = progressRows[index - 1];
                        setFileProgressError(progressRow, index, total);

                        JOptionPane.showMessageDialog(
                                Frame.this,
                                "上傳失敗：" + sourceFile.getName() + "\n" + ex.getMessage()
                        );
                    }
                });
            }

            @Override
            public void onAllFinished(final int total) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        double elapsedSeconds = elapsedTime / 1000.0;

                        uploadButton.setEnabled(true);
                        refreshButton.setEnabled(true);

                        setStatusText(
                                "全部上傳完成，共 " + total + " 張 ("
                                        + String.format("%.1f", elapsedSeconds)
                                        + " 秒)",
                                lastUploadedFile[0]
                        );
                    }
                });
            }
        };
    }

    // 建立單一檔案專用的進度列。
    private UploadProgressRow createUploadProgressRow(File sourceFile, int currentNumber, int totalCount) {
        JProgressBar fileProgressBar = new JProgressBar(0, 100);

        // 文字另外放在 JLabel，進度條只負責顯示百分比，避免文字壓在進度條上。
        fileProgressBar.setStringPainted(false);
        fileProgressBar.setBorderPainted(false);
        fileProgressBar.setOpaque(false);
        fileProgressBar.setForeground(PROGRESS_BAR_COLOR);
        fileProgressBar.setBackground(PROGRESS_TRACK_COLOR);
        fileProgressBar.putClientProperty("JProgressBar.arc", 999);
        fileProgressBar.setValue(0);

        // 每條進度條固定高度，畫面比較整齊。
        fileProgressBar.setPreferredSize(new Dimension(0, 8));
        fileProgressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));

        JLabel progressLabel = new JLabel();
        progressLabel.setFont(progressLabel.getFont().deriveFont(Font.BOLD, 13f));
        progressLabel.setForeground(PROGRESS_TEXT_COLOR);
        progressLabel.setPreferredSize(new Dimension(120, 22));
        progressLabel.setToolTipText(sourceFile.getName());

        JPanel progressRow = new JPanel(new BorderLayout(10, 0));
        progressRow.setBackground(PROGRESS_ROW_BACKGROUND);
        progressRow.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        progressRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        progressRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressRow.add(progressLabel, BorderLayout.WEST);
        progressRow.add(fileProgressBar, BorderLayout.CENTER);

        JPanel rowWrapper = new JPanel(new BorderLayout());
        rowWrapper.setBackground(PROGRESS_AREA_BACKGROUND);
        rowWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        rowWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        rowWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowWrapper.add(progressRow, BorderLayout.CENTER);

        UploadProgressRow uploadProgressRow = new UploadProgressRow(
                rowWrapper,
                progressLabel,
                fileProgressBar
        );
        setFileProgressText(uploadProgressRow, currentNumber, totalCount, 0);

        return uploadProgressRow;
    }

    private void loadUploadImages() {
        /*
         * 載入 uploads 資料夾流程：
         * 1. 清空舊清單。
         * 2. 讀取 uploads 資料夾裡的所有檔案。
         * 3. 只把圖片檔加入 imageListModel。
         * 4. imageList 會自動根據 imageListModel 更新畫面。
         */

        // 重新載入前先清空舊清單，避免重複出現。
        imageListModel.clear();

        // listFiles 會取得資料夾裡的所有檔案。
        File[] files = uploadDir.listFiles();

        if (files == null) {
            return;
        }

        // 只把圖片檔加進清單，非圖片檔不顯示。
        for (File file : files) {
            if (uploadService.isImageFile(file)) {
                imageListModel.addElement(file);
            }
        }

        if (imageListModel.isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("uploads 資料夾目前沒有圖片");
            setStatusText("uploads 資料夾目前沒有圖片", null);
        }
    }

    // 把剛上傳完成的圖片加入左側清單。
    private void addUploadedFileToList(File uploadedFile) {
        /*
         * 為什麼不直接呼叫 loadUploadImages()？
         *
         * loadUploadImages() 會整個清單清空後重新讀取 uploads 資料夾。
         * 這樣也可以，但上傳多張圖片時，每完成一張就重讀整個資料夾，
         * 對初學者來說比較不容易看出「新增一筆」的動作。
         *
         * 這裡只把剛完成的 uploadedFile 加進清單，
         * 比較符合「一張完成，清單馬上多一筆」的排隊感。
         */

        if (uploadedFile == null) {
            return;
        }

        // 避免同一個檔案已經在清單中，又被重複加入一次。
        for (int i = 0; i < imageListModel.size(); i++) {
            File fileInList = imageListModel.getElementAt(i);

            if (fileInList.getAbsolutePath().equals(uploadedFile.getAbsolutePath())) {
                return;
            }
        }

        imageListModel.addElement(uploadedFile);

        // 讓剛加入的檔案捲到可見範圍。
        imageList.ensureIndexIsVisible(imageListModel.size() - 1);

        // 多執行緒版會用 SwingUtilities.invokeLater 回到畫面執行緒，
        // 所以資料加入 model 後，JList 可以正常刷新。
    }

    private void showImage(File file) {
        /*
         * 顯示圖片流程：
         * 1. 更新上方狀態與檔案大小。
         * 2. 用 ImageIcon 讀取圖片。
         * 3. 取得右側預覽區大小。
         * 4. 依照預覽區大小等比例縮放圖片。
         * 5. 把縮放後的圖片放到 imageLabel。
         */

        setStatusText("目前顯示", file);

        // ImageIcon 可以直接用檔案路徑讀取圖片。
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // 如果寬高小於等於 0，代表圖片讀取失敗。
        if (imageWidth <= 0 || imageHeight <= 0) {
            imageLabel.setIcon(null);
            imageLabel.setText("圖片讀取失敗");
            return;
        }

        // 依照右側可視區域等比例縮放圖片。
        // imageLabel 放在 JScrollPane 裡，所以 parent 通常是 JViewport。
        Dimension viewSize = imageLabel.getParent() instanceof JViewport
                ? ((JViewport) imageLabel.getParent()).getExtentSize()
                : imageLabel.getSize();

        // 預留一點邊距，不要讓圖片緊貼邊界。
        int maxWidth = Math.max(100, viewSize.width - 20);
        int maxHeight = Math.max(100, viewSize.height - 20);

        // 分別算出寬度比例和高度比例，取比較小的那個。
        // 這樣圖片不會超出預覽區，也不會變形。
        double scale = Math.min(
                (double) maxWidth / imageWidth,
                (double) maxHeight / imageHeight
        );

        // 算出縮放後實際顯示的寬高。
        int displayWidth = Math.max(1, (int) (imageWidth * scale));
        int displayHeight = Math.max(1, (int) (imageHeight * scale));

        // 產生縮放後的 Image。
        Image image = icon.getImage().getScaledInstance(
                displayWidth,
                displayHeight,
                Image.SCALE_SMOOTH
        );

        // 清掉原本的文字，改顯示圖片。
        imageLabel.setText("");
        imageLabel.setIcon(new ImageIcon(image));
    }

    // 統一設定單一進度條文字，避免不同地方各自組字串造成顯示不一致。
    private void setFileProgressText(
            UploadProgressRow progressRow,
            int currentNumber,
            int totalCount,
            int progress
    ) {
        /*
         * fileProgressBar.setValue(progress)
         * 設定進度條填滿的比例，progress 通常是 0 到 100。
         */
        progressRow.progressBar.setValue(progress);
        progressRow.label.setText("第 " + currentNumber + "/" + totalCount + " 張 " + progress + "%");
        progressRow.label.setForeground(progress >= 100 ? PROGRESS_DONE_COLOR : PROGRESS_TEXT_COLOR);
    }

    private void setFileProgressError(UploadProgressRow progressRow, int currentNumber, int totalCount) {
        progressRow.progressBar.setValue(0);
        progressRow.progressBar.setForeground(PROGRESS_ERROR_COLOR);
        progressRow.label.setText("第 " + currentNumber + "/" + totalCount + " 張失敗");
        progressRow.label.setForeground(PROGRESS_ERROR_COLOR);
    }

    // 統一設定上方資訊。上方不顯示檔名，檔名只放在左側清單。
    private void setStatusText(String text, File file) {
        // 狀態文字，例如：上傳中、上傳成功、目前顯示。
        statusLabel.setText("狀態：" + text);

        // 如果沒有檔案，就把大小顯示成 -。
        if (file == null) {
            fileSizeLabel.setText("大小：-");
            statusLabel.setToolTipText(null);
            return;
        }

        // 有檔案時，顯示檔案大小。
        fileSizeLabel.setText("大小：" + formatFileSize(file.length()));

        // tooltip 是滑鼠移到狀態文字上時顯示的小提示。
        // 這裡用來保存完整路徑，但畫面上不直接顯示完整路徑。
        statusLabel.setToolTipText(file.getAbsolutePath());
    }

    // 把 byte 轉成比較好讀的 KB / MB。
    // 1024 byte => 1 KB
    // 1024 KB => 1 MB
    // 1024 MB => 1 GB
    // 1024 GB => 1 TB
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        }

        double kb = size / 1024.0;
        if (kb < 1024) {
            return String.format("%.1f KB", kb);
        }

        double mb = kb / 1024.0;
        return String.format("%.1f MB", mb);
    }

    private static class UploadProgressRow {
        private final JPanel panel;
        private final JLabel label;
        private final JProgressBar progressBar;

        private UploadProgressRow(JPanel panel, JLabel label, JProgressBar progressBar) {
            this.panel = panel;
            this.label = label;
            this.progressBar = progressBar;
        }
    }

}
