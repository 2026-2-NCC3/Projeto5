package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesEventoActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);

        sessionManager = new SessionManager(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        courseId = getIntent().getStringExtra("course_id");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("course_date");

        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        if (tvHeaderTitle != null && title != null && !title.isEmpty()) {
            tvHeaderTitle.setText(title);
        }

        TextView tvFullDate = findViewById(R.id.tvFullDate);
        if (tvFullDate != null && date != null && !date.isEmpty()) {
            tvFullDate.setText(date.replace("T", " ").replace(".000Z", ""));
        }

        MaterialButton btnVerMais = findViewById(R.id.btnVerMais);
        if (btnVerMais != null) {
            btnVerMais.setText("INSCREVER-SE NO CURSO");
            btnVerMais.setOnClickListener(v -> inscreverNoCurso());
        }

        MaterialButton btnTirarDuvidas = findViewById(R.id.btnTirarDuvidas);
        if (btnTirarDuvidas != null) {
            btnTirarDuvidas.setText("REGISTRAR PRESENÇA");
            btnTirarDuvidas.setOnClickListener(v -> {
                Intent intent = new Intent(this, PresencaActivity.class);
                if (courseId != null) {
                    intent.putExtra("course_id", courseId);
                }
                startActivity(intent);
            });
        }
    }

    private void inscreverNoCurso() {
        if (courseId == null || courseId.trim().isEmpty()) {
            Toast.makeText(this, "Curso inválido para inscrição.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.obterToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Faça login para se inscrever.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getApiService().enrollInCourse("Bearer " + token, courseId)
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            String msg = response.body().getMessage() != null
                                    ? response.body().getMessage()
                                    : "Inscrição confirmada com sucesso!";
                            Toast.makeText(DetalhesEventoActivity.this, msg, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetalhesEventoActivity.this, "Não foi possível realizar a inscrição.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(DetalhesEventoActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
