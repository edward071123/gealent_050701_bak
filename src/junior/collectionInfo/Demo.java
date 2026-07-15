package junior.collectionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Demo {
    public static void main(String[] args) {
        /*
         * Collection（集合）可以一次存放很多筆資料。
         * 跟陣列相對比: 
         * 陣列需要先劃出倉庫大小
         * 集合則像是有彈性的袋子
         */

        // ==================== 1. List ====================
        /*
         * List 的特性：
         * 1. 有順序。
         * 2. 有索引，索引從 0 開始。
         * 3. 可以保存重複資料。
         *
         * 可能的使用情境：
         * 1. 購物車商品，商品可能重複。
         * 2. 學生點名名單，需要保留順序。
         * 3. 播放清單，需要按照加入順序播放。
         *
         * 完整寫法：
         * List<String> fruits = new ArrayList<String>();
         *
         * 簡寫：
         * List<String> fruits = new ArrayList<>();
         *
         * 常用方法：
         * add()    新增資料
         * get()    依照索引取得資料
         * set()    修改資料
         * remove() 刪除資料
         * size()   取得資料筆數
         */
        List<String> fruits = new ArrayList<>();

        fruits.add("蘋果");
        fruits.add("香蕉");
        fruits.add("蘋果"); // List 可以保存重複資料

        System.out.println("================ List ================");
        System.out.println(fruits);
        System.out.println("第一個水果：" + fruits.get(0));

        // for-each 逐筆取得 List 裡面的資料
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // ==================== 2. 泛型 Generic ====================
        /*
         * 沒有使用泛型時，可以直接寫：
         * List list = new ArrayList();
         *
         * 因為沒有指定型別，所以集合裡什麼資料都能放。
         * 但是取出資料時，Java 只知道它是 Object。
         * 要使用原本的型別，就必須自己進行強制轉型。
         */
        System.out.println("================ 沒有泛型 開始 ================");
        
        // List list = new ArrayList();

        // list.add("Tom");
        // list.add(100);
        // list.add(true);

        
        // System.out.println(list);

        // // 沒有泛型時，取出的資料型別是 Object。
        // Object data = list.get(0);
        // System.out.println("Object 資料：" + data);

        // // 要使用原本型別，必須自己強制轉型。
        // String name = (String) list.get(0);
        // Integer number = (Integer) list.get(1);
        // Boolean result = (Boolean) list.get(2);

        // System.out.println("姓名：" + name);
        // System.out.println("數字：" + number);
        // System.out.println("布林值：" + result);

        System.out.println("================ 沒有泛型 結束 ================");

        // 如果轉成錯誤的型別，會發生 ClassCastException。
        // String error = (String) list.get(1);

        /*
         * 為了解決混合型別與強制轉型問題，可以使用泛型。
         *
         * List<String> 中的 <String> 就是泛型，
         * 表示這個 List 只能保存 String。
         *
         * List<String>  只能保存字串。
         * List<Integer> 只能保存整數。
         *
         * 泛型的好處：
         * 1. 編譯時就能阻止錯誤型別加入集合。
         * 2. 取出資料時不需要強制轉型。
         *
         * 泛型不能直接使用基本型別：
         * 錯誤：List<int>
         * 正確：List<Integer>
         *
         * 常用基本型別對應：
         * int     -> Integer
         * double  -> Double
         * boolean -> Boolean
         * char    -> Character
         */
        List<String> words = new ArrayList<>();
        words.add("Java");
        words.add("集合");
        // words.add(100); // 錯誤：List<String> 不能加入整數

        // 使用泛型後，可以直接用 String 接收，不需要 (String) 強制轉型。
        String firstWord = words.get(0);

        System.out.println("================ 泛型 ================");
        System.out.println("字串集合：" + words);
        System.out.println("第一筆字串：" + firstWord);

        // ==================== 3. Set ====================
        /*
         * Set 的特性：
         * 1. 不會保存重複資料。
         * 2. 沒有索引，不能使用 get(0)。
         *
         * 可能的使用情境：
         * 1. 會員帳號，每個帳號不能重複。
         * 2. 商品標籤，不需要出現相同標籤。
         * 3. 活動簽到名單，同一個人只能簽到一次。
         *
         * 完整寫法：
         * Set<String> names = new HashSet<String>();
         *
         * 簡寫：
         * Set<String> names = new HashSet<>();
         *
         * 常用方法：
         * add()      新增資料
         * contains() 判斷是否包含資料
         * remove()   刪除資料
         * size()     取得資料筆數
         */
        Set<String> studentNames = new HashSet<>();

        studentNames.add("小明");
        studentNames.add("小美");
        studentNames.add("小明"); // 重複的小明不會再次加入

        System.out.println("================ Set ================");
        System.out.println(studentNames);
        System.out.println("是否有小明：" + studentNames.contains("小明"));

        for (String studentName : studentNames) {
            System.out.println(studentName);
        }

        // ==================== 4. Map ====================
        /*
         * Map 使用 key 和 value 保存資料。
         * 可以透過 key 快速取得對應的 value。
         *
         * Map<String, String>：
         * 第一個 String 是 key 的型別。
         * 第二個 String 是 value 的型別。
         *
         * key 不可以重複，使用相同 key 會更新原本的 value。
         *
         * 可能的使用情境：
         * 1. 學號對應學生姓名。
         * 2. 商品編號對應商品名稱。
         * 3. 國家代碼對應國家名稱。
         *
         * 完整寫法：
         * Map<String, String> students = new HashMap<String, String>();
         *
         * 簡寫：
         * Map<String, String> students = new HashMap<>();
         *
         * 常用方法：
         * put()         新增或修改資料
         * get()         透過 key 取得 value
         * remove()      透過 key 刪除資料
         * containsKey() 判斷是否包含 key
         * keySet()      取得所有 key
         * size()        取得資料筆數
         */
        Map<String, String> students = new HashMap<>();

        students.put("S001", "小明");
        students.put("S002", "小美");

        System.out.println("================ Map ================");
        System.out.println(students);
        System.out.println("S001 的學生：" + students.get("S001"));

        // keySet() 取得所有 key，再使用 key 取得 value。
        for (String studentId : students.keySet()) {
            System.out.println("學號：" + studentId);
            System.out.println("姓名：" + students.get(studentId));
        }

        // ==================== 5. 自建泛型 class ====================
        /*
         * 前面看到的 List<String>、Set<String>、Map<String, String>
         * 都是 Java 幫我們寫好的泛型類別。
         *
         * 其實我們也可以自己寫泛型類別。
         *
         * 泛型類別的寫法：
         *
         * class Box<T> {
         *     private T value;
         * }
         *
         * T 不是固定型別。
         * T 是一個「型別佔位符」。
         *
         * 當你寫：
         * Box<String> stringBox
         *
         * T 就會變成 String。
         *
         * 當你寫：
         * Box<Integer> integerBox
         *
         * T 就會變成 Integer。
         *
         * 常見泛型命名：
         * T：Type，普通型別
         * E：Element，集合元素
         * K：Key，Map 的 key
         * V：Value，Map 的 value
         */
        System.out.println("================ 自建泛型 class：Box<T> ================");

        Box<String> stringBox = new Box<>();
        stringBox.setValue("這是一段文字");

        String boxText = stringBox.getValue();
        System.out.println("String Box：" + boxText);

        Box<Integer> integerBox = new Box<>();
        integerBox.setValue(100);

        Integer boxNumber = integerBox.getValue();
        System.out.println("Integer Box：" + boxNumber);

        /*
         * 因為 stringBox 是 Box<String>，
         * 所以只能放 String。
         *
         * 下面這行如果打開會編譯錯誤：
         */
        // stringBox.setValue(123);

        /*
         * 因為 integerBox 是 Box<Integer>，
         * 所以只能放 Integer。
         *
         * 下面這行如果打開會編譯錯誤：
         */
        // integerBox.setValue("abc");

        // ==================== 6. 自建多個泛型參數 ====================
        /*
         * 泛型也可以一次使用多個型別參數。
         *
         * Pair<K, V>
         *
         * K 代表 key 的型別。
         * V 代表 value 的型別。
         *
         * 這個概念很像 Map<K, V>。
         */
        System.out.println("================ 自建泛型 class：Pair<K, V> ================");

        Pair<String, Integer> score = new Pair<>("Tom", 90);

        String scoreName = score.getKey();
        Integer scoreValue = score.getValue();

        System.out.println("學生：" + scoreName);
        System.out.println("分數：" + scoreValue);

        Pair<Integer, String> product = new Pair<>(1001, "鍵盤");

        Integer productId = product.getKey();
        String productName = product.getValue();

        System.out.println("商品編號：" + productId);
        System.out.println("商品名稱：" + productName);
    }

}

/*
* 自建泛型類別 Box<T>
*
* T 是型別佔位符。
* 真正使用 Box 時，才決定 T 是什麼型別。
*
* Box<String>  裡面的 T 就是 String。
* Box<Integer> 裡面的 T 就是 Integer。
*/
class Box<T> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

/*
* 自建泛型類別 Pair<K, V>
*
* K 代表 key 的型別。
* V 代表 value 的型別。
*
* 例如：
* Pair<String, Integer>
* K = String
* V = Integer
*/
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
