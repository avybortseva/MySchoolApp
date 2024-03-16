package we.nstu.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import we.nstu.registration.databinding.ActivityGenerateCodeBinding;

public class GenerateCodeActivity extends AppCompatActivity {

    private ActivityGenerateCodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String code = getIntent().getStringExtra("inviteCode");

        binding.code.setText(code);

        binding.copyButton.setOnClickListener(v -> {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.code.getText().toString());
            clipboard.setPrimaryClip(clip);

        });

        binding.shareCodeButton.setOnClickListener(v -> {
            String textToShare = "Код для регистрации: " + binding.code.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

    }
}