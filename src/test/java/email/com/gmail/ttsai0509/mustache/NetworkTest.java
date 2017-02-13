package email.com.gmail.ttsai0509.mustache;

import com.sun.net.httpserver.HttpServer;
import email.com.gmail.ttsai0509.mustache.reddit.Post;
import email.com.gmail.ttsai0509.mustache.reddit.RedditService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class NetworkTest {

    public static void main(String[] args) throws IOException {
        RedditService reddit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RedditService.class);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", httpExchange -> {
            httpExchange.sendResponseHeaders(200, 0);
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String subreddit = (path.length > 1) ? path[1] : "all";
            List<Post> posts = reddit.getTop(subreddit).execute().body().data.children.stream().map(pw -> pw.data).collect(Collectors.toList());
            String template = "{{#posts}}<p>{{$.author}} @{{$.created_utc}} ({{$.score}}) {{#thumbnail}}<img src='{{$.thumbnail}}'/>{{/}} <a href='{{$.url}}'>{{$.title}}</a></p>{{/}}";
            MustacheReader in = new MustacheReader(new StringReader(template), Collections.singletonMap("posts", posts));
            OutputStreamWriter out = new OutputStreamWriter(httpExchange.getResponseBody());
            int read;
            while ((read = in.read()) != -1) out.write(read);
            in.close();
            out.close();
        });
        server.start();

    }

}
