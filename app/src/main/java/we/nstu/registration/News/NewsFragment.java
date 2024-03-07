package we.nstu.registration.News;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import we.nstu.registration.MainActivity;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentNewsBinding;
import we.nstu.registration.databinding.FragmentScheduleBinding;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class NewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentNewsBinding binding;
    private NewsAdapter adapter;
    private List<News> newsList;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {

                                    String newsJson = ds.get("newsJson").toString();

                                    SchoolNews schoolNews = SchoolNews.newsFromJson(newsJson);

                                    newsList = schoolNews.getNewsList();

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
                                    binding.progressBar.setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке новостей", Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.GONE);
                                });

                    }
                }).addOnFailureListener(e->{
                    Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });

        return binding.getRoot();
    }

    @Override
    public void onItemClick(News news) {
        Intent i = new Intent(requireActivity(), NewsFull.class);
        i.putExtra("newsID", news.getNewsID());
        startActivity(i);
    }
}