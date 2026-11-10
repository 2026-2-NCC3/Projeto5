package com.example.nextstepapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.network.ApiClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatIaActivity extends AppCompatActivity {

    private LinearLayout containerMessages;
    private ScrollView scrollChat;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ia);

        containerMessages = findViewById(R.id.containerMessages);
        scrollChat = findViewById(R.id.scrollChat);
        etMessage = findViewById(R.id.etMessage);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView btnSend = findViewById(R.id.btnSend);
        if (btnSend != null) {
            btnSend.setOnClickListener(v -> enviarMensagemDigitada());
        }

        findViewById(R.id.chipCertificado).setOnClickListener(v -> enviarPrompt("Como baixo meu certificado?"));
        findViewById(R.id.chipPresenca).setOnClickListener(v -> enviarPrompt("Como confirmo presença?"));
        findViewById(R.id.chipHorarios).setOnClickListener(v -> enviarPrompt("Onde vejo os horários?"));
    }

    private void enviarMensagemDigitada() {
        if (etMessage == null) return;
        String texto = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(texto)) return;

        etMessage.setText("");
        enviarPrompt(texto);
    }

    private void enviarPrompt(String mensagem) {
        adicionarBolhaMensagem(mensagem, true);

        Map<String, String> body = new HashMap<>();
        body.put("message", mensagem);

        ApiClient.getApiService().sendChatMessage(body).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Object replyObj = response.body().getData().get("reply");
                    if (replyObj != null) {
                        adicionarBolhaMensagem(replyObj.toString(), false);
                        return;
                    }
                }
                gerarRespostaIaFallback(mensagem);
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                gerarRespostaIaFallback(mensagem);
            }
        });
    }

    private void gerarRespostaIaFallback(String mensagem) {
        String msgLower = mensagem.toLowerCase();
        String resposta = "Olá! Como posso te ajudar com os cursos, presença ou certificados do Próxima Etapa?";

        if (msgLower.contains("certificado") || msgLower.contains("diploma")) {
            resposta = "Você pode visualizar e baixar todos os seus certificados acessando o card 'Certificados' na tela inicial!";
        } else if (msgLower.contains("presença") || msgLower.contains("presenca") || msgLower.contains("qr")) {
            resposta = "Para confirmar sua presença, acesse a aba 'Presenças' no menu inferior e apresente seu QR Code único para o professor ou clique em 'Confirmar Presença Manual'.";
        } else if (msgLower.contains("curso") || msgLower.contains("inscrição") || msgLower.contains("inscricao")) {
            resposta = "Você pode explorar os cursos disponíveis na aba 'Cursos' e se inscrever clicando no curso desejado e em 'INSCREVER-SE NO CURSO'.";
        } else if (msgLower.contains("horario") || msgLower.contains("horário") || msgLower.contains("agenda")) {
            resposta = "Consulte os horários de suas aulas na aba 'Agenda', onde você pode navegar pelos dias da semana!";
        } else if (msgLower.contains("ponto") || msgLower.contains("nivel")) {
            resposta = "Você ganha pontos a cada presença confirmada e curso concluído. Confira seu saldo no Perfil!";
        }

        adicionarBolhaMensagem(resposta, false);
    }

    private void adicionarBolhaMensagem(String texto, boolean isUser) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(14f);
        tv.setPadding(24, 18, 24, 18);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 16;

        if (isUser) {
            params.gravity = Gravity.END;
            params.leftMargin = 80;
            tv.setBackgroundResource(R.drawable.bg_filter_selected);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            params.gravity = Gravity.START;
            params.rightMargin = 80;
            tv.setBackgroundResource(R.drawable.bg_event_card);
            tv.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        tv.setLayoutParams(params);
        if (containerMessages != null) {
            containerMessages.addView(tv);
        }

        if (scrollChat != null) {
            scrollChat.post(() -> scrollChat.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }
}
