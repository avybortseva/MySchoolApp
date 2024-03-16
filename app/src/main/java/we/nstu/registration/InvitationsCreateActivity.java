package we.nstu.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import we.nstu.registration.News.AddNewsDialog;
import we.nstu.registration.databinding.ActivityInvitationsCreateBinding;

public class InvitationsCreateActivity extends AppCompatActivity {

    private ActivityInvitationsCreateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations_create);
        binding = ActivityInvitationsCreateBinding.inflate(getLayoutInflater());

        binding.createInvitationButton.setOnClickListener(v -> {
            CodeDialog dialogFragment = new CodeDialog();
            dialogFragment.show(getSupportFragmentManager(), "my_dialog");
        });


    }
}