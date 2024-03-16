package we.nstu.registration.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import we.nstu.registration.R;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AddMessageAdapter addMessageAdapter;
    private List<UserMessage> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.recyclerUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализируем список пользователей
        userList = new ArrayList<>();
        // Заполняем список пользователей данными
        userList.add(new UserMessage("Иван", "Иванов", "учащийся", R.drawable.ic_profile_class));
        userList.add(new UserMessage("Петр", "Петров","учащийся", R.drawable.ic_profile_class));
        // ... добавьте других пользователей

        // Создаем и устанавливаем адаптер
        addMessageAdapter = new AddMessageAdapter(userList);
        recyclerView.setAdapter(addMessageAdapter);
    }
}