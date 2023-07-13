package waed.dev.adminhoria.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import waed.dev.adminhoria.MainActivity;
import waed.dev.adminhoria.databinding.ActivityLoginBinding;
import waed.dev.adminhoria.firebase.notification.APIService;
import waed.dev.adminhoria.firebase.notification.Client;
import waed.dev.adminhoria.firebase.notification.Data;
import waed.dev.adminhoria.firebase.notification.Sender;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";

    ActivityLoginBinding binding;

    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = Client.getClient().create(APIService.class);

        binding.btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
//            sendNotification(null, null);
        });
    }

    private void sendNotification(String message, String messageImageUrl) {
        Data data = new Data(
                "معنى أن الغاية هي الشكل وليس المحتوى",
                """
                         هذا النص هو مثال لنص يمكن أن يستبدل في نفس المساحة، لقد تم توليد هذا النص من مولد النص العربى، حيث يمكنك أن تولد مثل هذا النص أو العديد من النصوص الأخرى إضافة إلى زيادة عدد الحروف التى يولدها التطبيق.
                          إذا كنت تحتاج إلى عدد أكبر من الفقرات يتيح لك مولد النص العربى زيادة عدد الفقرات كما تريد، النص لن يبدو مقسما ولا يحوي أخطاء لغوية، مولد النص العربى مفيد لمصممي المواقع على وجه الخصوص، حيث يحتاج العميل فى كثير من الأحيان أن يطلع على صورة حقيقية لتصميم الموقع.
                          ومن هنا وجب على المصمم أن يضع نصوصا مؤقتة على التصميم ليظهر للعميل الشكل كاملاً،دور مولد النص العربى أن يوفر على المصمم عناء البحث عن نص بديل لا علاقة له بالموضوع الذى يتحدث عنه التصميم فيظهر بشكل لا يليق.
                        """
        );
        Sender sender = new Sender(data, "/topics/news");
        sendNotificationFCM(sender);
    }

    private void sendNotificationFCM(Sender sender) {
        apiService.sendNotification(sender)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                long messageId = object.getLong("message_id");
                                if (messageId > 0) {
                                    Toast.makeText(getBaseContext(), "تم ارسال الاشعار بنجاح", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getBaseContext(), "لم يتم ارسال الاشعار", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onResponse: " + response.toString());
                                }
                            } catch (JSONException | IOException e) {
                                Toast.makeText(getBaseContext(), "لم يتم ارسال الاشعار", Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                            // {"message_id":8184558659465678489}
                        }
                    }

                    // // {"multicast_id":6143843070518083714,"success":0,"failure":1,"canonical_ids":0,"results":[{"error":"NotRegistered"}]}
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }
}