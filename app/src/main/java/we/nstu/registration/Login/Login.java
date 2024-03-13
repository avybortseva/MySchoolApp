package we.nstu.registration.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import we.nstu.registration.MainActivity;
import we.nstu.registration.Registration.Registration;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentLoginBinding;
import com.google.firebase.firestore.DocumentReference;

public class Login extends AppCompatActivity {
    private FragmentLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String email = MainActivity.getEmail(getApplicationContext());
        if (email != null && !email.isEmpty()) {
            startActivity(new Intent(Login.this, MainActivity.class));
        }

        binding.loginButton.setOnClickListener(v->{
            if(binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty())
            {
                Toast.makeText(Login.this, "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();

                DocumentReference reference = MainActivity.database.collection("users").document(username);

                reference.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                User user = documentSnapshot.toObject(User.class);
                                if (user.getPassword().equals(password)) {
                                    Toast.makeText(Login.this, "Успешно авторизованы", Toast.LENGTH_SHORT).show();
                                    MainActivity.saveEmail(getApplicationContext(),username);
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e->{
                            Toast.makeText(Login.this, "Ошибка при авторизации", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        binding.signupButton.setOnClickListener(v ->  {
            Intent i = new Intent(Login.this, Registration.class);
            startActivity(i);
        });
    }
}