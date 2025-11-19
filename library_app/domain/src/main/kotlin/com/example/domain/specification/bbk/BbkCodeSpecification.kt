package com.example.domain.specification.bbk

import com.example.domain.model.BbkModel
import com.example.domain.specification.Specification

class BbkCodeSpecification(val code: String) : Specification<BbkModel> {
    override fun specified(candidate: BbkModel): Boolean =
        candidate.code.equals(code, ignoreCase = true)
}