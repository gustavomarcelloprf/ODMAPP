package com.gustavo.odmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroOngActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextLink;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextCnpj;
    private RadioGroup radioGroupODS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);

        // Inicialize os EditTexts e o RadioGroup
        editTextNome = findViewById(R.id.editTextNome);
        editTextLink = findViewById(R.id.editTextLink);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        editTextCnpj = findViewById(R.id.editTextCnpj);
        radioGroupODS = findViewById(R.id.radioGroupODS);

        // Configura o botão de salvar/cadastrar
        Button buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarMarcador();
            }
        });

        // Adiciona o TextWatcher para formatar e permitir apagar o CNPJ
        editTextCnpj.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String cnpj = s.toString();

                if (cnpj.isEmpty()) {
                    isUpdating = true;
                    editTextCnpj.setText("");
                    editTextCnpj.setSelection(0);
                    return;
                }

                if (cnpj.length() == 14) {
                    // Formata o CNPJ
                    cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8)
                            + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);

                    isUpdating = true;
                    editTextCnpj.setText(cnpj);
                    editTextCnpj.setSelection(cnpj.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void adicionarMarcador() {
        String nome = editTextNome.getText().toString();
        String link = editTextLink.getText().toString();
        String latitudeStr = editTextLatitude.getText().toString();
        String longitudeStr = editTextLongitude.getText().toString();
        String cnpj = editTextCnpj.getText().toString().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

        if (nome.isEmpty() || link.isEmpty() || cnpj.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            // Mostra uma mensagem de erro se algum campo obrigatório estiver vazio
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se a latitude e longitude estão no formato correto
        try {
            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);
            if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            // Mostra uma mensagem de erro se a latitude ou longitude não estiverem no formato correto
            Toast.makeText(this, "Formato inválido para latitude ou longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica qual ODS foi selecionado
        int selectedId = radioGroupODS.getCheckedRadioButtonId();
        int ods = 0; // Valor padrão se nenhum ODS for selecionado
        if (selectedId != -1) {
            View radioButton = radioGroupODS.findViewById(selectedId);
            int index = radioGroupODS.indexOfChild(radioButton);
            ods = index + 1; // Os índices no RadioGroup são baseados em zero
        }

        // Se chegou até aqui, todos os campos estão preenchidos e as coordenadas estão no formato correto
        Intent intent = new Intent();
        intent.putExtra("nome", nome);
        intent.putExtra("link", link);
        intent.putExtra("latitude", latitudeStr);
        intent.putExtra("longitude", longitudeStr);
        intent.putExtra("cnpj", cnpj);
        intent.putExtra("ods", ods);
        setResult(RESULT_OK, intent);
        finish();
    }

}
