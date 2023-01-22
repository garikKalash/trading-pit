package com.gk.tradingpit.service;

import com.gk.tradingpit.persistance.entity.AffiliateClientMap;
import com.gk.tradingpit.persistance.repository.AffiliateClientMapRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AffiliateClientMapService {
    private final AffiliateClientMapRepository affiliateClientMapRepository;

    public AffiliateClientMapService(AffiliateClientMapRepository affiliateClientMapRepository) {
        this.affiliateClientMapRepository = affiliateClientMapRepository;
    }

    public Optional<AffiliateClientMap> getByClientId(String clientId) {
        return this.affiliateClientMapRepository.findTopByClientIdOrderByCreationDateDesc(clientId);
    }

    public AffiliateClientMap save(AffiliateClientMap affiliateClientMap) {
        return this.affiliateClientMapRepository.save(affiliateClientMap);
    }
}
