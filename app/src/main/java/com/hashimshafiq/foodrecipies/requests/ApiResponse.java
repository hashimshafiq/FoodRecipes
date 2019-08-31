package com.hashimshafiq.foodrecipies.requests;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    public ApiResponse<T> create(Throwable error){
        return new ApiErrorResponse<>(!error.getMessage().equals("") ? error.getMessage() : "Unknown Error\n check network connection");
    }

    public ApiResponse<T> create(Response<T> response){
        if(response.isSuccessful()){
            T body = response.body();

            if(body == null || response.code() == 204){
                return  new ApiEmptyResponse<>();
            }else{
                return new ApiSuccessResponse<>(body);
            }

        }else{
            String errMessae = "";
            try {
                errMessae = response.errorBody().toString();

            }catch (Exception e){
                    e.printStackTrace();
                    errMessae = response.message();
            }
            return new ApiErrorResponse<>(errMessae);

        }
    }

    public class ApiSuccessResponse<T> extends ApiResponse<T>{

        private T body;

        ApiSuccessResponse(T body){
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }

    public class ApiErrorResponse<T> extends ApiResponse<T>{

        private String errorMessage;

        ApiErrorResponse(String errorMessage){
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public class ApiEmptyResponse<T> extends ApiResponse<T>{}


}
