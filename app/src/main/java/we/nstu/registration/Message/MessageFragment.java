package we.nstu.registration.Message;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import we.nstu.registration.MainActivity;
import we.nstu.registration.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements MessageAdapter.OnItemClickListener
{

    private FragmentMessageBinding binding;
    private MessageAdapter adapter;
    private List<Message> messageList;

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

        String email = MainActivity.getEmail(getContext());



        String companionEmail = "k";

        messageList = new ArrayList<>();
        messageList.add(new Message("Заголовок 1", "Текст сообщения 1", email, companionEmail));
        messageList.add(new Message("Заголовок 2", "Текст сообщения 2", email, companionEmail));
        // Добавьте другие сообщения в список, если нужно

        adapter = new MessageAdapter(messageList);
        adapter.setOnItemClickListener(this);
        binding.recyclerView.setAdapter(adapter);

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