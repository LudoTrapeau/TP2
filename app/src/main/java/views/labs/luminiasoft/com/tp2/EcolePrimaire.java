package views.labs.luminiasoft.com.tp2;

/**
 * Created by Ludovic TRAPEAU on 08/02/2018.
 */

public class EcolePrimaire {

    /*****
     * Déclaration des variables
     * *****/
    private String nom, adresse, latitude, longitude, status, nbEleves;
    private int id;


    /******
     * Constructeur
     * *****/
    public EcolePrimaire(int id, String nom, String adresse, String latitude, String longitude, String status, String nbEleves) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.nbEleves = nbEleves;
    }

    /******
     * Getters et Setters
     * ******/

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNbEleves() {
        return nbEleves;
    }

    public void setNbEleves(String nbEleves) {
        this.nbEleves = nbEleves;
    }

    /**
     * Choix de la couleur en fonction du nombre d'élèves par école, on exploitera cela dans la page ListeEcoles
     * */
    public String getColor() {
        String color = null;
        if(Integer.parseInt(this.nbEleves)<150){
            color = "#6CDA3A";
        }else if(Integer.parseInt(this.nbEleves)>150 && Integer.parseInt(this.nbEleves)<300){
            color = "#FFA23D";
        }else if(Integer.parseInt(this.nbEleves)>300){
            color = "#F93D3D";
        }else{
            color = "#FFFFFF";
        }
        return color;
    }
}
