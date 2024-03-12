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

public class EventsFragment extends Fragment implements EventAdapter.OnItemClickListener, AddEventDialog.OnEventAddedListener
{
    private FragmentEventsBinding binding;
    private EventAdapter adapter;
    private List<Event> eventList;

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

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.progressBar.setVisibility(View.VISIBLE);

        DocumentReference usersReference = MainActivity.database.collection("users").document(MainActivity.getEmail(getContext()));
        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                                .addOnSuccessListener(ds -> {
                                    String eventsJson = ds.get("eventsJson").toString();

                                    if (eventsJson != "")
                                    {
                                        SchoolEvent schoolEvent = SchoolEvent.eventFromJson(eventsJson);
                                        eventList = schoolEvent.getEventList();

                                        Collections.reverse(eventList);

                                        adapter = new EventAdapter(eventList);
                                        adapter.setOnItemClickListener(this);
                                        binding.recyclerView.setAdapter(adapter);
                                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackEvent(adapter));
                                        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Новостей нет", Toast.LENGTH_SHORT).show();
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.GONE);
                                });

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });

        binding.addEventButton.setOnClickListener(v -> showAddEventDialog());

        return binding.getRoot();
    }

    private void showAddEventDialog() {
        AddEventDialog dialog = new AddEventDialog();
        dialog.setOnEventsAddedListener(this);
        dialog.show(getChildFragmentManager(), "AddEventDialog");
    }
    @Override
    public void onItemClick(Event event) {
        Intent i = new Intent(requireActivity(), EventFull.class);
        i.putExtra("eventID", event.getEventID());
        startActivity(i);
    }

    @Override
    public void onEventAdded() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new EventsFragment());
        fragmentTransaction.commit();
    }
}