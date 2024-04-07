package we.nstu.registration.Message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationService {
    @POST("send")
    Call<Void> sendNotification(@Query("token") String token, @Body NotificationBody body);
}