package br.com.devmaker.rbn.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.util.CacheData;

/**
 * Created by Des. Android on 30/06/2017.
 */

public class SobreDialogFragment extends DialogFragment {

    private static String TAG = SobreDialogFragment.class.getSimpleName();
    private RelativeLayout facebook;
    private RelativeLayout site;
    private RelativeLayout instagram;
    private RelativeLayout twitter;
    private RelativeLayout email;
    private RelativeLayout telefone;
    private RelativeLayout whatsup;
    private TextView txtName;
    private ArrayList<String> sociais = new ArrayList<String>();
    private int aux = 0;
    private String auxFacebook, auxSite, auxInstagram, auxTwitter, auxEmail, auxTelefone, auxWhatsapp;
    private ImageView imgLogo;

    public SobreDialogFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final CacheData cacheData = new CacheData(getContext());
        View rootView = inflater.inflate(R.layout.fragment_sobre, null, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        facebook = (RelativeLayout) rootView.findViewById(R.id.facebook);
        site = (RelativeLayout) rootView.findViewById(R.id.site);
        instagram = (RelativeLayout) rootView.findViewById(R.id.instagram);
        twitter = (RelativeLayout) rootView.findViewById(R.id.twitter);
        telefone = (RelativeLayout) rootView.findViewById(R.id.telefone);
        whatsup = (RelativeLayout) rootView.findViewById(R.id.whatsup);
        email = (RelativeLayout) rootView.findViewById(R.id.email);
        txtName = rootView.findViewById(R.id.textView2);
        imgLogo = rootView.findViewById(R.id.imgLogo);


        txtName.setText(cacheData.getString("nomeRadio"));

        sociais = cacheData.getListString("social");

        String logo = cacheData.getString("logo");

        if(!logo.equals("")) {
            Picasso.with(getContext())
                    .load(logo)
                    .error(R.drawable.radio)
                    .into(imgLogo);
        }

        Log.d("aaaa", logo);

        for (int i = 0; i < sociais.size(); i++){
            String[] parts = sociais.get(i).split(";");
            String part1 = parts[0];
            String part2 = "";
            if (parts.length != 1) {
                part2 = parts[1];


                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(0, aux, 0, 0);

                switch (part1) {
                    case "website":
                        site.setVisibility(View.VISIBLE);
                        site.setLayoutParams(layoutParams);
                        aux += 80;
                        auxSite = part2;
                        break;
                    case "email":
                        email.setVisibility(View.VISIBLE);
                        email.setLayoutParams(layoutParams);
                        aux += 80;
                        auxEmail = part2;
                        break;
                    case "facebook":
                        facebook.setVisibility(View.VISIBLE);
                        facebook.setLayoutParams(layoutParams);
                        aux += 80;
                        auxFacebook = part2;
                        break;
                    case "twitter":
                        twitter.setVisibility(View.VISIBLE);
                        twitter.setLayoutParams(layoutParams);
                        aux += 80;
                        auxTwitter = part2;
                        break;
                    case "phone":
                        telefone.setVisibility(View.VISIBLE);
                        telefone.setLayoutParams(layoutParams);
                        aux += 80;
                        auxTelefone = part2;
                        break;
                    case "whatsapp":
                        whatsup.setVisibility(View.VISIBLE);
                        whatsup.setLayoutParams(layoutParams);
                        aux += 80;
                        auxWhatsapp = part2;
                        break;
                    case "instragram":
                        instagram.setVisibility(View.VISIBLE);
                        instagram.setLayoutParams(layoutParams);
                        aux += 80;
                        auxInstagram = part2;
                        break;
                }
            }
        }


        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Facebook");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(auxFacebook));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        site.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Site");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(auxSite));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Instagram");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(auxInstagram));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Twitter");
                try {
                    Intent intentContato = new Intent(Intent.ACTION_VIEW, Uri.parse(auxTwitter));
                    startActivity(intentContato);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        telefone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Telefone");
                AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                customBuilder .setTitle("Ligar");
                customBuilder .setMessage("Você deseja efetuar a ligação\npara " + auxTelefone + "?");
                customBuilder .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri call = Uri.parse("tel:" + auxTelefone);
                        try {
                            Intent ligar = new Intent(Intent.ACTION_DIAL, call);
                            startActivity(ligar);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                customBuilder .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialogLigar = customBuilder.create();

                dialogLigar.show();
                Button btnNegative = dialogLigar.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnNegative.setTextColor(Color.parseColor(cacheData.getString("color")));

                Button btnPositive = dialogLigar.getButton(DialogInterface.BUTTON_POSITIVE);
                btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));
            }
        });
        whatsup.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Log.v("Click", "Whatsapp");
                AlertDialog.Builder customBuilder  = new AlertDialog.Builder(getActivity());
                customBuilder .setTitle("Enviar Mensagem");
                customBuilder .setMessage("Você deseja efetuar uma mensagem\npara "+ auxWhatsapp +"?");
                customBuilder .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?1=pt_BR&phone=55" + auxWhatsapp));
                        v.getContext().startActivity(intent);
                    }
                });

                customBuilder .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialogLigar = customBuilder.create();

                dialogLigar.show();
                Button btnNegative = dialogLigar.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnNegative.setTextColor(Color.parseColor(cacheData.getString("color")));

                Button btnPositive = dialogLigar.getButton(DialogInterface.BUTTON_POSITIVE);
                btnPositive.setTextColor(Color.parseColor(cacheData.getString("color")));
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Email");
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                intentEmail.setData(Uri.parse("mailto:"));
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{auxEmail});
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Contato realizado através do aplicativo Rádio Controle");
                if (intentEmail.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intentEmail);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CacheData cacheData = new CacheData(getContext());
        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(color);
        }
        return dialog;
    }

}
