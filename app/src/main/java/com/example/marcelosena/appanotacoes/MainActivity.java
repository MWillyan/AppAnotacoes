package com.example.marcelosena.appanotacoes;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Toolbar tb_main;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb_main = (Toolbar) findViewById(R.id.toolbar_main);
        tb_main.setTitle(R.string.app_name);
        setSupportActionBar(tb_main);

        configureGoogleApiClient();

        configurarDrawer();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void configurarDrawer() {
        // Configura a library Glide para carregar a imagem no Drawer
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }
        });


        // Obtem o usuário logado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Cria o header com os detalhes do usuário
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(user.getDisplayName())
                                .withEmail(user.getEmail())
                                .withIcon(user.getPhotoUrl())
                )
                .build();
        // Cria o item de logout com o listener
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withName("Sign out");
        item1.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                signOut();
                return true;
            }
        });

        // Cria o drawer
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(tb_main)
                .addDrawerItems(new PrimaryDrawerItem().withName("Contatos"))
                .addDrawerItems(new PrimaryDrawerItem().withName("Mapa"))
                .addDrawerItems(new PrimaryDrawerItem().withName("Configurações"))
                .addDrawerItems(new DividerDrawerItem())
                .addDrawerItems(item1)
                .withAccountHeader(headerResult)
                .build();
    }

    public static void startNewInstance(LoginActivity loginActivity) {
    }

    private void signOut(){
        // Realiza o logout no Firebase
        FirebaseAuth.getInstance().signOut();
        // Realiza o logout na Api do Google
        Auth.GoogleSignInApi.signOut(client).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // Logout realizado, volta para a tela de Login
                        LoginActivity.startNewInstance(MainActivity.this);
                        Toast.makeText(MainActivity.this,"Até a próxima!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void configureGoogleApiClient() {
        // Cria a opções de login do Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Cria uma nova instância do cliente do Google Api com as opções criadas acima
        Object mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
