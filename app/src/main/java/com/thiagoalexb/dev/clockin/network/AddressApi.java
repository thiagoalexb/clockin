package com.thiagoalexb.dev.clockin.network;

import com.thiagoalexb.dev.clockin.data.models.AddressApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AddressApi {

    @GET("search/{query}")
    Single<List<AddressApiResponse>> get(@Path("query") String query, @Query("format") String format, @Query("limit") String limit);
}
