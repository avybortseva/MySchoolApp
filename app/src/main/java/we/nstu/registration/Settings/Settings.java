package we.nstu.registration.Settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import we.nstu.registration.databinding.ActivitySettingsBinding;

public class Settings extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String newsText = getIntent().getStringExtra("news");
    }
}