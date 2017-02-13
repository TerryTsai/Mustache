package email.com.gmail.ttsai0509.mustache;

import java.util.function.Function;

class FunctionTest {

    private String text;

    private Function<FunctionTest, String> length = functionTest -> "The length of this text is " + functionTest.text.length();

    FunctionTest(String text) {
        this.text = text;
    }
}
