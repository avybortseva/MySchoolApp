package we.nstu.registration.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import we.nstu.registration.Login.Login;
import we.nstu.registration.MainActivity;
import we.nstu.registration.User.Invite;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentRegistrationBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity
{
    private FragmentRegistrationBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = FragmentRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.registrationButton.setOnClickListener(v -> {
            User user = new User(
                    binding.userFirstName.getText().toString(),
                    binding.userSecondName.getText().toString(),
                    binding.userThirdName.getText().toString(),
                    binding.userEmail.getText().toString().toLowerCase(),
                    binding.userPassword.getText().toString()
            );

            if (user.checkFields() && !binding.userCode.getText().toString().isEmpty())
            {
                if (user.checkPassword()) {

                    DocumentReference inviteReference = db.collection("invitations").document(binding.userCode.getText().toString());
                    inviteReference.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists())
                                {
                                    Invite invite = documentSnapshot.toObject(Invite.class);

                                    int numOfUses = invite.getNumOfUses() - 1;

                                    if (numOfUses >= 0)
                                    {
                                        user.setSchoolID(invite.getSchoolID());
                                        user.setClassroomID(invite.getClassroomID());
                                        user.setAccessLevel(invite.getAccessLevel());

                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("numOfUses", numOfUses);
                                        inviteReference.update(updates)
                                                .addOnSuccessListener(a->{
                                                    DocumentReference reference = db.collection("users").document(user.getEmail());

                                                    reference.get()
                                                                    .addOnSuccessListener(documentSnapshot1 -> {
                                                                       if(!documentSnapshot1.exists())
                                                                       {
                                                                           //Прикрипление учащегося к школе
                                                                           MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).get()
                                                                                   .addOnSuccessListener(ds -> {
                                                                                       String studentsID = ds.get("studentsID").toString();
                                                                                       if (studentsID == "")
                                                                                       {
                                                                                           studentsID = user.getEmail();
                                                                                       }
                                                                                       else
                                                                                       {
                                                                                           studentsID = studentsID + " " + user.getEmail();
                                                                                       }
                                                                                       MainActivity.database.collection("schools").document(String.valueOf(user.getSchoolID())).update("studentsID", studentsID);
                                                                                   });

                                                                           //Регестрация юзера
                                                                           reference.set(user)
                                                                                   .addOnSuccessListener(aVoid -> {
                                                                                       Toast.makeText(Registration.this, "Вы успешно зарегестрированны", Toast.LENGTH_SHORT).show();
                                                                                       MainActivity.saveEmail(getApplicationContext(), user.getEmail());
                                                                                       startActivity(new Intent(Registration.this, MainActivity.class));
                                                                                       finish();
                                                                                   })
                                                                                   .addOnFailureListener(e -> {
                                                                                       Toast.makeText(Registration.this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
                                                                                   });
                                                                       }
                                                                       else
                                                                       {
                                                                           Toast.makeText(Registration.this, "Пользователь с такой почтой уже зарегестрирован", Toast.LENGTH_SHORT).show();
                                                                       }
                                                                    });
                                                });
                                    }
                                    else
                                    {
                                        Toast.makeText(Registration.this, "Количество использований кода приглашения превышено", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(Registration.this, "Неверный код приглашения", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Registration.this, "Ошибка при выполнении запроса", Toast.LENGTH_SHORT).show();
                            });

                }
                else{
                    Toast.makeText(Registration.this, "Необходимо более 7 символов с строчными, заглавными буквами и цифрами", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(Registration.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });
    }
}