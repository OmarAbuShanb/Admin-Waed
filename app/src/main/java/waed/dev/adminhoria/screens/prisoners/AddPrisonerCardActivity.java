package waed.dev.adminhoria.screens.prisoners;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddPrisonerCardBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.PrisonerCard;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddPrisonerCardActivity extends AppCompatActivity {
    private ActivityAddPrisonerCardBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri imageUri;
    private String imageName;

    private String uuid, imageUrl, name, dateOfArrest, judgment, living;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrisonerCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        putDate();
        setUpListeners();
    }

    private void setUpListeners() {
        binding.btnAddPrisonerCard.setOnClickListener(view -> performSetPrisonerCard());
        binding.etSelectArrestedDate.setOnClickListener(view -> showDatePickerDialog());

        binding.buChoosePrisonerImage.setOnClickListener(view ->
                getPrisonerImageLauncher.launch("image/*"));
    }

    private void putDate() {
        PrisonerCard prisonerCardModel = (PrisonerCard) getIntent().getSerializableExtra("model");
        if (prisonerCardModel != null) {
            uuid = prisonerCardModel.getUuid();
            imageUrl = prisonerCardModel.getImageUrl();

            UtilsGeneral.getInstance()
                    .loadImage(this, prisonerCardModel.getImageUrl())
                    .into(binding.ivPrisoner);

            binding.etName.setText(prisonerCardModel.getName());

            binding.etSelectArrestedDate.setText(prisonerCardModel.getDateOfArrest());
            binding.etSelectArrestedDate.setTextColor(getResources().getColor(R.color.black));

            binding.etJudgment.setText(prisonerCardModel.getJudgment());
            binding.etLiving.setText(prisonerCardModel.getLiving());

            binding.headContentName.setText(R.string.update_the_prisoner_card);
            binding.btnAddPrisonerCard.setText(R.string.update);
        }
    }

    private boolean checkData(String name, String dateOfArrest, String judgment, String living) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(dateOfArrest)
                && !TextUtils.isEmpty(judgment) && !TextUtils.isEmpty(living)
                // If the user comes to add data
                && (imageUri != null
                // Or comes to update data
                || imageUrl != null);
    }

    private void performSetPrisonerCard() {
        String name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        String dateOfArrest = Objects.requireNonNull(binding.etSelectArrestedDate.getText()).toString().trim();
        String judgment = Objects.requireNonNull(binding.etJudgment.getText()).toString().trim();
        String living = Objects.requireNonNull(binding.etLiving.getText()).toString().trim();

        if (checkData(name, dateOfArrest, judgment, living)) {
            this.name = name;
            this.dateOfArrest = dateOfArrest;
            this.judgment = judgment;
            this.living = living;

            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }

            loadingDialog.show(getSupportFragmentManager(), "AddPrisonerCardActivity");
            checkFollowingProcess();
        }
    }

    private void uploadImage() {
        firebaseController.uploadPrisonerImage(imageUri, imageName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                imageUri = null;
                imageUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setNews(PrisonerCard prisonerCard) {
        firebaseController.setPrisonerCard(prisonerCard, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                dismissDialogAndFinishSuccessfully();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void dismissDialogAndFinishSuccessfully() {
        UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.task_completed_successfully));
        loadingDialog.dismiss();
        finish();
    }

    private final ActivityResultLauncher<String> getPrisonerImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            imageName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            binding.ivPrisoner.setImageURI(result);
                        }
                    }
            );

    public void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = DatePickerDialog
                .newInstance((view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    setUpEditTextSelectArrestedDate();
                }, calendar);
        pickerDialog.setAccentColor(getResources().getColor(R.color.app_color));
        pickerDialog.setMaxDate(calendar);
        calendar.set(Calendar.YEAR, 1950);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        pickerDialog.setMinDate(calendar);
        pickerDialog.show(getSupportFragmentManager(), "showDatePickerDialog");
    }

    private void setUpEditTextSelectArrestedDate() {
        String stringDate = UtilsGeneral.getInstance().getStringDateFromDate(calendar.getTime());
        binding.etSelectArrestedDate.setText(stringDate);
        binding.etSelectArrestedDate.setTextColor(getResources().getColor(R.color.black));
    }

    private void checkFollowingProcess() {
        if (imageUri != null) {
            uploadImage();
        } else {
            setNews(getPrisonerCard());
        }
    }

    private PrisonerCard getPrisonerCard() {
        return new PrisonerCard(uuid, imageUrl, name, dateOfArrest, judgment, living, Timestamp.now());
    }
}