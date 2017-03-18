package com.patrickzhong.aplux;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class ReportActivity extends AppCompatActivity {

    // UI references.
    private EditText idView;
    private EditText descView;
    private View progressView;
    private View loginFormView;
    public static ReportActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // Set up the login form.
        idView = (EditText) findViewById(R.id.id);

        descView = (EditText) findViewById(R.id.description);
        Button reportButton = (Button) findViewById(R.id.report_button);
        reportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                report();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        instance = this;
    }

    private void report(){

        idView.setError(null);
        descView.setError(null);

        String id = idView.getText().toString();
        String desc = descView.getText().toString();

        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(desc)) {
            showProgress(true);
            //Util.go(this, id, desc);
            Util.preparePush(id, desc);
            Util.reportRequesting = true;
            Util.requestLoc(this);
            System.out.println("");
            System.out.println("New Report: ");
            System.out.println("  ID: "+id);
            System.out.println("  Description: "+desc);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            //Util.go(this, requestCode);
            Util.requestLoc(this);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}