package oliver.bookhunter.KeywordFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.R;

public class KeywordFragment extends Fragment {
    // the fragment view
    private View mDemoView;
    // the Website text
    private TextView mDemoTextView;

    private Button mSubmit;
    // add keyword text
    private EditText mKeyword_text;

    private String keyword;
    // file where websites are saved
    private final String file_name = "bookhunter_file2";
    // the linear layout where the websites are shown
    private LinearLayout linearLayout;


    //all keyword
    private List<ItemData2> itemsData ;
    //Recycle View





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        //get main view
        mDemoView = inflater.inflate(R.layout.fragment_keyword, container, false);

        mSubmit = (Button) mDemoView.findViewById(R.id.submit);
        //input text
        mKeyword_text = (EditText) mDemoView.findViewById(R.id.website_new);
        //linearLayout were the keyword text is displayed


        //Arrays

        // 1. get a reference to recyclerView
        final RecyclerView recyclerView = (RecyclerView) mDemoView.findViewById(R.id.RecyclerView01);

        // this is data fro recycler view
        itemsData = new ArrayList<ItemData2>();

        try {
            // GETTING stings out of file


            String Message;
            final FileInputStream fileinput = getContext().openFileInput(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message=bufferedReader.readLine())!=null)){
                stringBuffer.append(Message+"\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;
            int index = 0;
            while( (line=bufReader.readLine()) != null ) {
                itemsData.add(new ItemData2(line, R.drawable.ic_delete_black_24dp));

                index++;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. create an adapter
        final oliver.bookhunter.KeywordFragment.MyAdapter2 mAdapter = new MyAdapter2(itemsData,getActivity());
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());






        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = mKeyword_text.getText().toString();










                itemsData.add(new ItemData2(keyword, R.drawable.ic_delete_black_24dp));
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();

                try {
                    FileOutputStream fileoutput = getContext().openFileOutput(file_name, Context.MODE_APPEND);
                    keyword +='\n';
                    fileoutput.write(keyword.getBytes());
                    fileoutput.close();


                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });

        return mDemoView;
    }


}
