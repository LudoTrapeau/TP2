package views.labs.luminiasoft.com.tp2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/****
 * Le CustomAdapter va nous permettre de faire la mise en forme de chaque item de la listview
 * ****/
public class CustomAdapter extends ArrayAdapter<EcolePrimaire> {

    //tweets est la liste des models à afficher
    public CustomAdapter(Context context, List<EcolePrimaire> ecolesPrimaires) {
        super(context, 0, ecolesPrimaires);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_ecoles,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            /***
             * Association des variables aux éléments du layout
             * ***/
            viewHolder.nomEcole = (TextView) convertView.findViewById(R.id.nomEcole);
            viewHolder.adresseEcole = (TextView) convertView.findViewById(R.id.adresseEcole);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.linearLayout);
            //viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        EcolePrimaire ecolePrimaire = getItem(position);

        /**
         * Récupération de la couleur que l'on a récupéré en amont dans la classe métier EcolePrimaire
         * */
        String couleur = ecolePrimaire.getColor();

        System.out.println("MA COULEUR : " + couleur);


        //il ne reste plus qu'à remplir notre vue
        viewHolder.nomEcole.setText(ecolePrimaire.getNom());
        viewHolder.adresseEcole.setText(ecolePrimaire.getAdresse());
        viewHolder.linearLayout.setBackgroundColor(Color.parseColor(couleur));


        return convertView;
    }

    /***
     * Déclaration des différents champs
     * ***/
    private class TweetViewHolder{
        public TextView nomEcole;
        public TextView adresseEcole;
        public LinearLayout linearLayout;
    }
}