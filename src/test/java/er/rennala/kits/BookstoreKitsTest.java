package er.rennala.kits;

import er.rennala.domain.RennalaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookstoreKitsTest {

    @Test
    void testPullTextSuccess() {
        String s = BookstoreKits.pullText("link-melina/app-online.toml");
        System.out.println(s);
    }

    @Test
    void testPullTextEmptyFullName() {
        RennalaException exception = assertThrows(RennalaException.class, () -> BookstoreKits.pullText(""));
        assertEquals("[Bookstore] Please set fullName", exception.getMessage());
    }

    @Test
    void testWriteToFile() {
        String content = "Hello, World!";
        String s = BookstoreKits.writeTextToFile("test.txt", content);
        System.out.println(s);
    }

}