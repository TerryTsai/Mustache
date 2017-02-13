package email.com.gmail.ttsai0509.mustache;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class FunctionalTest {

    @Test
    public void simple() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("error", "There was an error.");
        test("/simple", hash);
    }

    @Test
    public void section() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("pairs", Arrays.asList(
                new Pair<>("one", "a"),
                new Pair<>("two", "b"),
                new Pair<>("three", "c"),
                new Pair<>("four", "d"),
                new Pair<>("five", "e")
        ));
        test("/section", hash);
    }

    @Test
    public void inverse() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("exist", "asdf");
        test("/inverse", hash);
    }

    @Test
    public void nestlist() throws IOException {
        List<Map<String, List<Pair<String, String>>>> list = Arrays.asList(
                Collections.singletonMap("pairs", Arrays.asList(new Pair<>("1", "a"), new Pair<>("1", "b"))),
                Collections.singletonMap("pairs", Arrays.asList(new Pair<>("2", "a"), new Pair<>("2", "b"))),
                Collections.singletonMap("pairs", Arrays.asList(new Pair<>("3", "a"), new Pair<>("3", "b")))
        );
        Map<String, Object> hash = new HashMap<>();
        hash.put("list", list);
        test("/nestlist", hash);
    }

    @Test
    public void github() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("name", "Chris");
        hash.put("value", 10000);
        hash.put("taxed_value", 10000 - (10000 * 0.4));
        hash.put("in_ca", true);
        test("/github", hash);
    }

    @Test
    public void empty() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("empty", Collections.emptyList());
        test("/empty", hash);
    }

    @Test
    public void primitive() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("int", Integer.MAX_VALUE);
        hash.put("byte", Byte.MAX_VALUE);
        hash.put("bool", true);
        hash.put("float", 3.5f);
        hash.put("long", Long.MAX_VALUE);
        hash.put("char", 'f');
        hash.put("short", Short.MAX_VALUE);
        hash.put("string", "string thing");
        hash.put("double", 1.0004);
        hash.put("map", Collections.singletonMap("key", "value"));
        hash.put("list", Collections.singletonList("item"));
        hash.put("array", new String[] {"1"});
        hash.put("zero", 0);
        hash.put("null", null);
        hash.put("false", false);
        hash.put("emptylist", Collections.emptyList());
        hash.put("emptyarray", new String[0]);
        test("/primitive", hash);
    }

    @Test
    public void dotnot() throws IOException {
        Map<String, Object> hash = new HashMap<>();
        hash.put("a", Collections.singletonMap("b", Collections.singletonMap("c", "This is the output of a.b.c")));
        hash.put("z", new Pair<>(new Pair<>(new Pair<>("This is the output of z.key.key.key", "z"), "z"), "z"));
        test("/dotnot", hash);
    }

    @Test
    public void ignore() throws IOException {
        List<Person> people = Arrays.asList(
                new Person("terry", 28, "Waterford Dr.", "programmer", "climber", "woodworker"),
                new Person("andrea", 26, "Stovall Rd.", "dancer", "reader", "tea", "music"),
                new Person("billy", 999, null),
                new Person("bob", 1234, null, "bob", "bob", "bob")
        );

        Map<String, Object> hash = new HashMap<>();
        hash.put("people", people);
        test("/ignore", hash);
    }

    @Test
    public void hidden() throws IOException {
        List<Map<String, List<String>>> list = Arrays.asList(
                Collections.singletonMap("variable", Arrays.asList("a", "b", "c", "d")),
                Collections.singletonMap("variable", Arrays.asList("e", "f", "g", "h")),
                Collections.singletonMap("variable", Arrays.asList("i", "j", "k", "l"))
        );
        Map<String, Object> hash = new HashMap<>();
        hash.put("variable", list);
        test("/hidden", hash);
    }

    @Test
    public void function() throws IOException {
        List<FunctionTest> list = Arrays.asList(
                new FunctionTest("ONE"),
                new FunctionTest("lkasjfdalkjfds"),
                new FunctionTest("flksdjaskldjfaslkdjfsfdklasjfaskjf")
        );
        Map<String, Object> hash = new HashMap<>();
        hash.put("tests", list);
        test("/function", hash);
    }

    private static void test(String folder, Object hash) throws IOException {
        File testFile = new File(FunctionalTest.class.getResource(folder + "/" + "te" + "st").getFile());
        Reader testReader = new MustacheReader(new FileReader(testFile), hash);

        File solnFile = new File(FunctionalTest.class.getResource(folder + "/" + "so" + "ln").getFile());
        Reader solnReader = new FileReader(solnFile);

        int i = -1;
        int error = -1;
        int test, soln;
        String test1 = "";
        String soln1 = "";
        do {
            i++;
            test = testReader.read();
            soln = solnReader.read();
            test1 += (char) test;
            soln1 += (char) soln;
            error = (test != soln && error == -1) ? i : error;
        } while (test != -1 || soln != -1);
        Assert.assertEquals("Failed at character " + error, soln1, test1);
    }

}
