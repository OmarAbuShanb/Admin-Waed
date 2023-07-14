package waed.dev.adminhoria.screens.posters;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.PrisonerPostersAdapter;
import waed.dev.adminhoria.R;
import waed.dev.adminhoria.Utils.RotationPageTransformer;
import waed.dev.adminhoria.databinding.ActivityPrisonersPostersBinding;

public class PrisonersPostersActivity extends AppCompatActivity {
    private ActivityPrisonersPostersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersPostersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupPosterPager(createDummyData());
    }

    private ArrayList<Integer> createDummyData() {
        ArrayList<Integer> postersImage = new ArrayList<>();
        postersImage.add(R.drawable.temp_poster_1);
        postersImage.add(R.drawable.temp_poster_2);
        postersImage.add(R.drawable.temp_poster_3);
        postersImage.add(R.drawable.temp_poster_4);
        return postersImage;
    }

    private void setupPosterPager(ArrayList<Integer> postersImage) {
        binding.postersPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        binding.postersPager.setPageTransformer(new RotationPageTransformer(160));
        binding.postersPager.setAdapter(new PrisonerPostersAdapter(postersImage));
    }
}