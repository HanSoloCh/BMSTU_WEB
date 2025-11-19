package com.example.domain.exception

import java.util.UUID

class ModelNotFoundException(modelName: String, modelId: UUID) :
    BaseDomainException("$modelName with id $modelId not found")