package andrescaicedo.petagramrestapisincronizado.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import andrescaicedo.petagramrestapisincronizado.R;
import andrescaicedo.petagramrestapisincronizado.db.ConstructorMascotas;
import andrescaicedo.petagramrestapisincronizado.pojo.Mascota;
import andrescaicedo.petagramrestapisincronizado.restApi.ConstantesRestApi;
import andrescaicedo.petagramrestapisincronizado.restApi.EndpointsApi;
import andrescaicedo.petagramrestapisincronizado.restApi.adapter.RestApiAdapter;
import andrescaicedo.petagramrestapisincronizado.restApi.model.MascotaResponse;
import andrescaicedo.petagramrestapisincronizado.restApi.model.PerfilResponse;
import andrescaicedo.petagramrestapisincronizado.restApi.model.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ANDRES on 02/10/2017.
 */

public class FotosPerfilAdaptador extends RecyclerView.Adapter<FotosPerfilAdaptador.FotosPerfilViewHolder>{

    //4.- Declarando la coleccion de los contactos
    ArrayList<Mascota> mascotas;
    Activity activity;

    //1.-
    public static class FotosPerfilViewHolder extends RecyclerView.ViewHolder{
        //2.- Se declaran todos los views declarados dentro del cardview aqui es donde se adaptan
        private ImageView imgFotoPerfil;
        private TextView tvLikesCVPerfil;
        private ImageView imgBoneCVPerfil;

        public FotosPerfilViewHolder(View itemView) {
            super(itemView);
            // 3.- Se asocia el ImageView y se hace el casting
            imgFotoPerfil         = (ImageView)   itemView.findViewById(R.id.imgFotoPerfil);
            tvLikesCVPerfil       = (TextView)    itemView.findViewById(R.id.tvNombreCV);
            imgBoneCVPerfil       = (ImageView)   itemView.findViewById(R.id.imgBone);
        }

    }

    //10.- Generando el Metodo Constructor
    public FotosPerfilAdaptador(ArrayList<Mascota> mascotas, Activity activity) {
        this.mascotas = mascotas;
        this.activity = activity;
    }

    @Override
    //6.- Esto infla o le da vida a nuestro layout cardview, Infla el layout y lo pasa al viewholder
    public FotosPerfilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_perfil, parent, false);
        return new FotosPerfilViewHolder(v);
    }

    @Override //7.- Aqui vamos a pasar la lista de Mascotas
    public void onBindViewHolder(final FotosPerfilViewHolder fotosPerfilViewHolder, final int position) {
        fotosPerfilViewHolder.tvLikesCVPerfil.setText(String.valueOf(mascotas.get(position).getLikes()));
        //fotosPerfilViewHolder.imgFotoPerfil.setImageResource(mascota.getFoto());
        Picasso.with(activity)
                .load(mascotas.get(position).getUrlFoto())
                .placeholder(R.drawable.huella)
                .into(fotosPerfilViewHolder.imgFotoPerfil);

        ConstructorMascotas constructorMascotas = new ConstructorMascotas(activity);

        final Mascota mascota = mascotas.get(position);
        //8.- Haciendo Click en la Imagen
        fotosPerfilViewHolder.imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, mascota.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //9.- Haciendo Click en el boton Like
        fotosPerfilViewHolder.imgBoneCVPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verificarMascota;
                ConstructorMascotas constructorMascotas = new ConstructorMascotas(activity);

                verificarMascota = 0;//constructorMascotas.verificarMascota(mascota);

                if (verificarMascota == 0){
                    //constructorMascotas.insertarMascota(mascota);
                    //System.out.println("mas==================="+mascota.getUrlFoto()+"-"+mascota.getId()+"-"+mascota.getLikes());
                    postLikeMediaInstagram(mascota.getId());
                    fotosPerfilViewHolder.tvLikesCVPerfil.setText(""+(Integer.parseInt(fotosPerfilViewHolder.tvLikesCVPerfil.getText().toString())+1) );
                    //Toast.makeText(activity, "Like: " + mascota.getId(), Toast.LENGTH_SHORT).show();
                } else {
                    constructorMascotas.insertarLikeMascota(mascota);
                    fotosPerfilViewHolder.tvLikesCVPerfil.setText(Integer.toString(constructorMascotas.obtenerLikesMascota(mascota)));
                    //Toast.makeText(activity, "Like: " + mascota.getId(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override //5.- EL numero total de mascotas
    public int getItemCount() {
        return mascotas.size();
    }

    public void postLikeMediaInstagram(String id_media) {
        //1.- Inicializando el Adaptador RestApiAdapter
        RestApiAdapter restApiAdapter = new RestApiAdapter();

        //2.- Creando el GSON
        Gson gson = restApiAdapter.construyendoDeserializadorBusqueda();

        //3.- Instanciando la llamada hacia el Endpoint con el gson que acabamos de crear
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionInicial(gson);

        //4.- Creando el Call
        String url = "media/"+id_media+"/likes"+ConstantesRestApi.KEY_ACCESS_TOKEN+ConstantesRestApi.ACCESS_TOKEN;
        //System.out.println("Url fotos perfil adaptador "+url);
        final Call<MascotaResponse> mascotaResponseCall = endpointsApi.likeMascotaInstagram(url);
        mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
            @Override
            public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                MascotaResponse mascotaResponse = response.body();

                try{
                    //Toast.makeText(activity, "Se agregó un like a la foto seleccionada,\n configure la cuenta de nuevo para ver los cambios", Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    System.out.println("ErrorFotosLike "+mascotaResponse+"\n error "+ex);
                }

//                usuario = searchResponse.getCuenta();
//
//                if(!usuario.getUsuario().equals("NotFound")){
//                    getInstagramProfile();
//                } else{
//                    usuario.setUsuario("NotFound");
//                    getInstagramProfile();
//                }
            }

            @Override
            public void onFailure(Call<MascotaResponse> call, Throwable t) {
                Toast.makeText(activity, "Perfil: " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
