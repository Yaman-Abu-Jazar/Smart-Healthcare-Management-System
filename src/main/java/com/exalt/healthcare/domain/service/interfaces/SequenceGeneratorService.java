package com.exalt.healthcare.domain.service.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface SequenceGeneratorService {
    public long generateSequence(String seqName);
}
