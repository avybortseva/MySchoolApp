package we.nstu.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;

import java.util.ArrayList;
import java.util.List;

import we.nstu.registration.User.User;
import we.nstu.registration.databinding.ActivityUsersBinding;

public class UsersActivity extends AppCompatActivity {
    private UserAdapter userAdapter;
    private List<UserRe> userList;
    private String email;
    private ActivityUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        email = MainActivity.getEmail(getApplicationContext());
        DocumentReference usersReference = MainActivity.database.collection("users").document(email);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    User user = documentSnapshot.toObject(User.class);

                    MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).collection("classrooms").document(String.valueOf(user.getClassroomID())).get()
                            .addOnSuccessListener(ds -> {
                                String[] studentsID = ds.get("studentsID").toString().split(" ");

                                for (int i = 0; i < studentsID.length; i++)
                                {
                                    MainActivity.database.collection("users").document(studentsID[i])
                                            .get()
                                            .addOnSuccessListener(documentSnapshot1 -> {
                                                User userToAdd = documentSnapshot1.toObject(User.class);

                                                userList.add(new UserRe(userToAdd.getFirstName() + " " + userToAdd.getSecondName() + " " + userToAdd.getSurname(), userToAdd.accessLevelToText(), ds.get("classroomName").toString(), R.drawable.ic_login_person));

                                                userAdapter = new UserAdapter(userList);
                                                binding.recyclerUser.setAdapter(userAdapter);
                                            });
                                }



                            });

                });



        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(UsersActivity.this, InvitationsCreateActivity.class));
        });
    }
}