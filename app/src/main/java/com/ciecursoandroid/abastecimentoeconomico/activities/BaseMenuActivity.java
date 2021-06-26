package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ciecursoandroid.abastecimentoeconomico.R;

public abstract class BaseMenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.menu_abastecimentos:
                i = new Intent(this, AbastecimentosActivity.class);
                startActivity(i);
                if (this instanceof AbastecimentosActivity) finish();
                break;
            case R.id.menu_abastecer:
                if (this instanceof MainActivity) return false;
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
}
