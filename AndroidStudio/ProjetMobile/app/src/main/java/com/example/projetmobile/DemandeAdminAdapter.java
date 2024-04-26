package com.example.projetmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class DemandeAdminAdapter extends RecyclerView.Adapter<DemandeAdminAdapter.ViewHolder> {
    private Context contexte;
    private List<Demande> demandeListe;

    public DemandeAdminAdapter(Context context, List<Demande> demandeList) {
        this.contexte = context;
        this.demandeListe = demandeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexte).inflate(R.layout.demand_admin_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Demande demande = demandeListe.get(position);
        holder.titleTextView.setText(demande.getTitle());
        holder.sujetTextView.setText(demande.getSujet());
        holder.dateTextView.setText(demande.getDate());
        holder.etatTextView.setText(demande.getEtat());

        holder.btnTraiter.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(contexte);
            builder.setTitle("Entrez le motif");

            final EditText input = new EditText(contexte);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);

            builder.setPositiveButton("Accepter", (dialog, which) -> {
                envoyerRequete("newSolve", position, input.getText().toString());
            });
            builder.setNegativeButton("Refuser", (dialog, which) -> {
                envoyerRequete("newReject", position, input.getText().toString());
            });

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return demandeListe.size();
    }

    private void envoyerRequete(String endpoint, int position, String motif) {
        SharedPreferences sharedPref = contexte.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        long idDemande = demandeListe.get(position).getIdDemande();

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("motif", motif);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "http://10.0.2.2:9090/reclamation/" + endpoint + "?token=" + token + "&id=" + idDemande,
                    jsonBody,
                    response -> {
                        String etat = endpoint.equals("newSolve") ? "Solved" : "Rejected";
                        Toast.makeText(contexte, "Demande " + etat.toLowerCase(), Toast.LENGTH_SHORT).show();
                        demandeListe.get(position).setEtat(etat);
                        notifyItemChanged(position);
                    },
                    error -> Toast.makeText(contexte, "Erreur lors de la requête", Toast.LENGTH_SHORT).show()
            );

            RequestQueue queue = Volley.newRequestQueue(contexte);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Toast.makeText(contexte, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sujetTextView, dateTextView, etatTextView;
        Button btnTraiter;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            sujetTextView = itemView.findViewById(R.id.sujetTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            etatTextView = itemView.findViewById(R.id.etatTextView);
            btnTraiter = itemView.findViewById(R.id.btnTraiter);
        }
    }
}
