package we.nstu.registration.Event;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import we.nstu.registration.Lesson.Lesson;
import we.nstu.registration.Lesson.LessonAdapter;
import we.nstu.registration.Lesson.Schedule;
import we.nstu.registration.MainActivity;
import we.nstu.registration.News.NewsFragment;
import we.nstu.registration.News.SwipeToDeleteCallback;
import we.nstu.registration.R;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentEventsBinding;
import we.nstu.registration.databinding.FragmentNewsBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsFragment extends Fragment implements  AddEventDialog.OnEventAddedListener
{
    private FragmentEventsBinding binding;
    private EventAdapter adapter;
    private List<Event> eventList;
    private FirebaseFirestore database;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.progressBar.setVisibility(View.VISIBLE);

        DocumentReference usersReference = database.collection("users").document(MainActivity.getEmail(getContext()));
        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        if (user.getAccessLevel() != 0)
                        {
                            binding.addEventButton.setVisibility(View.VISIBLE);
                        }

                        database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                                .addOnSuccessListener(ds -> {
                                    String eventsJson = ds.get("eventsJson").toString();

                                    if (eventsJson != "")
                                    {
                                        SchoolEvent schoolEvent = SchoolEvent.eventFromJson(eventsJson);
                                        eventList = schoolEvent.getEventList();

                                        Collections.reverse(eventList);

                                        adapter = new EventAdapter(eventList);

                                        binding.recyclerView.setAdapter(adapter);
                                        if (user.getAccessLevel() != 0)
                                        {
                                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackEvent(adapter, getContext()));
                                            itemTouchHelper.attachToRecyclerView(binding.recyclerView);
                                        }

                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        if(getContext() != null) {
                                            Toast.makeText(getContext(), "Событий нет", Toast.LENGTH_SHORT).show();
                                        }
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    if(getContext() != null)
                                    {
                                        Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                    }
                                    binding.progressBar.setVisibility(View.GONE);
                                });

                    }
                }).addOnFailureListener(e -> {
                    if(getContext() != null) {
                        Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                    }
                });

        binding.addEventButton.setOnClickListener(v -> showAddEventDialog());

        return binding.getRoot();
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog();
        dialog.setOnEventsAddedListener(this, getContext());
        dialog.show(getChildFragmentManager(), "AddEventDialog");
    }


    @Override
    public void onEventAdded() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new EventsFragment());
        fragmentTransaction.commit();
    }
}