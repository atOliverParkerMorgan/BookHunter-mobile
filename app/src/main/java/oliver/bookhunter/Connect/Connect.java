package oliver.bookhunter.Connect;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import oliver.bookhunter.R;

public class Connect extends AsyncTask<String,String,JSONObject>{

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ConnectAction login;
    private String action;
    private String actionEnd;

    // only retain a weak reference to the activity
    public Connect(Context context, ConnectAction login, String action, String actionEnd) {
        this.context = context;
        this.login = login;
        this.action = action;
        this.actionEnd = actionEnd;
    }

    @Override
    protected JSONObject doInBackground(String... input) {
        return oliver.bookhunter.Connect.Connect.connect(input[0], input[1], "https://www.morganbooks.eu/api/"+action+"/", actionEnd);

    }



    @Override
    public void onPostExecute(JSONObject result){
        Log.d("RESULT", String.valueOf(result));
        login.action(result,context);
    };



    public static JSONObject connect(String user, String password, String urlBegin, String urlEnd) {
        Log.d("USER",user);
        Log.d("passwrod",password);
        String userEncoded = encode(user,2);
        String passwordEncoded = encode(password,2);
        String url;
        if(urlEnd.length()!=0) url = urlBegin+userEncoded.replace('/',(char) 0)+"/"+passwordEncoded.replace('/',(char) 0)+'/'+urlEnd.replace('/','~');
        else url = urlBegin+userEncoded.replace('/',(char) 0)+"/"+passwordEncoded.replace('/',(char) 0);
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.toString().equals("[]")){
                Log.d("ERROR","EEJFS");
                return null;
            }


            if((response.toString().charAt(0)=='[' && urlEnd.length()==0 )|| urlBegin.contains("Find")){
                int index = 0;
                Log.d("RESPP", String.valueOf(response));
                while (response.toString().contains("website")){
                    response.replace(response.indexOf("website"),response.indexOf("website")+7, "Website"+index);
                    index++;
                }
                index = 0;
                while (response.toString().contains("keyword")){
                    response.replace(response.indexOf("keyword"),response.indexOf("keyword")+7, "Keyword"+index);
                    index++;
                }
                index = 0;
                while (response.toString().contains("date")){
                    response.replace(response.indexOf("date"),response.indexOf("date")+4, "Date"+index);
                    index++;
                }
                response.substring(2,response.toString().length()-2);
                String r = response.toString().replaceAll(Pattern.quote("{"),
                        "").replaceAll(Pattern.quote("}"),"");


                return new JSONObject("{"+r.substring(1,r.length()-1)+"}");
            }
            return new JSONObject(response.toString());



        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void Alert(String Title, String text, Context context){
        new AlertDialog.Builder(context)
                .setTitle(Title)
                .setMessage(text)

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.continue1, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Function to move string character
    public static String encode(String s, int k) {

        // changed string
        StringBuilder newS = new StringBuilder();

        // iterate for every characters
        for (int i = 0; i < s.length(); ++i) {
            // ASCII value
            int val = s.charAt(i);
            // store the duplicate
            int dup = k;

            // if k-th ahead character exceed 'z'
            if (val + k > 122) {
                k -= (122 - val);
                k = k % 26;

                newS.append((char) (96 + k));
            } else {
                newS.append((char) (val + k));
            }

            k = dup;
        }

        // print the new string
        return newS.toString();
    }
}
