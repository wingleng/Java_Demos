import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

public class JustTest {
    @Test
    public void test(){
        String salt = "mszlu!@#";
        String password = DigestUtils.md5Hex(1 + salt);

        System.out.println(password);
     }
}
