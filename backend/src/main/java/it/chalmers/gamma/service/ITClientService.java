package it.chalmers.gamma.service;

import it.chalmers.gamma.db.entity.ITClient;
import it.chalmers.gamma.db.entity.Text;
import it.chalmers.gamma.db.repository.ITClientRepository;
import it.chalmers.gamma.requests.AddITClientRequest;
import it.chalmers.gamma.util.TokenUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ITClientService implements ClientDetailsService {

    @Value("${application.auth.accessTokenValidityTime}")       // TODO Fix this
    private int accessTokenValidityTime;

    @Value("${application.auth.autoApprove}")
    private boolean autoApprove;

    @Value("${application.auth.refreshTokenValidityTime}")
    private int refreshTokenValidityTime;

    private final ITClientRepository itClientRepository;

    public ITClientService(ITClientRepository itClientRepository) {
        this.itClientRepository = itClientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return this.itClientRepository.findByClientId(clientId);
    }

    public void createITClient(AddITClientRequest request) {
        ITClient client = new ITClient();
        client.setName(request.getName());
        Text description = new Text();
        description.setEn(request.getDescription().getEn());
        description.setSv(request.getDescription().getSv());
        client.setDescription(description);
        client.setWebServerRedirectUri(request.getWebServerRedirectUri());
        client.setCreatedAt(Instant.now());
        client.setLastModifiedAt(Instant.now());
        client.setAccessTokenValidity(this.accessTokenValidityTime);
        client.setAutoApprove(this.autoApprove);
        client.setRefreshTokenValidity(this.refreshTokenValidityTime);
        client.setClientId(TokenUtils.generateToken());
        client.setClientSecret(TokenUtils.generateToken());
        this.itClientRepository.save(client);
    }

    public List<ITClient> getAllClients() {
        return this.itClientRepository.findAll();
    }

    public ITClient getITClient(UUID id) {
        return this.itClientRepository.findById(id).orElseThrow();
    }

    public void removeITClient(UUID id) {
        this.itClientRepository.deleteById(id);
    }

    public void editClient(UUID id, AddITClientRequest request) {
        ITClient client = this.itClientRepository.findById(id).orElseThrow();
        client.setLastModifiedAt(Instant.now());
        client.setName(request.getName() == null ? client.getName() : request.getName());
        client.setDescription(request.getDescription() == null ? client.getDescription() : request.getDescription());
        client.setWebServerRedirectUri(request.getWebServerRedirectUri() == null
                ? client.getWebServerRedirectUri() : request.getWebServerRedirectUri());
    }

    public boolean clientExists(UUID id) {
        return this.itClientRepository.existsById(id);
    }

    public boolean clientExistsByClientId(String clientId) {
        return this.itClientRepository.existsITClientByClientId(clientId);
    }

    public void addITClient(ITClient itClient) {
        this.itClientRepository.save(itClient);
    }
}
