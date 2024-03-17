package we.nstu.registration.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import we.nstu.registration.MainActivity;
import we.nstu.registration.News.NewsFull;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.ActivityMessageBinding;

public class StartNewDialogActivity extends AppCompatActivity implements AddMessageAdapter.OnItemClickListener {

    private ActivityMessageBinding binding;
    private AddMessageAdapter addMessageAdapter;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        String email = MainActivity.getEmail(getApplicationContext());
        DocumentReference usersReference = MainActivity.database.collection("users").document(email);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    User user = documentSnapshot.toObject(User.class);

                    MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                            .addOnSuccessListener(ds -> {
                                String[] studentsID = ds.get("studentsID").toString().split(" ");

                                for (int i = 0; i < studentsID.length; i++)
                                {
                                    MainActivity.database.collection("users").document(studentsID[i])
                                            .get()
                                            .addOnSuccessListener(documentSnapshot1 -> {
                                                User userToAdd = documentSnapshot1.toObject(User.class);

                                                if(!userToAdd.getEmail().equals(MainActivity.getEmail(getApplicationContext())))
                                                {
                                                    userList.add(userToAdd);
                                                    addMessageAdapter = new AddMessageAdapter(userList, this);
                                                    binding.recyclerUser.setAdapter(addMessageAdapter);
                                                }
                                            });
                                }



                            });

                });
    }

    @Override
    public void onItemClick(User user) {
        String email = MainActivity.getEmail(getApplicationContext());

        MainActivity.database.collection("users").document(email).get()
                .addOnSuccessListener(ds -> {
                    String dialogs = ds.get("dialogs").toString();
                    if (dialogs == "")
                    {
                        dialogs = user.getEmail();
                    }
                    else
                    {
                        dialogs = dialogs + " " + user.getEmail();
                    }
                    MainActivity.database.collection("users").document(email).update("dialogs", dialogs);
                });

        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
        i.putExtra("companionEmail", user.getEmail());
        startActivity(i);

    }
}