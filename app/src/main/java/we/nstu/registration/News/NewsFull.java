package we.nstu.registration.News;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import we.nstu.registration.MainActivity;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.NewsActivityBinding;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class NewsFull extends AppCompatActivity
{

    private NewsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String id = getIntent().getStringExtra("newsID");

        String email = MainActivity.getEmail(getApplicationContext());

        DocumentReference usersReference = MainActivity.database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
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


    }
}
