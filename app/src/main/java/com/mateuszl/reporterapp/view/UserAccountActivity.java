package com.mateuszl.reporterapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mateuszl.reporterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Ekran główny aplikacji po zalogowaniu użytkownika. Częściowo pochodzi z dokumentacji Firebase.
 */
public class UserAccountActivity extends AppCompatActivity {

    private static final String EXTRA_IDP_RESPONSE = "extra_idp_response";

    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;

    @BindView(R.id.user_email)
    TextView mUserEmail;

    @BindView(R.id.user_display_name)
    TextView mUserDisplayName;

    public static Intent createIntent(
            Context context,
            IdpResponse idpResponse) {

        Intent startIntent = new Intent();
        if (idpResponse != null) {
            startIntent.putExtra(EXTRA_IDP_RESPONSE, idpResponse);
        }

        return startIntent.setClass(context, UserAccountActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_user_account);
        ButterKnife.bind(this);
        populateProfile();
    }

    @OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginActivity.createIntent(UserAccountActivity.this));
                            finish();
                        } else {
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    @MainThread
    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "Brak adresu email użytkownika" : user.getEmail());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "Brak danych użytkownika" : user.getDisplayName());
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.topics_all_btn)
    public void onAllTopicsBtnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), AllTopicsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.topics_user_btn)
    public void onUserTopicsBtnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
        startActivity(intent);
    }
}