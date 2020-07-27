# Requirements

You will need to create a `secrets.properties` file at the root of your project and add the following in it.

## Open Weather Map API key 

```properties
OPEN_WEATHER_MAP_API_KEY=yourapikey
```

## Google Places API key

```properties
PLACES_API_KEY=yourapikey 
```
[Quick guide to get a Places API key](https://developers.google.com/places/android-sdk/get-api-key#get_key)

# Insights

I used some of clean architecture patterns and SOLID principles but had to go faster in the end, for the app. 

## weatherlib

Android Library. Has a few public classes and an interface. All that is left is internal in `internal` package.

Dependency inversion: Exposing a `WeatherRepositoryFactory` returning a `WeatherRepository` interface, but is actually an instance of `WeatherRepositoryImpl`.

This repository depends on a Retrofit service for `OpenWeatherMapApi` and the OpenWeatherMap API key injected.

The library embeds weather icons and an util object to map them to the API value (they were renamed because a drawable id can't start with a figure).

### What could be improved

- Models could be segregated: OpenWeatherMap related ones would be only used to deserialize the API response, then mapped to our domain-specific models, fully agnostic of the data source. 
- Eventually, a more granular API to get only the data we need (daily, minutely, current).
- Finally, a data store to cache responses (Room Database ? OkHttp Cache ? Anyway, it would be hidden by a Repository<->DataSource pattern).

## app

Single Activity app. Uses Google Places SDK to search for locations (cities) and store them in our data store (SharedPreferences).
`WeatherDetailsFragment`:
```
Activity(ViewModel)<->Fragment(ViewModel)<->ViewModel(LiveData, weatherlib)
```
`LocationsFragment`:
```
Activity(ViewModel)<->Fragment(ViewModel)<->ViewModel(LiveData, Repository)<->Repository(DataSource)
```

### What could be improved

- Implement Hilt (Dagger) for dependency injection, to ease testing
- Unit testing on ViewModels (verify on each LiveData states) and on Repositories.
- Get Places SDK listener out of the Fragment.
- Better looking UI.

