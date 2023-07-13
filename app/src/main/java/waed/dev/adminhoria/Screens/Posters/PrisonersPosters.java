package waed.dev.adminhoria.Screens.Posters;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import waed.dev.adminhoria.Adapters.PrisonerPostersAdapter;
import waed.dev.adminhoria.R;
import waed.dev.adminhoria.Utils.RotationPageTransformer;
import waed.dev.adminhoria.databinding.ActivityPrisonersPostersBinding;

public class PrisonersPosters extends AppCompatActivity {
    ActivityPrisonersPostersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersPostersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Integer> postersImage = new ArrayList<>();
        postersImage.add(R.drawable.temp_poster_1);
        postersImage.add(R.drawable.temp_poster_2);
        postersImage.add(R.drawable.temp_poster_3);
        postersImage.add(R.drawable.temp_poster_4);

        binding.postersPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        binding.postersPager.setPageTransformer(new RotationPageTransformer(160));
        binding.postersPager.setAdapter(new PrisonerPostersAdapter(postersImage));
    }
}