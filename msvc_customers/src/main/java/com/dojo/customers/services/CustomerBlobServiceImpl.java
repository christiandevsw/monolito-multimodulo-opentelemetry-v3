package com.dojo.customers.services;

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
public class CustomerBlobServiceImpl implements CustomerBlobService{
    private BlobContainerClient containerClient;

    public CustomerBlobServiceImpl(@Value("${connection-string.blob.storage}") String connectionString,
                                   @Value("${container.name.customer}") String containerName){
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = serviceClient.getBlobContainerClient(containerName);
    }

    public List<String> listBlobNames() {
        List<String> blobNames = new ArrayList<>();
        for (BlobItem blobItem : containerClient.listBlobs()) {
            blobNames.add(blobItem.getName());
        }
        return blobNames;
    }


    //v1
//    @Override
//    public Optional<String> findFileBlob(String name) {
//        Optional<BlobItem> optional = containerClient.listBlobs()
//                .stream()
//                .filter(b -> b.getName().equalsIgnoreCase(name))
//                .findFirst();
//        if (optional.isPresent()) {
//            return Optional.of(optional.get().getName());
//        }
//        return Optional.empty();
//     }

    //v2
    @Override
    public Optional<BlobClient> findFileBlob(String name) {
        BlobClient bobClient=containerClient.getBlobClient(name);
        if(bobClient.exists()){
            return Optional.of(bobClient);
        }
        return Optional.empty();
    }

//    @Override
//    public ResponseEntity<byte[]> getBytesBlob(String name) {
//        BlobClient blobClient1 = blobClient.getBlobClient(name);
//        byte[] content = blobClient1.downloadContent().toBytes();
//        String contentType = blobClient1.getProperties().getContentType();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(contentType));
//        headers.setContentLength(content.length);
//
//        return new ResponseEntity<>(content, headers, HttpStatus.OK);
//    }


}
