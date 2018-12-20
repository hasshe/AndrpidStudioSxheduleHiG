package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.barankazan.kronoxapp.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Button btn = v.findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity(),
                        "Unavailable", Toast.LENGTH_SHORT);
                toast.show();
                loginFragment f= new loginFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, f, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
            });
        return v;
    }

}
