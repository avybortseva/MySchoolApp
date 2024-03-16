package we.nstu.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import we.nstu.registration.Registration.Registration;
import we.nstu.registration.databinding.ActivityUsersBinding;

public class UsersActivity extends AppCompatActivity {

     private ActivityUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());

        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), InvitationsCreateActivity.class));
        });

    }
}