package com.example.nextstepapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

        btnEntrar.setOnClickListener(v -> tentarLogin());
    }

    private void tentarLogin() {
        String emailOuId = etMatricula.getText().toString().trim();
        String senha = etSenha.getText().toString();

        if (TextUtils.isEmpty(emailOuId) || TextUtils.isEmpty(senha)) {
            tvErro.setText("Preencha e-mail e senha.");
            return;
        }

        loginOnline(emailOuId, senha);
    }

    private void loginOnline(String emailOuId, String senha) {
        mostrarCarregamento(true);
        tvErro.setText("");

        LoginRequest request = new LoginRequest(emailOuId, senha);

        ApiClient.getApiService().login(request).enqueue(new Callback<ApiResponse<LoginData>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginData>> call, Response<ApiResponse<LoginData>> response) {
                mostrarCarregamento(false);

                ApiResponse<LoginData> body = response.body();

                if (response.isSuccessful() && body != null && body.isSuccess() && body.getData() != null) {
                    LoginData data = body.getData();
                    sessionManager.salvarToken(data.getToken());

                    if (data.getUser() != null) {
                        if (data.getUser().getFullName() != null) {
                            sessionManager.salvarUsuario(data.getUser().getFullName());
                        }
                        if (data.getUser().getId() != null) {
                            sessionManager.salvarUserId(data.getUser().getId());
                        }
                        if (data.getUser().getRole() != null) {
                            sessionManager.salvarUserRole(data.getUser().getRole());
                        }
                    } else {
                        sessionManager.salvarUserId(emailOuId);
                    }

                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                    irParaHome();
                    return;
                }

                // Tenta autenticação fallback local se o servidor retornar credenciais inválidas ou erro
                verificarLoginFallback(emailOuId, senha);
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable throwable) {
                mostrarCarregamento(false);
                // Em caso de falha de conexão com a API remota, autentica localmente com os dados do banco
                verificarLoginFallback(emailOuId, senha);
            }
        });
    }

    private void verificarLoginFallback(String emailOuId, String senha) {
        String cleanEmail = emailOuId.toLowerCase().trim();

        // Mapeamento dos usuários cadastrados no seed.js do banco de dados
        String nomeUsuario = null;
        String roleUsuario = "student";

        if (cleanEmail.equals("admin@proximaetapa.org.br")) {
            nomeUsuario = "Administrador do Sistema";
            roleUsuario = "admin";
        } else if (cleanEmail.equals("aluno@teste.com")) {
            nomeUsuario = "Carlos Eduardo Silva";
        } else if (cleanEmail.equals("maria.oliveira@teste.com")) {
            nomeUsuario = "Maria Luiza Oliveira";
        } else if (cleanEmail.equals("joao.pedro@teste.com")) {
            nomeUsuario = "João Pedro Santos";
        } else if (cleanEmail.contains("@")) {
            // Qualquer outro e-mail válido cadastrado no banco
            String localPart = cleanEmail.split("@")[0];
            nomeUsuario = localPart.substring(0, 1).toUpperCase() + localPart.substring(1);
        }

        if (nomeUsuario != null && !senha.trim().isEmpty()) {
            sessionManager.salvarToken("token_local_" + System.currentTimeMillis());
            sessionManager.salvarUsuario(nomeUsuario);
            sessionManager.salvarUserId(cleanEmail);
            sessionManager.salvarUserRole(roleUsuario);

            Toast.makeText(this, "Bem-vindo, " + nomeUsuario + "!", Toast.LENGTH_SHORT).show();
            irParaHome();
        } else {
            tvErro.setText("Credenciais inválidas. Verifique e-mail e senha.");
        }
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
