package com.ias.quickfix.service;

import com.ias.quickfix.dto.ClientDto;
import com.ias.quickfix.dto.JobDto;
import com.ias.quickfix.dto.ProviderDto;
import com.ias.quickfix.exceptions.UserAlreadyExistsException;
import com.ias.quickfix.model.Client;
import com.ias.quickfix.model.Job;
import com.ias.quickfix.model.Provider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private ModelMapper modelMapper;
    private FirebaseService firebaseService;
    private final  String CLIENT_COLLECTION = "clients";
    private final  String PROVIDER_COLLECTION = "providers";

    public ClientDto saveClient(ClientDto clientDto) {
        Client client = modelMapper.map(clientDto, Client.class);

        if (firebaseService.documentExists(CLIENT_COLLECTION, client.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + client.getEmail() + " already exists");
        }

        // save doc with mail as document_id
        firebaseService.saveObject(CLIENT_COLLECTION, client.getEmail(), client);

        log.info("Saving client: {}", client);

        return this.modelMapper.map(client, ClientDto.class);
    }

    public List<ClientDto> getAllClients() {
        // Retrieve all documents from the CLIENT_COLLECTION
        List<Client> clients = firebaseService.getAllObjects(CLIENT_COLLECTION, Client.class);

        // Map each Client entity to a ClientDto
        return clients.stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    public ClientDto getClientByEmail(String email) {
        // Retrieve a specific document by its ID (email)
        Client client = firebaseService.getObjectById(CLIENT_COLLECTION, email, Client.class);

        // Map the Client entity to a ClientDto
        return modelMapper.map(client, ClientDto.class);
    }

    public ClientDto updateClient(String email, ClientDto clientDto) {
        if (!firebaseService.documentExists(CLIENT_COLLECTION, email)) {
            throw new RuntimeException("User with email " + email + " does not exist");
        }

        Client updatedClient = modelMapper.map(clientDto, Client.class);

        firebaseService.saveObject(CLIENT_COLLECTION, email, updatedClient);
        log.info("Updated client: {}", updatedClient);

        return modelMapper.map(updatedClient, ClientDto.class);
    }

    public void deleteClient(String email) {
        // Verifică dacă documentul există
        if (!firebaseService.documentExists(CLIENT_COLLECTION, email)) {
            throw new RuntimeException("User with email " + email + " does not exist");
        }

        // Șterge documentul din Firestore
        firebaseService.deleteObject(CLIENT_COLLECTION, email);

        log.info("Deleted client with email: {}", email);
    }

    public ProviderDto saveProvider(ProviderDto providerDto) {
        Provider provider = modelMapper.map(providerDto, Provider.class);

        // Verifică dacă provider-ul deja există
        if (firebaseService.documentExists(PROVIDER_COLLECTION, provider.getEmail())) {
            throw new UserAlreadyExistsException("Provider with email " + provider.getEmail() + " already exists");
        }

        // Populează jobId pentru fiecare job din provider
        if (provider.getJobs() != null) {
            for (Job job : provider.getJobs()) {
                // Crează jobId folosind jobName și industry
                String jobId = job.getJobName() + "_" + job.getIndustry();
                job.setJobId(jobId);  // Setează jobId în JobDto
            }
        }

        // Salvează documentul cu email-ul ca ID
        firebaseService.saveObject(PROVIDER_COLLECTION, provider.getEmail(), provider);
        log.info("Saving provider: {}", provider);

        return modelMapper.map(provider, ProviderDto.class);
    }

    public List<ProviderDto> getAllProviders() {
        // Obține toate documentele din colecția PROVIDER_COLLECTION
        List<Provider> providers = firebaseService.getAllObjects(PROVIDER_COLLECTION, Provider.class);

        // Map fiecare entitate Provider la un DTO ProviderDto
        return providers.stream()
                .map(provider -> modelMapper.map(provider, ProviderDto.class))
                .collect(Collectors.toList());
    }

    public ProviderDto getProviderByEmail(String email) {
        // Obține un document specific utilizând email-ul ca ID
        Provider provider = firebaseService.getObjectById(PROVIDER_COLLECTION, email, Provider.class);

        // Map entitatea Provider la un DTO ProviderDto
        return modelMapper.map(provider, ProviderDto.class);
    }

    public ProviderDto updateProvider(String email, ProviderDto providerDto) {
        // Verifică dacă documentul există
        if (!firebaseService.documentExists(PROVIDER_COLLECTION, email)) {
            throw new RuntimeException("Provider with email " + email + " does not exist");
        }

        Provider updatedProvider = modelMapper.map(providerDto, Provider.class);

        // Actualizează documentul în Firestore
        firebaseService.saveObject(PROVIDER_COLLECTION, email, updatedProvider);
        log.info("Updated provider: {}", updatedProvider);

        return modelMapper.map(updatedProvider, ProviderDto.class);
    }

    public void deleteProvider(String email) {
        // Verifică dacă documentul există
        if (!firebaseService.documentExists(PROVIDER_COLLECTION, email)) {
            throw new RuntimeException("Provider with email " + email + " does not exist");
        }

        // Șterge documentul din Firestore
        firebaseService.deleteObject(PROVIDER_COLLECTION, email);
        log.info("Deleted provider with email: {}", email);
    }


    public boolean providerExists(String providerEmail) {
        // Verifică dacă provider-ul deja există
        return firebaseService.documentExists(PROVIDER_COLLECTION, providerEmail);
    }
}
