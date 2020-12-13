package oliver.bookhunter.Website;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import oliver.bookhunter.R;

public class WebsiteFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_website, container, false);
    }



}
