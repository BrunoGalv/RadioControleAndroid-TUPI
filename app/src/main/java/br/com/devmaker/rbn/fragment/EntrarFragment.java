package br.com.devmaker.rbn.fragment;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.ForgotPasswordActivity;
import br.com.devmaker.rbn.activity.MainActivity;
import br.com.devmaker.rbn.activity.SignUpConfirm;
import br.com.devmaker.rbn.dialog.SuaContaDialogFragment;
import br.com.devmaker.rbn.util.ActivityResultBus;
import br.com.devmaker.rbn.util.ActivityResultEvent;
import br.com.devmaker.rbn.util.AppHelper;
import br.com.devmaker.rbn.util.CacheData;
import br.com.devmaker.rbn.util.CognitoClientManager;
import br.com.devmaker.rbn.util.CognitoSyncClientManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Des. Android on 27/06/2017.
 */

public class EntrarFragment extends Fragment {
    private static String TAG = EntrarFragment.class.getSimpleName();
    private EditText inputEmail;
    private EditText inputSenha;
    private Button btnLogin;
    private TextView esqueceuSenha;
    CallbackManager callbackManager;
    JSONObject resposta, profile_pic_data, profile_pic_url;

    private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;
    private ForgotPasswordContinuation forgotPasswordContinuation;
    private NewPasswordContinuation newPasswordContinuation;
    private String username;
    private String password;
    private AlertDialog userDialog;
    private AlertDialog confirmDialog;
    private ProgressDialog waitDialog;
    private String[] primeiraLetraEmail;
    public ViewPager viewPager;
    private TextView cadastrarButtom;
    private static MobileAnalyticsManager analytics;
    private CognitoUserPool userPool;
    private Map userInfo;

    public EntrarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        callbackManager = CallbackManager.Factory.create();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_entrar, container, false);

        CognitoSyncClientManager.init(getContext());

        CognitoClientManager.init(getActivity());


        inputEmail = (EditText) rootView.findViewById(R.id.inputEmail);
        inputSenha = (EditText) rootView.findViewById(R.id.inputSenha);
        cadastrarButtom = (TextView) rootView.findViewById(R.id.textView2);
        cadastrarButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        /*inputSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (inputSenha.getText().toString().trim().length() < 6) {
                        inputSenha.setError("A senha deve conter no mínimo 6 dígitos.");
                    } else {
                        inputSenha.setError(null);
                    }
                } else {
                    if (inputSenha.getText().toString().trim().length() < 6) {
                        inputSenha.setError("A senha deve conter no mínimo 6 dígitos.");
                    } else {
                        inputSenha.setError(null);
                    }
                }
            }
        });*/

        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Btn Entrar");

                showWaitDialog("Entrando...");

                username = inputEmail.getText().toString().toLowerCase();
                if(username == null || username.length() < 1) {
                    showDialogMessage("Rádio Controle", "Por favor, informe seu email.");
                    return;
                }

                password = inputSenha.getText().toString();
                if(password == null || password.length() < 6) {
                    inputSenha.setError("A senha deve conter no mínimo 6 dígitos.");
                    return;
                }

                ClientConfiguration clientConfiguration = new ClientConfiguration();
                userPool = new CognitoUserPool(getContext(), "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                userPool.getUser(username).getSessionInBackground(authenticationHandler);

            }
        });

        esqueceuSenha = (TextView) rootView.findViewById(R.id.esqueceuSenha);
        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = inputEmail.getText().toString().toLowerCase();
                if(username == null || username.length() < 1) {
                    showDialogMessage("Rádio Controle", "Por favor, informe seu email.");
                    return;
                }else {
                    ClientConfiguration clientConfiguration = new ClientConfiguration();
                    userPool = new CognitoUserPool(getContext(), "us-east-1_uEcyGgDBj", "h4q14gu4a1le3juib4sosncb1", "1dpl7kohsao2g9nrvbm8i8rqrmvqgps9oo1f616et9u6aa3sid0d", clientConfiguration);
                    userPool.getUser(username).forgotPasswordInBackground(forgotPasswordHandler);
                }
            }
        });



        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.connectWithFbButton);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("Resultado: ", "sucesso!");
                showWaitDialog("Entrando...");
                CognitoClientManager.getCredentials().clear();
                CognitoClientManager.addLogins("graph.facebook.com", AccessToken.getCurrentAccessToken().getToken());
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.v("Resultado: ", "cancelado!");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("Resultado: ", "erro!");
            }
        });


        return rootView;
    }

    public interface OnHeadlineSelectedListener {
        public void clickTabLayout(int position);
    }

    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        this.forgotPasswordContinuation = forgotPasswordContinuation;
        Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
        intent.putExtra("destination",forgotPasswordContinuation.getParameters().getDestination());
        intent.putExtra("deliveryMed", forgotPasswordContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(intent, 3);
    }

    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            closeWaitDialog();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showDialogMessage("Rádio Controle", "Senha alterada com sucesso.");
                }
            });
            inputSenha.setText("");
            inputSenha.requestFocus();
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            closeWaitDialog();
            getForgotPasswordCode(forgotPasswordContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showDialogMessage("Rádio Controle", "Erro ao alterar senha, tente novamente mais tarde");
                }
            });
        }
    };

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.e(TAG, "Auth Success");

            CognitoUser user = userPool.getUser(username);

            user.getDetails(new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    cognitoUserDetails.getAttributes();
                    userInfo = cognitoUserDetails.getAttributes().getAttributes();
                    Log.i(TAG, "userID3 = " + cognitoUserDetails.getAttributes().getAttributes().toString());
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i(TAG, "userID3 = " + exception.toString());
                }
            });

            CognitoClientManager.getCredentials().clear();
            CognitoClientManager.addLogins("cognito-idp.us-east-1.amazonaws.com/us-east-1_uEcyGgDBj", cognitoUserSession.getIdToken().getJWTToken());
            Log.i(TAG, "userID2 = " + cognitoUserSession.getAccessToken());
            new InsertUserTask().execute();
            CacheData cacheData = new CacheData(getContext());
            cacheData.putString("userId", cognitoUserSession.getUsername());

        }


        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            //closeWaitDialog();
            //Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            //closeWaitDialog();
            mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            //closeWaitDialog();
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            Log.e(TAG, "Auth Not Success");
            System.out.println(AppHelper.formatException(e));
            if (AppHelper.formatException(e).equals("User is not confirmed. ")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Rádio Controle").setMessage("Falha ao fazer login, você precisa confirmar o cadastro antes.").setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            confirmDialog.dismiss();
                            Intent intent = new Intent(getContext(), SignUpConfirm.class);
                            intent.putExtra("source","signin");
                            intent.putExtra("name", inputEmail.getText().toString().toLowerCase());
                            startActivityForResult(intent, 10);
                        } catch (Exception e) {
                            //
                        }
                    }
                });
                confirmDialog = builder.create();
                confirmDialog.show();
                CacheData cacheData = new CacheData(getApplicationContext());
                Button btnPositive = confirmDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));

            } else if (AppHelper.formatException(e).equals("User does not exist. ")){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogMessage("Rádio Controle", "Falha ao fazer login, o usuário não existe.");
                    }
                });
            } else if (AppHelper.formatException(e).equals("Incorrect username or password. ")){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogMessage("Rádio Controle", "Usuário ou senha incorreto.");
                    }
                });
            } else if (AppHelper.formatException(e).equals("Password attempts exceeded ")){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogMessage("Rádio Controle", "Tentativas excedidas de login, tente novamente mais tarde.");
                    }
                });
            }

        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }



    private void abrirPerfil() {
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            ((MainActivity)getActivity()).abrirPerfil();
            df.dismiss();
        }
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
        CacheData cacheData = new CacheData(getApplicationContext());
        Button btnPositive = userDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));
    }

    private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        multiFactorAuthenticationContinuation = continuation;
        /*Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", multiFactorAuthenticationContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(mfaActivity, 5);*/
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if(username != null) {
            this.username = username;
        }

        if(username == null) {
            showDialogMessage("Rádio Controle","Por favor, informe sua email.");
        }else if(this.password == null) {
            password = inputSenha.getText().toString();
            if(password == null || password.length() < 6) {
                showDialogMessage("Rádio Controle","Por favor, informe sua senha de usuário.");
                return;
            }
        }else {
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username.toLowerCase(), password, null);
            continuation.setAuthenticationDetails(authenticationDetails);
            continuation.continueTask();
        }
    }

    /*@Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("fragment result", "aaaa");
    }*/

    public void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response) {
                        try {
                            resposta = json_object;
                            Log.v("Id facebook: ", resposta.get("id").toString());
                            Log.v("Email facebook: ", resposta.get("email").toString());
                            Log.v("Nome facebook: ", resposta.get("name").toString());
                            profile_pic_data = new JSONObject(resposta.get("picture").toString());
                            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                            Log.v("Url foto facebook: ", profile_pic_url.getString("url"));
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {


                                    CognitoClientManager.getCredentials().refresh();
                                    Log.i(TAG, "userID = " + CognitoClientManager.getCredentials().getIdentityId());
                                    Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                                    try {
                                        profileData.put("name", resposta.get("name").toString());
                                        profileData.put("email", resposta.get("email").toString());
                                        profileData.put("phone", "");
                                        profileData.put("picture", profile_pic_url.getString("url"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    profileData.synchronize(new SyncCallback() {

                                        @Override
                                        public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                                            Log.d(TAG, "Sync success" + dataset);
                                            System.out.println(dataset.get("name"));
                                            System.out.println(dataset.get("email"));
                                            System.out.println(dataset.get("phone"));
                                            if (userInfo!=null){
                                                CacheData cacheData = new CacheData(getContext());
                                                cacheData.putString("userId", username);
                                                cacheData.putString("userEmail", username);
                                                cacheData.putString("userNome", (String) userInfo.get("name"));
                                                cacheData.putString("userTelefone", (String) userInfo.get("phone_number"));
                                                if (dataset.get("picture") !=null) {
                                                    cacheData.putString("userUrlFoto", dataset.get("picture"));
                                                }
                                            }else {
                                                CacheData cacheData = new CacheData(getContext());
                                                cacheData.putString("userId", dataset.get("email"));
                                                cacheData.putString("userEmail", dataset.get("email"));
                                                cacheData.putString("userNome", dataset.get("name"));
                                                cacheData.putString("userTelefone", dataset.get("phone"));
                                                cacheData.putString("userUrlFoto", dataset.get("picture"));
                                            }
                                            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_dialog");
                                            closeWaitDialog();
                                            if (prev != null) {
                                                DialogFragment df = (DialogFragment) prev;
                                                SuaContaDialogFragment suaContaDialogFragment = (SuaContaDialogFragment) getParentFragment();
                                                suaContaDialogFragment.abrirTelaDesejada();
                                                //df.dismiss();
                                            }
                                        }

                                        @Override
                                        public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                                            Log.d(TAG, "Conflict");
                                            List<Record> resolved = new ArrayList<Record>();
                                            for (SyncConflict conflict : conflicts) {
                                                resolved.add(conflict.resolveWithLocalRecord());
                                            }
                                            dataset.resolve(resolved);
                                            return true; // so that synchronize() can retry

                                        }

                                        @Override
                                        public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                                            Log.d(TAG, "Dataset deleted");
                                            return false;
                                        }

                                        @Override
                                        public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                                            Log.d(TAG, "Datasets merged");
                                            return true;
                                        }

                                        @Override
                                        public void onFailure(DataStorageException dse) {
                                            Log.e(TAG, "Sync fails", dse);
                                        }
                                    });
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    super.onPostExecute(result);


                                }
                            }.execute();

                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(300).height(300)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        if(resultCode == RESULT_OK && requestCode == 3) {
            String newPass = data.getStringExtra("newPass");
            String code = data.getStringExtra("code");
            if (newPass != null && code != null) {
                if (!newPass.isEmpty() && !code.isEmpty()) {
                    showWaitDialog("Salvando nova senha...");
                    forgotPasswordContinuation.setPassword(newPass);
                    forgotPasswordContinuation.setVerificationCode(code);
                    forgotPasswordContinuation.continueTask();
                }
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        if (waitDialog==null) {
            waitDialog = new ProgressDialog(getActivity());
        }
        waitDialog.setMessage(message);
        waitDialog.show();
    }


    class InsertUserTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            CognitoUser user = userPool.getUser(username);

            user.getDetails(new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    cognitoUserDetails.getAttributes();
                    userInfo = cognitoUserDetails.getAttributes().getAttributes();
                    Log.i(TAG, "userID3 = " + cognitoUserDetails.getAttributes().getAttributes().toString());
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i(TAG, "userID3 = " + exception.toString());
                }
            });


            String userID = null;
            if (CognitoClientManager.isAuthenticated()) {

                //Log.i(TAG, "userID = " + CognitoClientManager.getCredentials().getIdentityId());



                CognitoClientManager.getCredentials().refresh();
                userID = CognitoClientManager.getCredentials().getIdentityId();
                Log.i(TAG, "userID = " + userID);

                Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");

                profileData.synchronize(new SyncCallback() {

                    @Override
                    public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                        Log.d(TAG, "Sync success" + dataset.get("email") + " " + dataset.get("name"));
                        //System.out.println(dataset.get("name"));
                        //System.out.println(dataset.get("email"));
                        //System.out.println(dataset.get("phone"));
                        CacheData cacheData = new CacheData(getContext());
                        cacheData.putString("userId", username);
                        cacheData.putString("userEmail", username);
                        cacheData.putString("userNome", (String) userInfo.get("name"));
                        cacheData.putString("userTelefone", (String) userInfo.get("phone_number"));
                        if (dataset.get("picture") !=null) {
                            cacheData.putString("userUrlFoto", dataset.get("picture"));
                        }
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_dialog");
                        closeWaitDialog();
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            SuaContaDialogFragment suaContaDialogFragment = (SuaContaDialogFragment) getParentFragment();

                            suaContaDialogFragment.abrirTelaDesejada();
                            //df.dismiss();
                        }
                        AnalyticsEvent sessionEvent = analytics.getEventClient().createEvent("Session");
                        if (!cacheData.getString("userEmail").equals("")){
                            primeiraLetraEmail = cacheData.getString("userEmail").split("");
                            sessionEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
                            sessionEvent.addAttribute("Logged", "True");
                            sessionEvent.addAttribute("Name", cacheData.getString("userNome"));
                            sessionEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
                        }else {
                            sessionEvent.addAttribute("Logged", "False");
                        }
                        analytics.getEventClient().recordEvent(sessionEvent);
                    }

                    @Override
                    public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                        //Log.d(TAG, "Conflict");
                        Log.d(TAG, "Conflict");
                        List<Record> resolved = new ArrayList<Record>();
                        for (SyncConflict conflict : conflicts) {
                            resolved.add(conflict.resolveWithLocalRecord());
                        }
                        dataset.resolve(resolved);
                        return true;
                    }

                    @Override
                    public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                        //Log.d(TAG, "Dataset deleted");
                        return true;
                    }

                    @Override
                    public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                        //Log.d(TAG, "Datasets merged");
                        return true;
                    }

                    @Override
                    public void onFailure(DataStorageException dse) {
                        //showDialogMessage("Rádio Controle", "Falha ao fazer login, contate o administrador deste aplicativo");

                    }
                });

            } else {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waitDialog!=null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }
}
