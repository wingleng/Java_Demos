import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JustTest {

    StringEncryptor stringEncryptor;

    @Test
    public void test(){
        String salt = "mszlu!@#";
        String password = DigestUtils.md5Hex(1 + salt);

        System.out.println(password);
     }
     @Test
    public void getPassword(){
         BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
         //加密所需的salt(盐)
         textEncryptor.setPassword("");
         //要加密的数据（数据库的用户名或密码）
         String username = textEncryptor.encrypt("");
         String password = textEncryptor.encrypt("");
         System.out.println("username:\n"+username);
         System.out.println("password:\n"+password);
     }
}
