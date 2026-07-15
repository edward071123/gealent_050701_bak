package senior.borrowGiveBackSystemPlus.librarySystemPlusIII;

import java.util.List;
import java.util.Scanner;

import senior.borrowGiveBackSystemPlus.librarySystemPlusIII.services.LibraryService;
import senior.borrowGiveBackSystemPlus.models.Book;
import senior.borrowGiveBackSystemPlus.models.Category;
import senior.borrowGiveBackSystemPlus.models.Item;
import senior.borrowGiveBackSystemPlus.models.Member;

public class Demo {
    public static void main(String[] args) {
        boolean working = true;
        LibraryService service = null;

        try {
            service = new LibraryService();
        } catch (Exception e) {
            working = false;
            System.out.println(e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        Member currentMember = null;

        while (working && currentMember == null) {
            try {
                System.out.println("===== 校園圖書館系統登入 =====");
                System.out.print("帳號：");
                String account = sc.nextLine();
                System.out.print("密碼：");
                String password = sc.nextLine();
                currentMember = service.login(account, password);
                System.out.println("登入成功，" + currentMember.getAccount()
                        + (currentMember.isAdmin() ? "（admin）" : ""));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.print("是否重新登入？(y/n)：");
                if (!sc.nextLine().equalsIgnoreCase("y")) {
                    working = false;
                }
            }
        }

        while (working) {
            printMainMenu(currentMember);
            String choice = sc.nextLine();

            try {
                if (choice.equals("1")) {
                    printBookInfo(service.getAllBooks());
                } else if (choice.equals("2")) {
                    System.out.print("請輸入書籍編號(數字)：");
                    int number = Integer.parseInt(sc.nextLine());
                    System.out.println(service.borrow(number, currentMember));
                } else if (choice.equals("3")) {
                    System.out.print("請輸入書籍編號(數字)：");
                    int number = Integer.parseInt(sc.nextLine());
                    System.out.println(service.giveBack(number));
                } else if (choice.equals("4") && currentMember.isAdmin()) {
                    manageMembers(sc, service, currentMember);
                } else if (choice.equals("5") && currentMember.isAdmin()) {
                    manageCategories(sc, service, currentMember);
                } else if (choice.equals("6") && currentMember.isAdmin()) {
                    manageItems(sc, service, currentMember);
                } else if (choice.equals("7") && currentMember.isAdmin()) {
                    manageBooks(sc, service, currentMember);
                } else if (choice.equals("0")) {
                    System.out.println("===== 離開 =====");
                    working = false;
                } else {
                    System.out.println("===== 輸入錯誤 =====");
                }
            } catch (Exception e) {
                System.out.println("");
                System.out.println("=====操作錯誤====");
                System.out.println(e.getMessage());
            }
        }

        sc.close();
    }

    private static void printMainMenu(Member member) {
        System.out.println("===== 校園圖書館系統 =====");
        System.out.println("1. 查看所有書籍");
        System.out.println("2. 借書");
        System.out.println("3. 還書");

        if (member.isAdmin()) {
            System.out.println("4. 管理會員");
            System.out.println("5. 管理分類");
            System.out.println("6. 管理子分類");
            System.out.println("7. 管理書籍");
        }

        System.out.println("0. 離開");
        System.out.print("請輸入操作數字：");
    }

    private static void manageMembers(Scanner sc, LibraryService service, Member currentMember) throws Exception {
        System.out.println("===== 會員管理 =====");
        printMemberInfo(service.getAllMembers(currentMember));
        System.out.println("1. 新增會員");
        System.out.println("2. 修改會員資料");
        System.out.println("3. 修改會員密碼");
        System.out.println("4. 刪除會員");
        System.out.print("請輸入操作數字：");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.print("帳號：");
            String account = sc.nextLine();
            System.out.print("顯示姓名：");
            String displayName = sc.nextLine();
            System.out.print("密碼：");
            String password = sc.nextLine();
            System.out.print("是否為 admin？(y/n)：");
            boolean admin = sc.nextLine().equalsIgnoreCase("y");
            service.addMember(currentMember, account, displayName, password, admin);
            System.out.println("新增會員成功");
        } else if (choice.equals("2")) {
            System.out.print("會員ID：");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("帳號：");
            String account = sc.nextLine();
            System.out.print("顯示姓名：");
            String displayName = sc.nextLine();
            System.out.print("是否為 admin？(y/n)：");
            boolean admin = sc.nextLine().equalsIgnoreCase("y");
            service.updateMemberInfo(currentMember, id, account, displayName, admin);
            System.out.println("修改會員資料成功");
        } else if (choice.equals("3")) {
            System.out.print("會員ID：");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("新密碼：");
            String password = sc.nextLine();
            service.updateMemberPassword(currentMember, id, password);
            System.out.println("修改會員密碼成功");
        } else if (choice.equals("4")) {
            System.out.print("會員ID：");
            int id = Integer.parseInt(sc.nextLine());
            service.deleteMember(currentMember, id);
            System.out.println("刪除會員成功");
        }
    }

    private static void manageCategories(Scanner sc, LibraryService service, Member currentMember) throws Exception {
        System.out.println("===== 分類管理 =====");
        printCategoryInfo(service.getAllCategories());
        System.out.println("1. 新增分類");
        System.out.println("2. 修改分類");
        System.out.println("3. 刪除分類");
        System.out.print("請輸入操作數字：");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.print("分類名稱：");
            String name = sc.nextLine();
            service.addCategory(currentMember, name);
            System.out.println("新增分類成功");
        } else if (choice.equals("2")) {
            System.out.print("分類ID：");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("分類名稱：");
            String name = sc.nextLine();
            service.updateCategory(currentMember, id, name);
            System.out.println("修改分類成功");
        } else if (choice.equals("3")) {
            System.out.print("分類ID：");
            int id = Integer.parseInt(sc.nextLine());
            service.deleteCategory(currentMember, id);
            System.out.println("刪除分類成功");
        }
    }

    private static void manageItems(Scanner sc, LibraryService service, Member currentMember) throws Exception {
        System.out.println("===== 子分類管理 =====");
        printCategoryInfo(service.getAllCategories());
        printItemInfo(service.getAllItems());
        System.out.println("1. 新增子分類");
        System.out.println("2. 修改子分類");
        System.out.println("3. 刪除子分類");
        System.out.print("請輸入操作數字：");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.print("分類ID：");
            int categoryId = Integer.parseInt(sc.nextLine());
            System.out.print("子分類名稱：");
            String name = sc.nextLine();
            service.addItem(currentMember, categoryId, name);
            System.out.println("新增子分類成功");
        } else if (choice.equals("2")) {
            System.out.print("子分類ID：");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("分類ID：");
            int categoryId = Integer.parseInt(sc.nextLine());
            System.out.print("子分類名稱：");
            String name = sc.nextLine();
            service.updateItem(currentMember, id, categoryId, name);
            System.out.println("修改子分類成功");
        } else if (choice.equals("3")) {
            System.out.print("子分類ID：");
            int id = Integer.parseInt(sc.nextLine());
            service.deleteItem(currentMember, id);
            System.out.println("刪除子分類成功");
        }
    }

    private static void manageBooks(Scanner sc, LibraryService service, Member currentMember) throws Exception {
        System.out.println("===== 書籍管理 =====");
        printBookInfo(service.getAllBooks());
        printCategoryInfo(service.getAllCategories());
        printItemInfo(service.getAllItems());
        System.out.println("1. 新增書籍");
        System.out.println("2. 修改書籍");
        System.out.println("3. 刪除書籍");
        System.out.print("請輸入操作數字：");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.print("書籍ID：");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("書名：");
            String title = sc.nextLine();
            System.out.print("作者：");
            String author = sc.nextLine();
            System.out.print("分類ID：");
            int categoryId = Integer.parseInt(sc.nextLine());
            System.out.print("子分類ID：");
            int itemId = Integer.parseInt(sc.nextLine());
            service.addBook(currentMember, id, title, author, categoryId, itemId);
            System.out.println("新增書籍成功");
        } else if (choice.equals("2")) {
            System.out.print("書籍ID：");
            int id = Integer.parseInt(sc.nextLine());
            System.out.print("書名：");
            String title = sc.nextLine();
            System.out.print("作者：");
            String author = sc.nextLine();
            System.out.print("分類ID：");
            int categoryId = Integer.parseInt(sc.nextLine());
            System.out.print("子分類ID：");
            int itemId = Integer.parseInt(sc.nextLine());
            service.updateBook(currentMember, id, title, author, categoryId, itemId);
            System.out.println("修改書籍成功");
        } else if (choice.equals("3")) {
            System.out.print("書籍ID：");
            int id = Integer.parseInt(sc.nextLine());
            service.deleteBook(currentMember, id);
            System.out.println("刪除書籍成功");
        }
    }

    public static void printBookInfo(List<Book> books) {
        System.out.println("===== 書籍列表起始 =====");

        for (int i = 0; i < books.size(); i++) {
            System.out.println();
            System.out.println(books.get(i).getInfo());
        }

        System.out.println("===== 書籍列表結束 =====");
    }

    private static void printMemberInfo(List<Member> members) {
        for (Member member : members) {
            System.out.println();
            System.out.println(member.getInfo());
        }
    }

    private static void printCategoryInfo(List<Category> categories) {
        System.out.println("===== 分類列表 =====");
        for (Category category : categories) {
            System.out.println(category.getId() + ". " + category.getName());
        }
    }

    private static void printItemInfo(List<Item> items) {
        System.out.println("===== 子分類列表 =====");
        for (Item item : items) {
            System.out.println(item.getId() + ". category_id=" + item.getCategoryId() + " / " + item.getName());
        }
    }
}
