package email.com.gmail.ttsai0509.mustache;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import email.com.gmail.ttsai0509.mustache.token.Token;
import email.com.gmail.ttsai0509.mustache.token.Type;
import email.com.gmail.ttsai0509.mustache.tokenizer.Tokenizer;

public class V2Test {

    public static void main(String[] args) throws IOException {
        File testFile = new File("src/test/resources/primitive/test");

        Tokenizer t = new Tokenizer(new FileReader(testFile));
        Token next;
        boolean asPlaintext = true;

        while (!(next = t.next(asPlaintext)).hasType(Type.EOF)) {
            System.out.println(next);
            switch (next.getType()) {
                case DELIMITER_START: asPlaintext = true; break;
                case DELIMITER_END: asPlaintext = false; break;
            }
        }
        System.out.println(next);
    }

}
