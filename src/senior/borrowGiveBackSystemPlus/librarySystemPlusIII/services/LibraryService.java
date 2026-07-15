package senior.borrowGiveBackSystemPlus.librarySystemPlusIII.services;

import java.util.ArrayList;
import java.util.List;

import senior.borrowGiveBackSystemPlus.librarySystemPlusIII.repositories.BookRepository;
import senior.borrowGiveBackSystemPlus.librarySystemPlusIII.repositories.MemberRepository;
import senior.borrowGiveBackSystemPlus.main.IBorrowGiveBack;
import senior.borrowGiveBackSystemPlus.models.Book;
import senior.borrowGiveBackSystemPlus.models.Category;
import senior.borrowGiveBackSystemPlus.models.Item;
import senior.borrowGiveBackSystemPlus.models.Member;

public class LibraryService implements IBorrowGiveBack<Member> {
    private BookRepository repository;
    private MemberRepository memberRepository;
    private List<Book> books;

    public LibraryService() throws Exception {
        this.repository = new BookRepository();
        this.memberRepository = new MemberRepository();
        books = repository.loadBooks();
    }

    public Member login(String account, String password) throws Exception {
        return memberRepository.login(account, password);
    }

    // 查看所有的書
    public List<Book> getAllBooks() throws Exception {
        books = repository.loadBooks();
        return new ArrayList<>(books);
    }

    public List<Member> getAllMembers(Member currentMember) throws Exception {
        checkAdmin(currentMember);
        return memberRepository.loadMembers();
    }

    public List<Category> getAllCategories() throws Exception {
        return repository.loadCategories();
    }

    public List<Item> getAllItems() throws Exception {
        return repository.loadItems();
    }

    // 借
    @Override
    public String borrow(int number, Member member) throws Exception {
        // 1. 檢查傳入的資料
        if (member == null) {
            throw new Exception("請先登入會員");
        }

        // 2. 根據number 須取得"目標書"的物件 => 檢查是否已被借走
        Book targetBook = checkBook(number);

        // 3. 有被借走的話要回傳: 已被xxx借走
        if (!targetBook.isAvailable()) {
            return "編號: " + number + ",書名: " + targetBook.getTitle() + " 已被" + targetBook.getBorrowUser() + "借走";
        }

        // 4. 沒被借走的話更改資料狀態(available:false, borrowUser)且回傳: 借閱 xx編號 xx書 成功
        targetBook.setAvailable(false);
        targetBook.setBorrowUser(member.getAccount());
        targetBook.setBorrowMemberId(member.getId());
        repository.saveBook(targetBook);

        return member.getAccount() + " 借閱 編號: " + number + ",書名: " + targetBook.getTitle() + " 成功";
    }

    // 還
    @Override
    public String giveBack(int number) throws Exception {
        // 1. 根據number 須取得"目標書"的物件
        Book targetBook = checkBook(number);
        // 2. 檢查是否已被借走, 沒有被借走要回傳: 此書並無借出
        if (targetBook.isAvailable()) {
            return "編號: " + number + ",書名: " + targetBook.getTitle() + "並無借出";
        }

        // 3. 有被借出的話 則要更改借出的狀態available:true, 並且把borrowUser 改成NULL
        targetBook.setAvailable(true);
        targetBook.setBorrowUser("NULL");
        targetBook.setBorrowMemberId(0);
        repository.saveBook(targetBook);

        return "編號: " + number + ",書名: " + targetBook.getTitle() + " 歸還成功";
    }

    public void addMember(Member currentMember, String account, String displayName, String password, boolean admin) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(account, "帳號");
        checkNotEmpty(displayName, "顯示姓名");
        checkNotEmpty(password, "密碼");
        memberRepository.addMember(account, displayName, password, admin);
    }

    public void updateMemberInfo(Member currentMember, int id, String account, String displayName, boolean admin) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(account, "帳號");
        checkNotEmpty(displayName, "顯示姓名");
        memberRepository.updateMemberInfo(id, account, displayName, admin);
    }

    public void updateMemberPassword(Member currentMember, int id, String password) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(password, "密碼");
        memberRepository.updateMemberPassword(id, password);
    }

    public void deleteMember(Member currentMember, int id) throws Exception {
        checkAdmin(currentMember);
        if (currentMember.getId() == id) {
            throw new Exception("不能刪除目前登入中的管理員帳號");
        }
        memberRepository.deleteMember(id);
    }

    public void addCategory(Member currentMember, String name) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(name, "分類名稱");
        repository.addCategory(name);
    }

    public void updateCategory(Member currentMember, int id, String name) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(name, "分類名稱");
        repository.updateCategory(id, name);
    }

    public void deleteCategory(Member currentMember, int id) throws Exception {
        checkAdmin(currentMember);
        repository.deleteCategory(id);
    }

    public void addItem(Member currentMember, int categoryId, String name) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(name, "子分類名稱");
        repository.addItem(categoryId, name);
    }

    public void updateItem(Member currentMember, int id, int categoryId, String name) throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(name, "子分類名稱");
        repository.updateItem(id, categoryId, name);
    }

    public void deleteItem(Member currentMember, int id) throws Exception {
        checkAdmin(currentMember);
        repository.deleteItem(id);
    }

    public void addBook(Member currentMember, int id, String title, String author, int categoryId, int itemId)
            throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(title, "書名");
        checkNotEmpty(author, "作者");
        repository.addBook(id, title, author, categoryId, itemId);
    }

    public void updateBook(Member currentMember, int id, String title, String author, int categoryId, int itemId)
            throws Exception {
        checkAdmin(currentMember);
        checkNotEmpty(title, "書名");
        checkNotEmpty(author, "作者");
        repository.updateBook(id, title, author, categoryId, itemId);
    }

    public void deleteBook(Member currentMember, int id) throws Exception {
        checkAdmin(currentMember);
        repository.deleteBook(id);
    }

    private Book checkBook(int number) throws Exception {
        books = repository.loadBooks();
        Book targetBook = null;

        for (Book oneBook : this.books) {
            // 把所有書每本取出比對number 得到該目標書籍 就break
            int oneBookNumber = Integer.parseInt(oneBook.getNumber());
            if (oneBookNumber == number) {
                targetBook = oneBook;
                break;
            }
        }

        if (targetBook == null) {
            throw new Exception("找不到編號: " + number + " 的書籍");
        }

        return targetBook;
    }

    private void checkAdmin(Member member) throws Exception {
        if (member == null || !member.isAdmin()) {
            throw new Exception("權限不足，只有 admin 可以操作此功能");
        }
    }

    private void checkNotEmpty(String value, String fieldName) throws Exception {
        if (value == null || value.trim().isEmpty()) {
            throw new Exception(fieldName + "不能為空");
        }
    }
}
