package com.example.app.exception

class ConvertFailureException(param: String) : RuntimeException("Cant convert $param to API call")