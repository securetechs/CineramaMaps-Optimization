package main.com.cineramamaps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import main.com.cineramamaps.R;

public class GLoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glogin);

        signInButton = (SignInButton)findViewById(R.id.btn_signin);

//        GoogleSignInOptions options = new
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//                .requestProfile()
//                .requestEmail()
//                .build();
//        client = new GoogleApiClient.Builder(this).
//                enableAutoManage(this,this).
//                addApi(Auth.GOOGLE_SIGN_IN_API,options).build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(GLoginActivity.this))
                .enableAutoManage(GLoginActivity.this, 0, connectionResult -> {
                    Snackbar.make(signInButton, "Connection failed..", Snackbar.LENGTH_SHORT).show();
                    Log.e("TAG", "Google connection Error: " + connectionResult.getErrorMessage());
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //Log.e(TAG,"mGoogleApiClient is connected");
                        mGoogleApiClient.clearDefaultAccountAndReconnect();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();



       signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               signIn();
           }
       });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_signin:
//                signIn();
//                break;
//        }
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable
//    Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_CODE){
//            GoogleSignInResult result =
//                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleResult(result);
//        }
//    }
public void signIn(){
    Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(intent,REQ_CODE);
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            // G+
//            Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//            System.out.println("Display Name: " + person.getDisplayName());
//            System.out.println("Gender: " + person.getGender());
//            System.out.println("AboutMe: " + person.getAboutMe());
//            System.out.println("Birthday: " + person.getBirthday());
//            System.out.println("Current Location: " + person.getCurrentLocation());
//            System.out.println("Language: " + person.getLanguage());

        }
    }


    public void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            // Get account information

            if (acct != null) {
                String Name = acct.getDisplayName();
                String Email = "";
                if (acct.getEmail() != null) {
                    Email = acct.getEmail();
                } else {
                    Email = "";
                }

                String SocialUserId = acct.getId();
                String Gender = "";

                String idToken = acct.getIdToken();
                String profileURL = Objects.requireNonNull(acct.getPhotoUrl()).toString();

                String status = "Status: \nFullname: " + Name + "\n Email: " + Email + "\nProfile URI: " + profileURL;
                Log.i("TAG", "Google signin " + status);
                Log.i("TAG", "ID Token: " + idToken);
                Log.i("TAG", "ID: " + acct.getId());

                //TODO Temporary "acct.getCompId()" pass "idToken"

                //  checkIsUserExists();
            }

        } else {
            //  hideProgressBar();
            Log.e("TAG", "Failed!! Google Result " + result.getStatus());

            int status_code = result.getStatus().getStatusCode();
            switch (status_code) {
                case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                    Snackbar.make(signInButton, "Google sign in has been cancelled.", Snackbar.LENGTH_SHORT).show();
                    break;
                case GoogleSignInStatusCodes.NETWORK_ERROR:
                    Snackbar.make(signInButton, "Application is unable to connect with internet", Snackbar.LENGTH_SHORT).show();
                default:
                    //AppUtils.showSnackBar(LandingActivity.this, btnLogin, GoogleSignInStatusCodes.getStatusCodeString(result.getStatus().getStatusCode()), R.integer.snackbar_duration_3sec);
                    break;
            }
        }
    }
}