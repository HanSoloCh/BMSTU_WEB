package com.example.domain.exception

import java.util.UUID

class ModelDuplicateException(modelName: String, modelId: UUID) :
    BaseDomainException("$modelName with id $modelId already exists")