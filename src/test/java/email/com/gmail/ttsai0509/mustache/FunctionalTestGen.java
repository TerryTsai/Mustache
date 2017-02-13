package email.com.gmail.ttsai0509.mustache;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class FunctionalTestGen {


    private static void gen(String file, Object hash) throws IOException {
        File testFile = new File(FunctionalTest.class.getResource(file).getFile());
        Reader testReader = new MustacheReader(new FileReader(testFile), hash);

        File solnFile = new File("so" + "ln");
        Writer solnWriter = new FileWriter(solnFile);

        int test;
        while ((test = testReader.read()) != -1) {
            solnWriter.write(test);
            System.out.print((char) test);
        }
        solnWriter.write(test);

        testReader.close();
        solnWriter.close();
    }

    public static void main(String[] args) throws IOException {
        String folder = "method";

        List<Person> list = new ArrayList<>();
        list.add(new Person("Terry", "Tsai"));
        list.add(new Person("Andrea", "Rougeau"));

        Map<String, Object> hash = new HashMap<>();
        hash.put("people", list);

        gen("/" + folder + "/te" + "st", hash);
    }

}
