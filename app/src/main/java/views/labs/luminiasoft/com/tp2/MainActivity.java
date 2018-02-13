package views.labs.luminiasoft.com.tp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /****
     * On déclare les variables
     *****/
    private static final double LONGITUDE_LYON = 4.850000; // Longitude de la ville de lyon
    private static final double LATITUDE_LYON = 45.750000; // Latitude de la ville de Lyon
    private static final int ZOOM_DEFAULT = 10; // Permet d'afficher avec une zoom optimale la zone des écoles.
    Button btnMaps, btnEcoles;
    StringRequest stringRequest;
    String urlGetEcole = "http://ynov-tp1.getsandbox.com/getEcolePrimaire", nomEcole,statusEcole,latitudeEcole,longitudeEcole,
    adresseEcole,nbElevesEcole;
    JSONObject json,jsonObject;
    RequestQueue requestQueue;
    ArrayList<EcolePrimaire> listeEcoles = new ArrayList<EcolePrimaire>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /****
         * Mise en relation avec le layout
         * **/
        setContentView(R.layout.activity_main);

        /*****
         * On récupère les éléments du layout
         * *****/
        btnMaps = (Button)findViewById(R.id.btnMaps);
        btnEcoles = (Button)findViewById(R.id.btnEcoles);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        /***
         * Lors du clic sur le bouton "Google maps
         * **/
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /****
                 * On se redirige vers l'activité MapsActivity, en envoyant des parametres
                 * ****/
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("long", LONGITUDE_LYON);
                intent.putExtra("lat", LATITUDE_LYON);
                intent.putExtra("zoom", ZOOM_DEFAULT);
                startActivity(intent);
            }
        });

        /****
         * Lors du clic sur le bouton Liste des écoles
         * ****/
        btnEcoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /****
                 * On se redirige vers l'activité MapsActivity
                 * ****/
                Intent intent = new Intent(MainActivity.this, ListeEcoles.class);
                startActivity(intent);
            }
        });

        /****
         * Récupération des écoles
         * ****/
        getEcoles();


    }

    /****
     * Méthode que l'on appelle en haut
     * ***/
    public void getEcoles() {
        stringRequest = new StringRequest(Request.Method.GET, urlGetEcole, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Ecoles : " + response);
                try {
                    JSONArray listeEcolesArray = new JSONArray(response);

                    /***
                     * On parcourt la liste en récupérant les parametres
                     * ****/
                    for (int i = 0; i < listeEcolesArray.length(); i++) {
                        jsonObject = listeEcolesArray.getJSONObject(i);
                        nomEcole = jsonObject.getString("nom");
                        adresseEcole = jsonObject.getString("addresse");
                        statusEcole  = jsonObject.getString("status");
                        latitudeEcole = jsonObject.getString("latitude");
                        longitudeEcole = jsonObject.getString("longitude");
                        nbElevesEcole = jsonObject.getString("nbEleve");

                        /****
                         * Création d'un nouvel Objet EcolePrimaire
                         * ****/
                        EcolePrimaire ecolePrimaire = new EcolePrimaire(i,nomEcole,adresseEcole,latitudeEcole,longitudeEcole,statusEcole,nbElevesEcole);

                        /*****
                         * Ajout à l'arraylist de l'objet
                         * *****/
                        listeEcoles.add(ecolePrimaire);

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
        }) {

        };
        requestQueue.add(stringRequest);
    }

}
