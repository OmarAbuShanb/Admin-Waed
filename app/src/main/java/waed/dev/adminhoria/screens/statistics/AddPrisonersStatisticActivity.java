package waed.dev.adminhoria.screens.statistics;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddPrisonersStatisticBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Statistic;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddPrisonersStatisticActivity extends AppCompatActivity {
    ActivityAddPrisonersStatisticBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri pdfUri, iconUri;
    private String pdfName, iconName;

    private String uuid, iconUrl, pdfUrl, title;
    private int number;

    private boolean deleteIcon, deletePdf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrisonersStatisticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setupListeners();
        putDataIfExist();
    }

    private void putDataIfExist() {
        Statistic statisticModel = (Statistic) getIntent().getSerializableExtra("model");
        if (statisticModel != null) {
            uuid = statisticModel.getUuid();
            pdfUrl = statisticModel.getPdfUrl();
            iconUrl = statisticModel.getIconUrl();

            binding.tvChoosePdfFile.setText(R.string._1_file_chooses);

            UtilsGeneral.getInstance()
                    .loadImage(this, statisticModel.getIconUrl())
                    .into(binding.ivImageStatistic);

            binding.etStatisticTitle.setText(statisticModel.getTitle());
            binding.etStatisticNumber.setText(String.valueOf(statisticModel.getNumber()));

            binding.btnAddStatistic.setText(R.string.update_the_statistic);
            binding.btnAddStatistic.setText(R.string.update);
        }
    }

    private void setupListeners() {
        binding.buChoosePdfFile.setOnClickListener(view ->
                getPdfLauncher.launch(new String[]{"application/pdf"}));

        binding.buChooseImageStatistic.setOnClickListener(view ->
                getStatisticImageLauncher.launch("image/*"));

        binding.btnAddStatistic.setOnClickListener(v -> performSetStatistic());
    }

    private boolean checkData(String title, String number) {
        boolean isValidNumber = false;
        if (!TextUtils.isEmpty(number)) {
            try {
                Integer.parseInt(number);
                isValidNumber = true;
            } catch (NumberFormatException e) {
                System.out.println(getString(R.string.please_enter_a_valid_number));
            }
        }
        return !TextUtils.isEmpty(title) && isValidNumber &&
                // If the user comes to add data
                ((pdfUri != null && iconUri != null)
                        // Or comes to update data
                        || (pdfUrl != null && iconUrl != null));
    }

    private void performSetStatistic() {
        String title = Objects.requireNonNull(binding.etStatisticTitle.getText()).toString().trim();
        String number = Objects.requireNonNull(binding.etStatisticNumber.getText()).toString().trim();

        if (checkData(title, number)) {
            loadingDialog.show(getSupportFragmentManager(), "setStatistics");

            this.title = title;
            this.number = Integer.parseInt(number);

            // If the user comes to add data
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }
            checkFollowingProcess();
        }
    }

    private void uploadStatisticsPdf() {
        firebaseController.uploadStatisticFile(uuid, pdfUri, pdfName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                pdfUri = null;
                pdfUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void uploadStatisticIcon() {
        firebaseController.uploadStatisticFile(uuid, iconUri, iconName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                iconUri = null;
                iconUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setStatistics(Statistic statistic) {
        firebaseController.setStatistic(statistic, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                checkIfAdminChangePdfOrIcon();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteStatisticFiles(String uuid) {
        firebaseController.deleteStatisticFiles(uuid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteFileUsingUrl(String fileUrl) {
        firebaseController.deleteFileUsingUrl(fileUrl, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                checkIfAdminChangePdfOrIcon();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private final ActivityResultLauncher<String[]> getPdfLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), result -> {
                if (result != null) {
                    pdfUri = result;
                    pdfName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                    if (pdfUrl != null) {
                        deletePdf = true;
                    } else {
                        binding.tvChoosePdfFile.setText(R.string._1_file_chooses);
                    }
                }
            });

    private final ActivityResultLauncher<String> getStatisticImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            binding.ivImageStatistic.setImageURI(result);
                            iconUri = result;
                            iconName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            if (iconUrl != null) {
                                deleteIcon = true;
                            }
                        }
                    }
            );

    private void dismissDialogAndFinishSuccessfully() {
        UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.task_completed_successfully));
        loadingDialog.dismiss();
        finish();
    }

    private void checkFollowingProcess() {
        if (pdfUri != null) {
            uploadStatisticsPdf();
        } else if (iconUri != null) {
            uploadStatisticIcon();
        } else {
            setStatistics(getStatistic());
        }
    }

    private void checkIfAdminChangePdfOrIcon() {
        if (deleteIcon && deletePdf) {
            deleteStatisticFiles(uuid);
        } else if (deleteIcon) {
            deleteFileUsingUrl(iconUrl);
            deleteIcon = false;
        } else if (deletePdf) {
            deleteFileUsingUrl(pdfUrl);
            deletePdf = false;
        } else {
            dismissDialogAndFinishSuccessfully();
        }
    }

    private Statistic getStatistic() {
        return new Statistic(
                uuid, title, iconUrl, number, pdfUrl, Timestamp.now()
        );
    }
}