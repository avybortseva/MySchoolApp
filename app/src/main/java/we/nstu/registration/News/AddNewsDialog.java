package we.nstu.registration.News;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.checkerframework.checker.units.qual.A;

import java.util.List;

import we.nstu.registration.R;
import we.nstu.registration.Registration.Registration;

public class AddNewsDialog extends DialogFragment {
    private List<News> newsList;
    private NewsAdapter newsAdapter;

    public AddNewsDialog(List<News> newsList, NewsAdapter newsAdapter) {
        this.newsList = newsList;
        this.newsAdapter = newsAdapter;
    }

    private EditText newsTitleEditText;
    private EditText newsDescriptionEditText;
    private ImageButton addNewsButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_news, container, false);

        newsTitleEditText = view.findViewById(R.id.newsTitleEditText);
        newsDescriptionEditText = view.findViewById(R.id.newsDescriptionEditText);
        addNewsButton = view.findViewById(R.id.addNewsButton);

        addNewsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Получить размер экрана
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        // Установить ширину и высоту окна
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // Применить новые параметры
        getDialog().getWindow().setAttributes(params);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        return dialog;
    }
}