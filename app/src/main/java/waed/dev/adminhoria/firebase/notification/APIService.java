package waed.dev.adminhoria.firebase.notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA7M_oBqQ:APA91bGkt9jt4OPQFbDvMXA6OFwn1kQBddfFpbJtEH7Awj4aMmYQXqnZKJxS3eQdrTrfXXFfFenRp3eZ5eJqnWQdzRCVPjNsAdOf8P3POMC1fQz1kv0on70wBbx8Fw0iVMVWxHI_2vu4"
    })
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body Sender body);
}
