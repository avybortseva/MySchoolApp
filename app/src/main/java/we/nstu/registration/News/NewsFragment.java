package we.nstu.registration.News;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentNewsBinding;
import we.nstu.registration.databinding.FragmentScheduleBinding;

import com.google.firebase.firestore.DocumentReference;

import java.util.Collections;
import java.util.List;

public class NewsFragment extends Fragment implements NewsAdapter.OnItemClickListener, AddNewsDialog.OnNewsAddedListener {
    private FragmentNewsBinding binding;
    private NewsAdapter adapter;
    private List<News> newsList;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewsBinding.inflate(inflater, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.progressBar.setVisibility(View.VISIBLE);

        String email = MainActivity.getEmail(getContext());

        DocumentReference usersReference = MainActivity.database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        if (user.getAccessLevel() != 0)
                        {
                            binding.addNewsButton.setVisibility(View.VISIBLE);
                        }

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {

                                    String newsJson = ds.get("newsJson").toString();

                                    if (newsJson == "")
                                    {
                                        Toast.makeText(getContext(), "Новостей нет", Toast.LENGTH_SHORT).show();
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        SchoolNews schoolNews = SchoolNews.newsFromJson(newsJson);

                                        newsList = schoolNews.getNewsList();

                                        Collections.reverse(newsList);

                                        for (News news : newsList)
                                        {
                                            if(news.getNewsTitle().length() > 70)
                                            {
                                                news.setNewsTitle(news.getNewsTitle().substring(0,70) + "...");
                                            }
                                        }

                                        adapter = new NewsAdapter(newsList);
                                        adapter.setOnItemClickListener(this);
                                        binding.recyclerView.setAdapter(adapter);

                                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, getContext()));
                                        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке новостей", Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.GONE);
                                });

                    }
                }).addOnFailureListener(e->{
                    Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });

        binding.addNewsButton.setOnClickListener(v -> {
            showAddNewsDialog();
        });

        return binding.getRoot();
    }

    private void showAddNewsDialog() {
        AddNewsDialog dialog = new AddNewsDialog();
        dialog.setOnNewsAddedListener(this, getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "AddNewsDialog");
    }

    @Override
    public void onItemClick(News news) {
        Intent i = new Intent(requireActivity(), NewsFull.class);
        i.putExtra("newsID", news.getNewsID());
        startActivity(i);
    }

    @Override
    public void onNewsAdded() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new NewsFragment());
        fragmentTransaction.commit();
    }
}