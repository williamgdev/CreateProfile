package com.mac.fireflies.wgt.createprofile.core.interactor;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Created by willimail on 2/22/18.
 */

public class GoogleInteractor {
    private static final GoogleInteractor ourInstance = new GoogleInteractor();

    public static GoogleInteractor getInstance() {
        return ourInstance;
    }

    private GoogleInteractor() {
    }

    public void getGoogleAccount(Task<GoogleSignInAccount> task, GoogleInteractorListener listener) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            listener.onResult(account);
        } catch (ApiException e) {
            listener.onError(e.getMessage());
            e.printStackTrace();
        }
    }

    public interface GoogleInteractorListener{
        void onResult(GoogleSignInAccount account);

        void onError(String error);
    }
}
