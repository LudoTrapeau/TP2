package views.labs.luminiasoft.com.tp2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListeEcoles extends AppCompatActivity {

    /**
     * Déclaration des variables
     * **/
    ListView listviewEcoles;
    Button btnMaps;
    StringRequest stringRequest;
    /***
     * URL à laquelle on va récupérer les datas
     * **/
    String urlGetEcole = "http://ynov-tp1.getsandbox.com/getEcolePrimaire", nomEcole,statusEcole,latitudeEcole,longitudeEcole,
            adresseEcole,nbElevesEcole;
    JSONObject json,jsonObject;
    RequestQueue requestQueue;
    JSONArray listeEnfantsArray;
    ArrayList<EcolePrimaire> listeEcoles = new ArrayList<EcolePrimaire>();
    private String[] ecoles;
    CustomAdapter myCustomAdapter;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_ecoles);

        listviewEcoles = (ListView)findViewById(R.id.listviewEcoles);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        getEcoles();

        System.out.println("Nombre de lignes " + listeEcoles.size());

        listviewEcoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                /***
                 * Lors du clic sur une ligne, on crée un objet EcolePrimaire pour récupérer les données de la ligne et les envoyer à l'activité suivante
                 * ***/
                EcolePrimaire ecolePrimaire = (EcolePrimaire) parent.getItemAtPosition(position);
                Toast.makeText(ListeEcoles.this, "Mon ecole " + ecolePrimaire.getNom(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListeEcoles.this,MapsActivity.class);
                intent.putExtra("ecolePrimaire",ecolePrimaire.getId());
                intent.putExtra("lat",ecolePrimaire.getLatitude());
                intent.putExtra("long",ecolePrimaire.getLongitude());
                intent.putExtra("zoom",15);
                intent.putExtra("choix",2);
                startActivity(intent);
            }
        });


    }

    /****
     *  Récupération des écoles
     * ***/
    public void getEcoles() {
        stringRequest = new StringRequest(Request.Method.GET, urlGetEcole, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("Ecoles : " + response);
                try {
                    JSONArray listeEnfantsArray = new JSONArray(response);

                    /***
                     * On récupère chaque arguments en parcourant l'array
                     * **/
                    for (int i = 0; i < listeEnfantsArray.length(); i++) {
                        jsonObject = listeEnfantsArray.getJSONObject(i);
                        nomEcole = jsonObject.getString("nom");
                        adresseEcole = jsonObject.getString("addresse");
                        statusEcole  = jsonObject.getString("status");
                        latitudeEcole = jsonObject.getString("latitude");
                        longitudeEcole = jsonObject.getString("longitude");
                        nbElevesEcole = jsonObject.getString("nbEleve");

                        /*****
                         * Création de l'objet EcolePrimaire
                         * ****/
                        EcolePrimaire ecolePrimaire = new EcolePrimaire(i+1,nomEcole,adresseEcole,latitudeEcole,longitudeEcole,statusEcole,nbElevesEcole);

                        /***
                         * Ajout de l'objet à l'array
                         * ***/
                        listeEcoles.add(ecolePrimaire);

                        /***
                         * On associe notre arraylist à notre listview grace au CustomAdapter
                         * ***/
                        myCustomAdapter = new CustomAdapter(ListeEcoles.this, listeEcoles);
                        listviewEcoles.setAdapter(myCustomAdapter);

                        System.out.println("Ecole numéro: " + i + " qui s'appelle " + nomEcole);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) ;
        requestQueue.add(stringRequest);
    }
}
