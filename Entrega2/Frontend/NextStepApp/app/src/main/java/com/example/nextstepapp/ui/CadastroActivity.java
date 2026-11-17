package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.MainActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.LoginData;
import com.example.nextstepapp.model.RegisterRequest;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private EditText etConfirmarSenha;
    private Button btnCadastrar;
    private ProgressBar progressCadastro;
    private TextView tvErro;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = findViewById(R.id.etNomeCadastro);
        etEmail = findViewById(R.id.etEmailCadastro);
        etSenha = findViewById(R.id.etSenhaCadastro);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        progressCadastro = findViewById(R.id.progressCadastro);
        tvErro = findViewById(R.id.tvErroCadastro);
        sessionManager = new SessionManager(this);

        btnCadastrar.setOnClickListener(v -> cadastrar());
        findViewById(R.id.tvVoltarLogin).setOnClickListener(v -> finish());
    }

    private void cadastrar() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString();
        String confirmarSenha = etConfirmarSenha.getText().toString();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmarSenha)) {
            tvErro.setText("Preencha todos os campos.");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Digite um e-mail válido.");
            etEmail.requestFocus();
            return;
        }
        if (senha.length() < 8) {
            etSenha.setError("Use pelo menos 8 caracteres.");
            etSenha.requestFocus();
            return;
        }
        if (!senha.equals(confirmarSenha)) {
            etConfirmarSenha.setError("As senhas não coincidem.");
            etConfirmarSenha.requestFocus();
            return;
        }

        tvErro.setText("");
        mostrarCarregamento(true);
        RegisterRequest request = new RegisterRequest(email, nome, senha);
        ApiClient.getApiService().register(request).enqueue(new Callback<ApiResponse<LoginData>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginData>> call, Response<ApiResponse<LoginData>> response) {
                mostrarCarregamento(false);
                ApiResponse<LoginData> body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess()
                        && body.getData() != null && body.getData().getToken() != null
                        && !body.getData().getToken().trim().isEmpty()) {
                    LoginData data = body.getData();
                    sessionManager.limparSessao();
                    sessionManager.salvarToken(data.getToken());
                    if (data.getUser() != null) {
                        sessionManager.salvarUsuario(data.getUser().getFullName() != null
                                ? data.getUser().getFullName() : nome);
                        sessionManager.salvarUserId(data.getUser().getId() != null
                                ? data.getUser().getId() : email);
                        sessionManager.salvarUserRole(data.getUser().getRole() != null
                                ? data.getUser().getRole() : "student");
                    } else {
                        sessionManager.salvarUsuario(nome);
                        sessionManager.salvarUserId(email);
                        sessionManager.salvarUserRole("student");
                    }
                    startActivity(new Intent(CadastroActivity.this, HomeActivity.class));
                    finish();
                    return;
                }

                String mensagem = body != null ? body.getMessage() : null;
                if (TextUtils.isEmpty(mensagem) && response.errorBody() != null) {
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        mensagem = json.optString("message", null);
                    } catch (Exception e) {
                        Log.e("CadastroActivity", "Erro ao ler resposta de cadastro", e);
                    }
                }
                tvErro.setText(TextUtils.isEmpty(mensagem)
                        ? "Não foi possível criar sua conta. Verifique os dados e tente novamente."
                        : mensagem);
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable throwable) {
                mostrarCarregamento(false);
                Log.e("CadastroActivity", "Erro de conexão no cadastro", throwable);
                tvErro.setText("Não foi possível conectar ao servidor. Tente novamente.");
            }
        });
    }

    private void mostrarCarregamento(boolean carregando) {
        progressCadastro.setVisibility(carregando ? View.VISIBLE : View.GONE);
        btnCadastrar.setEnabled(!carregando);
    }
}