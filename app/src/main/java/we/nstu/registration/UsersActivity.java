package we.nstu.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import we.nstu.registration.User.User;
import we.nstu.registration.databinding.ActivityUsersBinding;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserRe> userList;

    private ActivityUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userList.add(new UserRe("Иван", "учащийся","11-2", R.drawable.ic_login_person));
        userList.add(new UserRe("Петр", "староста","11-2", R.drawable.ic_login_person));
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(UsersActivity.this, InvitationsCreateActivity.class));
        });
    }
}