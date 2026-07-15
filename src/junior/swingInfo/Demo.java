package junior.swingInfo;

import javax.swing.*;

public class Demo {
    public static void main(String[] args) {
        // 1. 建立視窗
        JFrame frame = new JFrame("我的第一個 Java GUI"); // Graphical User Interface
        // frame.setLocation(100, 100);
        // 設定視窗大小
        frame.setSize(300, 200);
        // 設定視窗是否要有結束按鈕
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. 建立組件
        JButton button = new JButton("點擊我");
        JLabel label = new JLabel("點擊次數：0");

        // 3. 事件監聽 (當按鈕被按下時)
        final int[] count = { 0 };
        // 匿名內部類別
        // e: event(事件)
        // 監聽按鈕的點擊事件
        // Lambada 語法
        button.addActionListener(e -> {
            // 統計點擊次數
            count[0]++;
            label.setText("點擊次數：" + count[0]);
        });
        // 4. 配置版面
        // frame是最底層, 倒數第二層是 panel, 最上層就是 label, button
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(label);
        frame.add(panel);

        // 5. 顯示視窗
        frame.setVisible(true);
    }
}
