package com.example.nextstepapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresencaActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private ImageView imgQrCode;
    private Button btnRegistrarPresenca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenca);

        sessionManager = new SessionManager(this);
        imgQrCode = findViewById(R.id.imgQrCode);
        btnRegistrarPresenca = findViewById(R.id.btnRegistrarPresenca);

        gerarQrCodeUnicoDoUsuario();
        configurarNavegacao();

        btnRegistrarPresenca.setOnClickListener(v -> registrarPresenca());
    }

    private void configurarNavegacao() {
        findViewById(R.id.btnVoltarPresenca).setOnClickListener(v -> finish());
        findViewById(R.id.btnNotificacoesPresenca).setOnClickListener(
                v -> abrirTela(NotificacoesActivity.class, false));

        findViewById(R.id.nav_inicio).setOnClickListener(
                v -> abrirTela(HomeActivity.class, true));
        findViewById(R.id.nav_agenda).setOnClickListener(
                v -> abrirTela(AgendaActivity.class, false));
        findViewById(R.id.nav_presencas).setOnClickListener(v -> {
            // Esta tela já está selecionada.
        });
        findViewById(R.id.nav_notificacoes).setOnClickListener(
                v -> abrirTela(NotificacoesActivity.class, false));
    }

    private void abrirTela(Class<?> tela, boolean limparTopo) {
        Intent intent = new Intent(this, tela);
        if (limparTopo) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        startActivity(intent);
        if (!limparTopo) {
            finish();
        }
    }

    private void gerarQrCodeUnicoDoUsuario() {
        String userId = sessionManager.obterUserId();
        String userName = sessionManager.obterNomeUsuario();
        String userRole = sessionManager.obterUserRole();

        TextView tvAluno = findViewById(R.id.tvAlunoPresenca);
        tvAluno.setText(userName == null || userName.trim().isEmpty()
                ? "Aluno" : userName);

        try {
            JSONObject payload = new JSONObject();
            payload.put("user_id", userId == null ? "" : userId);
            payload.put("name", userName == null ? "" : userName);
            payload.put("role", userRole == null ? "" : userRole);
            payload.put("app", "NextStepApp");

            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(
                    payload.toString(), BarcodeFormat.QR_CODE, 600, 600);
            imgQrCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Não foi possível gerar o QR Code.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarPresenca() {
        String token = sessionManager.obterToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Faça login novamente para registrar a presença.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String courseId = getIntent().getStringExtra("course_id");
        if (courseId != null && !courseId.trim().isEmpty()) {
            enviarCheckIn(token, courseId);
        } else {
            buscarCursoParaCheckIn(token);
        }
    }

    private void buscarCursoParaCheckIn(String token) {
        btnRegistrarPresenca.setEnabled(false);
        ApiClient.getApiService().getAgenda("Bearer " + token)
                .enqueue(new Callback<ApiResponse<List<Curso>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Curso>>> call,
                                           Response<ApiResponse<List<Curso>>> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getData() != null
                                && !response.body().getData().isEmpty()) {
                            Curso curso = response.body().getData().get(0);
                            if (curso.getId() != null && !curso.getId().trim().isEmpty()) {
                                enviarCheckIn(token, curso.getId());
                                return;
                            }
                        }
                        btnRegistrarPresenca.setEnabled(true);
                        Toast.makeText(PresencaActivity.this,
                                "Nenhum curso disponível para registrar presença.",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                        btnRegistrarPresenca.setEnabled(true);
                        Toast.makeText(PresencaActivity.this,
                                "Não foi possível carregar a agenda. Verifique sua conexão.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void enviarCheckIn(String token, String courseId) {
        btnRegistrarPresenca.setEnabled(false);
        ApiClient.getApiService().checkIn(
                "Bearer " + token,
                Collections.singletonMap("course_id", courseId)
        ).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {
                btnRegistrarPresenca.setEnabled(true);
                ApiResponse<Object> body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess()) {
                    String mensagem = body.getMessage() == null || body.getMessage().trim().isEmpty()
                            ? "Presença registrada com sucesso."
                            : body.getMessage();
                    Toast.makeText(PresencaActivity.this, mensagem, Toast.LENGTH_LONG).show();
                } else {
                    String mensagem = body != null && body.getMessage() != null
                            ? body.getMessage()
                            : "A API não confirmou o registro da presença.";
                    Toast.makeText(PresencaActivity.this, mensagem, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                btnRegistrarPresenca.setEnabled(true);
                Toast.makeText(PresencaActivity.this,
                        "Falha de conexão. A presença não foi registrada.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
