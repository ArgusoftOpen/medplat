package com.argusoft.medplat.query.nlp;

import com.argusoft.medplat.query.nlp.model.NlpParsedQuery;

public interface NlpParsingEngine {
    NlpParsedQuery parse(String naturalLanguageQuery, Integer limit);
}
