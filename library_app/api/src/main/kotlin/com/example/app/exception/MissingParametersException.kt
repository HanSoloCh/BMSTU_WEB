package com.example.app.exception

class MissingParametersException(param: String) :
    RuntimeException("Missing parameters for param: $param")