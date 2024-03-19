package we.nstu.registration.Message;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import we.nstu.registration.MainActivity;
import we.nstu.registration.User.User;
import we.nstu.registration.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements MessageAdapter.OnItemClickListener
{

    private FragmentMessageBinding binding;
    private MessageAdapter adapter;
    private List<Message> messageList;

    private FirebaseFirestore database;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        database = FirebaseFirestore.getInstance();

        String email = MainActivity.getEmail(getContext());
        messageList = new ArrayList<>();

        DocumentReference usersReference = database.collection("users").document(email);
        usersReference.get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    User user = documentSnapshot.toObject(User.class);

                    if(documentSnapshot.get("dialogs") == "")
                    {
                        Toast.makeText(getContext(), "Нет диалогов", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String[] dialogs = documentSnapshot.get("dialogs").toString().split(" ");

                        for (int i = 0; i < dialogs.length; i++)
                        {
                            database.collection("users").document(dialogs[i])
                                    .get()
                                    .addOnSuccessListener(documentSnapshot1 -> {
                                        User userToAdd = documentSnapshot1.toObject(User.class);

                                        if(!userToAdd.getEmail().equals(email))
                                        {
                                            messageList.add(new Message("ФИО", "Последнее сообщение", email, userToAdd.getEmail()));
                                            adapter = new MessageAdapter(messageList);
                                            adapter.setOnItemClickListener(this);
                                            binding.recyclerView.setAdapter(adapter);
                                        }
                                    });
                        }
                    }

                });


        binding.addMessageButton.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), StartNewDialogActivity.class));
        });

        return binding.getRoot();
    }

    @Override
    public void onItemClick(Message message) {
        Intent i = new Intent(requireActivity(), ChatActivity.class);
        i.putExtra("companionEmail", message.getCompanionEmail());
        startActivity(i);
    }
}