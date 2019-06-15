package oliver.bookhunter.KeywordFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Login.GoogleSignInActivity;
import oliver.bookhunter.R;

public class KeywordFragment extends Fragment {

    private Button mSubmit;
    // add keyword text
    private EditText mKeyword_text;

    private String keyword;

    //all keyword
    private List<ItemData2> itemsData ;
    //Recycle View


    //ALL DATA from database
    private List<String> AllDATA;




    // Adapter
    private oliver.bookhunter.KeywordFragment.MyAdapter2 mAdapter;

    public KeywordFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get main view
        // the fragment view
        View mDemoView = inflater.inflate(R.layout.fragment_keyword, container, false);

        mSubmit = mDemoView.findViewById(R.id.submit);
        //input text
        mKeyword_text = mDemoView.findViewById(R.id.website_new);
        //linearLayout were the keyword text is displayed


        //Arrays

        // 1. get a reference to recyclerView
        final RecyclerView recyclerView = mDemoView.findViewById(R.id.RecyclerView01);

        // this is data fro recycler view
        itemsData = new ArrayList<>();

        //get user and database instance database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //getting data from data base
        assert user != null;
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        AllDATA = (List<String>) document.get("keywords");
                        java.util.Collections.sort(AllDATA);

                        for(String data: AllDATA){
                            itemsData.add(new ItemData2(data, R.drawable.ic_delete_black_24dp));
                        }
                        // 2. set layoutManger
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        // 3. create an adapter
                        mAdapter = new MyAdapter2(itemsData,getActivity());
                        // 4. set adapter
                        recyclerView.setAdapter(mAdapter);
                        // 5. set item animator to DefaultAnimator
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        mSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                keyword = mKeyword_text.getText().toString();
                                mKeyword_text.setText("");


                                //get firebase user
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                assert user != null;
                                db.collection("users").document(user.getUid()).update("keywords", FieldValue.arrayUnion(keyword));


                                //add to the recycle viewer
                                itemsData.add(new ItemData2(keyword, R.drawable.ic_delete_black_24dp));
                                mAdapter.notifyDataSetChanged();

                                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();

                            }

                        });





                    } else {
                        Alert("ERROR","You're not logged into any account you should try to login or create a new account");
                    }
                } else {
                    Alert("You're offline","You're not connected to the internet all of your saved keywords should be saved after you connect to the internet");

                }
            }
        });







        return mDemoView;
    }
    private void Alert(String Title,String text){
        new AlertDialog.Builder(getActivity())
                .setTitle(Title)
                .setMessage(text)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.continue1, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



}
