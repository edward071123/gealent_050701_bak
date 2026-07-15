package senior.uploadImage.swing;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class Frame extends JFrame {

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
         *
         * BorderLayout.NORTH  ：上方工具列 topPanel
         * BorderLayout.CENTER ：中間左右分割 splitPane
         * splitPane 左邊      ：圖片清單 imageList
         * splitPane 右邊      ：圖片預覽 imageLabel
         */

        // 視窗基本設定。
        // setTitle：視窗標題。
        // setSize：視窗寬高。
        // setDefaultCloseOperation：按關閉按鈕時結束程式。
        // setLocationRelativeTo(null)：讓視窗出現在螢幕中央。
        setTitle("圖片上傳與瀏覽 - swing - 單檔");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        // NORTH 放上方工具列，CENTER 放主要內容。
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // 開啟視窗時先讀取 uploads 裡已經存在的圖片。
        loadUploadImages();

        setVisible(true);
    }

    private void uploadImage() {
        /*
         * 上傳流程：
         * 1. 開啟 JFileChooser 讓使用者選圖片。
         * 2. 確認使用者真的按下「開啟」。
         * 3. 檢查檔案副檔名是不是 jpg/jpeg/png/gif。
         * 4. 呼叫 UploadService.upload() 複製檔案。
         * 5. 重新整理左側清單。
         * 6. 右側顯示剛上傳的圖片。
         */

        // 讓使用者選擇圖片檔。
        JFileChooser chooser = new JFileChooser();
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

            // 取得使用者選擇的來源圖片。
            File sourceFile = chooser.getSelectedFile();

            // 防止使用者選到非圖片檔。
            if (!uploadService.isImageFile(sourceFile)) {
                JOptionPane.showMessageDialog(
                        this,
                        "只能上傳 jpg、jpeg、png、gif 圖片"
                );
                return;
            }

            // 先更新畫面狀態，讓使用者知道程式開始處理。
            setStatusText("上傳中...", null);

            try {
                // 呼叫 UploadService，真正的檔案複製邏輯放在另一個類別。
                File targetFile = uploadService.upload(sourceFile);

                // 上傳完成後重新整理清單，並顯示剛上傳的圖片。
                // 檔案已經複製到 uploads，所以重新讀取 uploads 清單。
                loadUploadImages();

                // 右側顯示剛上傳的圖片。
                showImage(targetFile);

                // 左側清單選中剛上傳的圖片。
                imageList.setSelectedValue(targetFile, true);

                setStatusText("上傳成功", targetFile);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "上傳失敗：" + ex.getMessage()
                );

                setStatusText("上傳失敗", null);
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
