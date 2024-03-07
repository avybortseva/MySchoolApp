package we.nstu.registration.Lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import we.nstu.registration.MainActivity;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentScheduleBinding;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentScheduleBinding binding;
    private LocalDate date;
    private LessonAdapter adapter;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = LocalDate.now();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setSchedule();
        updateDateTextView();

        binding.nextDay.setOnClickListener(v -> {
            showProgressBar();
            date = date.plusDays(1);
            setSchedule();
            updateDateTextView();
        });

        binding.prevDay.setOnClickListener(v -> {
            showProgressBar();
            date = date.minusDays(1);
            setSchedule();
            updateDateTextView();
        });

        return binding.getRoot();
    }

    private void updateDateTextView() {
        binding.dateTextview.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    private void setSchedule() {
        DocumentReference usersReference = MainActivity.database.collection("users").document(MainActivity.getEmail(getContext()));
        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                                .addOnSuccessListener(ds -> {
                                    String scheduleJson = ds.get("scheduleJSON").toString();
                                    Schedule schedule = Schedule.fromJson(scheduleJson);
                                    String formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                                    List<Lesson> lessons = schedule.getLessonsForDate(formattedDate);
                                    if (lessons == null) {
                                        lessons = new ArrayList<>();
                                        lessons.add(new Lesson("", "", "", "", "", ""));
                                    }
                                    adapter = new LessonAdapter(lessons);
                                    binding.recyclerView.setAdapter(adapter);
                                    hideProgressBar();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                    hideProgressBar();
                                });

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });
    }

    private void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }
}