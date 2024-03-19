package we.nstu.registration.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import we.nstu.registration.MainActivity;
import we.nstu.registration.databinding.ActivityUsersBinding;

public class UsersActivity extends AppCompatActivity {
    private UserAdapter userAdapter;
    private List<User> userList;
    private String email;
    private ActivityUsersBinding binding;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        email = MainActivity.getEmail(getApplicationContext());
        DocumentReference usersReference = database.collection("users").document(email);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    User user = documentSnapshot.toObject(User.class);

                    database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                            .addOnSuccessListener(ds -> {
                                String[] studentsID = ds.get("studentsID").toString().split(" ");

                                for (int i = 0; i < studentsID.length; i++)
                                {
                                    database.collection("users").document(studentsID[i])
                                            .get()
                                            .addOnSuccessListener(documentSnapshot1 -> {
                                                User userToAdd = documentSnapshot1.toObject(User.class);

                                                if(!userToAdd.getEmail().equals(MainActivity.getEmail(getApplicationContext())))
                                                {
                                                    userList.add(userToAdd);
                                                    userAdapter = new UserAdapter(userList);
                                                    binding.recyclerUser.setAdapter(userAdapter);
                                                }
                                            });
                                }
                            });
                });



        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(UsersActivity.this, InvitationCreateActivity.class));
        });
    }
}