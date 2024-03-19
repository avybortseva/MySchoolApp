package we.nstu.registration.News;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.NewsActivityBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewsFull extends AppCompatActivity
{
    private FirebaseFirestore database;
    private NewsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        String id = getIntent().getStringExtra("newsID");

        String email = MainActivity.getEmail(getApplicationContext());

        DocumentReference usersReference = database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {

                                    String newsJson = ds.get("newsJson").toString();

                                    SchoolNews schoolNews = SchoolNews.newsFromJson(newsJson);

                                    List<News> newsList = schoolNews.getNewsList();

                                    for (News news : newsList)
                                    {
                                        if (news.getNewsID().equals(id))
                                        {
                                            binding.titleText.setText(news.getNewsTitle());
                                            binding.textBody.setText(news.getNewsText());
                                            binding.date.setText(news.getNewsTime());
                                        }
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getApplicationContext(), "Ошибка при загрузке новостей", Toast.LENGTH_SHORT).show();
                                });

                    }
                }).addOnFailureListener(e->{
                    Toast.makeText(getApplicationContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });
        binding.backToNewsFragment.setOnClickListener(v -> {
            goBackToNewsFragment();
        });
    }

    private void goBackToNewsFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
