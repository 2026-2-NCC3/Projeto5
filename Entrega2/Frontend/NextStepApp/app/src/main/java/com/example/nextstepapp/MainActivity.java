package com.example.nextstepapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

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

                if (response.isSuccessful() && body != null && body.isSuccess()
                        && body.getData() != null
                        && body.getData().getToken() != null
                        && !body.getData().getToken().trim().isEmpty()) {
                    LoginData data = body.getData();
                    sessionManager.limparSessao();
                    sessionManager.salvarToken(data.getToken());

                    if (data.getUser() != null) {
                        sessionManager.salvarUsuario(data.getUser().getFullName() != null
                                ? data.getUser().getFullName() : "Estudante");
                        sessionManager.salvarUserId(data.getUser().getId() != null
                                ? data.getUser().getId() : emailOuId);
                        sessionManager.salvarUserRole(data.getUser().getRole() != null
                                ? data.getUser().getRole() : "student");
                    } else {
                        sessionManager.salvarUsuario("Estudante");
                        sessionManager.salvarUserId(emailOuId);
                        sessionManager.salvarUserRole("student");
                    }

                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                    irParaHome();
                    return;
                }

                String mensagem = null;
                if (body != null && body.getMessage() != null) {
                    mensagem = body.getMessage();
                } else if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        JSONObject obj = new JSONObject(errorJson);
                        if (obj.has("message")) {
                            mensagem = obj.getString("message");
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Erro ao ler resposta de erro", e);
                    }
                }

                if (mensagem == null || mensagem.trim().isEmpty()) {
                    mensagem = "Credenciais inválidas ou resposta inválida do servidor.";
                }
                tvErro.setText(mensagem);
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable throwable) {
                mostrarCarregamento(false);
                Log.e("MainActivity", "Erro de conexão no login", throwable);
                String detalhe = throwable.getMessage() != null ? throwable.getMessage() : "";
                if (detalhe.toLowerCase().contains("timeout") || detalhe.contains("SocketTimeout")) {
                    tvErro.setText("O servidor demorou para responder (Render iniciando). Tente novamente.");
                } else {
                    tvErro.setText("Não foi possível conectar ao servidor. " + detalhe);
                }
            }
        });
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
