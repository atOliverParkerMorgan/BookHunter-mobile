package oliver.bookhunter.KeywordFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import oliver.bookhunter.R;

public class KeywordFragment extends Fragment {
    private View mDemoView;
    private TextView mDemoTextView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDemoView = inflater.inflate(R.layout.fragment_keyword, container, false);


        return mDemoView;
    }
}
