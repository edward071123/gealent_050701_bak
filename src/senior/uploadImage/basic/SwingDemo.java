package senior.uploadImage.basic;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;

public class SwingDemo {
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
    public static void main(String[] args) {
        // 把工作丟給Swing的UI執行緒
        SwingUtilities.invokeLater(() -> {
            // 建立主視窗
            new Frame();
        });

    }
}

class Frame extends JFrame {
    
    DefaultListModel<File> imageListModel;
    JLabel statusLabel;
    JLabel fileSizeLabel;
    JLabel imageLabel;
    JProgressBar progressBar;

    public Frame() {
        // ========================畫面 起始================================
        // 範例互動網站:
        // https://edward071123.github.io/gealent_050701/src/senior/uploadImage/basic/swing-demo.html

        // Frame 基本設定
        settingFrame();

        //=================topPanel區塊的元件 起始============================
        JButton uploadButton = new JButton("上傳圖片");

        statusLabel = new JLabel("狀態：尚未選擇圖片");
        fileSizeLabel = new JLabel("大小：-");
        // GridLayout(列數, 欄數, 水平間距, 垂直間距)
        /* 
        * | +--------------+--------------------------------+ |
        * | | statusLabel  | fileSizeLabel                  | |
        * | +--------------+--------------------------------+ |
        */
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        infoPanel.add(statusLabel);
        infoPanel.add(fileSizeLabel);

        JButton refreshButton = new JButton("重新整理");

        // BorderLayout(水平間距, 垂直間距)
        JPanel topPanel = new JPanel(new BorderLayout(8, 0));
        topPanel.add(uploadButton, BorderLayout.WEST);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        //=================topPanel區塊的元件 結束=============================

        //=================splitPane區塊的元件 起始============================
        // 左側圖片清單一次只能選一張
        // DefaultListModel：清單資料來源，顯示uploads資料夾的 File。
        // JScrollPane不是一般的JPanel，會有ScrollBar滾動所以不能用add加上去其他的元件
        imageListModel = new DefaultListModel<>();
        JList<File> imageList = new JList<>(imageListModel);
        JScrollPane listScrollPane = new JScrollPane(imageList);
        // listScrollPane.add(imageList); --> 錯誤用法

        // 右邊的圖片顯示
        imageLabel = new JLabel("尚未選擇圖片", SwingConstants.CENTER);
        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        // listScrollPane.add(imageScrollPane); --> 錯誤用法

        // JSplitPane：左右分割畫面(因為兩個區塊)
        // HORIZONTAL_SPLIT 代表水平分割，也就是左邊一區、右邊一區。
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        // 設定左右分割線的位置。 往左數字小，往右數字大
        splitPane.setDividerLocation(160);
        splitPane.add(listScrollPane);
        splitPane.add(imageScrollPane);
        //=================splitPane區塊的元件 結束===========================

        //=================progressBar區塊的元件 起始=========================
        progressBar = new JProgressBar(0, 100);
        // 設定進度條上面畫文字上去顯示 0%、50%、100% 這種文字
        progressBar.setStringPainted(true);
        //=================progressBar區塊的元件 結束=========================

        // 三大區塊分北中南 貼到 Frame
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        // 列表的自訂每一列怎麼顯示
        imageList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = (JLabel) new DefaultListCellRenderer()
                    .getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus
                    );
            
            // 每列的檔案
            File file = (File) value;
            // 標籤設定檔名
            label.setText(file.getName());
            return label;
        });
       
        // ========================畫面 結束==================================

        // ========================事件 開始==================================
        // 一般事件的寫法
        // 要搭配以下import
        // import java.awt.event.ActionEvent;
        // import java.awt.event.ActionListener;
        // uploadButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         uploadImage();
        //     }
        // });

        // Lambda 寫法
        // 監聽按鈕事件
        uploadButton.addActionListener(e -> uploadImage());
        refreshButton.addActionListener(e -> loadImages());

        // 列表點選每一列的監聽事件
        imageList.addListSelectionListener(e -> {
            // System.out.println("已選擇檔案");
            File selectedFile = imageList.getSelectedValue();
            if(selectedFile != null) {
                showImage(selectedFile);
            } 
        });

        // ========================事件 結束==================================
        // 顯示所有的檔案
        loadImages();
        // 顯示視窗
        setVisible(true);
    }

    private void settingFrame() {
        setTitle("圖片上傳與瀏覽 - swing - 單檔");
        // 設定寬高
        setSize(900, 700);
        // 按按鈕時結束程式
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 視窗設定在中央的位置
        setLocationRelativeTo(null);
    }

    // 上傳檔案
    public void uploadImage() {
        // 1. 選擇檔案
        JFileChooser chooser = new JFileChooser();

        // 設定過濾檔案, 選擇圖檔就好
        // Extension: 副檔名
        chooser.setFileFilter(new FileNameExtensionFilter(
            "圖片檔案 (*.jpg, *.jpeg, *.png, *.gif)",
            "jpg",
            "jpeg",
            "png",
            "gif"
        ));
        chooser.setMultiSelectionEnabled(true);

        // 顯示打開對話筐
        // 會回傳以下動作的對應int
        // JFileChooser.CANCEL_OPTION
        // JFileChooser.APPROVE_OPTION --> 使用者選到圖片
        // JFileChooser.ERROR_OPTION
        int result = chooser.showOpenDialog(this);

        // 判斷結果有選到圖片我才繼續去做上傳的動作
        if(result == JFileChooser.APPROVE_OPTION) {
            // 2. 檔案上傳
            // 來源檔案
            File sourceFile = chooser.getSelectedFile();
            // 目標資料夾
            File uploadDir = new File("src/senior/uploadImage/uploads");
            
            statusLabel.setText("上傳中....");
            progressBar.setValue(0);
            progressBar.setString("0%");

            // 判斷資料夾是否存在
            if (!uploadDir.exists()) {
                // 創造資料夾
                uploadDir.mkdirs();
            }

            // 目標檔案 = 目標資料夾 + 來源檔案的檔名
            File targetFile = new File(uploadDir, sourceFile.getName());

            // 1024 byte => 1 KB
            // 1024 KB => 1 MB
            // 1024 MB => 1 GB
            // 1024 GB => 1 TB
            // 每次讀取 4096 bytes，不要一次把整張圖片全部讀進記憶體。

            // 要多一條執行緒另外去跑上傳的動作
            new Thread(() -> {
                byte[] buffer = new byte[4096];

                // 原始檔案的大小
                long totalSize = sourceFile.length();
                // 已複製的檔案大小(進度條計算使用)
                long copiedSize = 0;

                FileInputStream fis = null;
                FileOutputStream fos = null;

                try {
                    // FileInputStream：從原始圖片讀取資料。
                    fis = new FileInputStream(sourceFile);

                    // FileOutputStream：把資料寫到 uploads 裡的新檔案。
                    fos = new FileOutputStream(targetFile);

                    int len;

                    // fis.read(buffer) 會把資料讀進 buffer。
                    // 回傳值 len 表示這次實際讀到幾個 bytes。
                    // 如果回傳 -1，代表檔案讀完了。
                    while ((len = fis.read(buffer)) != -1) {
                        // 睡100毫秒, 為了觀察進度條
                        Thread.sleep(100);
                        // 只寫入本次實際讀到的長度 len。
                        fos.write(buffer, 0, len);

                        // 呼叫進度條更新的方法
                        copiedSize += len;
                        int value = (int) ((copiedSize  * 100) / totalSize);
                        // 更新swing的畫面
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setValue(value);
                            progressBar.setString(value + "%");
                        });

                    
                    }

                    System.out.println();
                    System.out.println("上傳成功");
                    // 更新swing的畫面
                    SwingUtilities.invokeLater(() -> {
                        loadImages();
                        showImage(targetFile);
                    });

                } catch (Exception e) {
                    System.out.println("圖片複製失敗：" + e.getMessage());

                } finally {
                    // finally 不管成功或失敗都會執行，適合拿來關閉檔案資源。
                    try {
                        if (fis != null) {
                            fis.close();
                        }

                        if (fos != null) {
                            fos.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            
        }

        // if(result == JFileChooser.CANCEL_OPTION) {
        //     System.out.println("取消選擇");
        // }
    }

    // 取得uploads的所有檔案
    public void loadImages() {
        // 每次都先清除一次再做取得
        imageListModel.clear();

        // 找uploads內的所有檔案
        File uploadDir = new File("src/senior/uploadImage/uploads");
        File[] files = uploadDir.listFiles();

        // 如果是空的話
        if(files == null) {
            return;
        }

        for(File oneFile: files) {
            imageListModel.addElement(oneFile);
        }

        if(imageListModel.isEmpty()) {
            statusLabel.setText("uploads 資料夾目前沒有圖片");
        }

    }

    // 顯示已選擇的圖片
    public void showImage(File file) {
        // 變更文字狀態
        statusLabel.setText("顯示圖片");
        fileSizeLabel.setText("大小: " + formatFileSize(file.length()));

        // 顯示圖片
        // ImageIcon 可以直接用檔案路徑讀取圖片。
        // getAbsolutePath(): 取得絕對路徑
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        // 得到圖片寬高
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        // 如果寬高小於等於 0，代表圖片讀取失敗。
        if (imageWidth <= 0 || imageHeight <= 0) {
            imageLabel.setIcon(null);
            imageLabel.setText("圖片讀取失敗");
            return;
        }

        /*
         * 取得右邊圖片顯示區目前可以使用的大小。
         * 互動網站: https://edward071123.github.io/gealent_050701/src/senior/uploadImage/basic/resize-image.html
         *
         * Dimension 是 一個用來裝寬度和高度的物件
         * 
         * imageLabel 放在 imageScrollPane 裡面時，
         * imageLabel.getParent() 通常會是 JViewport。
         * 
         * • 可以把它想成這幾層：
         *JScrollPane / imageScrollPane
         *└── JViewport
         *       └── imageLabel
         *          └── ImageIcon / 圖片
         *
         *  圖層概念：
         *  最外層：imageScrollPane
         *  +--------------------------------------+
         *  | JScrollPane                          |
         *  |                                      |
         *  |  中間可視區：JViewport                 |
         *  |  +--------------------------------+  |
         *  |  | JViewport                      |  |
         *  |  |                                |  |
         *  |  |  真正放圖片的元件：imageLabel     |  |
         *  |  |  +--------------------------+  |  |
         *  |  |  | imageLabel               |  |  |
         *  |  |  |                          |  |  |
         *  |  |  | ImageIcon 圖片            |  |  |
         *  |  |  +--------------------------+  |  |
         *  |  |                                |  |
         *  |  +--------------------------------+  |
         *  |                                      |
         *  |  如果圖片太大，JScrollPane 會出現捲軸    |
         *  +--------------------------------------+
         *
         * JViewport 是 JScrollPane 裡「目前看得到的區域」。
         * 所以如果 parent 是 JViewport，
         * 就用 getExtentSize() 取得可視範圍。
         *
         * 如果 parent 不是 JViewport，
         * 就退而求其次用 imageLabel 自己的大小。
         * 
         * 重點：
         *   imageScrollPane 是外框，負責提供捲軸
         *   JViewport 是真正看得到的視窗區域
         *   imageLabel 是放圖片的元件
         *   用生活比喻：
         *   
         *   imageScrollPane = 相框外殼，帶捲軸
         *   JViewport       = 相框中間透明玻璃，看得到的範圍
         *   imageLabel      = 放在相框裡的紙
         *   ImageIcon       = 紙上的圖片
         */
        Dimension viewSize = imageLabel.getParent() instanceof JViewport
            ? ((JViewport) imageLabel.getParent()).getExtentSize()
            : imageLabel.getSize();

        /*
         * 預留一點邊距，不要讓圖片緊貼邊界。
         *
         * viewSize.width  是右邊顯示區的寬度。
         * viewSize.height 是右邊顯示區的高度。
         *
         * 減 20 代表留一點空間。
         *
         * Math.max(100, ...)
         * 是為了避免剛開畫面時 viewSize 太小，
         * 導致 maxWidth 或 maxHeight 變成 0 或負數。
         *  Math.max = 取較大的值
         *  這裡是保底用，避免寬高太小
         */
        int maxWidth = Math.max(100, viewSize.width - 20);
        int maxHeight = Math.max(100, viewSize.height - 20);

        /*
         * 分別算出寬度比例和高度比例，取比較小的那個。
         *
         * 例如：
         * maxWidth = 400, imageWidth = 800
         * 寬度比例 = 400 / 800 = 0.5
         *
         * maxHeight = 300, imageHeight = 300
         * 高度比例 = 300 / 300 = 1.0
         *
         * 如果用 1.0，圖片寬度會超出畫面。
         * 所以用 Math.min(...) 取比較小的 0.5。
         *
         * 這樣圖片不會超出預覽區，也不會變形。
         */
        double scale = Math.min(
                (double) maxWidth / imageWidth,
                (double) maxHeight / imageHeight
        );

        /*
         * 算出縮放後實際顯示的寬高。
         *
         * imageWidth * scale  = 縮放後寬度
         * imageHeight * scale = 縮放後高度
         *
         * Math.max(1, ...)
         * 是為了避免計算結果變成 0。
         * 圖片寬高至少要是 1 才能顯示。
         */
        int displayWidth = Math.max(1, (int) (imageWidth * scale));
        int displayHeight = Math.max(1, (int) (imageHeight * scale));

        // 產生縮放後的 Image。
        Image image = icon.getImage().getScaledInstance(
                displayWidth,
                displayHeight,
                Image.SCALE_SMOOTH
        );

        imageLabel.setText("");
        imageLabel.setIcon(new ImageIcon(image));
    }

    // 轉換byte變成我們常用的計算顯示
    // 1024 byte => 1 KB
    // 1024 KB => 1 MB
    // 1024 MB => 1 GB
    // 1024 GB => 1 TB
    public String formatFileSize(long size) {
        if(size < 1024) {
            return size + " B";
        }

        double kb = size / 1024.0;
        if(kb < 1024) {
            return String.format("%.1f KB", kb);
        }

        double mb = kb / 1024.0;
        return String.format("%.1f MB", mb);
    }
}
