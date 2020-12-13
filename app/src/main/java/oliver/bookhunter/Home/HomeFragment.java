package oliver.bookhunter.Home;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import oliver.bookhunter.R;


public class HomeFragment extends Fragment {


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
