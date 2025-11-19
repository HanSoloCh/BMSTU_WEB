package com.example.domain.specification.apu

import com.example.domain.model.ApuModel
import com.example.domain.specification.Specification


class ApuTermSpecification(val term: String) : Specification<ApuModel> {
    override fun specified(candidate: ApuModel): Boolean =
        candidate.term.equals(term, ignoreCase = true)
}