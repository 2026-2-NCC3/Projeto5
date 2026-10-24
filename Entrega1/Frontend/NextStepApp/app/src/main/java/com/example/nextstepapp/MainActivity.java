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
import com.example.nextstepapp.ui.HomeActivity;

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

        btnEntrar.setOnClickListener(
                v -> tentarLogin()
        );
    }

    private void tentarLogin() {

        String matricula =
                etMatricula.getText()
                        .toString()
                        .trim();

        String senha =
                etSenha.getText()
                        .toString();

        if (TextUtils.isEmpty(matricula)
                || TextUtils.isEmpty(senha)) {

            tvErro.setText(
                    "Preencha e-mail e senha."
            );

            return;
        }

        // Para facilitar o desenvolvimento, vamos aceitar um login fixo
        if (matricula.equals("aluno@teste.com") && senha.equals("123456")) {
            irParaHome();
            return;
        }

        loginOnline(matricula, senha);
    }

    private void loginOnline(
            String matricula,
            String senha
    ) {

        mostrarCarregamento(true);
        tvErro.setText("");

        LoginRequest request =
                new LoginRequest(
                        matricula,
                        senha
                );

        ApiClient
                .getApiService()
                .login(request)
                .enqueue(
                        new Callback<ApiResponse<LoginData>>() {

                            @Override
                            public void onResponse(
                                    Call<ApiResponse<LoginData>> call,
                                    Response<ApiResponse<LoginData>> response
                            ) {

                                mostrarCarregamento(false);

                                ApiResponse<LoginData> body =
                                        response.body();

                                if (response.isSuccessful()
                                        && body != null
                                        && body.isSuccess()
                                        && body.getData() != null) {

                                    sessionManager.salvarToken(
                                            body.getData().getToken()
                                    );

                                    irParaHome();
                                    return;
                                }

                                tvErro.setText("Credenciais inválidas.");
                            }

                            @Override
                            public void onFailure(
                                    Call<ApiResponse<LoginData>> call,
                                    Throwable throwable
                            ) {
                                mostrarCarregamento(false);
                                tvErro.setText("Erro de conexão.");
                            }
                        }
                );
    }

    private void irParaHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void mostrarCarregamento(boolean carregando) {
        progressLogin.setVisibility(carregando ? View.VISIBLE : View.GONE);
        btnEntrar.setEnabled(!carregando);
    }
}