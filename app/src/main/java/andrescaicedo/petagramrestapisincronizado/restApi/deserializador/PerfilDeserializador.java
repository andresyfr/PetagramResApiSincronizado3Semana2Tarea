package andrescaicedo.petagramrestapisincronizado.restApi.deserializador;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import andrescaicedo.petagramrestapisincronizado.pojo.Mascota;
import andrescaicedo.petagramrestapisincronizado.restApi.JsonKeys;
import andrescaicedo.petagramrestapisincronizado.restApi.model.PerfilResponse;

public class PerfilDeserializador implements JsonDeserializer <PerfilResponse>{

    @Override
    public PerfilResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //Deserializando la Respuesta
        Gson gson = new Gson();
        PerfilResponse mascotaResponse = gson.fromJson(json, PerfilResponse.class);

        //Declarando el Objeto del Arreglo JSON
        JsonArray mascotaResponseData = json.getAsJsonObject().getAsJsonArray(JsonKeys.MEDIA_RESPONSE_ARRAY);
        mascotaResponse.setMascotas(deserializarPerfilPetJson(mascotaResponseData));
        return mascotaResponse;
    }

    //Arraylist del Contacto
    private ArrayList<Mascota> deserializarPerfilPetJson(JsonArray mascotaResponseData){

        ArrayList<Mascota> mascotas = new ArrayList<>();

        //Para Cada Elemento del JSON
        for (int i = 0; i < mascotaResponseData.size(); i++) {
            JsonObject mascotaResponseDataObject = mascotaResponseData.get(i).getAsJsonObject();

            //Obteniendo los Datos de la Foto

            JsonObject imageJson        = mascotaResponseDataObject.getAsJsonObject(JsonKeys.MEDIA_IMAGES);
            JsonObject imagenStdJson    = imageJson.getAsJsonObject(JsonKeys.MEDIA_STANDARD_RESOLUTION);

            String id="";
//            try {
                id                   = mascotaResponseDataObject.get(JsonKeys.ID).getAsString();//imageJson.get(JsonKeys.ID).getAsString();//"A";//imagenStdJson.get(JsonKeys.ID).getAsString();\
//                System.out.println("Id mascota en perfil desealizador "+id);
//            }catch (Exception ex){
//                System.out.println("Error en perfil desealizador "+ex);
//            }

            String urlFoto              = imagenStdJson.get(JsonKeys.MEDIA_URL).getAsString();

            //System.out.println("PerfilDeserializador"+mascotaResponseData);

            //Obteniendo los Likes
            JsonObject likesJson    = mascotaResponseDataObject.getAsJsonObject(JsonKeys.MEDIA_LIKES);
            int likes               = likesJson.get(JsonKeys.MEDIA_LIKES_COUNT).getAsInt();

            //Llenando Datos
            Mascota fotoPerfilActual = new Mascota();
            fotoPerfilActual.setId(id);
            fotoPerfilActual.setUrlFoto(urlFoto);
            fotoPerfilActual.setLikes(likes);
            mascotas.add(fotoPerfilActual);

            System.out.println("ID mascota en Perfil deserializa "+" "+fotoPerfilActual.getId());
        }

        return mascotas;
    }
}
