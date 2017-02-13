package mycompany.com.doctorapp.utils;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import mycompany.com.doctorapp.common_activities.LoginActivity;

/**
 * Created by Dell on 10/1/2016.
 */
public class GetNameInForeground extends AbstractGetNameTask {


    public GetNameInForeground(LoginActivity activity, String email, String mScope) {
        super(activity, email,mScope);
    }

    /**
     37
     * Get a authentication token if one is not available. If the error is not recoverable then
     38
     * it displays the error message on parent activity right away.
     39
     */
    @Override
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (GooglePlayServicesAvailabilityException playEx) {
            // GooglePlayServices.apk is either old, disabled, or not present.
        } catch (UserRecoverableAuthException userRecoverableException) {
            // Unable to authenticate, but the user can fix this.
            // Forward the user to the appropriate activity.
            mActivity.startActivityForResult(userRecoverableException.getIntent(), mRequestCode);
        } catch (GoogleAuthException fatalException) {
            onError("Unrecoverable error " + fatalException.getMessage(), fatalException);
        }
        return null;
    }
}

