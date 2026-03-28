package com.dojo.customers.services;

import com.azure.storage.blob.BlobClient;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerBlobService {

    //    ResponseEntity<byte[]> getBytesBlob(String name);
    List<String> listBlobNames();
    Optional<BlobClient>  findFileBlob(String name);
}
