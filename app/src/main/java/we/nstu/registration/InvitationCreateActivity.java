package we.nstu.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import we.nstu.registration.News.NewsFull;
import we.nstu.registration.Registration.Registration;
import we.nstu.registration.User.Invite;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.ActivityInvitationCreateBinding;
import we.nstu.registration.databinding.NewsActivityBinding;

public class InvitationCreateActivity extends AppCompatActivity {

    private ActivityInvitationCreateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvitationCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createInvitationButton.setOnClickListener(v -> {

            if(binding.numOfUses.getText().toString() == "" || binding.classroomName.getText().toString() == "" || (!binding.student.isChecked() && !binding.headman.isChecked() && !binding.teacher.isChecked() && !binding.admin.isChecked()))
            {
                Toast.makeText(getApplicationContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            int accessLevel;

            if(binding.headman.isChecked())
            {
                accessLevel = 1;
            }
            else if (binding.teacher.isChecked())
            {
                accessLevel = 2;
            }
            else if (binding.admin.isChecked()) {
                accessLevel = 3;
            }
            else
            {
                accessLevel = 0;
            }

            String email = MainActivity.getEmail(getApplicationContext());

            DocumentReference usersReference = MainActivity.database.collection("users").document(email);
            usersReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);

                        MainActivity.database
                                .collection("schools")
                                .document(String.valueOf(user.getSchoolID()))
                                .collection("classrooms")
                                .get()
                                .addOnSuccessListener(ds2 -> {
                                    Invite invite = new Invite(user.getSchoolID(), ds2.size(), Integer.parseInt(binding.numOfUses.getText().toString()), accessLevel);

                                    DocumentReference reference3 = MainActivity.database.collection("schools")
                                            .document(String.valueOf(user.getSchoolID()))
                                            .collection("classrooms")
                                            .document(String.valueOf(ds2.size()));

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("scheduleJSON", "");
                                    data.put("eventsJson", "");
                                    data.put("classroomName", binding.classroomName.getText().toString());
                                    data.put("teacher", binding.teatcherFullName.getText().toString());

                                    reference3.set(data);

                                    String code = generateRandomCode();

                                    DocumentReference reference = MainActivity.database.collection("invitations").document(code);
                                    reference.get()
                                            .addOnSuccessListener(ds -> {
                                                if(!ds.exists())
                                                {
                                                    reference.set(invite);

                                                    Intent i = new Intent(getApplicationContext(), GenerateCodeActivity.class);
                                                    i.putExtra("inviteCode", code);
                                                    startActivity(i);
                                                }
                                            });

                                });
                    });

        });

    }

    public String generateRandomCode()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(90000000) + 10000000;
        return String.valueOf(randomNumber);
    }

}