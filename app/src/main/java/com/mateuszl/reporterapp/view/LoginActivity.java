package com.mateuszl.reporterapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.mateuszl.reporterapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";

    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.sign_in)
    Button mSignIn;

    @BindView(R.id.topics_cheat)
    Button topicsCheatBtn;

    @BindView(R.id.root)
    View mRootView;

    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startUserAccountActivity(null);
            finish();
            return;
        }
    }

    @OnClick(R.id.sign_in)
    public void signIn(View view) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())
                        .setLogo(getSelectedLogo())
                        .setAvailableProviders(getSelectedProviders())
                        .setTosUrl(getTosUrl())
                        .setPrivacyPolicyUrl(getPrivacyPolicyUrl())
                        .setIsSmartLockEnabled(true, true)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startUserAccountActivity(response);
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }

    private void startUserAccountActivity(IdpResponse response) {
        startActivity(
//                UserAccountActivity.createIntent(
//                this,
//                response,
//                new UserAccountActivity.SignedInConfig(
//                        getSelectedLogo(),
//                        getSelectedTheme(),
//                        getSelectedProviders(),
//                        getTosUrl(),
//                        true,
//                        true)));
                UserAccountActivity.createIntent(
                        this,
                        response));
    }

    @MainThread
    @StyleRes
    private int getSelectedTheme() {
        return AuthUI.getDefaultTheme();
    }

    @MainThread
    @DrawableRes
    private int getSelectedLogo() {
        return AuthUI.NO_LOGO;
    }

    @MainThread
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();

        selectedProviders.add(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                        .build());

        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

        return selectedProviders;
    }

    @MainThread
    private String getTosUrl() {
        return FIREBASE_TOS_URL;
    }

    @MainThread
    private String getPrivacyPolicyUrl() {
        return FIREBASE_PRIVACY_POLICY_URL;
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.topics_cheat)
    public void onTopicsBtnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), AllTopicsActivity.class);
        startActivity(intent);
    }


//////////////////////////////////////////// TEST TODO

//        Topic topic2 = new Topic();
//        topic2.setId("12121212");
//        topic2.setTitle("title2");
//        topic2.setAuthor("auth2");
//        topic2.setDescription("desc2");
//        topic2.setTimestamp("1510146332");
//        Event event = new Event();
//        event.setId("11111");
//        event.setTopic(topic2.getId());
//        event.setContent("content111");
//        event.setTimestamp("1510146332");
//        Event event2 = new Event();
//        event2.setId("22222");
//        event2.setTopic(topic2.getId());
//        event2.setContent("content22222");
//        event2.setTimestamp("1510146332");
//        List<Event> events = new ArrayList<>();
//        events.add(event);
//        events.add(event2);
//        topic2.setEvents(events);
//
//        Topic topic3 = new Topic();
//        topic3.setId("3434343434");
//        topic3.setTitle("title2");
//        topic3.setAuthor("auth2");
//        topic3.setDescription("desc2");
//        topic3.setTimestamp("1510146332");
//        Event event3 = new Event();
//        event3.setId("33333");
//        event3.setTopic(topic3.getId());
//        event3.setContent("content111");
//        event3.setTimestamp("1510146332");
//        Event event24 = new Event();
//        event24.setId("444444");
//        event24.setTopic(topic3.getId());
//        event24.setContent("content22222");
//        event24.setTimestamp("1510146332");
//        List<Event> events2 = new ArrayList<>();
//        events2.add(event3);
//        events2.add(event24);
//        topic3.setEvents(events2);
//
//        RepositoryManager repositoryManager = RepositoryManager.getInstance();
//        Random random = new Random();
//
//        repositoryManager.getRoot().child("topics2").child(topic2.getId()).setValue(topic2);
//        repositoryManager.getRoot().child("topics2").child(topic3.getId()).setValue(topic3);

//        repositoryManager.getRoot().child("topics2").child("topic30").child("events").child("3").removeValue();

//        FirebaseDatabase.getInstance().getReference().child("topics2").child("3434343434").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Object object = dataSnapshot.getValue();
//                System.out.println(object);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    //////////////////////////////////////////// TEST

}

