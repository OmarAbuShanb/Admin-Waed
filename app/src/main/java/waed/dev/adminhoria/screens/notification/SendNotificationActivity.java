package waed.dev.adminhoria.screens.notification;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import waed.dev.adminhoria.databinding.ActivitySendNotificationBinding;
import waed.dev.adminhoria.firebase.notification.APIService;
import waed.dev.adminhoria.firebase.notification.Client;
import waed.dev.adminhoria.firebase.notification.Data;
import waed.dev.adminhoria.firebase.notification.Sender;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class SendNotificationActivity extends AppCompatActivity {
    private ActivitySendNotificationBinding binding;

    private APIService apiService;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        apiService = Client.getClient().create(APIService.class);
        loadingDialog = new LoadingDialog();

        setUpListener();
    }

    private void setUpListener() {
        binding.buPushNotification.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.etTitle.getText()).toString().trim();
            String details = Objects.requireNonNull(binding.etDetails.getText()).toString().trim();

            if (checkData(title, details)) {
                sendNotification(title, details);
            }
        });
    }

    private boolean checkData(String title, String details) {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(details);
    }

    private void sendNotification(String title, String details) {
        Data data = new Data(title, details);
        Sender sender = new Sender(data, "/topics/news");
        sendNotificationFCM(sender);
    }

    private void sendNotificationFCM(Sender sender) {
        loadingDialog.show(getSupportFragmentManager(), "Login");

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
                                    Toast.makeText(getBaseContext(), "حدث خطأ ما :(", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | IOException e) {
                                Toast.makeText(getBaseContext(), "حدث خطأ ما :(\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        loadingDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        UtilsGeneral.getInstance().showToast(getBaseContext(), t.getMessage());
                        loadingDialog.dismiss();
                    }
                });
    }
}