package we.nstu.registration.Event;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import we.nstu.registration.databinding.EventActivityBinding;

public class EventFull extends AppCompatActivity {
    private EventActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EventActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
