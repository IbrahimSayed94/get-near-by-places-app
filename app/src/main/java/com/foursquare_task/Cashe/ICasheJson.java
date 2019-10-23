package com.foursquare_task.Cashe;





public interface ICasheJson
{
    void saveJsonToCashe(Object object, String request);

    Object getJsonFromCashe(String request);
}
