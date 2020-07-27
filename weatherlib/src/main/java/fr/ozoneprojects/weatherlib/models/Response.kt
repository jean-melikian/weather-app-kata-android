package fr.ozoneprojects.weatherlib.models

sealed class Response<T>

sealed class Error<T>(val throwable: Throwable) : Response<T>()

class Success<T>(val value: T) : Response<T>()

class GenericError<T>(throwable: Throwable) : Error<T>(throwable)
