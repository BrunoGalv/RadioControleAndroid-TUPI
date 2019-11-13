package br.com.devmaker.rbn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.devmaker.rbn.MyApplication;
import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.adapter.PremiosAdapter;
import br.com.devmaker.rbn.dialog.SuaContaDialogFragment;
import br.com.devmaker.rbn.model.Premio;
import br.com.devmaker.rbn.model.Promocao;
import br.com.devmaker.rbn.util.CacheData;
import br.com.devmaker.rbn.util.CognitoSyncClientManager;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import com.amazonaws.mobileconnectors.cognito.Dataset.SyncCallback;

/**
 * Created by Des. Android on 04/08/2017.
 */

public class PromocaoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private Toolbar toolbar;
    private List<Promocao> promocao;
    private List<Premio> dataPremio;
    private int itemPosicao;

    private ImageView imagemPromocao;
    private ImageButton btnCompartilhar;
    private ImageButton btnVideoPromocao;
    private TextView tituloPromocao;
    private TextView dataEncerramentoPromocao;
    private TextView dataSorteioPromocao;
    private TextView participando;
    private Button btnRegulamento;
    private View spacerButton;
    private Button btnParticipar;
    private TextView txtPremios;
    private RecyclerView recyclerView;
    private PremiosAdapter adapter;

    private Context mContext;
    private Activity mActivity;

    private static final int RECOVERY_REQUEST = 1;
    private View youtube_fragment;

    private Boolean participandoCheck = false;
    private Boolean forCheck = false;
    private ArrayList<String> promocoesParticipantes = new ArrayList<String>();

    private CognitoCachingCredentialsProvider  credentialsProvider;
    private Table dbTable;
    private AmazonDynamoDBClient dbClient;
    private CacheData cacheData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_promocao);

        mContext = getApplicationContext();
        mActivity = PromocaoActivity.this;

        Bundle args = getIntent().getExtras();
        promocao = (List<Promocao>) args.getSerializable("lista");
        itemPosicao = args.getInt("posicao");
        cacheData = new CacheData(mContext);

        // Create a new credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), "us-east-1:9434eddb-ce1a-4204-bb3b-4a7f88b97b17", Regions.US_EAST_1);

        // Create a connection to DynamoDB
        dbClient = new AmazonDynamoDBClient(credentialsProvider);



        MyApplication application = (MyApplication) getApplication();
        /*
        Tracker mTracker = application.getDefaultTracker();
        Log.i("promo", "Setting screen name: " + "Tela PROMO");
        mTracker.setScreenName("Image~" + "Tela PROMO");
        mTracker.send(new HitBuilders .ScreenViewBuilder().build());
        */

        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        }

        toolbar = (Toolbar) findViewById(R.id.editarPerfilToolbar);
        toolbar.setTitle("Promoção");
        toolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        youtube_fragment = findViewById(R.id.youtube_fragment);
        imagemPromocao = (ImageView) findViewById(R.id.imagemPromocao);
        btnCompartilhar = (ImageButton) findViewById(R.id.btnCompartilhar);
        btnVideoPromocao = (ImageButton) findViewById(R.id.btnVideoPromocao);
        tituloPromocao = (TextView) findViewById(R.id.tituloPromocao);
        dataEncerramentoPromocao = (TextView) findViewById(R.id.dataEncerramentoPromocao);
        dataSorteioPromocao = (TextView) findViewById(R.id.dataSorteioPromocao);
        participando = (TextView) findViewById(R.id.participando);
        btnRegulamento = (Button) findViewById(R.id.btnRegulamento);
        btnParticipar = (Button) findViewById(R.id.btnParticipar);
        spacerButton = findViewById(R.id.spacerButton);
        txtPremios = (TextView) findViewById(R.id.txtPremios);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        promocoesParticipantes = cacheData.getListString("promocoesParticipantes");
        if (!cacheData.getString("userId").equals("")) {
            if (promocoesParticipantes.size()>0){
                for (String promocaoParticipante : promocoesParticipantes){
                    if (promocaoParticipante.equals(promocao.get(itemPosicao).getTimestamp()+"")){
                        forCheck = true;
                        break;
                    }
                }

                if (forCheck){
                    participando.setVisibility(View.VISIBLE);
                    btnParticipar.setVisibility(View.GONE);
                }else {
                    btnParticipar.setVisibility(View.VISIBLE);
                }
            }else {
                btnParticipar.setVisibility(View.VISIBLE);
            }
        }else {
            btnParticipar.setVisibility(View.VISIBLE);
        }

        Picasso.with(mContext)
                .load(promocao.get(itemPosicao).getImagemPromocao())
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(imagemPromocao);


        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Compartilhar.");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, "No dia " + promocao.get(itemPosicao).getDataEncerramentoPromocao() + " encerrará a promoção " + promocao.get(itemPosicao).getTituloPromocao() + ", venha participar comigo, baixe já o aplicativo Rádio controle:\nVersão Android: " + cacheData.getString("linkAndroid") +"\nVersão iOS: " + cacheData.getString("linkIos"));
                sendIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(sendIntent,"Compartilhar no:" ));
            }
        });

        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize("AIzaSyDPfAz7R_J7el29-2_HN6zmLGjAay1chX4", PromocaoActivity.this);
        btnVideoPromocao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão ver vídeo.");
                if (btnVideoPromocao.getTag().equals("imagem")){
                    btnVideoPromocao.setTag("video");
                    btnVideoPromocao.setImageResource(R.drawable.ic_file_image_gray);
                    youtube_fragment.setVisibility(View.VISIBLE);
                    imagemPromocao.setVisibility(View.GONE);
                }else {
                    btnVideoPromocao.setTag("imagem");
                    btnVideoPromocao.setImageResource(R.drawable.ic_video_gray);
                    youtube_fragment.setVisibility(View.GONE);
                    imagemPromocao.setVisibility(View.VISIBLE);
                }

            }
        });
        tituloPromocao.setText(promocao.get(itemPosicao).getTituloPromocao());
        dataEncerramentoPromocao.setText(promocao.get(itemPosicao).getDataEncerramentoPromocao());
        dataSorteioPromocao.setText(promocao.get(itemPosicao).getDataSorteioPromocao());
        if(promocao.get(itemPosicao).getPremio().size() != 0){
            txtPremios.setText(promocao.get(itemPosicao).getPremio().get(0).getTituloPremio());
        }
        /*if (promocao.get(itemPosicao).getParticipando()){
            participando.setVisibility(View.VISIBLE);
            btnParticipar.setVisibility(View.GONE);
        }else {
            participando.setVisibility(View.GONE);
            btnParticipar.setVisibility(View.VISIBLE);
        }*/
        if (!promocao.get(itemPosicao).getVigente()){
            btnParticipar.setVisibility(View.GONE);
            spacerButton.setVisibility(View.GONE);
        }
        Log.e("participando", " " + promocao.get(itemPosicao).getParticipando());
        Log.e("vigente", " " + promocao.get(itemPosicao).getVigente());
        btnRegulamento.setTextColor(Color.parseColor(cacheData.getString("color")));
        btnRegulamento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Regulamento.");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(promocao.get(itemPosicao).getLinkRegulamentoPromocao()));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        btnParticipar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Participar.");

                if (cacheData.getString("userId").equals("")){
                    System.out.println("Usuário deslogado!!!");
                    AlertDialog.Builder customBuilder  = new AlertDialog.Builder(mActivity);
                    customBuilder.setTitle("Rádio Controle");
                    customBuilder.setMessage("Você precisa estar logado para poder participar da promoção.");
                    customBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SuaContaDialogFragment suaContaDialogFragment = new SuaContaDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("tela", "promocao");
                            suaContaDialogFragment.setArguments(bundle);
                            suaContaDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.add(suaContaDialogFragment,"fragment_dialog");
                            ft.commit();
                        }
                    });

                    AlertDialog alertDialog = customBuilder.create();

                    alertDialog.show();
                    Button btnPositiveWhatsapp = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                    btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                }else {
                    System.out.println("Usuário logado!!!");


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PromocaoActivity.this);
                    alertDialog.setTitle("Promoção");
                    alertDialog.setMessage("Para confirmar sua participação, complete os dados a seguir:");


                    LinearLayout layout = new LinearLayout(PromocaoActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText input = new EditText(PromocaoActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setHint("Digite o seu CPF");
                    final EditText input2 = new EditText(PromocaoActivity.this);
                    input2.setHint("Digite o seu CEP");
                    input2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    final EditText input3 = new EditText(PromocaoActivity.this);
                    input3.setHint("Digite o seu telefone");
                    input3.setInputType(InputType.TYPE_CLASS_NUMBER);

                    MaskEditTextChangedListener maskCPF = new MaskEditTextChangedListener("###.###.###-##", input);
                    input.addTextChangedListener(maskCPF);

                    MaskEditTextChangedListener maskCEP = new MaskEditTextChangedListener("#####-###", input2);
                    input2.addTextChangedListener(maskCEP);

                    MaskEditTextChangedListener maskFone = new MaskEditTextChangedListener("(###)#####-####", input3);
                    input3.addTextChangedListener(maskFone);

                    layout.addView(input);
                    layout.addView(input2);
                    layout.addView(input3);


                    alertDialog.setView(layout);

                    alertDialog.setPositiveButton("CONFIRMAR",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String cpf = input.getText().toString();
                                    final String cep = input2.getText().toString();
                                    final String fone = input3.getText().toString();
                                    if (cpf.compareTo("") != 0) {
                                        if (cep.compareTo("") != 0) {
                                            if (fone.compareTo("") != 0) {


                                                long value = promocao.get(itemPosicao).getTimestamp();
                                                BigDecimal d = new BigDecimal(value);
                                                Document newMemo = new Document();
                                                newMemo.put("promotionId", d);
                                                newMemo.put("radioId", cacheData.getString("idRadio"));
                                                newMemo.put("email", cacheData.getString("userEmail"));
                                                newMemo.put("cep", cep);
                                                newMemo.put("cpf", cpf);
                                                newMemo.put("fone", fone);
                                                newMemo.put("name", cacheData.getString("userNome"));

                                                CreateItemAsyncTask task = new CreateItemAsyncTask();
                                                task.execute(newMemo);

                                                participandoCheck = true;
                                                promocoesParticipantes.add(promocao.get(itemPosicao).getTimestamp()+"");
                                                cacheData.putListString("promocoesParticipantes", promocoesParticipantes);
                                                Dataset profileData = CognitoSyncClientManager.openOrCreateDataset("profileData");
                                                String promocoesDataset;
                                                if (cacheData.getString("promocoesDataset").equals("")){
                                                    promocoesDataset = promocao.get(itemPosicao).getTimestamp() + "";
                                                }else {
                                                    promocoesDataset = cacheData.getString("promocoesDataset") + "-" + promocao.get(itemPosicao).getTimestamp();
                                                }

                                                profileData.put("promos", promocoesDataset);
                                                profileData.synchronize(new SyncCallback() {
                                                    @Override
                                                    public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                                                        Log.e("SyncCallback", "Success.");
                                                    }

                                                    @Override
                                                    public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public void onFailure(DataStorageException dse) {
                                                        Log.d("","");
                                                    }
                                                });

                                            } else {
                                                alertError(2);
                                            }
                                        } else {
                                            alertError(1);
                                        }
                                    } else {
                                        alertError(0);
                                    }
                                }
                            });

                    alertDialog.setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();



                }
            }
        });
        txtPremios.setTextColor(Color.parseColor(cacheData.getString("color")));
        adapter = new PremiosAdapter(mContext, promocao.get(itemPosicao).getPremio());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    public void create(Document memo) {
        dbTable.putItem(memo);
    }

    private class CreateItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            // Create a table reference
            dbTable = Table.loadTable(dbClient, "rc_participants");
            create(documents[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (participandoCheck){
                AlertDialog.Builder customBuilder  = new AlertDialog.Builder(mActivity);
                customBuilder.setTitle("Rádio Controle");
                customBuilder.setMessage("Você está participando desta promoção.");
                customBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = customBuilder.create();

                alertDialog.show();
                Button btnPositiveWhatsapp = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                btnPositiveWhatsapp.setTextColor(Color.parseColor(cacheData.getString("color")));
                participando.setVisibility(View.VISIBLE);
                btnParticipar.setVisibility(View.GONE);
            }else {
                btnParticipar.setVisibility(View.VISIBLE);
            }
        }
    }

    void alertError(int index) {
        String field = "";
        switch (index) {
            case 0:
                field = "CPF";
                break;
            case 1:
                field = "CEP";
                break;
            case 2:
                field = "FONE";
                break;

        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PromocaoActivity.this);
        alertDialog.setTitle("Atenção");
        alertDialog.setMessage("Corriga o campo " + field + " ,esse apresenta estar errado.");
        alertDialog.show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(promocao.get(itemPosicao).getLinkVideoPromocao());

            if(matcher.find()){
                youTubePlayer.cueVideo(matcher.group());
            }

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Erro ao inicializar Youtube PlayerService: ", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}