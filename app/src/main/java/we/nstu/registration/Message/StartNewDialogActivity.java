package we.nstu.registration.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import we.nstu.registration.MainActivity;
import we.nstu.registration.News.NewsFull;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.ActivityMessageBinding;

public class StartNewDialogActivity extends AppCompatActivity implements AddMessageAdapter.OnItemClickListener {
    private ActivityMessageBinding binding;
    private AddMessageAdapter addMessageAdapter;
    private List<User> userList;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        String email = MainActivity.getEmail(getApplicationContext());
        DocumentReference usersReference = database.collection("users").document(email);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                .addOnSuccessListener(ds -> {
                                    if (ds.exists()) {
                                        String[] studentsID = ds.get("studentsID").toString().split(" ");
                                        String[] dialogs = user.getDialogs().split(" ");

                                        for (int i = 0; i < studentsID.length; i++) {
                                            {
                                                database.collection("users").document(studentsID[i])
                                                        .get()
                                                        .addOnSuccessListener(documentSnapshot1 -> {
                                                            if (documentSnapshot1.exists()) {
                                                                User userToAdd = documentSnapshot1.toObject(User.class);

                                                                if (!userToAdd.getEmail().equals(MainActivity.getEmail(getApplicationContext())) || !Arrays.asList(dialogs).contains(userToAdd.getEmail()))
                                                                {
                                                                    userList.add(userToAdd);
                                                                    addMessageAdapter = new AddMessageAdapter(userList, this);
                                                                    binding.recyclerUser.setAdapter(addMessageAdapter);
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                    }
                });
    };


    @Override
    public void onItemClick(User user) {
        String email = MainActivity.getEmail(getApplicationContext());

        database.collection("users").document(email).get()
                .addOnSuccessListener(ds -> {
                    if (ds.exists()) {
                        String dialogs = ds.get("dialogs").toString();
                        if (dialogs.isEmpty()) {
                            dialogs = user.getEmail();
                        } else {
                            dialogs = user.getEmail() + " " + dialogs;
                            String[] dialogsArray = dialogs.split(" ");
                            Set<String> dialogsSet = new HashSet<>(Arrays.asList(dialogsArray));
                            dialogs = String.join(" ", dialogsSet);
                        }

                        database.collection("users").document(email).update("dialogs", dialogs);
                    }
                });

        database.collection("users").document(user.getEmail()).get()
                .addOnSuccessListener(ds -> {
                    if (ds.exists()) {
                        String dialogs = ds.get("dialogs").toString();
                        if (dialogs.isEmpty()) {
                            dialogs = email;
                        } else {
                            dialogs = email + " " + dialogs;
                            String[] dialogsArray = dialogs.split(" ");
                            Set<String> dialogsSet = new HashSet<>(Arrays.asList(dialogsArray));
                            dialogs = String.join(" ", dialogsSet);
                        }

                        database.collection("users").document(user.getEmail()).update("dialogs", dialogs);
                    }
                });

        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
        i.putExtra("companionEmail", user.getEmail());
        startActivity(i);
    }
}