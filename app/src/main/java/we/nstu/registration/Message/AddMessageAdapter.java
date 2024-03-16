package we.nstu.registration.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import we.nstu.registration.R;
import we.nstu.registration.User.User;


public class AddMessageAdapter extends RecyclerView.Adapter<AddMessageAdapter.UserViewHolder> {

    private List<UserMessage> userList;

    public AddMessageAdapter(List<UserMessage> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserMessage user = userList.get(position);
        holder.textNameUser.setText(user.nameUser);
        holder.textStatusUser.setText(user.statusUser);
        holder.textClassUser.setText(user.classUser);
        holder.photoUser.setImageResource(user.photoUser);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textNameUser;
        public TextView textStatusUser;
        public TextView textClassUser;
        public ImageView photoUser;

        public UserViewHolder(View itemView) {
            super(itemView);
            textNameUser = itemView.findViewById(R.id.textNameUser);
            textStatusUser = itemView.findViewById(R.id.textStatusUser);
            textClassUser = itemView.findViewById(R.id.textClassUser);
            photoUser = itemView.findViewById(R.id.photoUser);
        }
    }
}
