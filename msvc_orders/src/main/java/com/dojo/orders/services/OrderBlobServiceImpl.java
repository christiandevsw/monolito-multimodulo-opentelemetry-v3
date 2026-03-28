package com.dojo.orders.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderBlobServiceImpl implements OrderBlobService {
    private BlobContainerClient containerClient;

    public OrderBlobServiceImpl(@Value("${conection-string.blob.storage}") String connectionString,
                                @Value("${container.name.order}") String containerName){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = serviceClient.getBlobContainerClient(containerName);
    }

    @Override
    public List<String> listBlobNames() {
        List<String> blobNames = new ArrayList<>();
        for (BlobItem blobItem : containerClient.listBlobs()) {
            blobNames.add(blobItem.getName());
        }
        return blobNames;
    }

    @Override
    public Optional<BlobClient> findFileBlob(String name) {
        BlobClient bobClient=containerClient.getBlobClient(name);
        if(bobClient.exists()){
            return Optional.of(bobClient);
        }
        return Optional.empty();
    }

}
