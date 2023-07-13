package waed.dev.adminhoria.Screens.Prisoners;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.Adapters.PrisonerCardAdapter;
import waed.dev.adminhoria.Models.PrisonerCard;
import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityPrisonersCardsBinding;

public class PrisonersCards extends AppCompatActivity {
    ActivityPrisonersCardsBinding binding;
    PrisonerCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersCardsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatAddPrisoner.setOnClickListener(view -> startActivity(new Intent(PrisonersCards.this, AddPrisoner.class)));

        ArrayList<PrisonerCard> models = new ArrayList<>();
        models.add(new PrisonerCard("1", R.drawable.temp_card_1, "محمد الحلبي", "30/12/2016", "Kill 5 terrorists", "Gaza, Palestine"));
        models.add(new PrisonerCard("2", R.drawable.temp_card_2, "خضر عدنان", "30/12/2016", "Kill 5 terrorists", "Gaza, Palestine"));
        models.add(new PrisonerCard("3", R.drawable.temp_card_3, "أحمد رزق الزهار", "30/12/2016", "Kill 5 terrorists", "Gaza, Palestine"));
        models.add(new PrisonerCard("4", R.drawable.temp_card_4, "وليد دقة", "30/12/2016", "Kill 5 terrorists", "Gaza, Palestine"));

        adapter = new PrisonerCardAdapter(models);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        binding.prisonersCardsRecyclerView.setLayoutManager(manager);
        binding.prisonersCardsRecyclerView.setAdapter(adapter);
        binding.prisonersCardsRecyclerView.setHasFixedSize(true);
    }
}