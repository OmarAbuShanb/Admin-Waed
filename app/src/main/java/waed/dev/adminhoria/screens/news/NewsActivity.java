package waed.dev.adminhoria.screens.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.NewsAdapter;
import waed.dev.adminhoria.databinding.ActivityNewsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.NewsListListener,
        NewsAdapter.DeleteNewsListListener {

    private ActivityNewsBinding binding;
    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();
        setupListeners();
        setupNewsAdapter();
        getNews();
    }

    private void getNews() {
        binding.progressNews.setVisibility(View.VISIBLE);
        firebaseController.getNews(new FirebaseController.GetNewsCallback() {
            @Override
            public void onSuccess(ArrayList<News> news) {
                binding.progressNews.setVisibility(View.GONE);
                updateNewsAdapter(news);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void setupNewsAdapter() {
        newsAdapter = new NewsAdapter(new ArrayList<>());
        binding.newsRecyclerView.setAdapter(newsAdapter);
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.newsRecyclerView.setHasFixedSize(true);
    }

    private void updateNewsAdapter(ArrayList<News> models) {
        newsAdapter.setNews(models);
        newsAdapter.setNewsListCallback(this);
        newsAdapter.setDeleteNewsCallback(this);
    }

    private void setupListeners() {
        binding.floatAddNews.setOnClickListener(view ->
                startActivity(new Intent(getBaseContext(), AddNewsActivity.class)));
    }

    @Override
    public void onClickItemListener(News model) {
        Intent intent = new Intent(getBaseContext(), AddNewsActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteListener(String newsId, int positionItem) {
        loadingDialog.show(getSupportFragmentManager(), "deleteNews");
        firebaseController.deleteNews(newsId, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                newsAdapter.removeItem(positionItem);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }
}