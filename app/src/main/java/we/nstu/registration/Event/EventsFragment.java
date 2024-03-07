package we.nstu.registration.Event;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import we.nstu.registration.R;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment implements EventAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventList = new ArrayList<>();

        eventList.add(new Event("Мероприятие 1", "Описание мероприятия 1", "1","", "0"));
        eventList.add(new Event("Мероприятие 2", "Описание мероприятия 2", "2","", "1"));
        eventList.add(new Event("Мероприятие 3", "Описание мероприятия 3", "3","", "2"));
        eventList.add(new Event("Мероприятие 4", "Описание мероприятия 4", "4","", "3"));

        adapter = new EventAdapter(eventList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onItemClick(Event event) {
        Intent i = new Intent(requireActivity(), EventFull.class);
        i.putExtra("eventID", event.getEventID());
        startActivity(i);
    }
}