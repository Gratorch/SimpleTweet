package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private EditText etCompose;
    private Button btnTweet;

    public static  final int MAX_TWEET_LENGT=140;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //client=TwitterApp.getRestClient(this);
        client=TwitterApp.getRestClient(this);
        etCompose=findViewById(R.id.etCompose);
        btnTweet=findViewById(R.id.btnTweet);

        // Set click listener on Button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent =etCompose.getText().toString();
                // TODO: Error-Handling
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this,"Your Tweet is empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length()>MAX_TWEET_LENGT){
                    Toast.makeText(ComposeActivity.this,"Your Tweet is Long!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this,tweetContent, Toast.LENGTH_LONG).show();

                // Make API call to Twitter to publish the content in edit text
                client.composeTweet(tweetContent,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet=Tweet.fromJson(response);
                            Intent data=new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            // set result Code and bundle data for response
                            setResult(RESULT_OK, data);
                            // closes the activity, pass data to parent
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("TwitterClient","Succesfully posted tweet! "+response.toString());


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("TwitterClient","Failed to post tweet"+responseString.toString());
                    }
                });
            }
        });

    }

}
