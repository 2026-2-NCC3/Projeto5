package com.example.nextstepapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.LoginData;
import com.example.nextstepapp.model.LoginRequest;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;
import com.example.nextstepapp.ui.CursosActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etMatricula;
    private EditText etSenha;
    private Button btnEntrar;
    private TextView tvErro;
    private ProgressBar progressLogin;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMatricula = findViewById(R.id.etMatricula);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvErro = findViewById(R.id.tvErro);
        progressLogin = findViewById(R.id.progressLogin);

        sessionManager = new SessionManager(this);

        btnEntrar.setOnClickListener(v -> tentarLogin());
    }
    private void tentarLogin() {
        String matricula = etMatricula.getText().toString().trim();
        String senha = etSenha.getText().toString();
        if (TextUtils.isEmpty(matricula) || TextUtils.isEmpty(senha)) {
            tvErro.setText("Preencha matrícula e senha."
            );
            return;
        }
        mostrarCarregamento(true);tvErro.setText("");LoginRequest request = new LoginRequest(matricula, senha);ApiClient.getApiService().login(request).enqueue(
                new Callback<ApiResponse<LoginData>>() {
                    @Override
                            public void onResponse(Call<ApiResponse<LoginData>> call, Response<ApiResponse<LoginData>> response) {mostrarCarregamento(false);
                        ApiResponse<LoginData> body = response.body();
                                if (response.isSuccessful() && body != null && body.isSuccess() && body.getData() != null && body.getData().getToken() != null) {
                                    sessionManager.salvarToken(body.getData().getToken()
                                    );
                                    Intent intent = new Intent(MainActivity.this, CursosActivity.class
                                    );
                                    startActivity(intent);finish();return;
                                }
                                if (response.code() == 401) {tvErro.setText("Usuário ou senha inválidos.");
                                } else if (response.code() == 403) {
                                    tvErro.setText("Usuário bloqueado.");
                                } else {
                                    tvErro.setText("Não foi possível realizar o login.");
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable throwable) {
                                mostrarCarregamento(false);
                                tvErro.setText("Não foi possível conectar ao servidor.");
                            }
                        }
                );
    }
    private void mostrarCarregamento(boolean carregando) {
        progressLogin.setVisibility(carregando ? View.VISIBLE : View.GONE);

        btnEntrar.setEnabled(!carregando);
        etMatricula.setEnabled(!carregando);
        etSenha.setEnabled(!carregando);
    }
}