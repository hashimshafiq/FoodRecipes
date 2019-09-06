package com.hashimshafiq.foodrecipies.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.hashimshafiq.foodrecipies.AppExecutors;
import com.hashimshafiq.foodrecipies.requests.ApiResponse;

// CacheObject: Type for the Resource data.
// RequestObject: Type for the API response.
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> result = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    private void init(){
        //assign loading status
        result.setValue((Resource<CacheObject>) Resource.loading(null));

        final LiveData<CacheObject> dbSource = loadFromDb();

        result.addSource(dbSource, cacheObject -> {
            result.removeSource(dbSource);
            if(shouldFetch(cacheObject)){
                fetchFromNetwork(dbSource);
            }else{
                result.addSource(dbSource, cacheObject1 -> setValue(Resource.success(cacheObject1)));
            }
        });

    }

    private void setValue(Resource<CacheObject> newValue) {
        if(result.getValue() != newValue){
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<CacheObject> dbSource){

        result.addSource(dbSource, cacheObject -> setValue(Resource.loading(cacheObject)));

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();
        result.addSource(apiResponse, requestObjectApiResponse -> {
            result.removeSource(dbSource);
            result.removeSource(apiResponse);


            if(requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse){
                appExecutors.getDiskIO().execute(() -> {
                    // save the response to local database
                    saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse)requestObjectApiResponse));
                    appExecutors.mainThread().execute(() -> result.addSource(loadFromDb(), cacheObject -> setValue(Resource.success(cacheObject))));
                });

            }else if(requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse){
                appExecutors.mainThread().execute(() -> {
                    result.addSource(loadFromDb(), cacheObject -> setValue(Resource.success(cacheObject)));
                });
            }else if(requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse){
                    result.addSource(dbSource, cacheObject -> setValue(Resource.error(((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),cacheObject)));
            }
        });

    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response){
        return (CacheObject) response.getBody();
    }


    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return result;
    }
}

