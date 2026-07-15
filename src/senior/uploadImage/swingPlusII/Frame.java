package senior.uploadImage.swingPlusII;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class Frame extends JFrame {

    // 一次最多可以選幾張圖片。
    // 之後如果要改成 10 張，只要改這裡，不用到處找數字。
    private static final int MAX_UPLOAD_COUNT = 5;

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

    // JProgressBar：顯示上傳進度，0 到 100 代表百分比。
    private JProgressBar progressBar = new JProgressBar(0, 100);

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
         * | progressBar                                                       |
         * | [ 0% ----------------------------------------------------- 100% ] |
         * +-------------------------------------------------------------------+
         *
         * BorderLayout.NORTH  ：上方工具列 topPanel
         * BorderLayout.CENTER ：中間左右分割 splitPane
         * BorderLayout.SOUTH  ：下方進度條 progressBar
         * splitPane 左邊      ：圖片清單 imageList
         * splitPane 右邊      ：圖片預覽 imageLabel
         */

        // 視窗基本設定。
        // setTitle：視窗標題。
        // setSize：視窗寬高。
        // setDefaultCloseOperation：按關閉按鈕時結束程式。
        // setLocationRelativeTo(null)：讓視窗出現在螢幕中央。
        setTitle("圖片上傳與瀏覽 - swingPlusII - 多檔排隊版");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 進度條設定。
        // setStringPainted(true)：讓進度條顯示 0%、50%、100% 這種文字。
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setString("0%");

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

        // JFrame 預設就是 BorderLayout。
        // NORTH 放上方工具列，CENTER 放主要內容，SOUTH 放進度條。
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        // 開啟視窗時先讀取 uploads 裡已經存在的圖片。
        loadUploadImages();

        setVisible(true);
    }

    private void uploadImage() {
        /*
         * 多張圖片上傳流程：
         * 1. 開啟 JFileChooser 讓使用者選圖片。
         * 2. 允許使用者一次選多張圖片。
         * 3. 檢查選擇數量，最多只能 5 張。
         * 4. 逐張檢查副檔名是不是 jpg/jpeg/png/gif。
         * 5. 使用 for 迴圈一張一張上傳。
         * 6. 每一張上傳時，progressBar 顯示「目前這張」的進度。
         * 7. 全部上傳完成後，重新整理左側清單。
         *
         * 注意：
         * 這個版本故意不使用 SwingWorker，也沒有多執行緒。
         * 所以檔案會照順序排隊：
         * 第 1 張完成後，才會開始第 2 張。
         *
         * 如果圖片很大，畫面可能暫時不能操作。
         * 這是為了讓初學者先看懂「排隊上傳」的流程。
         */

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

        if (result == JFileChooser.APPROVE_OPTION) {

            // getSelectedFiles() 會取得使用者選擇的多個檔案。
            // 回傳型別是 File[]，也就是 File 陣列。
            File[] sourceFiles = chooser.getSelectedFiles();

            // 如果使用者沒有選到任何檔案，就不往下處理。
            if (sourceFiles.length == 0) {
                return;
            }

            // 本範例限制最多一次上傳 5 張，避免初學範例一次處理太多檔案。
            if (sourceFiles.length > MAX_UPLOAD_COUNT) {
                JOptionPane.showMessageDialog(
                        this,
                        "一次最多只能上傳 " + MAX_UPLOAD_COUNT + " 張圖片"
                );
                return;
            }

            // 先檢查所有檔案。
            // 只要其中一個不是圖片，就整批不開始上傳。
            for (File sourceFile : sourceFiles) {
                if (!uploadService.isImageFile(sourceFile)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "只能上傳 jpg、jpeg、png、gif 圖片"
                    );
                    return;
                }
            }

            // 先更新畫面狀態，讓使用者知道程式開始處理。
            setStatusText("準備上傳 " + sourceFiles.length + " 張圖片", null);
            progressBar.setValue(0);
            progressBar.setString("0%");
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
            long startTime = System.currentTimeMillis();

            File lastUploadedFile = null;

            try {
                // 使用一般 for 迴圈逐張上傳。
                // i 從 0 開始，所以顯示第幾張時要用 i + 1。
                for (int i = 0; i < sourceFiles.length; i++) {
                    File sourceFile = sourceFiles[i];
                    /*
                     * currentNumber 是目前第幾張。
                     *
                     * 下面的匿名類別 IUploadProgress 也會用到這個數字。
                     * 寫成 final 可以讓初學者清楚知道：
                     * 這一輪迴圈裡，currentNumber 的值不會再被改掉。
                     */
                    final int currentNumber = i + 1;
                    final int totalCount = sourceFiles.length;

                    setStatusText(
                            "上傳中 " + currentNumber + " / " + totalCount,
                            sourceFile
                    );
                    setUploadProgressText(currentNumber, totalCount, 0);
                    repaintUploadStatusNow();

                    /*
                     * 建立一個 IUploadProgress 物件。
                     *
                     * IUploadProgress 是一個介面。
                     * 介面本身不能直接 new，所以這裡用匿名類別實作它。
                     *
                     * UploadService 複製檔案時，會呼叫 onProgress(progress)。
                     * 這裡收到 progress 後，直接更新 progressBar。
                     *
                     * 這個版本沒有 SwingWorker，
                     * 所以這段程式會照順序執行，不會同時上傳多張。
                     */
                    IUploadProgress uploadProgress = new IUploadProgress() {
                        @Override
                        public void onProgress(int progress) {
                            setUploadProgressText(currentNumber, totalCount, progress);

                            /*
                             * 因為這個版本沒有使用 SwingWorker，
                             * for 迴圈和檔案複製都在目前的畫面事件中執行。
                             *
                             * 一般情況下，Swing 會等事件結束後才重畫畫面。
                             * 但這裡為了教學，要讓學生看到進度變化，
                             * 所以每次進度改變後，主動要求進度條立刻重畫。
                             */
                            repaintUploadStatusNow();
                        }
                    };

                    // 呼叫 UploadService，真正的檔案複製邏輯放在另一個類別。
                    lastUploadedFile = uploadService.upload(sourceFile, uploadProgress);

                    // 只要有一張上傳完成，就馬上更新左側清單。
                    // 這樣使用者不用等全部圖片上傳完，才看到清單變化。
                    addUploadedFileToList(lastUploadedFile);
                }

                progressBar.setValue(100);
                progressBar.setString("全部完成 100%");

                // 檔案已經複製到 uploads，所以重新讀取 uploads 清單。
                loadUploadImages();

                // 右側顯示最後一張上傳完成的圖片。
                if (lastUploadedFile != null) {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    double elapsedSeconds = elapsedTime / 1000.0;

                    showImage(lastUploadedFile);

                    // 左側清單選中最後一張上傳完成的圖片。
                    imageList.setSelectedValue(lastUploadedFile, true);
                    setStatusText(
                            "全部上傳成功，共 " + sourceFiles.length + " 張 ("
                                    + String.format("%.1f", elapsedSeconds)
                                    + " 秒)",
                            lastUploadedFile
                    );
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    Frame.this,
                    "上傳失敗：" + ex.getMessage()
                );

                setStatusText("上傳失敗", null);

            } finally {
                uploadButton.setEnabled(true);
                refreshButton.setEnabled(true);
            }
        }
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

        // 這個版本沒有使用 SwingWorker，
        // 所以上傳中的 for 迴圈會暫時佔住 Swing 畫面事件。
        // 如果不主動重畫，清單可能要等全部上傳完才看得到新資料。
        repaintImageListNow();
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

    // 同步上傳時，主動要求狀態文字和進度條立刻重畫。
    private void repaintUploadStatusNow() {
        /*
         * paintImmediately 是「馬上重畫」的意思。
         *
         * 這個範例沒有使用 SwingWorker，
         * 所以上傳檔案時，畫面事件會被目前的 for 迴圈佔住。
         *
         * 如果只寫 repaint()，Swing 可能會等上傳全部結束後才重畫。
         * 這裡改用 paintImmediately()，
         * 是為了讓學生看得到一張一張排隊上傳的進度變化。
         */
        statusLabel.paintImmediately(statusLabel.getVisibleRect());
        fileSizeLabel.paintImmediately(fileSizeLabel.getVisibleRect());
        progressBar.paintImmediately(progressBar.getVisibleRect());
    }

    // 同步上傳時，主動要求左側清單立刻重畫。
    private void repaintImageListNow() {
        /*
         * imageListModel.addElement(uploadedFile)
         * 只代表「資料已經加入清單模型」。
         *
         * 但是畫面什麼時候重畫，是 Swing 事件執行緒負責。
         *
         * 這個範例故意不用 SwingWorker，
         * 所以上傳流程還沒結束時，Swing 可能還沒空更新 JList 畫面。
         *
         * 因此每完成一張圖片後，
         * 我們手動要求 imageList 和它外層的 viewport 立刻重畫，
         * 讓學生可以看到「完成一張，左側清單馬上多一筆」。
         */
        imageList.revalidate();
        imageList.paintImmediately(imageList.getVisibleRect());

        if (imageList.getParent() instanceof JViewport) {
            JViewport viewport = (JViewport) imageList.getParent();
            viewport.paintImmediately(viewport.getVisibleRect());
        }
    }

    // 統一設定進度條文字，避免不同地方各自組字串造成顯示不一致。
    private void setUploadProgressText(int currentNumber, int totalCount, int progress) {
        /*
         * progressBar.setValue(progress)
         * 設定進度條填滿的比例，progress 通常是 0 到 100。
         */
        progressBar.setValue(progress);

        /*
         * progressBar.setString(...)
         * 設定進度條中間顯示的文字。
         *
         * 原本寫成「第 1 張：30%」比較長，
         * 在某些系統的 Swing 進度條上，快速重畫時可能顯示不完整。
         *
         * 這裡使用比較短的格式：
         * 第 1/5 張 30%
         *
         * currentNumber：目前第幾張
         * totalCount   ：這次總共選了幾張
         * progress     ：目前這張的上傳百分比
         */
        progressBar.setString(
                "第 " + currentNumber + "/" + totalCount + " 張 " + progress + "%"
        );
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

}
