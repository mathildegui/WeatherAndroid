package com.mathildeguillossou.weathersensor.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathildeguillossou.weathersensor.R;

import java.text.Format;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotFoundActivity extends Activity {

    @Bind(R.id.history) TextView mHistoryTv;
    @Bind(R.id.main_description) TextView mDescriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_found);

        ButterKnife.bind(this);

        mDescriptionTv.setText(Html.fromHtml("La <b>Peugeot 404</b> est une automobile de la marque " +
                "Peugeot produite à partir de mai 1960. Ce sera, en mai 1962, la première berline" +
                " française de série (tout comme le premier coupé/cabriolet) équipée d'un moteur" +
                " à injection. <br /><br /> La 404 a été retirée du catalogue français en octobre 1975, " +
                "à l'exception de la camionnette qui fut commercialisée en France jusqu'en 1979." +
                " La production s'est prolongée pendant plusieurs années à l'étranger : " +
                "les dernières 404 étaient des utilitaires assemblés en 19891 à l'usine de " +
                "Mombassa (Kenya). <br /><br />De 1960 à 1975, 2 885 377 exemplaires (toutes versions " +
                "confondues) sont sortis des chaînes de montage, dont 1 672 395 berlines."));

        mHistoryTv.setText(Html.fromHtml("Lorsqu'en 1955, la firme de Sochaux présente la 403, " +
                "peu de temps après Citroën lance la DS. La clientèle traditionnelle n'a pas vraiment " +
                "succombé à la suspension hydraulique d'autant que les premiers ennuis techniques sont " +
                "vite apparus, mais le directoire de Peugeot a réagi très rapidement, chose très inhabituelle " +
                "pour le constructeur. Dès la fin de l'année 1955, la décision de lancer en urgence l'étude " +
                "de la remplaçante de la 403 était prise, elle s'appellerait 404. " +
                "<br /><br /> " +
                "Un premier projet avec une motorisation V8 et une suspension hydraulique est envisagé mais très " +
                "vite abandonné. Les déboires du concurrent ont précipité cette décision. En pleine période de " +
                "guerre de Suez, Peugeot parie sur une berline familiale traditionnelle mais d'aspect moderne. " +
                "Très satisfait de sa première collaboration avec le carrossier italien Pininfarina qui fut " +
                "l'auteur de la 403, la direction de Peugeot lui confia l'étude de la 404 mais avec une " +
                "contrainte de poids, la réduction du temps d'étude, avec la possibilité de trouver, dans " +
                "les cartons du maître en design, un projet déjà prêt. Pininfarina proposa l'étude réalisée " +
                "pour Fiat qui donnera la Fiat 1800/2100 et le britannique Austin. Avec quelques savantes " +
                "retouches pour faire accepter à ces deux constructeurs que Peugeot dévoile une carrosserie " +
                "semblable et la 404 est née en à peine 3 mois." +
                "<br /><br />" +
                "La partie mécanique elle, n'eut que peu d'études car elle reprenait intégralement celle de la " +
                "403 avec un moteur dont la cylindrée était portée à 1,6 litres et 65 Ch de puissance. " +
                "Le moteur ne sera doté de l'injection portant la puissance à 86 Ch qu'en 1963 pour " +
                "corriger les performances trop moyennes."));
    }
}
