package we.nstu.registration.Profile;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import we.nstu.registration.Login.Login;
import we.nstu.registration.MainActivity;
import we.nstu.registration.Settings.Settings; // Убедитесь, что класс Settings существует и импортирован
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentProfileBinding;
import com.google.firebase.firestore.DocumentReference;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String email = MainActivity.getEmail(getContext());

        DocumentReference usersReference = MainActivity.database.collection("users").document(email);

        usersReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        binding.phoneNumber.setText(user.getPhoneNumber());
                        binding.fullName.setText(user.getFirstName() + " " + user.getSecondName() + " " + user.getSurname());
                        binding.email.setText(email);

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {
                                   binding.schoolName.setText(ds.get("schoolName").toString());
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                });

                        MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                                .addOnSuccessListener(ds -> {
                                    binding.classNumber.setText(ds.get("classroomName").toString());
                                    binding.teatcherFullName.setText(ds.get("teacher").toString());
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Ошибка при загрузке данных школы", Toast.LENGTH_SHORT).show();
                                });

                    }
                }).addOnFailureListener(e->{
                    Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
                });


        binding.logoutButton.setOnClickListener(v -> {
            MainActivity.clearEmail(getContext());
            Intent i = new Intent(requireActivity(), Login.class);
            startActivity(i);
        });

        binding.settingsButton.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), Settings.class);
            startActivity(i);
        });

        return view;
    }
}