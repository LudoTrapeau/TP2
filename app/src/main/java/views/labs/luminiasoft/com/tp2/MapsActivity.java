package views.labs.luminiasoft.com.tp2;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /***
     * On déclare les variables
     * **/
    private GoogleMap mMap;
    ListView listviewEcoles;
    StringRequest stringRequest;
    String urlGetEcole = "http://ynov-tp1.getsandbox.com/getEcolePrimaire", nomEcole,statusEcole,latitudeEcole,longitudeEcole,
            adresseEcole,nbElevesEcole;
    JSONObject json,jsonObject;
    RequestQueue requestQueue;
    ArrayList<EcolePrimaire> listeEcoles = new ArrayList<EcolePrimaire>();
    LatLng pointEcole;
    private BitmapDescriptor bitmapDescriptorGreen,bitmapDescriptorOrange,bitmapDescriptorRed,bitmapDescriptorColorDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listviewEcoles = (ListView)findViewById(R.id.listviewEcoles);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        init();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();

        /****
         * On met en relation les variables avec le layout
         * ****/
        int choix = intent.getIntExtra("choix",0);
        int longitudeCamera = intent.getIntExtra("long",0);
        int latitudeCamera = intent.getIntExtra("lat",0);
        int zoom = intent.getIntExtra("zoom",0);

        int idEcole = intent.getIntExtra("ecolePrimaire",1);

        System.out.println("2.Choix : " + choix);
        System.out.println("2.Id Ecole : " + idEcole);

        //Enregistre la position de la caméra avant d'afficher la map selon la latitude et longitude fournie.
        LatLng posCamera = new LatLng(latitudeCamera,longitudeCamera);
        //Place la caméra selon la position fourni.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posCamera,zoom));

        /****
         * Choix 2 correspond lorsque l'on va à la map en cliquant sur une école en particulier et le else quand on va directement sur la map
         * ****/
        if(choix==2){
            getEcoles(idEcole,2);
        }else{
            getEcoles(0,1);
        }
    }

    /*****
     * Récupération des écoles
     * ******/
    public void getEcoles(final int idEcole, final int choix) {
        stringRequest = new StringRequest(Request.Method.GET, urlGetEcole, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("Ecoles : " + response);
                try {
                    /**
                     * On récupère un JsonArray
                     */

                    JSONArray listeEcolesArray = new JSONArray(response);

                    System.out.println("Mon choix est le suivant " + choix + " et mon id ecole est " + idEcole);

                    /****
                     * Parcours de l'array
                     * ***/
                    for (int i = 0; i < listeEcolesArray.length(); i++) {
                        jsonObject = listeEcolesArray.getJSONObject(i);
                        nomEcole = jsonObject.getString("nom");
                        adresseEcole = jsonObject.getString("addresse");
                        statusEcole  = jsonObject.getString("status");
                        latitudeEcole = jsonObject.getString("latitude");
                        longitudeEcole = jsonObject.getString("longitude");
                        nbElevesEcole = jsonObject.getString("nbEleve");

                        /*****
                         * Création de l'objet EcolePrimaire
                         * *******/
                        EcolePrimaire ecolePrimaire = new EcolePrimaire(i+1,nomEcole,adresseEcole,latitudeEcole,longitudeEcole,statusEcole,nbElevesEcole);

                        /***
                         * Ajout de l'objet à l'arraylist
                         * ***/
                        listeEcoles.add(ecolePrimaire);

                        /******
                         * Si on vient de la liste des écoles
                         * ******/
                        if(choix==2){
                            /*********
                             * Si l'id de l'école récupérée correspond à la valeur de "i" dans le tableau, on place uniquement le curseur de cette école sur la carte
                             * ******/
                            if(idEcole==(i+1)) {
                                pointEcole = new LatLng(Double.parseDouble(latitudeEcole), Double.parseDouble(longitudeEcole));
                                mMap.addMarker(new MarkerOptions().position(pointEcole).title(nomEcole + "\n" + adresseEcole));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(pointEcole));
                                System.out.println("TEST CURSOR" );

                            }
                            /***
                             * Si on vient directement grace au bouton Google maps
                             * ****/
                        }else{
                            pointEcole = new LatLng(Double.parseDouble(latitudeEcole), Double.parseDouble(longitudeEcole));
                            mMap.addMarker(new MarkerOptions().position(pointEcole).title(nomEcole+"\n"+adresseEcole));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(pointEcole));
                            System.out.println("Toutes les écoles");


                            MarkerOptions option = new MarkerOptions().position(pointEcole).title(ecolePrimaire.getNom());

                            /****
                             * Vérification de la couleur de l'objet et coloriage du cursor
                             * ***/
                            if (ecolePrimaire.getColor().equals("#6CDA3A")) {
                                option.icon(bitmapDescriptorGreen);
                            } else if (ecolePrimaire.getColor().equals("#FFA23D")) {
                                option.icon(bitmapDescriptorOrange);
                            } else if (ecolePrimaire.getColor().equals("#F93D3D")) {
                                option.icon(bitmapDescriptorRed);
                            } else {
                                option.icon(bitmapDescriptorColorDefault);
                            }

                            mMap.addMarker(option);
                        }

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

    public void init(){
        /****
         * Mise en couleur
         * ****/
        bitmapDescriptorGreen
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN);

        bitmapDescriptorOrange
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_ORANGE);

        bitmapDescriptorRed
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED);

        bitmapDescriptorColorDefault
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_CYAN);
    }
}
