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
import com.example.nextstepapp.network.OfflineManager;
import com.example.nextstepapp.session.SessionManager;
import com.example.nextstepapp.ui.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etMatricula;
    private EditText etSenha;
    private Button btnEntrar;
    private Button btnModoOffline;
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
        btnModoOffline = findViewById(R.id.btnModoOffline);
        tvErro = findViewById(R.id.tvErro);
        progressLogin = findViewById(R.id.progressLogin);

        sessionManager = new SessionManager(this);

        btnEntrar.setOnClickListener(v -> tentarLogin());

        if (btnModoOffline != null) {
            btnModoOffline.setOnClickListener(v -> entrarModoOffline());
        }
    }

    private void tentarLogin() {
        String matricula = etMatricula.getText().toString().trim();
        String senha = etSenha.getText().toString();

        // Se estiver em branco ou sem servidor, ativa o modo offline automaticamente para facilitar testes
        if (TextUtils.isEmpty(matricula) || TextUtils.isEmpty(senha)) {
            entrarModoOffline();
            return;
        }

        // Login local de teste
        if (matricula.equals("aluno@teste.com") && senha.equals("123456")) {
            OfflineManager.setOfflineMode(this, true);
            irParaHome();
            return;
        }

        loginOnline(matricula, senha);
    }

    private void entrarModoOffline() {
        OfflineManager.setOfflineMode(this, true);
        Toast.makeText(this, "Modo Offline ativado para testes.", Toast.LENGTH_SHORT).show();
        irParaHome();
    }

    private void loginOnline(String matricula, String senha) {
        mostrarCarregamento(true);
        tvErro.setText("");

        LoginRequest request = new LoginRequest(matricula, senha);

        ApiClient.getApiService().login(request).enqueue(new Callback<ApiResponse<LoginData>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginData>> call, Response<ApiResponse<LoginData>> response) {
                mostrarCarregamento(false);
                ApiResponse<LoginData> body = response.body();

                if (response.isSuccessful() && body != null && body.isSuccess() && body.getData() != null) {
                    OfflineManager.setOfflineMode(MainActivity.this, false);
                    sessionManager.salvarToken(body.getData().getToken());
                    irParaHome();
                    return;
                }

                // Se falhar o servidor, entra em modo offline automaticamente para permitir testes
                entrarModoOffline();
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable throwable) {
                mostrarCarregamento(false);
                // Quando o servidor estiver offline, entra em modo offline automaticamente
                entrarModoOffline();
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
