package me.fanjie.testfaceplusplus.facedetect;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jw on 2017/6/2.
 */

public interface FaceApi {

    String DETECT = "detect";
    String ADD_FACE = "faceset/addface";
    String COMPARE = "compare";

    @FormUrlEncoded
    @POST(DETECT)
    Call<DetectResp> detect(@Field("api_key") String api_key,
                            @Field("api_secret") String api_secret,
                            @Field("return_landmark") int return_landmark,
                            @Field("image_base64") String image_base64);


    @FormUrlEncoded
    @POST(ADD_FACE)
    Call<ResponseBody> addFace(@Field("api_key") String api_key,
                               @Field("api_secret") String api_secret,
                               @Field("faceset_token") String faceset_token,
                               @Field("face_tokens") String face_tokens);

    @FormUrlEncoded
    @POST(COMPARE)
    Call<CompareResp> compare(@Field("api_key") String api_key,
                               @Field("api_secret") String api_secret,
                               @Field("face_token1") String face_token1,
                               @Field("face_token2") String face_token2);

}
