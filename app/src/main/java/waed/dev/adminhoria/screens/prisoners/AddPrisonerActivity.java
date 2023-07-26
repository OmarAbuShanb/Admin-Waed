package waed.dev.adminhoria.screens.prisoners;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityAddPrisonerBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.PrisonerCard;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class AddPrisonerActivity extends AppCompatActivity {

    private ActivityAddPrisonerBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private PrisonerCard prisonerCardModel;
    private Uri imageUri = null;

    private String name;
    private String dateOfArrest;
    private String judgment;
    private String living;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrisonerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        prisonerCardModel = (PrisonerCard) getIntent().getSerializableExtra("model");
        if (prisonerCardModel != null) {
            putDate(prisonerCardModel);

            binding.btnAddPrisonerCard.setOnClickListener(view ->
                    performSetPrisonerCard(true));
        } else {
            binding.btnAddPrisonerCard.setOnClickListener(view ->
                    performSetPrisonerCard(false));
        }

        binding.etSelectArrestedDate.setOnClickListener(view -> showDatePickerDialog());

        binding.buChoosePrisonerImage.setOnClickListener(view ->
                getContentLauncher.launch("image/*"));
    }

    private void putDate(PrisonerCard model) {
        UtilsGeneral.getInstance()
                .loadImage(this, model.getImageUrl())
                .into(binding.ivPrisoner);


        binding.etName.setText(model.getName());

        binding.etSelectArrestedDate.setText(model.getDateOfArrest());
        binding.etSelectArrestedDate.setTextColor(getResources().getColor(R.color.black));

        binding.etJudgment.setText(model.getJudgment());
        binding.etLiving.setText(model.getLiving());
        binding.btnAddPrisonerCard.setText("Update prisonerCard");
    }

    private boolean checkData(String name, String dateOfArrest, String judgment, String living) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(dateOfArrest)
                && !TextUtils.isEmpty(judgment) && !TextUtils.isEmpty(living);
    }

    private void performSetPrisonerCard(boolean updatePrisonerCard) {
        name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        dateOfArrest = Objects.requireNonNull(binding.etSelectArrestedDate.getText()).toString().trim();
        judgment = Objects.requireNonNull(binding.etJudgment.getText()).toString().trim();
        living = Objects.requireNonNull(binding.etLiving.getText()).toString().trim();

        if (checkData(name, dateOfArrest, judgment, living)) {
            loadingDialog.show(getSupportFragmentManager(), "setPrisonerCard");

            if (updatePrisonerCard) {
                if (imageUri != null) {
                    uploadImage(true);
                } else {
                    prisonerCardModel.setName(name);
                    prisonerCardModel.setDateOfArrest(dateOfArrest);
                    prisonerCardModel.setJudgment(judgment);
                    prisonerCardModel.setLiving(living);
                    setNews(prisonerCardModel, prisonerCardModel.getId());
                }
            } else {
                if (imageUri != null) {
                    uploadImage(false);
                }
            }
        }
    }

    private void uploadImage(boolean updatePrisonerCard) {
        firebaseController.uploadImage(imageUri, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                if (updatePrisonerCard) {
                    prisonerCardModel.setImageUrl(imageUrl);
                    prisonerCardModel.setName(name);
                    prisonerCardModel.setDateOfArrest(dateOfArrest);
                    prisonerCardModel.setJudgment(judgment);
                    prisonerCardModel.setLiving(living);
                    setNews(prisonerCardModel, prisonerCardModel.getId());
                } else {
                    String id = UUID.randomUUID().toString();
                    PrisonerCard prisonerCard =
                            new PrisonerCard(id, imageUrl, name, dateOfArrest, judgment, living);
                    setNews(prisonerCard, id);
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setNews(PrisonerCard prisonerCard, String id) {
        firebaseController.setPrisonerCard(prisonerCard, id, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                UtilsGeneral.getInstance().showToast(getBaseContext(), "تمت العملية بنجاح");
                loadingDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }


    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            binding.ivPrisoner.setImageURI(result);
                        }
                    }
            );

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            binding.etSelectArrestedDate.setText(toStringSimpleDate(calendar.getTime()));
            binding.etSelectArrestedDate.setTextColor(getResources().getColor(R.color.black));
        }, calendar);
        pickerDialog.setAccentColor(getResources().getColor(R.color.app_color));
        pickerDialog.setMaxDate(calendar);
        calendar.set(Calendar.YEAR, 1950);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        pickerDialog.setMinDate(calendar);
        pickerDialog.show(getSupportFragmentManager(), "");
    }

    public static String toStringSimpleDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(date);
    }
}