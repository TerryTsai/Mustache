package email.com.gmail.ttsai0509.mustache.reddit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RedditService {

    @GET("{sr}/top.json")
    Call<Response> getTop(@Path("sr") String sr);

}
